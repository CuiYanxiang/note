package org.apache.spark.sql

import org.apache.spark.sql.catalyst.expressions.{ AggregateWindowFunction, AttributeReference, ExpectsInputTypes, Expression, Literal }
import org.apache.spark.sql.{ functions, Column }
import org.apache.spark.sql.types.{ AbstractDataType, DataType, IntegerType, LongType, StringType }

case class CountWindowFunction(name: Expression) extends AggregateWindowFunction with ExpectsInputTypes {

  // 入参
  override def children: Seq[Expression] = Seq(name)
  // 输入类型
  override def inputTypes: Seq[AbstractDataType] = Seq(StringType)
  // 返回类型
  override def dataType: DataType = IntegerType
  // 初始化值
  override val initialValues: Seq[Expression] = Literal(null, IntegerType) :: Nil

  // 计算值的 buffer
  private val value                                         = AttributeReference("value", IntegerType)()
  override def aggBufferAttributes: Seq[AttributeReference] = value :: Nil

  private val process =
    functions.udf[Integer, Integer, String] { (buffer, name) =>
      if (name.nonEmpty) buffer + 1
      else buffer
    }

  // 更新值
  override val updateExpressions: Seq[Expression] = {
    val result = process(new Column(value), new Column(name))
    result.expr :: Nil
  }

  // 最终值
  override val evaluateExpression: Expression = value

}
