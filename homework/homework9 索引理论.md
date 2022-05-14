



14.3  Construct a B+-tree for the following set of key values: (2, 3, 5, 7, 11, 17, 19, 23, 29, 31) 680 Chapter 14 Indexing Assume that the tree is initially empty and values are added in ascending order. Construct B+-trees for the cases where the number of pointers that will fit in one node is as follows: 

a. Four 



分裂的时候右边3个左边2个



c. Eight







14.4

For each B+-tree of Exercise 14.3, show the form of the tree after each of the following series of operations: 对于练习 14.3 的每个 B+ 树，在以下每个系列操作之后显示树的形式：

a. Insert 9. b. Insert 10. c. Insert 8. d. Delete 23. e. Delete 19







14.11

In write-optimized trees such as the LSM tree or the stepped-merge index, entries in one level are merged into the next level only when the level is full. Suggest how this policy can be changed to improve read performance during periods when there are many reads but no updates.

在 LSM 树或步进合并索引等写优化树中，只有在该级别已满时，才会将一个级别中的条目合并到下一个级别。 建议如何更改此策略以提高读取次数多但没有更新期间的读取性能。



24.10
