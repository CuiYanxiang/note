package cn.github.note.arrow.substrait

import io.substrait.isthmus.SqlToSubstrait
import io.substrait.proto.Plan
import org.apache.arrow.dataset.file.{ FileFormat, FileSystemDatasetFactory }
import org.apache.arrow.dataset.jni.NativeMemoryPool
import org.apache.arrow.dataset.scanner.ScanOptions
import org.apache.arrow.dataset.substrait.AceroSubstraitConsumer
import org.apache.arrow.memory.RootAllocator

import java.nio.ByteBuffer
import scala.collection.JavaConverters.{ mapAsJavaMapConverter, seqAsJavaListConverter }

object SubstraitDemo {

  def main(args: Array[String]): Unit = {
    // vm option: --add-opens=java.base/java.nio=org.apache.arrow.memory.core,ALL-UNNAMED

    val url           = getClass.getResource("/user.snappy.orc").toString
    val scanOptions   = new ScanOptions(32768)
    val rootAllocator = new RootAllocator()
    val systemDatasetFactory =
      new FileSystemDatasetFactory(rootAllocator, NativeMemoryPool.getDefault, FileFormat.ORC, url)

    val dataset = systemDatasetFactory.finish()
    val scanner = dataset.newScan(scanOptions)
    val reader  = scanner.scanBatches()

    val mapTableToArrowReader = Map("TEST" -> reader)

    val plan = getPlan()

    val substraitPlan = ByteBuffer.allocateDirect(plan.toByteArray.length)
    substraitPlan.put(plan.toByteArray)

    val arrowReader = new AceroSubstraitConsumer(rootAllocator).runQuery(substraitPlan, mapTableToArrowReader.asJava)

    while (arrowReader.loadNextBatch()) {
      println(arrowReader.getVectorSchemaRoot.contentToTSVString())
    }

  }

  def getPlan(): Plan = {
    val sql            = "select name,age from test"
    val user           = "CREATE TABLE test (name VARCHAR(256),age INT NOT NULL)"
    val sqlToSubstrait = new SqlToSubstrait()
    sqlToSubstrait.execute(sql, List(user).asJava)
  }

}
