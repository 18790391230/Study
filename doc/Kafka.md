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



### 控制器

在kafka集群中会有一个或多个broker，其中一个broker会被选举为控制器。依赖zk，在/controller下创建临时节点，创建成功，则成为leader，即控制器。创建失败的broker会保存leader的brokerid

它负责管理整个集群和副本的状态，当某个分区的leader副本出现故障时，由控制器为该分区选举出新的leader副本

当检测到某个分区的ISR集合发生变化时，控制器负责通知所有的broker更新其元数据信息

/controller_epoch：控制器纪元，持久化节点，每个和控制器交互的请求都会携带controller_epoch，

### 分区leader选举

分区leader副本选举有控制器负责具体实施

当创建分区（创建主题和增加分区都有创建分区的动作）或分区上线（原来的leader副本下线，此时分区需要选举出一个新的leader上线对外提供服务）时都需要执行leader选举。

**选举策略**：按照AR集合中副本的顺序查找第一个存活的副本，并且这个副本在ISR集合中（只要不发生重分配的情况，集合内部副本的顺序是保持不变的，而分区的ISR中副本的顺序可能会改变）。



### 参数解密

1. **broker.id**：broker启动前必须配置的参数之一，在kafka集群中，每个broker都有一个唯一的brokerid区分彼此。broker启动时会在zk的/brokers/ids路径下创建一个以当前brokerId为名称的临时节点，其他broker或客户端通过判断路径下是否有此broker的brokerId节点来确定该broker的健康状态。

2. **bootstrap.servers**：是kafka producer、kafka consumer的必备参数；在kafka connect，streams，KafkaAdminClient也有涉及。

   - 这个参数配置的是用来发现Kafka集群元数据信息的服务地址

     客户端与Kafka集群连接需要经历3个过程

     - 客户端根据**bootstrap.servers**指定的Server连接，并发送**MetadataRequest**请求来获取集群的元数据信息
     - Server在收到请求后，返回**MetadataResponse**给客户端（包含了集群的元数据信息）
     - 客户端收到**MetadataResponse**后解析其中的元数据信息，然后与集群中的各个节点建立连接，之后就可以发送消息了



### 分区分配策略

1. **RangeAssignor**：范围分配。假设n=分区数/消费者数量，m=分区数%消费者数量，那么前m个消费者每个分配n+1个分区，后面的每个消费者分配n个分区。可能导致分配不均匀，比如：2个消费者消费2个主题，每个主题3个分区时，第一个消费者消费4个分区，第二个消费者消费2个分区，导致第一个消费者过载。

2. **RoundRbinAssignor**：将消费组内所有消费者及消费者订阅的所有主题的分区按照字典序排序，通过轮训逐个将分区分配给每个消费者（也可能会导致分配不均匀）

   ![image-20200711161258267](Kafka.assets/image-20200711161258267.png)

3. **StickyAssignor**：分区的分配尽可能均匀。分区的分配尽可能与上次的分配保持相同。当两者发生冲突时，第一个目标优先于第二个目标。

### 幂等

enable.idempotence设置为true

Kafka引入producer_id（PID）和序列号（sequencenumber）两个概念实现生产者幂等性。

每个生产者实例在初始化时都会被分配一个PID，这个PID对用户而言是透明的。对于每个PID，消息发送到每一个分区时都有对应的序列号，这些序列号从0开始单调递增。生产者每发送一条消息，就会将<PID, 分区>对应的序列号加1。

broker端会在内存中维护每一对<PID, 分区>维护一个序列号。对于收到的每一条消息，只有当它的序列号（SN_new）比broker端维护的序列号的值（SN_old）大1时，broker才会接收它。如果SN_new<SN_old+1，说明消息被重复写入，broker可以直接将其丢弃。SN_new > SN_old+1，说明中间有数据尚未写入，出现了乱序，可能有数据丢失，对应的生产者会抛出OutOfOrderSequenceException。

**Kafka的幂等只能保证单个生产者会话中单分区的幂等**。

## 可靠性探究

### 副本剖析

* 副本是相对于分区而言的，副本即是特定分区的副本

* 一个分区中包含一个或多个副本，其中一个是Leader副本，其他的是Follower副本，各副本位于不同的broker节点中。只有Leader副本对外提供服务，Follower副本只负责数据同步

