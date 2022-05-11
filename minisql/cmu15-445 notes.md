



buffer pool 

page table 映射到 buffer pool。 frame里装 page。

保存dirty flag和pin/reference counter。

page directory 在磁盘中， id 到 page location

page table 在内存中， id到buffer pool中的copy 

一个DBMS 可能有多个buffer pool
