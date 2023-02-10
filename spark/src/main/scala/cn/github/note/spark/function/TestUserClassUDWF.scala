package cn.github.note.spark.function

import org.apache.spark.sql.catalyst.expressions.{ AggregateWindowFunction, AttributeReference, Expression, Literal }
import org.apache.spark.sql.{ functions, Column }
import org.apache.spark.sql.types.{ DataType, MapType, StringType }

case class TestUserClassUDWF(mergeMap: Expression) extends AggregateWindowFunction {

  private val emptyMap = Literal.create(Map.empty[String, String], MapType(StringType, StringType))

  val current = AttributeReference("current", MapType(StringType, StringType), nullable = true)()

  override def dataType: DataType = MapType(StringType, StringType)

  override val evaluateExpression: Expression = aggBufferAttributes(0)

  private val update =
    functions.udf[Map[String, String], Map[String, String], Map[String, String]]((input, buffer) => {
      var ret: Map[String, String] = buffer
      input.foreach(item => {
        ret = ret.updated(item._1, item._2)
      })
      ret
    })

  override val updateExpressions: Seq[Expression] = {
    val updateMap = update(new Column(mergeMap), new Column(current))
    updateMap.expr :: Nil
  }

  override def aggBufferAttributes: Seq[AttributeReference] = current :: Nil
  override def children: Seq[Expression]                    = Seq(mergeMap)

  override val initialValues: Seq[Expression] = emptyMap :: Nil

  def inputTypes: Seq[DataType] = Seq(MapType(StringType, StringType))
}
