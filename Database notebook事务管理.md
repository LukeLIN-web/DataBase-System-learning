#  Database notebook

作业答案： https://wenku.baidu.com/view/69529f1aaf1ffc4ffe47acd9.html

### 索引

假如你现在看一本书，首先肯定会先看书的目录，看看这本书到底有哪些内容，然后通过目录找到自己感兴趣的章节进行阅读

这里的书就相当于数据库中的表，目录就相当于索引，查询表中的数据通过索引可以快速找到对应的数据

索引的数据结构是B+树，这里的B指的是balance（平衡）

```sql
//普通索引
alter table table_name add index index_name (column_list) ;
//唯一索引
alter table table_name add unique (column_list) ;
//主键索引
alter table table_name add primary key (column_list) ;
一个主键可以有多个列.学生的名字、年龄、班级都可能重复，无法使用单个字段来唯一标识，这时，我们可以将多个字段设置为主键，形成复合主键，这多个字段联合标识唯一性
复合主键就是含有一个以上的字段组成,如ID+name,ID+phone等,而联合主键要同时是两个表的主题组合起来的。这是和复合主键最大的区别
```

```sql
drop index index_name on table_name ;
alter table table_name drop index index_name ;
alter table table_name drop primary key ;
```

 建表时，LOGIN_NAME长度为100，这里用16，是因为一般情况下名字的长度不会超过16，这样会加快索引查询速度，还会减少索引文件的大小，提高INSERT，UPDATE的更新速度。

​    如果分别给LOGIN_NAME,CITY,AGE建立单列索引，让该表有3个单列索引，查询时和组合索引的效率是大不一样的，甚至远远低于我们的组合索引。虽然此时有三个索引，但mysql只能用到其中的那个它认为似乎是最有效率的单列索引，另外两个是用不到的，也就是说还是一个全表扫描的过程。

建立这样的组合索引，就相当于分别建立如下三种组合索引：

```
LOGIN_NAME,CITY,AGE
LOGIN_NAME,CITY
LOGIN_NAME
```

　　为什么没有CITY,AGE等这样的组合索引呢？这是因为mysql组合索引“最左前缀”的结果。简单的理解就是只从最左边的开始组合，并不是只要包含这三列的查询都会用到该组合索引。也就是说**name_city_age(LOGIN_NAME(16),CITY,AGE)从左到右进行索引，如果没有左前索引，mysql不会执行索引查询**。

   如果索引列长度过长,这种列索引时将会产生很大的索引文件,不便于操作,可以使用前缀索引方式进行索引，前缀索引应该控制在一个合适的点,控制在0.31黄金值即可(大于这个值就可以创建)。

### 事务

atomic原子性: 要么全部完成, 要么全部失败. 

一致性consistent:   转账前后两个金额的和应该保持不变.

isolation隔离性: 一个事务感受不到另一个事务在并发执行.

durability持久性: 数据库崩溃后可以回到之前的状态. 

commit, 事务启动后的更改被写入磁盘.

rollback, 事务启动后的更改 回到开始前的状态.

Atomic: Either all complete or all fail.

Consistent: The sum of the two amount of account  before and after the transfer should remain the same.

Isolation: One transaction cannot feel that another transaction is executing concurrently.

Durability: After the database crashes, it can return to the previous state.

commit, the changes after the transaction started are written to disk.

rollback, the changes after the transaction started return to the state before the start.

#### 什么情况会用到事务? 

事务一般用在并发操作多张表的时候使用，用于保护用户数据的完整性。或者说，事务是在对数据进行操作，并且确定两种操作同时成立时运用，这样做的目的就是保证两个操作都正确，都达到目的，只要一方出错，就会回滚数据，保证了两个操作的安全。

事务的五个状态:

active  执行中

partially committed部分提交, 最后一个操作执行完成, 但是没有变更刷新到磁盘.

fail失败的, 事务无法继续执行. 

abort中止的, 失败,回滚操作完毕, 恢复到执行前状态 

committed  从partially committed->committed, 修改过的数据都同步到磁盘后, 就是committed

脏写: 一个事务A修改了其他事务B未提交的数据. B回滚了, A写的也就没了.

