package cn.github.note.arrow.substrait

import io.substrait.isthmus.SqlExpressionToSubstrait
import org.apache.arrow.dataset.file.{ FileFormat, FileSystemDatasetFactory }
import org.apache.arrow.dataset.jni.NativeMemoryPool
import org.apache.arrow.dataset.scanner.ScanOptions
import org.apache.arrow.memory.RootAllocator

import java.nio.ByteBuffer
import java.util.{ Base64, Optional }
import scala.collection.JavaConverters.seqAsJavaListConverter

object ProjectionFilterDemo {

  def main(args: Array[String]): Unit = {
    // vm option: --add-opens=java.base/java.nio=org.apache.arrow.memory.core,ALL-UNNAMED

    val url = getClass.getResource("/user.snappy.parquet").toString
    val scanOptions = new ScanOptions.Builder(32768)
      .columns(Optional.empty())
      .substraitFilter(getByteBuffer(Array("age > 18")))
      .substraitProjection(getByteBuffer(Array("name", "age + 1")))
      .build()

    val rootAllocator = new RootAllocator()
    val systemDatasetFactory =
      new FileSystemDatasetFactory(rootAllocator, NativeMemoryPool.getDefault, FileFormat.PARQUET, url)

    val dataset = systemDatasetFactory.finish()
    val scanner = dataset.newScan(scanOptions)
    val reader  = scanner.scanBatches()

    while (reader.loadNextBatch()) {
      println(reader.getVectorSchemaRoot.contentToTSVString())
    }

  }

  def getByteBuffer(sqlExpression: Array[String]): ByteBuffer = {
    val test                  = "CREATE TABLE TEST (name VARCHAR(256),age INT NOT NULL)"
    val expressionToSubstrait = new SqlExpressionToSubstrait()
    val expression            = expressionToSubstrait.convert(sqlExpression, List(test).asJava)
    val expressionToByte      = Base64.getDecoder.decode(Base64.getEncoder.encodeToString(expression.toByteArray))
    val byteBuffer            = ByteBuffer.allocateDirect(expressionToByte.length)
    byteBuffer.put(expressionToByte)
    byteBuffer
  }
}
