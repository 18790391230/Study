### Producer

#### producer生产消息流程

> KafkaProducer：生产者
>
> ​	-> ProducerInterceptors ： 拦截器
>
> ​           -> Serializer ：序列化器
>
> ​				-> Partitioner：分区器
>
> ​						-> RecordAccumulator：消息累加器，里面包含每个分区与 ProducerBatch（包含多个ProducerRecord）的映射关系
>
> ​								-> sender线程创建request并发送



#### 生产者重要参数

1. **acks**：用来指明分区中必须要有多少个副本收到这条消息，生产者才会认为这条消息成功写入。涉及消息可靠性和吞吐量的衡量

   - acks=1 ：默认值为1，生产者发送消息后，只要分区的leader副本成功写入消息，那么他就会收到来自服务端的成功响应。

     - 如果消息无法写入leader副本，比如leader副本崩溃、正在选举新的leader副本的过程中，生产者会收到一个错误的响应，为了避免消息丢失，生产者可以选择重发消息。如果消息写入leader并返回成功响应给生产者，且在其他follower副本拉取之前leader副本崩溃，这种情况消息还是会丢失，因为新选举出来的leader副本中没有该消息

   - acks=0：生产者发送消息后不需要等待服务器任何响应。

     - 如果在消息发送到写入到Kafka过程中出现任何异常，导致Kafka没有接收到这条消息，生产者也无从得知，消息也就丢失了。可以保证最大吞吐量

   - acks=-1或all ：生产者在发送消息后，必须等到所有ISR中的所有副本都写入成功后，才会接收到服务端返回的成功响应。

     - 在其他配置完全相同的情况下，该值可以达到最强的可靠性，但并不是说一定可靠，如果ISR中只有leader副本，则退化成acks=1的情况。要获得更高的可靠性，需要配合min.insync.replicas等参数的联动

       

2. **max.request.size**：限制生产者客户端能发送的消息最大值，默认为1M
   - 不建议盲目更改，如果该值大于broker端的message.max.bytes，生产者投递消息时可能报异常

3. **retries和retry.backoff.ms**：retries用来配置生产者的重试次数，默认为0。retry.backoff.ms默认100，用来设定两次重试的间隔，避免无效的频繁重试
   - Kafka保证发送到同一个分区的数据是有序的。如果acks配置非0，并且max.flight.requests.per. connection参数配置的值大于1，会出现消息错序的情况：如果第一个消息写入失败，第二个消息写入成功，那么生产者重发第一个消息，如果发送成功，则消息错序

4. **compression.type**：指定消息压缩方式，默认为none。以时间换空间的方式
5. **connections.max.idle.ms**：指定多久之后关闭限制的连接，默认540000ms，即9min
6. **linger.ms**：指定生产者发送ProducerBatch之前等待更多的消息（ProducerRecord）加入ProducerBatch的时间，默认为0。生产者客户端会在ProducerBatch被填满或linger.ms时间到达后将消息发送出去。这个参数增加了消息的延时，但是也提升了一定的吞吐量。与Nagle有异曲同工之妙
7. **request.timeout.ms**：配置Producer等待响应的最长时间，默认30000（ms）。请求超时时可以选择重试。这个值要比broker的replica.lag.time.max.ms要大，可以减少因客户端重试引起的消息重复的概率



### Consumer

#### 消费者必需参数

1. bootstrap.servers：指定连接kafka集群所需的broker地址清单，多个用逗号分隔。不需要设置集群中所有broker的地址，消费者会从现有的配置中查找到全部的kafka集群成员
2. group.id：消费者隶属的消费者组id，必填

#### 消费者拦截器

KafkaConsumer会在poll()返回之前调用拦截器的onConsume()对消息进行定制化处理。

> 使用：修改返回消息的内容、按照某种规则过滤消息（可能会减少poll方法返回的消息的个数），如果onConsume()出现异常，会被记录日志，不会向上抛出

