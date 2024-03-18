## Spark AQE调优

### AQE开关
spark.sql.adaptive.enabled=true #默认false，为true时开启自适应查询，在运行过程中基于统计信息重新优化查询计划
spark.sql.adaptive.forceApply=true #默认false，自适应查询在没有shuffle或子查询时将不适用，设置为true将始终使用
spark.sql.adaptive.advisoryPartitionSizeInBytes=64M #默认64MB,开启自适应执行后每个分区的大小。合并小分区和分割倾斜分区都会用到这个参数,建议的shuffle分区的大小，在合并分区和处理join数据倾斜的时候用到

### 开启合并shuffle分区
spark.sql.adaptive.coalescePartitions.enabled=true #当spark.sql.adaptive.enabled也开启时，合并相邻的shuffle分区，避免产生过多小task,是否合并临近的shuffle分区（根据'spark.sql.adaptive.advisoryPartitionSizeInBytes'的阈值来合并）
spark.sql.adaptive.coalescePartitions.initialPartitionNum=200 #合并之前shuffle分区数的初始值，默认值是spark.sql.shuffle.partitions，可设置高一些
spark.sql.adaptive.coalescePartitions.minPartitionNum=20 #合并后的最小shuffle分区数。默认值是Spark集群的默认并行性
spark.sql.adaptive.maxNumPostShufflePartitions=500 #reduce分区最大值，默认500，可根据资源调整

### 开启动态调整Join策略
spark.sql.adaptive.join.enabled=true #与spark.sql.adaptive.enabled都开启的话，开启AQE动态调整Join策略

### 开启优化数据倾斜
spark.sql.adaptive.skewJoin.enabled=true #与spark.sql.adaptive.enabled都开启的话，开启AQE动态处理Join时数据倾斜
spark.sql.adaptive.skewedPartitionMaxSplits=5 #处理一个倾斜Partition的task个数上限，默认值为5；
spark.sql.adaptive.skewedPartitionRowCountThreshold=1000000 #倾斜Partition的行数下限，即行数低于该值的Partition不会被当作倾斜，默认值一千万
spark.sql.adaptive.skewedPartitionSizeThreshold=64M #倾斜Partition的大小下限，即大小小于该值的Partition不会被当做倾斜，默认值64M
spark.sql.adaptive.skewedPartitionFactor=5 #倾斜因子，默认为5。判断是否为倾斜的 Partition。如果一个分区(DataSize>64M*5) || (DataNum>(1000w*5)),则视为倾斜分区。
spark.shuffle.statistics.verbose=true #默认false，打开后MapStatus会采集每个partition条数信息，用于倾斜处理
spark.sql.adaptive.skewJoin.skewedPartitionFactor=5	数据倾斜判断因子，必须同时满足skewedPartitionFactor和skewedPartitionThresholdInBytes	
spark.sql.adaptive.skewJoin.skewedPartitionThresholdInBytes=256MB	数据倾斜判断阈值,必须同时满足skewedPartitionFactor和skewedPartitionThresholdInBytes
spark.sql.adaptive.logLevel=debug	配置自适应执行的计划改变日志	调整为info级别，便于观察自适应计划的改变
spark.sql.adaptive.nonEmptyPartitionRatioForBroadcastJoin=0.2	转为broadcastJoin的非空分区比例阈值，>=该值，将不会转换为broadcastjoin	

### DPP
spark.sql.optimizer.dynamicPartitionPruning.enabled=true;  # 其默认值就是true, spark3 默认是开启DPP的
spark.sql.optimizer.dynamicPartitionPruning.reuseBroadcastOnly=true; # 默认是true,这时只会在动态修剪过滤器中重用BroadcastExchange时，才会应用 DPP，如果设置为false可以在非Broadcast场景应用DPP。
spark.sql.optimizer.dynamicPartitionPruning.useStats=true; # 如果为true，则将使用不同计数统计信息来计算动态分区修剪后分区表的数据大小，以评估在广播重用不适用的情况下是否值得添加额外的子查询作为修剪过滤器。
spark.sql.optimizer.dynamicPartitionPruning.fallbackFilterRatio=0.5; # 当统计信息不可用或配置为不使用时，此配置将用作回退过滤器比率，用于计算动态分区修剪后分区表的数据大小，以评估在广播重用不适用的情况下是否值得添加额外的子查询作为修剪过滤器













