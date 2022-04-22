7.1, 

Suppose that we decompose the schema R = (A, B, C, D, E) into 

(A, B, C) (A, D, E). 

Show that this decomposition is a lossless decomposition if the following set F of functional dependencies holds: 

A → BC 

CD → E 

B → D 

E → A

怎么证明是无损分解?

分解成R1和R2  公共属性要么是R1 , 要么是R2. 要确保有公共属性. 这样就是无损连接

交集是A, A决定BC.  A 决定B, A决定C, A决定D, A决定CD 传递 A决定E.

A 是候选码, R1 交R2 函数决定R1



7.3, 

Explain how functional dependencies can be used to indicate the following: 

• A one-to-one relationship set exists between entity sets student and instructor. 

• A many-to-one relationship set exists between entity sets student and instructor.

student -> instructor

S1s2s3 -> instructor





7.21, 

Give a lossless decomposition into BCNF of schema R of Exercise 7.1

B->D. 

{(A,B,C,E), (B,D)}是BCNF的.



7.22, 

Give a lossless, dependency-preserving decomposition into 3NF of schema R of Exercise 7.1.



7.29

Show that the following decomposition of the schema R of Exercise 7.1 is not a lossless decomposition: (A, B, C) (C, D, E). Hint: Give an example of a relation r(R) such that ΠA, B, C (r) ⋈ ΠC, D, E (r) ≠ r