脏写的问题太严重了，任何隔离级别都必须避免。其它无论是脏读，不可重复读，还是幻读，它们都属于数据库的读一致性的问题，都是在一个事务里面前后两次读取出现了不一致的情况。

脏读: 一个事务读到了其他事务未提交的数据.

不可重复读（Non-Repeatable Read）: **不可重复读指的是在一个事务执行过程中，读取到其它事务已提交的数据，导致两次读取的结果不一致**。

幻读（Phantom） **幻读是指的是在一个事务执行过程中，读取到了其他事务新插入数据，导致两次读取的结果不一致**。

mysql会自动为增删改语句加事务

不可重复读和幻读的区别在于**不可重复读是读到的是其他事务修改或者删除的数据，而幻读读到的是其它事务新插入的数据**。

`InnoDB`支持四个隔离级别（和`SQL`标准定义的基本一致）。隔离级别越高，事务的并发度就越低。唯一的区别就在于，`**InnoDB**` **在`可重复读（REPEATABLE READ）`的级别就解决了幻读的问题**。这也是`InnoDB`使用`可重复读` 作为事务默认隔离级别的原因。

### MVCC

- **版本链**

在`InnoDB`中，每行记录实际上都包含了两个隐藏字段：事务id(`trx_id`)和回滚指针(`roll_pointer`)。

1. `trx_id`：事务id。每次修改某行记录时，都会把该事务的事务id赋值给`trx_id`隐藏列。
2. `roll_pointer`：回滚指针。每次修改某行记录时，都会把`undo`日志地址赋值给`roll_pointer`隐藏列。

`InnoDB`通过`ReadView`实现了这个功能

事务并发访问同一数据资源的情况主要就分为`读-读`、`写-写`和`读-写`三种。

1. `读-读` 即并发事务同时访问同一行数据记录。由于两个事务都进行只读操作，不会对记录造成任何影响，因此并发读完全允许。
2. `写-写` 即并发事务同时修改同一行数据记录。这种情况下可能导致`脏写`问题，这是任何情况下都不允许发生的，因此只能通过`加锁`实现，也就是当一个事务需要对某行记录进行修改时，首先会先给这条记录加锁，如果加锁成功则继续执行，否则就排队等待，事务执行完成或回滚会自动释放锁。
3. `读-写` 即一个事务进行读取操作，另一个进行写入操作。这种情况下可能会产生`脏读`、`不可重复读`、`幻读`。最好的方案是**读操作利用多版本并发控制（`MVCC`），写操作进行加锁**。

## lec14 并发控制

2个操作是有冲突的，则二者执行次序不可交换。     若2个操作不冲突，则可以交换次序

### 可恢复性

Recoverable schedule

Cascadeless schedules — cascading rollbacks cannot occur;

数据库并发控制原理 - Smith的文章 - 知乎 https://zhuanlan.zhihu.com/p/464283526

### 2阶段锁协议

一个阶段只加锁, growing , 一个阶段只释放锁。shrinking.

满足2阶段锁协议肯定是可以串行化的.2阶段锁比较严格, 很多时候不用这个协议也不一定有问题. 

没有循环就是冲突可串行化的.  可以证明和串行调度等价.  

冲突可串行化不一定满足2阶段锁协议

数据库中有个锁表, 赋予锁和收回锁. 考试可能就给你个锁表.

例

1。画出precedence graph for the schedule ， 前面获得的指向后面获得的。 

2. 是否冲突串行化?
3. 是否有可能由2PL protocol Chans这个schedule? 请解释.

cascadeless schedule

## lec15 数据库恢复

考点

WAL (write-ahead logging)

undo & redo, compensation log

checkpoint, fuzzy checkpoint

dump

remote backup

logical undo (& physical redo), operation logging

ARIES (analysis, redo, undo)

#### 事务故障恢复

1. 扫描日志， 找到更新操作
2. undo 所有更新操作
3. 直到读到事务的开始标记

#### 系统故障 

一些提交的事务还留在缓冲区。 可能停电或CPU故障

1. 正向扫描日志， 找出已经提交的事务（有begin 和commit） 把他们放入redo 队列找出未完成的事务（有begin 没有commit）放入undo 队列
2. 处理undo队列， 把日志记录中“更新前的值”写入
3. 处理redo 队列， 把日志记录中“更新后的值”写入

