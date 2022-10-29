package cn.github.note.flink.sourrce

import org.apache.flink.api.common.state.{ ListState, ListStateDescriptor }
import org.apache.flink.api.common.typeinfo.{ TypeHint, TypeInformation }
import org.apache.flink.runtime.state.{ FunctionInitializationContext, FunctionSnapshotContext }
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction

import java.util.concurrent.TimeUnit
import scala.collection.JavaConverters._
import java.util.concurrent.locks.ReentrantLock

class MockSource extends SourceFunction[Int] with CheckpointedFunction {

  @volatile var isRunning: Boolean = true
  var start: Int                   = 0
  var state: ListState[Int]        = _

  override def run(ctx: SourceFunction.SourceContext[Int]): Unit = {
    while (isRunning) {
      ctx.getCheckpointLock.synchronized {
        ctx.collect(start)
        start += 1
        if (start == Integer.MAX_VALUE) {
          start = 0
        }
      }
      TimeUnit.SECONDS.sleep(1l)
    }
  }

  override def cancel(): Unit = isRunning = false

  override def snapshotState(context: FunctionSnapshotContext): Unit = {
    state.clear()
    state.add(start)
  }

  override def initializeState(context: FunctionInitializationContext): Unit = {
    state = context.getOperatorStateStore.getListState(
      new ListStateDescriptor[Int]("list", TypeInformation.of(new TypeHint[Int] {}))
    )
    state.get().asScala.toSeq.foreach(i => start = i)
  }
}