* 分区中的所有副本统称为AR，ISR是指与leader副本保持同步状态的副本集合，leader副本本身也是ISR集合中的一员

* LEO标识每个分区中最后一条消息的下一个位置，分区中的每个副本都有自己的LEO，ISR集合中最小的LEO即为HW（高水位），消费者只能拉取到HW之前的消息

  从生产者发出的一条消息首先会写入分区的leader副本，不过还需要等到ISR集合中的所有follower副本都同步完之后才能认为是已提交，之后才会去更新HW，进而消费者才可以消费到这条消息

#### 失效副本

正常情况下，分区的所有副本都处于ISR集合中，当发生异常情况时，异常副本会被剥离出ISR集合。在ISR集合之外，也就是处于同步失效或功能失效（宕机）的副本统称为失效副本，失效副本对应的分区被称为同步失效分区。

**判定标准**：当ISR集合中的一个follower副本超过replica.lag.time.max.ms（默认10s）未追上leader，则认为同步时效，被剔除出ISR。当follower副本将leader副本的LEO之前的日志全部同步时，认为follower副本已经追上leader副本，此时更新该副本的lastCaughtUpTimeMs。

**实现原理**：Kafka副本管理器会启动一个副本过期检测的定时任务，定时检查当前时间与lastCaughtUpTimeMs是否大于replica.lag.time.max.ms。

#### ISR的伸缩

##### 缩容

Kafka在启动时会开启2个和ISR相关的定时任务。isr-expiration任务定时检测每个分区是否需要缩减其ISR集合，周期是replica.lag.time.max.ms/2。当检测到ISR集合中有失效副本时，收缩ISR集合。如果某个分区的ISR集合发生变更，会将变更记录到zk的/brokers/topics/< topic>/partition/< partition>/state节点中。节点数据示例如下

```json
{ "controller_epoch": 26, "leader": 0, "version": 1, "leader_epoch" :2, "isr": [0, l]}
//当前kafka控制器的epoch ，   当前分区leader副本所在brokerid              变化后的ISR列表
```

除此之外，当ISR集合发生变更时还会将变更后的记录缓存到isrChangSet中，isr-change-propagation定时任务会定时检查isrChangeSet（固定为2500ms），如果发现isrChangeSet中有ISR集合的变更记录，那么会在zk的 /isr_change_notification路径下创建以isr_change_开头的顺序持久节点。Kafka控制器为isr_change_notification添加了一个Watcher，当这个节点中有子节点变化时会触发Watcher动作，通知控制器更新相关元数据信息并向它管理的broker节点发送更新元数据的请求，最后删除路径下已被处理的节点。频繁触发Watcher会影响性能，当检测到ISR集合发生变化时，Kafka还会检查以下2个条件：1.上一次ISR集合发生变化距离现在已经超过5s。2.上一次写入zk的时间举例现在已经超过60s。满足上面条件任意一个才可以将ISR集合的变化写入目标节点。

##### 扩容

当follower副本的LEO不小于Leader副本的HW，就认为follower追上了leader。扩容同样会更新zk的/brokers/topics/< topic>/partition/< partition>/state节点和isrChangeSet，之后的步骤和缩容相同。

#### LEO与HW

假设某个分区有3个副本，分别位于broker0,1,2节点中，broker1是leader副本，消息追加过程如下

1. 生产者客户端发送消息到leader副本

2. 消息被追加到leader副本的本地日志，并更新日志的偏移量

3. follower副本向leader副本请求同步数据（follower副本的请求数据中包含自身的LEO）

4. leader副本读取本地日志，并更新对应拉取的follower副本的信息（更新自身保存的对应follower副本的LEO）

5. leader副本将拉取结果返回给follower副本（返回数据中包含消息数据及leader自身的HW）

6. follower副本收到leader副本返回的拉取结果，将消息追加到本地日志，并更新日志偏移量信息（更新自身LEO，与leader返回的HW对比，选一个较小的作为自身的HW）

   ![image-20200711200442851](Kafka.assets/image-20200711200442851.png)

   

LEO和HW在各副本中的维护情况

![image-20200711201120034](Kafka.assets/image-20200711201120034.png)

leader副本除了维护自身的LEO和HW外，还维护所有follower副本的LEO。follower副本仅维护自身的LEO及HW。