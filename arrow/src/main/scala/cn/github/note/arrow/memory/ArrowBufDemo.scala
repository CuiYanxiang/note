package cn.github.note.arrow.memory

import org.apache.arrow.memory.{ BufferAllocator, RootAllocator }

object ArrowBufDemo {
  def main(args: Array[String]): Unit = {
    val rootAllocator: BufferAllocator = new RootAllocator(6 * 1024)
    val arrowBuf                       = rootAllocator.buffer(3 * 1024)
    println(arrowBuf)

    arrowBuf.close()
  }

}
