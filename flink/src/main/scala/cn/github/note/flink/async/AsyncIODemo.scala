package cn.github.note.flink.async

import cn.github.note.flink.sourrce.MockSource
import cn.github.note.flink.utils.EnvironmentUtils
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.async.{ ResultFuture, RichAsyncFunction }
import org.apache.flink.util.concurrent.Executors

import scala.concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.{ ThreadLocalRandom, TimeUnit }
import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success }

object AsyncIODemo {
  def main(args: Array[String]): Unit = {
    val env = EnvironmentUtils.getEnv()
    env.setParallelism(3)
    val input: DataStream[Int]     = env.addSource(new MockSource)
    val function                   = new SampleAsyncFunction
    val result: DataStream[String] = AsyncDataStream.orderedWait(input, function, 10000L, TimeUnit.MILLISECONDS, 20)

    result.print("result==>")
    env.execute("Async IO Demo: ")
  }
}

class SampleAsyncFunction extends RichAsyncFunction[Int, String] {
  var client: AsyncClient = _

  override def open(parameters: Configuration): Unit = {
    client = new AsyncClient
  }

  override def asyncInvoke(input: Int, resultFuture: ResultFuture[String]): Unit = {
    client.query(input).onComplete {
      case Success(value)     => resultFuture.complete(Set(value))
      case Failure(exception) => resultFuture.completeExceptionally(exception)
    }
  }
}

class AsyncClient {

//  implicit lazy val executor: ExecutionContext = ExecutionContext.fromExecutor(Executors.directExecutor())
  def query(key: Int): Future[String] = Future {
    val sleep = (ThreadLocalRandom.current().nextFloat() * 100).toLong
    try {
      Thread.sleep(sleep)
    } catch {
      case e: Exception =>
        throw e
    }
    if (ThreadLocalRandom.current.nextFloat < 0.001f) {
      throw new RuntimeException("wahahahaha...")
    } else {
      s"key_${key % 10}"
    }
  }
}
