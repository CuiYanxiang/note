package cn.github.note.arrow.ipc

import org.apache.arrow.memory.RootAllocator
import org.apache.arrow.vector.ipc.{ ArrowFileReader, ArrowFileWriter }
import org.apache.arrow.vector.types.pojo.{ ArrowType, Field, FieldType, Schema }
import org.apache.arrow.vector.{ IntVector, VarCharVector, VectorSchemaRoot }

import java.io.{ File, FileInputStream, FileOutputStream }
import scala.collection.JavaConverters.{ asJavaIterableConverter, asScalaBufferConverter }

object IpcReaderDemo {
  def main(args: Array[String]): Unit = {

    val rootAllocator    = new RootAllocator()
    val file             = new File("data.arrow")
    val fileOutputStream = new FileInputStream(file)
    val reader           = new ArrowFileReader(fileOutputStream.getChannel, rootAllocator)

    println(s"size: ${reader.getRecordBlocks.size()}")
    reader.getRecordBlocks.asScala.foreach { arrowBlock =>
      reader.loadRecordBatch(arrowBlock)
      val root = reader.getVectorSchemaRoot
      println(root.contentToTSVString())
      println("==========")
    }

  }
}
