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



#### 算法

首先得到最左边的叶子.

然后



叶子节点提供了什么?

```cpp
public:
  // After creating a new leaf page from buffer pool, must call initialize
  // method to set default values
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
```

