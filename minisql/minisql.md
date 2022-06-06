https://git.zju.edu.cn/3180103721/minisql

https://www.yuque.com/yingchengjun/pcp6qx/kue938

https://zg3aga.yuque.com/hbre0c 我们的yuque

https://www.bilibili.com/video/BV1VL411w72p?p=5



参考 https://github.com/xingdl2007/cmu15-445

https://github.com/nefu-ljw/database-cmu15445-fall2020/blob/main/src/include/storage/index/index_iterator.h



disk manager的具体实现方式





1. allocatge page ,  新建了分区后 bitmap 不应该从磁盘中读出.
2. 数据映射用小数据查看是否正确.
3. 更新文档, 描述算法.对照自己的描述看一遍算法.
4. 不需要加 metapage 指针和current extent,  直接用num extent就可以.
5. 删掉在原本文件上加的注解.


写法:

1. 判断, 如果没有分区, 就新建分区. 


cpp 的报错实在太少了

debug心得

1. 需要自己加一些报错, 多加if  判断.  
2. 不要在乎时间复杂度. 完成就行. 不然很容易完不成. 

### disk manager

问题： 启动第二个dbinstance时，  allocate Allocate page 会把已经占用的page 当作空的再次分配。

解决方法： 之前没有写入metapage。

### index 迭代器

Begin是怎么获得首迭代器的?

怎么组织为单向链表?

叶节点存储的是rowid, 那么怎么获得rowid?

怎么找到下一个叶节点?

行（Row）：在src/include/record/row.h中被定义，与元组的概念等价，用于存储记录或索引键，一个Row由一个或多个Field构成。

```
#define INDEXITERATOR_TYPE IndexIterator<KeyType, ValueType, KeyComparator>
#define INDEX_TEMPLATE_ARGUMENTS template <typename KeyType, typename ValueType, typename KeyComparator>
class IndexIterator  就会传入INDEX_TEMPLATE_ARGUMENTS 
比如传入一个IndexIterator<GenericKey<32>, RowId, GenericComparator<32> 
```

#### 算法

首先得到最左边的叶子.

然后

叶子节点提供了什么?

```cpp
leaf.h
public:
  // After creating a new leaf page from buffer pool, must call initialize method to set default 
  void Init(page_id_t page_id, page_id_t parent_id = INVALID_PAGE_ID, int max_size = LEAF_PAGE_SIZE);
  // helper methods
  page_id_t GetNextPageId() const;
  void SetNextPageId(page_id_t next_page_id);
  KeyType KeyAt(int index) const;
  int KeyIndex(const KeyType &key, const KeyComparator &comparator) const;
  const MappingType &GetItem(int index);
  // insert and delete methods
  int Insert(const KeyType &key, const ValueType &value, const KeyComparator &comparator);
  bool Lookup(const KeyType &key, ValueType &value, const KeyComparator &comparator) const;
  int RemoveAndDeleteRecord(const KeyType &key, const KeyComparator &comparator);
  // Split and Merge utility methods
  void MoveHalfTo(BPlusTreeLeafPage *recipient);
  void MoveAllTo(BPlusTreeLeafPage *recipient);
  void MoveFirstToEndOf(BPlusTreeLeafPage *recipient);
  void MoveLastToFrontOf(BPlusTreeLeafPage *recipient);
//基类
     bool IsLeafPage() const;
  bool IsRootPage() const;
  void SetPageType(IndexPageType page_type);
  int GetSize() const;
  void SetSize(int size);
  void IncreaseSize(int amount);
  int GetMaxSize() const;
  void SetMaxSize(int max_size);
  int GetMinSize() const;
  page_id_t GetParentPageId() const;
  void SetParentPageId(page_id_t parent_page_id);
  page_id_t GetPageId() const;
  void SetPageId(page_id_t page_id);
  void SetLSN(lsn_t lsn = INVALID_LSN);
```

##### 用法

```cpp
//BPlusTreeIndexSimpleTest
IndexIterator<INDEX_KEY_TYPE, RowId, INDEX_COMPARATOR_TYPE> iter = index->GetBeginIterator();
  uint32_t i = 0;
  for (; iter != index->GetEndIterator(); ++iter) {
      //获得的是一个RowId inline page_id_t GetPageId() const { return page_id_; }
//  inline uint32_t GetSlotNum() const { return slot_num_; }
    ASSERT_EQ(1000, (*iter).second.GetPageId());// first是key
    ASSERT_EQ(i, (*iter).second.GetSlotNum());
    i++;
  }

  // index 的test
 BPlusTree<int, int, BasicComparator<int>> tree(0, engine.bpm_, comparator, 4, 4);
  int ans = 1;
  for (auto iter = tree.Begin(); iter != tree.End(); ++iter, ans += 2) {
    EXPECT_EQ(ans, (*iter).first);
    EXPECT_EQ(ans * 100, (*iter).second);
  }
```



问题:

## parser

```
execfile "sql.txt";
create database db0;
drop database db0;
show databases;
use db0;
show tables;
create table t2(d int, e char(25), f float);
create table t1(a int, b char(0) unique, c float, primary key(a, c));
drop table t1;
create index idx1 on t1(a, b);
-- "btree" can be replaced with other index types
create index idx1 on t1(a, b) using btree;
drop index idx1;
show indexes;
select * from t1;
select id, name from t1;
select * from t1 where id = 1;
-- note: use left association
select * from t1 where id = 1 and name = "str";
select * from t1 where id = 1 and name = "str" or age is null and bb not null;
insert into t1 values(1, "aaa", 2.33);
delete from t1;
delete from t1 where id = 1 and amount = 2.33;
update t1 set c = 3;
update t1 set a = 1, b = "ccc" where b = 2.33;
quit;

```

看图 http://dreampuf.github.io/GraphvizOnline/

#### 创建数据库

```
explicit DBStorageEngine(std::string db_name, bool init = true,
                         uint32_t buffer_pool_size = DEFAULT_BUFFER_POOL_SIZE)
                         // 新建一个数据库实例, 
                         // 存储在哪里?
```

问题:  idx = 0 , 1->100

delete有问题

```
for (; iter != index->GetEndIterator(); ++iter) {
!= 的时候 itr 为NULL, 所以报错.  
因为只有节点的时候node没有赋值.
```



问题

1. col name不对， 怀疑是 schema name最后没有截断\0

Field 加了截断\0 还是不对。 

create table传入之前是对的， 执行过程看了一遍也都是对的。

因为 columns，  在函数中用 memheap ， ALLOC_COLUMN 在函数结束的时候会释放内存。

发现的方法： gdb一步步看， 在函数return的时候会释放栈中的变量。

2. 从磁盘中读取db文件时  categories manager出错。

3. insert之后 select不对。 

因为const char *Type::GetData(const Field &val) const  方法没有实现。 

可能原因：  创建的时候没有确定type的类型， 所以是基类而不是子类。

因为char.getdata有独特的包装， 需要单独判断。  

但是怎么获得float 和int？ protected也不能直接访问 

头疼， float转char[ ] ， 还需要新开char数组。

4. ShowIndexes ，idx_info->GetIndexKeySchema()->GetColumns(); 是空的。 

优先级不高， 不显示也行。 

5. 
