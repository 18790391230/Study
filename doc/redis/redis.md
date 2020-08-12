### String

#### 普通字符串





#### 数字







#### 二进制（图片等）



#### Bitmap

```shell
SETBIT key1 1 1   #开辟一个字节的空间，然后设置第一位是1

```



session， 计数器， 存放小文件





### Hash

```shell
HGETALL key
HKEYS key
HVALS key
HINCRBY key field val
```

场景：详情页，收藏数，





### SET

```shell
SRANDMEMBER key val  # 传一个大于0的数，返回这个数量的成员，小于0，则返回会重复的成员
```

场景：随机，交集，差集，并集

共同好友，好友推荐



### ZSET

场景：排行榜，评论排序（分页）



### 小命令

```shell
 man ascii
 man utf-8
 help @list
```





```c
typedef struct redisDb{
    int id;
    long long avg_ttl;   //存储数据库对象的平均ttl，用于统计
    
    dict *dict;          //数据库键值对
    dict *expires;       //存储键的过期时间
    dict *blocking_keys; //当使用BLPOP阻塞获取列表元素时，如果列表为空，会阻塞客户端，同时将列表key记录在该dict中，当使用PUSH命令向该列表添加元素时会从该dict中查找该列表键，如果找到说明有客户端正在阻塞获取此列表键，于是将列表键记录记录到ready_keys,以便后续响应正在阻塞的客户端
    dict *ready_keys;
    dict *watched_keys;  //key-value分别为键与客户端对象，用来实现事务，对于写操作，会遍历，如果找到要操作的键被客户端监控，则设置该客户端状态为dirty，当服务端收到exec命令时，拒绝执行
} reidsDb;
```



```c
void activeExpireCycle(void) {
    /*因为每次调用activeExpireCycle函数不会一次性检查所有Redis数据库，所以需要记录下
        每次函数调用处理的最后一个Redis数据库的编号，这样下次调用activeExpireCycle函数
        还可以从这个数据库开始继续处理，这就是current_db被声明为static的原因，而另外一
        个变量timelimit_exit是为了记录上一次调用activeExpireCycle函数的执行时间是否达
        到时间限制了，所以也需要声明为static
    */
    static unsigned int current_db = 0;
    static int timelimit_exit = 0;
    unsigned int j, iteration = 0;
 
    /**
        每次调用activeExpireCycle函数处理的Redis数据库个数为REDIS_DBCRON_DBS_PER_CALL
        unsigned int dbs_per_call = REDIS_DBCRON_DBS_PER_CALL;
        long long start = ustime(), timelimit;
        如果当前Redis服务器中的数据库个数小于REDIS_DBCRON_DBS_PER_CALL，则处理全部数据库，
        如果上一次调用activeExpireCycle函数的执行时间达到了时间限制，说明失效主键较多，也
        会选择处理全部数据库
    */
    if (dbs_per_call > server.dbnum || timelimit_exit)
        dbs_per_call = server.dbnum;
 
    /*
        执行activeExpireCycle函数的最长时间（以微秒计），其中REDIS_EXPIRELOOKUPS_TIME_PERC
        是单位时间内能够分配给activeExpireCycle函数执行的CPU时间比例，默认值为25，server.hz
        即为一秒内activeExpireCycle的调用次数，所以这个计算公式更明白的写法应该是这样的，即
            (1000000 * (REDIS_EXPIRELOOKUPS_TIME_PERC / 100)) / server.hz
    */
    timelimit = 1000000*REDIS_EXPIRELOOKUPS_TIME_PERC/server.hz/100;
    timelimit_exit = 0;
    if (timelimit <= 0) timelimit = 1;
 
    //遍历处理每个Redis数据库中的失效数据
    for (j = 0; j < dbs_per_call; j++) {
        int expired;
        redisDb *db = server.db+(current_db % server.dbnum);
      // 此处立刻就将current_db加一，这样可以保证即使这次无法在时间限制内删除完所有当前
      // 数据库中的失效主键，下一次调用activeExpireCycle一样会从下一个数据库开始处理，
       //从而保证每个数据库都有被处理的机会
        current_db++;
       // 开始处理当前数据库中的失效主键
        do {
            unsigned long num, slots;
            long long now;
           // 如果expires字典表大小为0，说明该数据库中没有设置失效时间的主键，直接检查下
          // 一数据库
            if ((num = dictSize(db->expires)) == 0) break;
            slots = dictSlots(db->expires);
            now = mstime();
          //  如果expires字典表不为空，但是其填充率不足1%，那么随机选择主键进行检查的代价
           //会很高，所以这里直接检查下一数据库
            if (num && slots > DICT_HT_INITIAL_SIZE &&
                (num*100/slots < 1)) break;
            expired = 0;
            //如果expires字典表中的entry个数不足以达到抽样个数，则选择全部key作为抽样样本
            if (num > REDIS_EXPIRELOOKUPS_PER_CRON)
                num = REDIS_EXPIRELOOKUPS_PER_CRON;
            while (num--) {
                dictEntry *de;
                long long t;
              //  随机获取一个设置了失效时间的主键，检查其是否已经失效
                if ((de = dictGetRandomKey(db->expires)) == NULL) break;
                t = dictGetSignedIntegerVal(de);
                if (now > t) {
           // 发现该主键确实已经失效，删除该主键
                    sds key = dictGetKey(de);
                    robj *keyobj = createStringObject(key,sdslen(key));
                    //同样要在删除前广播该主键的失效信息
                    propagateExpire(db,keyobj);
                    dbDelete(db,keyobj);
                    decrRefCount(keyobj);
                    expired++;
                    server.stat_expiredkeys++;
                }
            }
           // 每进行一次抽样删除后对iteration加一，每16次抽样删除后检查本次执行时间是否
          // 已经达到时间限制，如果已达到时间限制，则记录本次执行达到时间限制并退出
            iteration++;
            if ((iteration & 0xf) == 0 &&
                (ustime()-start) > timelimit)
            {
                timelimit_exit = 1;
                return;
            }
        //如果失效的主键数占抽样数的百分比大于25%，则继续抽样删除过程
        } while (expired > REDIS_EXPIRELOOKUPS_PER_CRON/4);
    }
}
```







https://developer.aliyun.com/article/38441







#### slave

replicationCron()每1s执行一次，检测主从连接是否超时，定时向主服务器发送心跳包，上报自己的复制偏移量（REPLCONF ACK <reploff>），如果超过repl_timeout,默认60s，slave会自动断开。主服务器通过repl_ack_time保存接收到该命令的时间，一次作为检测从服务器是否有效的标准





repl-ping-replica-period默认为10，master向slave发送心跳包

repl_good_slaves_count:当前有效slave个数，定时检测now() - repl_ack_time，如果超过min-slaves-max-lag（默认10s），就认为slave失效

repl_min_slaves_to_write：有效slave小于这个值时，master拒绝执行写请求

slave-read-only：slave只读，默认1

