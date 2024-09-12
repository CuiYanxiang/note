package cn.github.note.arrow.format

import org.apache.arrow.dataset.file.{ FileFormat, FileSystemDatasetFactory }
import org.apache.arrow.dataset.jni.{ DirectReservationListener, NativeMemoryPool }
import org.apache.arrow.dataset.scanner.ScanOptions
import org.apache.arrow.memory.RootAllocator
import org.apache.arrow.vector.{ VectorLoader, VectorSchemaRoot, VectorUnloader }
import org.apache.arrow.vector.ipc.message.ArrowRecordBatch

object ParquetDemo {

  def main(args: Array[String]): Unit = {

    val url           = getClass.getResource("/user.snappy.parquet").toString
    val rootAllocator = new RootAllocator()
    val pool          = NativeMemoryPool.createListenable(DirectReservationListener.instance())

    val systemDatasetFactory =
      new FileSystemDatasetFactory(rootAllocator, pool, FileFormat.PARQUET, url)
    val schema = systemDatasetFactory.inspect()
    println(s"schema: $schema")
    val dataset = systemDatasetFactory.finish()

    val scanOptions = new ScanOptions(32768)
//    val scanOptions = new ScanOptions(32768, Optional.of(Array[String]("age")))
    val scanner = dataset.newScan(scanOptions)
    println(s"scanner schema: ${scanner.schema().toJson}")
    val reader = scanner.scanBatches()

    val batchs = scala.collection.mutable.ListBuffer.empty[ArrowRecordBatch]
    while (reader.loadNextBatch()) {
      val vectorSchemaRoot = reader.getVectorSchemaRoot
      val unloader         = new VectorUnloader(vectorSchemaRoot)
      batchs += unloader.getRecordBatch
    }

    batchs.foreach { batch =>
      println(s"batch: $batch")
      val root   = VectorSchemaRoot.create(reader.getVectorSchemaRoot.getSchema, rootAllocator)
      val loader = new VectorLoader(root)
      loader.load(batch)
    }

    dataset.close()
  }
}
