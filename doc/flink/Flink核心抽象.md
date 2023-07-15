## 核心抽象

![环境对象关系.png](../img/环境对象关系.png)

### RuntimeEnvironment
Task开始执行时进行初始化，将Task运行相关的信息封装进去，包含配置信息，运行时各种服务
```text
// Task.java
doRun(){
Environment env = new RuntimeEnvironment(jobId, vertexId, executionId, executionConfig, taskInfo, jobConfiguration, taskConfiguration, userCodeClassLoader, memoryManager, ioManager, broadcastVariableManager, taskStateManager, aggregateManager, accumulatorRegistry, kvStateRegistry, inputSplitProvider, distributedCacheEntries, partitionWriters, inputGates, taskEventDispatcher, checkpointResponder,operatorCoordinatorEventGateway, taskManagerConfig, metrics, this, externalResourceInfoProvider);
}
```
### RuntimeContext
Task实例运行时概念，每个Task实例都有自己RuntimeContext, 是Function运行时的上下文,可获取到作业级别信息，如并行度、task名称、执行配置信息（ExecutionConfig）、State, 可使用getRunctionContext()访问该对象

### StreamRecord
数据流中一条记录或者一个事件，也叫做数据记录，其中包含数据值本身和时间戳

### LatencyMarker
在Source中创建，在Sink节点来估计数据在整个DAG图中流转花费的时间，用来评估总体处理延迟。其中包含在数据源创造出来的时间戳、算子编号，以及数据源算子所在Task编号

### Watermark
是一个时间戳，用来告诉算子所有时间早于等于Watermark的时间事件或记录都已经到达，算子可根据Watermark触发窗口的计算、清理资源等。

### StreamStatus
通知Task是否会继续接收到上游记录或者Watermark，在Source算子中生成，会向下游传播。有两种状态：1. 空闲状态(IDLE) 2. 活动状态(ACTIVE)

### Transformation
表示创建数据流操作，每一个数据流都有一个底层操作，描述数据流的起源。如DataStream#map会创建一个Tranformation树，到程序执行时，会转换成SteamGraph使用。
>  包括两大类： 物理Transformation和虚拟Transformation，DataStream API都会转成Transformation，Transformation再转换成实际算子，虚拟的则不会转换具体算子，如 ReBalance、Union、Split等不会形成实体算子。

![DataStream流水线到Transformation流水线.png](../img/DataStream流水线到Transformation流水线.png)
![虚拟Transformation被优化后的算子树.png](../img/虚拟Transformation被优化后的算子树.png)

Transformation包含：
1. name:转换器名称，用于可视化 
2. uid: 用户指定,目的在job重启时再次分配之前相同uid,用于持久保存状态
3. bufferTimeout: buffer超时时间
4. parallelism: 并行度
5. id: 与uid无关,生成方式是基于一个静态累加器
6. outputType: 输出类型，用来进行序列化数据
7. slotSharingGroup: 给当前的Transformation设置Slot共享组

![Transformation.png](../img/Transformation.png)
#### 物理Transformation

##### 1.SourceTransformation
> 从数据源读取数据的Transformation,flink作业的起点。只有下游Transformation,没有上游输入Transformation，一个作业可以有多个SourceTransformation，如多流Join、维表Join、BroadcastState场景
##### 2. SinkTransformation
> 数据写入外部存储的Transformation,flink作业的终点，只有上游Transformation,下游是外部存储，一个作业可以有多个SinkTransformation。
##### 3. OneInputTransformation
> 单输入的Transformation,只接收一个输入流,需要input(Transformation<IN>)和operator(OneInputStreamOperator<IN, OUT>)参数
##### 4. TwoInputTransformation
> 接收两种流作为输入，其他与3一样

#### 虚拟Transformation

##### 1.SideOutputTransformation
> 在旁路输出转换，上游Transformation的一个分流，可以有多个下游，每一个SideOutput通过OutputTag进行标识
##### 2. PartitionTransformation
> 用于改变输入元素分区,还需要提供一个StreamPartitioner实例进行分区，可统一表示批流数据Shuffle模式
##### 3. UnionTransformation
> 合并转换器，可将多个输入StreamTransformation进行合并,称为Union
##### 4. FeedbackTransformation
> flink Dag中反馈点,就是将符合条件的数据重新发回上游处理，可以连接一个或多个上游
##### 5. CoFeedbackTransformation
> 与4区别上游必须是TwoInputTransformation

![Transformation、算子、UDF关系.png](../img/Transformation、算子、UDF关系.png)

Transformation负责将初始化作业所需要的StreamTask和算子工程(StreamOperatorFactory)构建好，算子作为UDF的执行容器、将作业开发和作业运行联系起来

OneInputTransformation包装了算子StreamMap,算子包装了UDF。逻辑上连接了上下游Transformation
```java
// OneInputTransformation.java
public OneInputTransformation(Transformation<IN> input,String name,OneInputStreamOperator<IN, OUT> operator,TypeInformation<OUT> outputType,int parallelism) {
      this(input, name, SimpleOperatorFactory.of(operator), outputType, parallelism);
}
```
```java
// StreamMap.java
public StreamMap(MapFunction<IN, OUT> mapper) {
      super(mapper);
      chainingStrategy = ChainingStrategy.ALWAYS;
}
```
>OneInputTransformation -> StreamOperator(AbstractStreamOperator(AbstractUDFStreamOperator、OneInputStreamOperator(StreamMap))) -> MapFunction
### 算子
算子在Flink称为StreamOperator,由Task组成一个Dataflow,算子包含一个或多个算子，包装Function来执行。StreamTask算子容器，负责管理算子生命周期

#### 生命周期管理
1. setup: 初始化环境、时间服务、注册监控
2. open: 由具体算子负责实现，包含初始化逻辑，如状态初始化
3. close: 所有数据处理完毕之后关闭
4. dispose: 最后阶段执行，停止处理数据，资源释放

#### 状态与容错管理
当触发检查点的时候，保存状态快照，并且将快照异步保存到外部分布式存储，当作业失败时候算子负责从保存快照中恢复状态

#### 数据处理
1. OneInputStreamOperator
2. TwoInputStrEamOperator

> 单流输入算子,只接收上游1个数据流作为输入，StreamFilter、StreamMap、StreamSink、ProcessOperator
> 
> 双流输入算子,如CoGroup、Join操作，CoStreamMap、KeyedCoProcessOperator
> 
> 数据源算子，StreamSource
> 
> Join算子，HashJoinOperator、SortMergeJoinOperator
> 
> Sort算子，StreamSortOperator
> 
> Window算子，WindowOperator、ProcessOperator
> 
> 代码生成神算子，GeneratedOperator
> 
> 异步算子，AsyncWaitOperator(1,顺序输出模式 2.无序输出模式（组内无序，组间顺序）)

![Flink算子体系.png](../img/Flink算子体系.png)
![Flink算子体系2.png](../img/Flink算子体系2.png)

### 函数Function(UDF)
> Source Function -> Trans Function -> Sink Function
> 
#### 按函数层次从低到高：
> ProcessFunction: UDF接口 + 状态 + 生命周期 + 触发器 。如JoinFunction
> 
> RichFunction: UDF接口 + 状态 + 生命周期。如RichMapFunction
> 
> 无状态Function: UDF接口。如MapFunction

#### 处理函数(ProcessFunction)
> RichFunction -> AbstractRichFunction -> (ProcessFunction、ProcessWindowFunction、KeyedProcessFunction、RichFilterFunction等)












