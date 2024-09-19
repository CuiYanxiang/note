package cn.github.note.flink.utils

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object EnvironmentUtils {

  def getEnv(): StreamExecutionEnvironment = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val settings                        = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tEnv: StreamTableEnvironment    = StreamTableEnvironment.create(env, settings)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env
  }
}
