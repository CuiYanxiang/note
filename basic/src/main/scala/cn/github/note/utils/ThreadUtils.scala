package cn.github.note.utils

import com.google.common.util.concurrent.ThreadFactoryBuilder

import java.util.concurrent._
import scala.concurrent.forkjoin.{ ForkJoinPool => SForkJoinPool, ForkJoinWorkerThread => SForkJoinWorkerThread }
import scala.util.control.NonFatal

object ThreadUtils {

  /**
    * Create a thread factory that names threads with a prefix and also sets the threads to daemon.
    */
  def namedThreadFactory(prefix: String): ThreadFactory = {
    new ThreadFactoryBuilder().setDaemon(true).setNameFormat(prefix + "-%d").build()
  }

  /**
    * Wrapper over newFixedThreadPool. Thread names are formatted as prefix-ID, where ID is a
    * unique, sequentially assigned integer.
    */
  def newDaemonFixedThreadPool(nThreads: Int, prefix: String): ThreadPoolExecutor = {
    val threadFactory = namedThreadFactory(prefix)
    Executors.newFixedThreadPool(nThreads, threadFactory).asInstanceOf[ThreadPoolExecutor]
  }

  def newBlockedDaemonFixedThreadPool(nThreads: Int, prefix: String): ThreadPoolExecutor = {
    new ThreadPoolExecutor(nThreads,
                           nThreads,
                           0L,
                           TimeUnit.MILLISECONDS,
                           new LinkedBlockingQueue[Runnable],
                           namedThreadFactory(prefix)) {

      val semaphore = new Semaphore(nThreads)

      override def execute(command: Runnable): Unit = {
        var acquired: Boolean = false
        do {
          try {
            semaphore.acquire()
            acquired = true
          } catch {
            case _: InterruptedException =>
          }
          Thread.sleep(10)
        } while (!acquired)

        try {
          super.execute(command)
        } catch {
          case e: RuntimeException =>
            semaphore.release()
            throw e
          case e: Error =>
            semaphore.release()
            throw e
        }
      }

      override def afterExecute(r: Runnable, t: Throwable): Unit = {
        super.afterExecute(r, t)
        semaphore.release()
      }
    }
  }

  /**
    * Construct a new Scala ForkJoinPool with a specified max parallelism and name prefix.
    */
  def newForkJoinPool(prefix: String, maxThreadNumber: Int): SForkJoinPool = {
    // Custom factory to set thread names
    val factory = new SForkJoinPool.ForkJoinWorkerThreadFactory {
      override def newThread(pool: SForkJoinPool): SForkJoinWorkerThread =
        new SForkJoinWorkerThread(pool) {
          setName(prefix + "-" + super.getName)
        }
    }

    new SForkJoinPool(maxThreadNumber,
                      factory,
                      null, // handler
                      false // asyncMode
    )
  }

  /**
    * Run a piece of code in a new thread and return the result. Exception in the new thread is
    * thrown in the caller thread with an adjusted stack trace that removes references to this
    * method for clarity. The exception stack traces will be like the following
    *
    * SomeException: exception-message
    * at CallerClass.body-method (sourcefile.scala)
    * at ... run in separate thread using org.apache.spark.util.ThreadUtils ... ()
    * at CallerClass.caller-method (sourcefile.scala)
    * ...
    *
    */
  def runInNewThread[T](threadName: String, isDaemon: Boolean = true)(body: => T): T = {
    @volatile var exception: Option[Throwable] = None
    @volatile var result: T                    = null.asInstanceOf[T]

    val thread = new Thread(threadName) {
      override def run(): Unit = {
        try {
          result = body
        } catch {
          case NonFatal(e) =>
            exception = Some(e)
        }
      }
    }
    thread.setDaemon(isDaemon)
    thread.start()
    thread.join()

    exception match {
      case Some(realException) =>
        // Remove the part of the stack that shows method calls into this helper method
        // This means drop everything from the top until the stack element
        // ThreadUtils.runInNewThread(), and then drop that as well (hence the `drop(1)`).
        val baseStackTrace =
          Thread.currentThread().getStackTrace.dropWhile(!_.getClassName.contains(this.getClass.getSimpleName)).drop(1)

        // Remove the part of the new thread stack that shows methods call from this helper method
        val extraStackTrace =
          realException.getStackTrace.takeWhile(!_.getClassName.contains(this.getClass.getSimpleName))

        // Combine the two stack traces, with a place holder just specifying that there
        // was a helper method used, without any further details of the helper
        val placeHolderStackElem = new StackTraceElement(
          s"... run in separate thread using ${ThreadUtils.getClass.getName.stripSuffix("$")} ..",
          " ",
          "",
          -1
        )
        val finalStackTrace = extraStackTrace ++ Seq(placeHolderStackElem) ++ baseStackTrace

        // Update the stack trace and rethrow the exception in the caller thread
        realException.setStackTrace(finalStackTrace)
        throw realException
      case None =>
        result
    }
  }
}
