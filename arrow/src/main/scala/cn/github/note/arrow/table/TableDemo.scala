package cn.github.note.arrow.table

import org.apache.arrow.memory.RootAllocator
import org.apache.arrow.vector.table.Table
import org.apache.arrow.vector.{ FieldVector, IntVector, VarCharVector, VectorLoader, VectorSchemaRoot, VectorUnloader }
import scala.collection.JavaConverters.{ asScalaIteratorConverter, seqAsJavaListConverter }

object TableDemo {
  def main(args: Array[String]): Unit = {
    val rootAllocator = new RootAllocator

    val bitVector     = new IntVector("age", rootAllocator)
    val varCharVector = new VarCharVector("name", rootAllocator)
    bitVector.allocateNew()
    varCharVector.allocateNew()
    (0 until 10).foreach { i =>
      bitVector.setSafe(i, i)
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

    table.iterator().asScala.foreach { row =>
      val age  = row.getInt("age")
      val name = new String(row.getVarChar("name"))
      println(s"name: $name, age: $age")
    }

  }
}
