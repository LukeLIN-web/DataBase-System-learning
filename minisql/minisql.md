https://git.zju.edu.cn/3180103721/minisql

https://www.yuque.com/yingchengjun/pcp6qx/kue938

https://zg3aga.yuque.com/hbre0c 我们的yuque

https://www.bilibili.com/video/BV1VL411w72p?p=5

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

