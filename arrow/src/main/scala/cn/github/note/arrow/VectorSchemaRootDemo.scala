package cn.github.note.arrow

import org.apache.arrow.memory.RootAllocator
import org.apache.arrow.vector.{ IntVector, VarCharVector, VectorSchemaRoot }
import org.apache.arrow.vector.types.pojo.{ ArrowType, Field, FieldType, Schema }

import scala.collection.JavaConverters.{ asJavaIterableConverter, asScalaBufferConverter }

object VectorSchemaRootDemo {

  def main(args: Array[String]): Unit = {
    val age  = new Field("age", FieldType.nullable(new ArrowType.Int(32, true)), null)
    val name = new Field("name", FieldType.nullable(new ArrowType.Utf8), null)

    val schema        = new Schema(Seq(age, name).asJava, null)
    val rootAllocator = new RootAllocator()
    val root          = VectorSchemaRoot.create(schema, rootAllocator)
    val ageVector     = root.getVector("age").asInstanceOf[IntVector]
    val nameVector    = root.getVector("name").asInstanceOf[VarCharVector]
    ageVector.allocateNew(3)
    ageVector.set(0, 30)
    ageVector.set(1, 10)
    ageVector.set(2, 20)
    nameVector.allocateNew(3)
    nameVector.set(0, "zhangsan".getBytes())
    nameVector.set(1, "lisi".getBytes())
    nameVector.set(2, "wangwu".getBytes())
    root.setRowCount(3)

    println(root.contentToTSVString())
  }
}
