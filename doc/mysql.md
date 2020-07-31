* 锁定粒度
* 加锁效率
* 冲突概率
* 并发性能





lock tablesxxx read;

lock tables xxx write;

unlock tables;

* 隔离性实现：基于mvcc的多版本控制和基于锁的并发控制
* 原子性实现：undo log
* 持久性实现：redo log和double write双写缓冲（页的副本）



| 隔离级别                  | 脏读(Dirty Read) | 不可重复读（NonRepeatable Read） | 幻读（Phantom Read）               |
| ------------------------- | ---------------- | -------------------------------- | ---------------------------------- |
| 读未提交(Read uncommited) | 可能             | 可能                             | 可能                               |
| 读已提交(Read commited)   | 不可能           | 可能                             | 可能                               |
| 可重复读(Repeatable read) | 不可能           | 不可能                           | **InnoDB不可能（使用Next-key锁）** |
| 可串行化(Serializable)    | 不可能           | 不可能                           | 不可能                             |



### 优化

#### 客户端

* 连接池大小
* 表设计
  * 字段长度选择
  * 存储引擎
  * 字段拆分为多个表

#### 服务器

* 缓冲池大小
* 最大连接数
* 主从

SQL与索引

存储引擎与表结构

数据库架构

MySQL配置

硬件与操作系统

### 主从

* 异步复制
* 同步复制
* 半同步复制：有一个slave写入reloy完成，则认为完成（需要安装插件，semisync_slave.so， semisync_master.so）

```sql
INSTALL PLUGIN rpl_semi_sync_master SONAME 'semisync_master.so'
```



查看服务器运行线程状态：show processlist



