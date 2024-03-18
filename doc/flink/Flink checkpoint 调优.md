## Flink Checkpoint 调优
> val env = StreamExecutionEnvironment.getExecutionEnvironment

1. checkpoint间隔：控制checkpoint的频率，可以通过设置checkpointInterval来实现。
> env.enableCheckpointing(15000)
2. checkpoint数据的存储位置：可以使用本地文件系统或者HDFS等分布式文件系统。

3. 并行度：checkpoint并行度决定了checkpoint的速度，可以通过设置parallelism来控制。

4. 状态合并：使用合并状态来减小checkpoint数据的大小。

5. 数据压缩：使用数据压缩来减小checkpoint数据的大小。

6. 状态后端：可以使用内存状态后端或者RocksDB状态后端等。

7. 重试次数：如果checkpoint失败，可以设置重试次数来处理。

8. checkpoint的task本地恢复

```
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
    //设置失败后一直重启
    env.setRestartStrategy(RestartStrategies.failureRateRestart(3, Time.milliseconds(1000), Time.minutes(5)));
    env.disableOperatorChaining(); 
    env.enableCheckpointing(1000 * 60 * 15, CheckpointingMode.AT_LEAST_ONCE);
    env.getCheckpointConfig().setFailOnCheckpointingErrors(true);
    //业务比较复杂设置超时时间1个小时。
    env.getCheckpointConfig().setCheckpointTimeout(1000 * 60 * 60);
    env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000  * 10);
    env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION);
```