```java
/**
 *  对消息设置一个有效期的属性， 如果某条消息在既定的时间窗口内无
 * 法到达， 那么就会被视为无效， 它也就不需要再被继续处理了
 */
public class ConsumerInterceptorTTL implements ConsumerInterceptor<String, String> {
    
    private static long EXPIRE_INTERVAL = 10 * 1000;

    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {
        long now = System.currentTimeMillis();
        Map<TopicPartition, List<ConsumerRecord<String, String>>> newRecords = new HashMap<>();
        for (TopicPartition tp : records.partitions()) {
            List<ConsumerRecord<String, String>> tpRecords = records.records(tp);
            List<ConsumerRecord<String, String>> newTpRecords = new ArrayList<>();
            for (ConsumerRecord<String, String> record : tpRecords) {
                if (now - record.timestamp() < EXPIRE_INTERVAL) {
                    newTpRecords.add(record);
                }
            }
            if (!newTpRecords.isEmpty()) {
                newRecords.put(tp, newTpRecords);
            }
        }
        return new ConsumerRecords<>(newRecords);
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
        offsets.forEach((tp, offset) -> System.out.println(tp + ":" + offset.offset()));
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
```

#### 重要的消费者参数

1. **fetch.min.bytes**：用来配置消费者一次拉取请求能从Kafka中拉取的最小数据量，默认为1（B）。Kafka在接收到消费者的拉取请求时，如果返回给消费者的数据量小于该值，那么它会一直等待，直到满足这个数据量为止。可以适当调大该值，不过也会导致额外的延迟
2. **fetch.max.bytes**：用来配置消费者在一次拉取请求中，从Kafka中拉取的最大数据量，默认50MB。
3. **fetch.max.wait.ms**：与fetch.min.bytes相关，指定消费者从Kafka拉取数据时，Kafka没有足够的数据返回，等待Kafka返回消息时间，默认500ms。
4. **max.partition.fetch.bytes**：配置从每个分区返回给消费者的最大数据量，默认1MB。
5. **max.pull.records**：配置消费者在一次拉取请求中，拉取的最大消息数，默认500。
6. **connections.max.idle.ms**：指定多久之后关闭闲置连接，默认9min。
7. **exclude.internal.topics**：指定Kafka中的内部主题是否可以向消费者公开，默认true，只能使用subscribe(Collection)方式订阅，如果为false，则没有限制（   ______consumer_offsets和 __transaction_state）
8. **request.timeout.ms**：配置消费者等待请求响应的最长时间，默认30s
9. **metadata.max.age.ms**：配置元数据过期时间，默认5min。如果元数据在该值限定范围内没有更新，则会被强制更新，即使没有任何分区变化和broker的加入
10. **isolation.level**：TODO
11. **heartbeat.interval.ms**：使用kafka分组管理功能时，心跳到消费者协调器的预计时间，用于确保消费者的会话保持活动状态。当有新的消费者加入或离开消费组时方便重新平衡。必须比session.timeout.ms小，通常不高于1/3。
12. **session.timeout.ms**：组管理协议中用来检测消费者是否失效的超时时间，默认10000
13. **max.pull.interval.ms**：当通过消费组管理消费者时，该配置指定拉取消息线程最长空闲时间，如果超过这个时间还没有发起poll操作，则认为消费者已离开消费组，进行rebalance，默认300000
14. **auto.offset.reset**：未指定消费位移时消费消息的策略，earliest、latest、none，其他值报错，默认lastest
15. **enable.auto.commit**：自动提交，默认true
16. **auto.commit.interval.ms**：指定自动提交消费位移的时间间隔，默认5000





### 存在的问题

#### 消息丢失

1. 如果消费者拉取到消息后就进行提交，但是在消费的过程中出现异常，在异常恢复后，重新拉取的消息中会跳过上次拉取的消息中异常发生后的那些消息（或者将拉取到的消息存入BlockingQueue，然后提交，另外一个线程A消费BlockingQueue的消息，如果线程A在消费过程中出现异常，则消息丢失）
2. 

#### 重复消费

1. 如果消费者在消费完消息后进行提交，但是在消息的消费过程中出现异常，异常恢复后，重新拉取的消息会包含重复消息
2. 消费者A消费完某个分区的一部分消息还没来得及提交消费位移时，发生rebalance，之后这个分区被分配给这个消费者组的另外一个消费者，另外一个消费者消费时会重复消费消费者A之前已经消费而未提交的消息（可以通过添加ConsumerRebalanceListener监听器，在rebalance发生前提交消费位移来解决）