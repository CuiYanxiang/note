package cn.github.note.spark.register

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.FunctionIdentifier
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry.{ FUNC_ALIAS, FunctionBuilder }
import org.apache.spark.sql.catalyst.analysis.FunctionRegistryBase
import org.apache.spark.sql.catalyst.expressions.aggregate.AggregateFunction
import org.apache.spark.sql.catalyst.expressions.{ Expression, ExpressionInfo }

import scala.reflect.ClassTag

object AggFunctionRegister {

  def register[T <: AggregateFunction: ClassTag](name: String)(spark: SparkSession): Unit = {
    val (funcName, (expressionInfo, builder)) = expression[T](name)
    spark.sessionState.functionRegistry.registerFunction(FunctionIdentifier(funcName), expressionInfo, builder)
  }

  private def expression[T <: Expression: ClassTag](
      name: String,
      setAlias: Boolean = false,
      since: Option[String] = None
  ): (String, (ExpressionInfo, FunctionBuilder)) = {
    val (expressionInfo, builder) = FunctionRegistryBase.build[T](name, since)
    val newBuilder = (expressions: Seq[Expression]) => {
      val expr = builder(expressions)
      if (setAlias) expr.setTagValue(FUNC_ALIAS, name)
      expr
    }
    (name, (expressionInfo, newBuilder))
  }
}