#### media 故障

磁盘数据破坏

1. 装入最新的backup 数据库副本， 
2. 装入日志文件副本，正向扫描日志， 找出已经提交的事务（有begin 和commit） 把他们放入redo 队列
3. 处理redo 队列， 把日志记录中“更新后的值”写入



#### 数据转存储

##### 转储类型

静态转储，转储期间不允许对数据库进行操作， 优点是简单， 保持一致性， 缺点是需要等待。

动态转储， 转储期间允许对数据库进行操作， 优点是效率高， 缺点是不能保持一致性， 要记录日志文件。

##### 转储方式

海量转储

增量转储

##### 登记日志的原则

1. 登记次序必须严格按并发事务执行的时间次序。
2. 必须先写日志文件， 后写数据库。



#### checkpoint 机制

用在log based recovery schemas 来减少recover时间。 

##### checkpoint记录的内容

1. 此时正在执行的事务， 也就是active的事务
2. 这些食物最近一个日志记录的地址

搜索整个日志很费时间， all 事务要undo redo from the log。 在checkpoint之前的log 可以recover的时候不用搜索。

第二个原因是当stable storage 满了的时候清理log 。

##### 恢复步骤

1. 找到最后一个checkpoint 在日志文件中的地址
2. 由该地址在日志文件中找到最后一个检查点记录
3. 从检查点开始正向扫描日志文件， 直到日志文件结束。

#### ARIES

`Algorithm for Recovery and Isolation Exploiting Semantics(ARIES)`。如今`ARIES`成为了一种事实标准，几乎所有工业级数据库都将`ARIES`作为自己的故障恢复算法。

##### LSN

Uses a log sequence number (LSN) to identify log records and stores LSNs in database pages to identify which operations have been applied to a database page. 用来标记每一条log record。 一定是顺序递增的。

Page LSN ， 最新对事务修改的 LSN

Rec LSN， 进入内存后最早对他修改的LSN。 recent LSN。

经历了RecLSN 到PageLSN的操作， 还没有stable。

##### physiological redo

`Physical Log`：记录数据更新前后所在`Page`的地址，在`Page`内的偏移，以及具体的字节内容，优势是回放速度快，容易`Repeat History`。

`Logical Log`：记录被修改数据的逻辑地址(比如主键值)，对于数据内容，可以记录逻辑操作，比如对值操作`inc 1`，优势是占据空间小，有更好的并发效率，允许`Page`内未`Commit`的内容被其他事务移动到其他`Page`的情况下仍能正常`Rollback`。

`Physiological Log`：`Physical Log`和`Logical Log`各有优劣的情况下诞生的产物，比如记录修改某个`Page`内第`#slot`个`tuple`的第`#n`列。

逻辑操作中间的物理操作都可以不做， 就是只做一个逻辑操作。 逻辑日志， 可以提高并发度。

##### dirty page table

以减少恢复过程中不必要的重做。如前所述，脏页是指那些已经在内存中更新的页面，而磁盘上的版本不是最新的。

##### fuzzy-checkpointing 

只记录脏页的信息和相关信息，甚至不需要脏页写入磁盘。它在后台持续地刷新脏页，抽空写入， 而不是在检查点期间写入脏页。

如果一个页面在分析过程开始时不在检查点脏页表中，检查点记录之前的重做记录不需要应用于它，因为这意味着该页面在检查点之前已经被刷到磁盘并从DirtyPageTable中移除。然而，该页可能在检查点之后被更新，这意味着它将在分析过程结束时出现在脏页表中。对于出现在检查点脏页表中的页，检查点之前的重做记录可能也需要被应用。

##### checkpoint

记录了dirty page table ， 和active 事务。每个事务的last LSN。 commit了就从active 事务表中去除。

还有每个page的pageLSN。

##### 三个pass

analysis pass： 确定哪些？从last checkpoint到end of log

1. undolist 
2. which page were dirty。 得到最新的dirty page table
3. Redo LSN； LSN from which redo should start。 redo是所有RscLSN中的最小值。

Redo pass：

1. repeat history ， redoing all actions from redo LSN。 

   Rec LSN 和page LSN 可以优化， 不用redo已经在page中的action。

Undo pass：

1. 回滚所有incomplete transactions 

