package cn.github.note.spark

import cn.github.note.spark.register.AggFunctionRegister
import com.fasterxml.jackson.databind.{ DeserializationFeature, ObjectMapper }
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.spark.SparkConf
import org.apache.spark.sql.{ CountWindowFunction, SparkSession }

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

    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    val employee = "employee" + Thread.currentThread().getId
    import spark.implicits._
    spark.read
      .json("spark/src/main/resources/data.json")
      .as[Employee]
      .toDF()
      .createOrReplaceTempView(employee)

    AggFunctionRegister.register[CountWindowFunction]("count_window")(spark)

    val sql = s"""
                 |select *
                 |from $employee
                 |""".stripMargin
    spark
      .sql(sql)
      .show()
  }
}
case class Employee(id: Long, name: String, salary: Long)
