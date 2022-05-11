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




cpp 的报错实在太少了,



debug心得

1. 需要自己加一些报错, 多加if  判断.  
2. 不要在乎时间复杂度. 完成就行. 不然很容易完不成. 
3. 不要乱改它的, 没这个能力. 
