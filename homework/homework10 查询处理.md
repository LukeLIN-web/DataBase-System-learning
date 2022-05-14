15.2

Consider the bank database of Figure 15.14, where the primary keys are underlined, and the following SQL query: 

```sql
select T.branch name 
from branch T, branch S 
where T.assets > S.assets and S.branch city = “Brooklyn” 
```

Write an efficient relational-algebra expression that is equivalent to this query. Justify your choice.

Query: 

![image-20220514172121480](/Users/juyilin/Library/Application Support/typora-user-images/image-20220514172121480.png)

我们要对尽可能少的数据执行 theta 连接。 先筛选出布鲁克林的 branch然后再连接，消除不需要的属性。

15.3

Let relations r1(A, B, C) and r2(C, D, E) have the following properties: r1 has 20,000 tuples, r2 has 45,000 tuples, 25 tuples of r1 fit on one block, and 30 tuples of r2 fit on one block. Estimate the number of block transfers and seeks required using each of the following join strategies for r1 ⋈ r2: 

r1 需要 800 个块，r2 需要 1500 个块。 如果有 M 页内存。 如果 M > 800，则可以在 1500 + 800 个磁盘中直接连接访问，甚至使用普通的嵌套循环连接。 所以我们只考虑 M ≤ 800 页。

a. Nested-loop join.  嵌套循环连接

 r1 作为outer关系，我们需要 20000 ∗ 1500 + 800 =
30、000、800 次磁盘访问，如果 r2 是外部关系，我们需要 45000 ∗
800 + 1500 = 36001500 次磁盘访问。

b. Block nested-loop join. 

c. Merge join. 

![image-20220514174011881](/Users/juyilin/Library/Application Support/typora-user-images/image-20220514174011881.png)

d. Hash join.

![image-20220514175540055](/Users/juyilin/Library/Application Support/typora-user-images/image-20220514175540055.png)

15.6

Consider the bank database of Figure 15.14, where the primary keys are underlined. Suppose that a B+-tree index on branch city is available on relation branch, and that no other index is available. 

List different ways to handle the following selections that involve negation: 

a. σ ¬(branch city<“Brooklyn”)(branch) 

定位到第一行branch_city字段有Brooklyn , 然后往下找到所有

b. σ ¬(branch city=“Brooklyn”)(branch) 

索引没有任何作用, 找到所有除了branch_city字段=brooklyn的 行.

c. σ ¬(branch city<“Brooklyn” ∨ assets<5000)(branch)

![image-20220514180725722](/Users/juyilin/Library/Application Support/typora-user-images/image-20220514180725722.png)

15.20

Estimate the number of block transfers and seeks required by your solution to Exercise 15.19 for r1 ⋈ r2, where r1 and r2 are as defined in Exercise 15.3.

15.19是Design a variant of the hybrid merge-join algorithm for the case where both relations are not physically sorted, but both have a sorted secondary index on the join attributes 为以下情况设计一个混合合并-连接算法的变体 关系不是物理排序的，但都有一个排序的二级索引在 连接属性上有一个排序的二级索引

