package cn.github.note

import cn.github.note.utils.ThreadUtils

import java.util.concurrent.TimeUnit

object ThreadPoolDemo {

  def main(args: Array[String]): Unit = {
    val threadNumber = 3
//    val executor = new ThreadPoolExecutor(threadNumber,
//                                          threadNumber,
//                                          0L,
//                                          TimeUnit.MILLISECONDS,
//                                          new LinkedBlockingQueue[Runnable],
//                                          new ThreadPoolExecutor.CallerRunsPolicy)

    val executor = ThreadUtils.newBlockedDaemonFixedThreadPool(threadNumber, "handler")

    (0 to 100) foreach { i =>
      executor.execute(new Test(i))
    }
  }
}

class Test(i: Int) extends Runnable {
  override def run(): Unit = {
    TimeUnit.MILLISECONDS.sleep(600)
    println(s"id: ${i}, tname: ${Thread.currentThread().getName}, tid: ${Thread.currentThread().getId}")
  }
}
