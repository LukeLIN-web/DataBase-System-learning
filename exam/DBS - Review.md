# DBS - Review

## Chap. 1 Introduction

## Chap. 2 Relational Model

relational model, keys

relational algebra

取消nested subquery， 就是先两个表自然连接， 其中一个表选择， 得到一张表之后再自然连接。

Year（detail.cdate）= 2018, 可以取出日期的年。

having count (distinct campus) = 1 就是只有一个campus

要找最大， 就是 sum() >=all

```sql
insert into deatilt( 对应的 attri) 
values ( )
日期和string都需要单引号。
 update card
 set balance = balance -20 
where cno = 'c0002';
```



## Chap. 3 Introduction to SQL

## Chap. 4 Intermediate SQL

transactions

authorization

## Chap. 5 Advanced SQL

trigger

## Chap. 7 Entity-Relationship Model

E-R diagram

extended E-R diagram

UML

## Chap. 8 Relational Database Design

1NF

functional dependency

lossless-join decomposition

closure, canonical cover, extraneous attributes

BCNF

dependency preservation

3NF

multivalued dependency

4NF

## Chap. 10 Storage and File Structure

physical storage

fixed-length records (record cross block, delete strategy)

variable-length records (fixed-size (offset, length) + data after fixed-size part, null bitmap, slotted page)

file organization

* heap (free-space map) - arbitrary, usually no move
* sequential (pointer) - ordered, reorganize from time to time
* multi-table clustering - 'natural join' on storage
* B+ tree
* hash

data dictionary / system catalog - metadata

buffer manager

LRU, pinned block, toss-immediate, MRU; clock (approximation of LRU)

column-oriented storage / columnar representation

hybrid row/column storage

ORC (column-oriented in file and row-oriented among files)

## Chap. 11 Indexing and Hashing

ordered index & hash index

primary index (clustering index) & secondary index (non-clustering index), index-sequential file

dense index, sparse index, multi-level index

B+ tree index, one node one block

non-unique search key -> combine it and primary key

B+ tree file organization (leaf nodes store records instead of pointers)

bulk loading - insert in order (always in buffer), bottom-up construction

B tree index

multiple key access, combined search key

## Chap. 12 Query Processing

selection

* linear: $b_r \times t_T + t_S$
* primary B+ tree index
  * equality on key: $(h + 1) \times (t_T + t_S)$
  * equality on non-key: $h \times (t_T + t_S) + t_S + b \times t_T$
  * less-equal: just linear search on records
  * great-equal: 1 B+ tree search and than go through on records
* secondary B+ tree index
  * equality on key: $(h + 1) \times (t_T + t_S)$
  * equality on non-key: $(h + n + m) \times (t_T + t_S)$
  * less-equal: just linear search on leaf nodes
  * great-equal: 1 B+ tree search and than go through on leaf nodes
* conjunctive selection
  * using 1 index to select 1 condition and linear search for others
  * composite search key index
  * using indices of each condition and take intersection (on pointers instead of on records)
* disjunctive selection
  * using indices of each condition and take union (on pointers instead of on records)
  * linear search
* negation
  * linear search
  * using index if very few records satisfy

sorting - external-merge sort

* total number of runs: $\lceil b_r / M \rceil$
* total number of merge passes: $\lceil \log_{M - 1}(b_r / M) \rceil$
* ignore final write cost
* total number of block transfers: $2b_r \lceil \log_{M - 1} (b_r / M) \rceil + b_r$
* total number of seeks: $2 \lceil b_r / M \rceil + b_r(2 \lceil \log_{M - 1}(b_r / M) \rceil - 1)$
* use $b_b$ blocks instead of 1 block for each run when merging, but merge $\lfloor M / b_b \rfloor - 1$ runs per pass
* transfer: $2b_r \lceil \log_{M/b_b - 1} (b_r / M) \rceil + b_r$
* seek: $2 \lceil b_r / M \rceil + \lceil b_r / b_b \rceil(2 \lceil \log_{M / b_b - 1}(b_r / M) \rceil - 1)$

join

* nested-loop
  * worst case: $n_r \times b_s + b_r$ block transfers, $n_r + b_r$ seeks
  * smaller one can be stored in memory: $b_r + b_s$ block transfers, 2 seeks
* block nested-loop
  * worst case: $b_r \times b_s + b_r$ T, $2b_r$ S
  * base case (smaller one fit): $b_r + b)s$ T, 2 S
  * use $M - 2$ blocks for outer relation and 1 for inner relation, worst case: $\lceil b_r / (M - 2) \rceil \times b_s + b_r$ T, $2 \lceil b_r / (M - 2) \rceil$ S
* indexed nested-loop
  * equi-join or natural join, inner relation has corresponding index (can construct one just for join)
  * $b_r (t_T + t_S) + n_r \times c$ , $c$ is the selection time in inner relation
  * if both r and s have index, use the one with fewer tuples as outer relation
* merge
  * equi-join or natural join
  * $(b_r + b_s)t_T + (\lceil b_r / b_b \rceil + \lceil b_s / b_b \rceil) t_S$
  * $x_r$ blocks for r and $x_s$ blocks for s ($x_r + x_s = M$), $\lceil b_r / x_r \rceil + \lceil b_s / x_s \rceil$ S, optimum x: $x_r = b_r M / (b_r + b_s)$, $x_s = b_s M / (b_r + b_s)$
  * hybrid: one is sorted and the other has secondary B+ tree index: do merge-join on (tuple, pointer), sort results by pointer address, fetch tuples
