package cn.github.note.flink.sql

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api._
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object WordCountSQL {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val settings                        = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()
    val tEnv: StreamTableEnvironment    = StreamTableEnvironment.create(env, settings)
    val sql =
      s"""
         |SELECT word, SUM(frequency) AS `count`
         |FROM (
         |  VALUES ('Hello', 1), ('Ciao', 1), ('Hello', 2)
         |)
         |AS WordTable(word, frequency)
         |GROUP BY word
         |""".stripMargin

    tEnv.executeSql(sql).print()
  }

}
