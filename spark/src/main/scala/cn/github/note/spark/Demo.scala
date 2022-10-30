package cn.github.note.spark

import com.fasterxml.jackson.core.JsonParser.Feature
import com.fasterxml.jackson.databind.{ DeserializationFeature, ObjectMapper }
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Demo {

  def main(args: Array[String]): Unit = {
    val config = new SparkConf()
      .setMaster("local[*]")
      .setAppName("local test app")
      .set("spark.sql.warehouse.dir", "file:///tmp/hive/warehouse")
      .set("spark.sql.catalogImplementation", "hive")
      .set("spark.debug.maxToStringFields", "100")
      .set("spark.sql.adaptive.shuffle.targetPostShuffleInputSize", "2m")
      .set("spark.sql.crossJoin.enabled", "true")
      .set("spark.sql.legacy.parser.havingWithoutGroupByAsWhere", "true")
      .set("spark.driver.bindAddress", "127.0.0.1")

    val spark =
      SparkSession.builder().config(config).getOrCreate()
    spark.conf.set("spark.driver.cores", "1")
    spark.conf.set("spark.driver.memory", "1g")
    spark.conf.set("spark.executor.memory", "1g")
    spark.conf.set("spark.sql.shuffle.partitions", "3")
    spark.conf.set("spark.driver.host", "localhost")
    spark.conf.set("spark.ui.enabled", "true")

    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    import spark.implicits._
    val df = spark.read
      .text("spark/src/main/resources/data.json")
    df.show()
  }
}
