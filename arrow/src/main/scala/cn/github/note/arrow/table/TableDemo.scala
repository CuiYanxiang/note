package cn.github.note.arrow.table

import org.apache.arrow.memory.RootAllocator
import org.apache.arrow.vector.table.Table
import org.apache.arrow.vector.{ BitVector, FieldVector, VarCharVector, VectorLoader, VectorSchemaRoot, VectorUnloader }
import org.apache.arrow.vector.types.pojo.{ ArrowType, Field, FieldType }

import java.util
import scala.collection.JavaConverters.{ asScalaIteratorConverter, seqAsJavaListConverter }

object TableDemo {
  def main(args: Array[String]): Unit = {
    val rootAllocator = new RootAllocator

    val metadata = Map("k1" -> "v1", "k2" -> "v2")
    val a        = new Field("A", FieldType.nullable(new ArrowType.Int(32, true)), null)
    val b        = new Field("B", FieldType.nullable(new ArrowType.Utf8), null)

    val bitVector     = new BitVector("bool", rootAllocator)
    val varCharVector = new VarCharVector("var", rootAllocator)
    bitVector.allocateNew()
    varCharVector.allocateNew()
    (0 until 10).foreach { i =>
      bitVector.setSafe(i, if (i % 2 == 0) 0 else 1)
      varCharVector.setSafe(i, s"test_$i".getBytes("UTF-8"))
    }
    bitVector.setValueCount(10)
    varCharVector.setValueCount(10)
    val fields  = List(bitVector.getField, varCharVector.getField).asJava
    val vectors = List(bitVector, varCharVector).map(_.asInstanceOf[FieldVector]).asJava

    val root1       = new VectorSchemaRoot(fields, vectors)
    val unloader    = new VectorUnloader(root1)
    val recordBatch = unloader.getRecordBatch
    println(recordBatch)

    val root2  = VectorSchemaRoot.create(root1.getSchema, rootAllocator)
    val loader = new VectorLoader(root2)
    loader.load(recordBatch)

    val table = new Table(vectors)
    println(table)
  }
}
