15.6

Consider the bank database of Figure 15.14, where the primary keys are underlined. Suppose that a B+-tree index on branch city is available on relation branch, and that no other index is available. List different ways to handle the following selections that involve negation: 

a. σ ¬(branch city<“Brooklyn”)(branch) 

定位到第一行branch_city字段有Brooklyn , 然后往下找到所有 

b. σ ¬(branch city=“Brooklyn”)(branch)

从左找到布鲁克林, 从布鲁克林找到右, 排除布鲁克林

 c. σ ¬(branch city<“Brooklyn” ∨ assets<5000)(branch)

This query is equivalent to the query σ(branch city≥′Brooklyn′ ∧ assets>5000)(branch) 

找到布鲁克林, 然后从左找到右,  并且判断assets >=5000的结果



16.5

Consider the relations r1(A, B, C), r2(C, D, E), and r3(E, F), with primary keys A, C, and E, respectively. Assume that r1 has 1000 tuples, r2 has 1500 tuples, and r3 has 750 tuples. Estimate the size of r1 ⋈ r2 ⋈ r3, and give an efficient strategy for computing the join

考虑关系r1(A, B, C), r2(C, D, E), 和r3(E, F), 分别具有主键A, C, 和E。假设r1有1000行，r2有1500行，r3有750行。估计r1⋈r2⋈r3的大小，并给出计算连接的有效策略。



16.16

Suppose that a B+-tree index on (dept name, building) is available on relation department. What would be the best way to handle the following selection? 

σ(building < “Watson”) ∧ (budget < 55000) ∧ (dept name = “Music”)(department)



```
branch(branch name, branch city, assets) customer (customer name, customer street, customer city) loan (loan number, branch name, amount) borrower (customer name, loan number) account (account number, branch name, balance ) depositor (customer name, account number) 

Figure 16.9 Banking database.
```



16.20

Explain how to use a histogram to estimate the size of a selection of the form σA≤v(r).

解释如何使用直方图来估计形式为σA≤v(r)的选择的大小。
