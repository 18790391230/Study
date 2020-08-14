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





### Cluster





#### 故障恢复

注意：只有master节点具有节点状态判断和参与leader选举的资格。同时redis集群中的cluster_size指的也是master的个数。

分为3部分

1. 故障发现

   PFail与Fail：redis节点每秒随机向其他节点发送一次PING，如果node_timeout/2时间内没有收到回复，会重新连接节点，如果连接超时，会将node状态更新为PFail，如果超过半数的master都标记这个节点为PFail，就更新这个节点的状态为Fail，向集群中所有节点发送Fail的这个节点的名字，强制其他节点将此节点状态更改为Fail

   ```c
    while((de = dictNext(di)) != NULL) {
           clusterNode *node = dictGetVal(de);
           now = mstime(); /* Use an updated time at every iteration. */
           mstime_t delay;
   
           // 当我们已经与对方节点建立了连接，同时我们向对方节点发送了PING命令，如果对方超时未回复
           // 有可能时当前节点与对方节点的连接出了问题，所以就重新建立连接
           if (node->link && /* is connected */
               now - node->link->ctime >
               server.cluster_node_timeout && /* was not already reconnected */
               node->ping_sent && /* we already sent a ping */
               node->pong_received < node->ping_sent && /* still waiting pong */
               /* and we are waiting for the pong more than timeout/2 */
               now - node->ping_sent > server.cluster_node_timeout/2)
           {
               /* 释放掉node->link,此时node->link=NULL， 这个结论在下面的链接重新建立会有用到. */
               freeClusterLink(node->link);
           }
       }
   ```

   ```c
   //PING失败，重新连接
   if (node->link == NULL) {
               clusterLink *link = createClusterLink(node);
               link->conn = server.tls_cluster ? connCreateTLS() : connCreateSocket();
               connSetPrivateData(link->conn, link);
               // 尝试再次建立连接
               if (connConnect(link->conn, node->ip, node->cport, NET_FIRST_BIND_ADDR,
                           clusterLinkConnectHandler) == -1) {
                   // 当建立连接失败时，记录连接建立失败的时刻
                   if (node->ping_sent == 0) node->ping_sent = mstime();
                   serverLog(LL_DEBUG, "Unable to connect to "
                       "Cluster Node [%s]:%d -> %s", node->ip,
                       node->cport, server.neterr);
   
                   freeClusterLink(link);
                   continue;
               }
               node->link = link;
           }
   ```

   标记为PFail

   ```c
    while((de = dictNext(di)) != NULL) {
           clusterNode *node = dictGetVal(de);
           now = mstime(); /* Use an updated time at every iteration. */
           mstime_t delay;
           // 计算节点3断连时间
           delay = now - node->ping_sent;
           // 如果节点3断连时间超过cluster_node_timeout， 则标记节点3为PFAIL
           if (delay > server.cluster_node_timeout) {
                   node->flags |= CLUSTER_NODE_PFAIL;
                   update_state = 1;
               }
           }
       }
   ```

   ```c
   void clusterProcessGossipSection(clusterMsg *hdr, clusterLink *link) {
       // 获取该条消息包含的节点数信息
       uint16_t count = ntohs(hdr->count);
       // clusterMsgDataGossip数组的地址
       clusterMsgDataGossip *g = (clusterMsgDataGossip*) hdr->data.ping.gossip;
       // 发送消息的节点
       clusterNode *sender = link->node ? link->node : clusterLookupNode(hdr->sender);
   
       // 遍历所有节点的信息
       while(count--) {
           // 获取节点的标识信息
           uint16_t flags = ntohs(g->flags);
           clusterNode *node;
   
           // 根据指定name从集群中查找并返回节点
           node = clusterLookupNode(g->nodename);
           // 如果node存在
           if (node) {
               // 如果发送者是主节点，且不是node本身
               if (sender && nodeIsMaster(sender) && node != myself) {
                   // 如果标识中指定了关于下线的状态
                   if (flags & (CLUSTER_NODE_FAIL|CLUSTER_NODE_PFAIL)) {
                       // 将sender的添加到node的故障报告中
                       if (clusterNodeAddFailureReport(node,sender)) {
                           serverLog(LL_VERBOSE,
                               "Node %.40s reported node %.40s as not reachable.",
                               sender->name, node->name);
                       }
                       // 判断node节点是否处于真正的下线FAIL状态
                       markNodeAsFailingIfNeeded(node);
                   // 如果标识表示节点处于正常状态
                   }else {
                       if (clusterNodeDelFailureReport(node,sender)) {
                           serverLog(LL_VERBOSE,
                               "Node %.40s reported node %.40s is back online.",
                               sender->name, node->name);
                       }
                   }
               }
       }
   }
   ```

   标记Fail状态

   ```c
   void markNodeAsFailingIfNeeded(clusterNode *node) {
       int failures;
       // 需要大多数的票数，超过一半的节点数量
       int needed_quorum = (server.cluster->size / 2) + 1;
       // 不处于pfail（需要确认是否故障）状态，则直接返回
       if (!nodeTimedOut(node)) return; /* We can reach it. */
       // 处于fail（已确认为故障）状态，则直接返回
       if (nodeFailed(node)) return; /* Already FAILing. */
       // 返回认为node节点下线（标记为 PFAIL or FAIL 状态）的其他节点数量
       failures = clusterNodeFailureReportsCount(node);
       // 如果当前节点是主节点，也投一票
       if (nodeIsMaster(myself)) failures++;
       // 如果报告node故障的节点数量不够总数的一半，无法判定node是否下线，直接返回
       if (failures < needed_quorum) return; /* No weak agreement from masters. */
   
       serverLog(LL_NOTICE, "Marking node %.40s as failing (quorum reached).", node->name);
   
       // 取消PFAIL，设置为FAIL
       node->flags &= ~CLUSTER_NODE_PFAIL;
       node->flags |= CLUSTER_NODE_FAIL;
       // 并设置下线时间
       node->fail_time = mstime();
   
       // 广播下线节点的名字给所有的节点，强制所有的其他可达的节点为该节点设置FAIL标识
       if (nodeIsMaster(myself)) clusterSendFail(node->name);
       clusterDoBeforeSleep(CLUSTER_TODO_UPDATE_STATE|CLUSTER_TODO_SAVE_CONFIG);
   }
   ```

   

