package cn.github.note.arrow.memory

import org.apache.arrow.dataset.file.{ FileFormat, FileSystemDatasetFactory }
import org.apache.arrow.dataset.jni.{ NativeMemoryPool, ReservationListener }
import org.apache.arrow.memory.RootAllocator

import java.util.concurrent.atomic.AtomicLong

object ListenerNativeMemDemo {

  def main(args: Array[String]): Unit = {
    val reserved      = new AtomicLong(0L)
    val rootAllocator = new RootAllocator()
    val listener = new ReservationListener {
      override def reserve(size: Long): Unit = {
        reserved.getAndAdd(size)
      }

      override def unreserve(size: Long): Unit = {
        reserved.getAndAdd(-size)
      }
    }

    val pool = NativeMemoryPool.createListenable(listener)
    val systemDatasetFactory = new FileSystemDatasetFactory(rootAllocator,
                                                            pool,
                                                            FileFormat.PARQUET,
                                                            getClass.getResource("/user.snappy.parquet").toString)
  }

}
