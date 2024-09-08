package cn.github.note.arrow.vector

import org.apache.arrow.memory.{ BufferAllocator, RootAllocator }
import org.apache.arrow.vector.complex.ListVector
import org.apache.arrow.vector.complex.impl.UnionListWriter
import org.apache.arrow.vector.complex.reader.{ FieldReader, IntReader }

object ListVectorDemo {
  def main(args: Array[String]): Unit = {
    val allocator: BufferAllocator = new RootAllocator(Long.MaxValue)
    val listVector                 = ListVector.empty("listVector", allocator)
    val writer: UnionListWriter    = listVector.getWriter

    (0 until 10).foreach { i =>
      writer.startList()
      writer.setPosition(i)
      (0 until 5).foreach { j =>
        writer.writeInt(j * i)
      }
      writer.setValueCount(5)
      writer.endList()
    }
    listVector.setValueCount(10)
    println(listVector)

    println("======get========")

    (0 until listVector.getValueCount)
      .foreach { i =>
        if (!listVector.isNull(i)) {
          val list = listVector.getObject(i)
          println(list)
        }
      }

    println("======reader========")
    val reader = listVector.getReader
    (0 until listVector.getValueCount)
      .foreach { i =>
        reader.setPosition(i)
        while (reader.next) {
          val r: IntReader = reader.reader
          if (r.isSet) {
            println(r.readInteger())
          }
        }
      }

  }
}