2. leader选举

   当slave收到对应master下线的Fail广播时，发送*CLUSTERMSG_TYPE_FAILOVER_AUTH_REQUEST*到集群中所有的节点，当收到过半的其他master节点的*FAILOVER_AUTH_ACK*回应后，自己成为leader，开始执行failover

   选举算法采用raft，在拉选票之前先休眠一段时间：

   > ```text
   > DELAY = 500 milliseconds + random delay between 0 and 500 milliseconds + SLAVE_RANK * 1000 milliseconds.
   > ```
   >
   > 前面的500ms是为了确保所有的node都接收到了fail消息，SLAVE_RANK是根据slave同步master的offset计算的，offset越大，SLAVE_RANK越小，也就越小醒来拉选票

   集群中的master接收到拉选票消息后会判断这个slave是否有资格成为leader

   ```c
    /* 计算与Master节点上次通信过去的时间*/
       if (server.repl_state == REPL_STATE_CONNECTED) {
           data_age = (mstime_t)(server.unixtime - server.master->lastinteraction)
                      * 1000;
       } else {
           data_age = (mstime_t)(server.unixtime - server.repl_down_since) * 1000;
       }
       if (data_age >server.repl_ping_slave_period * 1000+
           (server.cluster_node_timeout * server.cluster_slave_validity_factor)){
           该节点不具备参与竞选资格
       }
   ```

   ```c
    // 发现自己获得了超过半数的集群节点的投票
       if (server.cluster->failover_auth_count >= needed_quorum) {
           serverLog(LL_WARNING,"Failover election won: I'm the new master.");
   
           if (myself->configEpoch < server.cluster->failover_auth_epoch) {
               myself->configEpoch = server.cluster->failover_auth_epoch;
               serverLog(LL_WARNING,
                   "configEpoch set to %llu after successful failover",
                   (unsigned long long) myself->configEpoch);
           }
           // 执行自动或手动故障转移，从节点获取其主节点的哈希槽，并传播新配置
           clusterFailoverReplaceYourMaster();
       } else {
           clusterLogCantFailover(CLUSTER_CANT_FAILOVER_WAITING_VOTES);
       }
   ```

   

3. 配置更新