* hash
  * equi-join or natural join
  * hash value n and hash function h should be chosen such that each s~i~ can fit in memory
  * when partitioning r/s, each partition needs an output buffer (so $n < M$, else do partition recursively)
  * load each s~i~, use another hash function to build in-memory index, and check tuple from r~i~ one by one (s - build input, r - probe input)
  * typically $n = \lceil b_s / M \rceil f$, f is fudge factor and typically is 1.2
  * if no recursive partition: $3(b_r + b_s) + 4 n$ T, $2(\lceil b_r / b_b \rceil + \lceil b_s / b_b \rceil) + 2n$ S
  * with recursive partition: $2(b_r + b_s) \log_{M / b_b - 1}(b_s / M) + b_r + b_s$ T, $2(\lceil b_r / b_b \rceil + \lceil b_s / b_b \rceil) \log_{M / b_b - 1}(b_s / M)$ S

materialization

pipelining - demand driven (lazy, pull), producer driven (eager, push)



1. size。

record数量相乘/

2. number of block 。

   就是 block size /Icard

3. height of B+树

fanout。（4096-4）/（key size+ value size ） +1。

min hieght  log 455（100000）  向上取整。=2  就是满树

4.  计算出每一步的block transfer 次数， seek time。 

block number 就是tT的次数， 





## Chap. 13 Query Optimization

equivalence rules

size estimation, estimation of distinct values

cost-based optimization, heuristics optimization

left-deep join

## Chap. 14 Transaction

ACID

lost update, dirty read, unrepeatable read, phantom

serializability

conflict equivalence, precedence graph

recoverable schedule, cascading rollback, cascadeless schedule

serializable (default), repeatable read, read committed, read uncommitted

## Chap. 15 Concurrency Control

two-phase locking protocol -> conflict serializable, lock-point

* strict two-phase locking protocol (hold all X-locks)
* rigorous two-phase locking protocol (hold all locks)

deadlock & starvation

lock table

graph-based protocol, tree protocol

deadlock prevention: pre-declaration, graph-based protocol, wait-die, wound-die

timeout-based schemes

wait-for graph

multiple granularity, intention lock (IS, IX, SIX)

## Chap. 16 Recovery System

write-ahead logging

undo & redo, compensation log

checkpoint, fuzzy checkpoint

dump

remote backup

logical undo (& physical redo), operation logging

ARIES (analysis, redo, undo)

## Chap. 22 Object Based Database

## Chap. 23 XML

---

# A4

trigger

overlapping & disjoint

information repetition, insert anomalies, update difficulty

1NF, 3NF, BCNF, 4NF

## chap 10

records

* fixed-length records (record cross block, delete strategy)
* variable-length records (fixed-size (offset, length) + data after fixed-size part, null bitmap, slotted page)

file organization

* heap (free-space map) - arbitrary, usually no move
* sequential (pointer) - ordered, reorganize from time to time
* multi-table clustering - 'natural join' on storage
* B+ tree
* hash

data dictionary / system catalog - metadata

buffer manager

LRU, pinned block, toss-immediate, MRU

clock (approximation of LRU)

column-oriented storage / columnar representation

hybrid row/column storage

ORC (column-oriented in file and row-oriented among files)

## chap 11

ordered index & hash index

primary index (clustering index) & secondary index (non-clustering index), index-sequential file

dense index, sparse index (one index per block), multi-level index

B+ tree index, one node one block (P-K-P-K-...K-P, $n = \lfloor 4096 / (P + K) \rfloor + 1$)

height of B+ tree ($\lfloor \log_{\lceil n / 2 \rceil}(N / 2) \rfloor + 1$ & $\lceil \log_n(N) \rceil$)

2/3 with random

non-unique search key -> combine it and primary key (sk, pk)

B+ tree file organization (leaf nodes store records instead of pointers) (V-V-V-...-V-P) (primary index search key instead of pointers in 2nd level)

bulk loading - insert in order (always in buffer), bottom-up construction

B tree index

multiple key access, combined search key

## chap 12

selections...

joins...

## chap 13

equivalence rules

size estimation (n, b, l, f, V(A, r)) - select, join, aggregation, projection, set operation

estimation of distinct values

join order & DP, left-deep join

cost-based optimization, heuristics optimization

nested query optimization

materialized view

## chap 14

ACID

lost update, dirty read, unrepeatable read, phantom

serializability

conflict equivalence, precedence graph

recoverable schedule, cascading rollback, cascadeless schedule

serializable (default), repeatable read, read committed, read uncommitted

## chap 15

two-phase locking protocol -> conflict serializable, lock-point

* strict two-phase locking protocol (hold all X-locks)
* rigorous two-phase locking protocol (hold all locks)

deadlock & starvation

lock table

graph-based protocol, tree protocol

deadlock prevention: pre-declaration, graph-based protocol, wait-die, wound-die

timeout-based schemes

wait-for graph

multiple granularity, intention lock (IS, IX, SIX)

## Chap. 16 Recovery System

WAL (write-ahead logging)

undo & redo, compensation log

checkpoint, fuzzy checkpoint

dump

remote backup

logical undo (& physical redo), operation logging

ARIES (analysis, redo, undo)

