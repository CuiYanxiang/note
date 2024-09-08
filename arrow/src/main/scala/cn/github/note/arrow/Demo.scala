package cn.github.note.arrow

import org.apache.arrow.memory.RootAllocator
import org.apache.arrow.vector.complex.impl.{ IntWriterImpl, VarCharWriterImpl }
import org.apache.arrow.vector.types.pojo.{ ArrowType, Field, FieldType, Schema }
import org.apache.arrow.vector.{ IntVector, VarCharVector }

import scala.collection.JavaConverters.{ asJavaIterableConverter, mapAsJavaMapConverter, mapAsScalaMapConverter }

object Demo {

  def main(args: Array[String]): Unit = {
    val rootAllocator = new RootAllocator
    val vector        = new VarCharVector("myVector", rootAllocator)
    vector.allocateNew(3)
    vector.set(0, "one".getBytes())
    vector.set(1, "two".getBytes())
    vector.set(2, "three".getBytes())
    vector.setValueCount(3)

    val reader = vector.getReader
    (0 to vector.getValueCount).foreach { i =>
      reader.setPosition(i)
      if (reader.isSet) {
        println(reader.readText())
      }
    }

    println(vector)

    println("===================")
    // Filed
    val map   = Map("A" -> "Id card", "B" -> "Passportt", "C" -> "Visa")
    val field = new Field("doc", new FieldType(true, new ArrowType.Utf8, null, map.asJava), null)
    println(field.getMetadata.asScala)

    println("===================")

    // Schema
    val metadata = Map("k1" -> "v1", "k2" -> "v2")
    val a        = new Field("A", FieldType.nullable(new ArrowType.Int(32, true)), null)
    val b        = new Field("B", FieldType.nullable(new ArrowType.Utf8), null)
    val schema   = new Schema(List(a, b).asJava, metadata.asJava)
    println(schema)

  }
}