一旦有从节点赢得选举，就会通过PING和PONG数据包向其他节点宣布自己是master，发送自己负责的槽位，设置configEpoch为currentEpoch（选举开始时生成的），为了加速配置更新，该节点会发送PONG包到集群所有节点，其他节点发送有新的节点（带着更大的Epoch）负责处理之前的一个旧的master负责的slots，会更新自己的配置



```c
typedef struct clusterNodeFailReport {
    struct clusterNode *node;  /* Node reporting the failure condition. */
    mstime_t time;             /* Time of the last report from this node. */
} clusterNodeFailReport;

typedef struct clusterNode {
    mstime_t ctime; /* Node object creation time. */
    char name[CLUSTER_NAMELEN]; /* Node name, hex string, sha1-size */
    int flags;      /* CLUSTER_NODE_... */
    uint64_t configEpoch; /* Last configEpoch observed for this node */
    unsigned char slots[CLUSTER_SLOTS/8]; /* slots handled by this node */
    int numslots;   /* Number of slots handled by this node */
    int numslaves;  /* Number of slave nodes, if this is a master */
    struct clusterNode **slaves; /* pointers to slave nodes */
    struct clusterNode *slaveof; /* pointer to the master node. Note that it
                                    may be NULL even if the node is a slave
                                    if we don't have the master node in our
                                    tables. */
    mstime_t ping_sent;      /* Unix time we sent latest ping */
    mstime_t pong_received;  /* Unix time we received the pong */
    mstime_t fail_time;      /* Unix time when FAIL flag was set */
    mstime_t voted_time;     /* Last time we voted for a slave of this master */
    mstime_t repl_offset_time;  /* Unix time we received offset for this node */
    mstime_t orphaned_time;     /* Starting time of orphaned master condition */
    long long repl_offset;      /* Last known repl offset for this node. */
    char ip[NET_IP_STR_LEN];  /* Latest known IP address of this node */
    int port;                   /* Latest known clients port of this node */
    int cport;                  /* Latest known cluster port of this node. */
    clusterLink *link;          /* TCP/IP link with this node */
    list *fail_reports;         /* List of nodes signaling this as failing */
} clusterNode;

typedef struct clusterState {
    clusterNode *myself;  /* This node */
    uint64_t currentEpoch;
    int state;            /* CLUSTER_OK, CLUSTER_FAIL, ... */
    int size;             /* Num of master nodes with at least one slot */
    dict *nodes;          /* Hash table of name -> clusterNode structures */
    dict *nodes_black_list; /* Nodes we don't re-add for a few seconds. */
    clusterNode *migrating_slots_to[CLUSTER_SLOTS];
    clusterNode *importing_slots_from[CLUSTER_SLOTS];
    clusterNode *slots[CLUSTER_SLOTS];
    uint64_t slots_keys_count[CLUSTER_SLOTS];
    rax *slots_to_keys;
    /* The following fields are used to take the slave state on elections. */
    mstime_t failover_auth_time; /* Time of previous or next election. */
    int failover_auth_count;    /* Number of votes received so far. */
    int failover_auth_sent;     /* True if we already asked for votes. */
    int failover_auth_rank;     /* This slave rank for current auth request. */
    uint64_t failover_auth_epoch; /* Epoch of the current election. */
    int cant_failover_reason;   /* Why a slave is currently not able to
                                   failover. See the CANT_FAILOVER_* macros. */
    /* Manual failover state in common. */
    mstime_t mf_end;            /* Manual failover time limit (ms unixtime).
                                   It is zero if there is no MF in progress. */
    /* Manual failover state of master. */
    clusterNode *mf_slave;      /* Slave performing the manual failover. */
    /* Manual failover state of slave. */
    long long mf_master_offset; /* Master offset the slave needs to start MF
                                   or zero if stil not received. */
    int mf_can_start;           /* If non-zero signal that the manual failover
                                   can start requesting masters vote. */
    /* The followign fields are used by masters to take state on elections. */
    uint64_t lastVoteEpoch;     /* Epoch of the last vote granted. */
    int todo_before_sleep; /* Things to do in clusterBeforeSleep(). */
    /* Messages received and sent by type. */
    long long stats_bus_messages_sent[CLUSTERMSG_TYPE_COUNT];
    long long stats_bus_messages_received[CLUSTERMSG_TYPE_COUNT];
    long long stats_pfail_nodes;    /* Number of nodes in PFAIL status,
                                       excluding nodes without address. */
} clusterState;
```

