package cn.github.note.spark

import cn.github.note.spark.function.TestUserClassUDWF
import cn.github.note.spark.register.AggFunctionRegister
import org.apache.spark.SparkConf
import org.apache.spark.sql.catalyst.util.StringUtils
import org.apache.spark.sql.{ Row, SparkSession }

object Demo2 {

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

    val seq = Seq(
      ("a", 111, Map("a1" -> "b1", "a2" -> "b2", "a4" -> "b4")),
      ("a", 111, Map("a1" -> "c1", "a3" -> "b3")),
      ("a", 112, Map("a4" -> "c4")),
      ("b", 111, Map("a1" -> "b1", "a2" -> "b2", "a4" -> "b4")),
      ("b", 111, Map("a1" -> "c1", "a3" -> "b3")),
      ("b", 111, Map("a4" -> "c4"))
    )
    spark
      .createDataFrame(seq)
      .toDF("name", "time", "psmap")
      .createOrReplaceTempView("merge_map_table")

    AggFunctionRegister.register[TestUserClassUDWF]("merge_map")(spark)

    val sql =
      s"""
         |select
         |  name,
         |  merge_map(psmap) OVER(PARTITION BY name ORDER BY time, SIZE(psmap) ) aa,
         |  row_number() OVER(PARTITION BY name ORDER BY time desc, SIZE(psmap) desc ) rn
         |from merge_map_table
       """.stripMargin

    val df = spark.sql(sql)
    df.printSchema()
    df.show()

//    def getFunctions(pattern: String): Seq[Row] = {
//      StringUtils
//        .filterPattern(spark.sessionState.catalog.listFunctions("default").map(_._1.funcName), pattern)
//        .map(Row(_))
//    }
//    val rows: Seq[Row] = getFunctions("merge_map")
//    println(rows)
//    assert(rows.head.getString(0) == "merge_map")

  }
}
