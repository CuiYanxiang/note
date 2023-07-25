## Flink RPC框架
Flink集群组件之间的心跳、作业调度消息等也需要进行RPC消息通信。AKKA作为Flink自身消息的RPC通信框架
### RPC消息类型
#### 握手消息
* RemoteHandshakeMessage：与Actor握手消息
* HandshakeSuccessMessage: 与Actor握手成功消息
#### Fenced消息
* LocalFencedMessage: 本地Fence Token消息，同一个JVM内调用
* RemoteFencedMessage: 远程Fence Token消息，包括本地不同JVM和跨节点JVM调用
#### 调用消息
* LocalRpcInvocation: 本地RpcEndpoint调用消息，同一个JVM内调用
* RemoteRpcInvocation: 远程RpcEndpoint调用，包括本地不同JVM和跨节点的JVM调用
#### 执行消息
* RunAsync:执行消息，带有Runnable对象的异步执行请求消息
### RPC通信组件
#### RpcGateway
远程调用网关，对外提供调用接口。组件之间的通信行为都是通过RpcGateway进行交互的
```text
1. JobMasterGateway: 是JobMaster提供对外服务接口
2. TaskExecutorGateway: 是TaskManager提供对外服务接口
3. ResourceManagerGateway: 是ResourceManager资源管理器提供的对外服务接口
4. DispatcherGateway: 是Flink提供的作业提交接口
```
消息类型中Fenced消息，专门用来解决集群脑裂问题，JobMaster、ResourceManager、Dispatcher在高可用涉及Leader选举，都继承了FencedRpcGateway
#### RpcEndpoint
在RpcGateway的基础上提供了RPC服务组件的生命周期管理。同一个RpcEndpoint中的所有调用只有一个线程处理，叫作Endpoint的主线程。与Akka的Actor模型一样，所有对状态数据的修改在同一个线程中执行，所以不存在并发的问题。
> 使用RpcService作为参数，用来启动RPC通信服务，RpcEndpoint提供远程通信，RpcService负责管理RpcEndpoint生命周期。RPCEndpoint是RpcService、RPCServer的结合
#### RpcService
是RpcEndpoint成员变量
1. 启动和停止RpcServer和连接RpcEndpoint
2. 根据连接地址，连接到RpcServer会返回一个RpcGateway
3. 延迟/立刻调度Runnable、Callable
#### RpcServer
是RpcEndpoint成员变量，负责接收响应远端的RPC消息请求，实质上是通知底层的AkkaRpcActor切换到Start状态，两个实现AkkaInvocationHandler和FencedAkkaInvocationHandler。
> rpcEndpoint.tell(ControlMessages.START, ActorRef.noSender());
#### AkkaRpcActor
```text
负责处理如下类型消息
1. 本地Rpc调用LocalRpcInvocation,指派给RpcEndpoint进行处理，如有响应结果，则将响应结果返给Sender
2. RunAsync & CallAsync类型的消息，直接在Actor的线程中执行
3. 控制消息ControlMessages用来控制Actor的行为，ControlMessages#START启动Actor开始处理消息，ControlMessages#STOP停止处理消息，停止后收到的消息会被丢弃掉。
```
### RPC交互过程
#### 请求发送
> 在RpcService中调用connect()方法与对端的RpcEndpoint建立连接，connect()方法根据给的地址返回InvocationHandler(AkkaInvocationHandler或者FencedAkkaInvocationHandler)。
#### 请求响应
> RPC消息是通过RpcEndpoint所绑定的Actor的ActorRef发送的，AkkaRpcActor是消息接收的入口，AkkaRpcActor在RpcEndpoint中构造生成，负责将消息交给不同的方法进行处理
