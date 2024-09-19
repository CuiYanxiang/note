package cn.github.note.spark.function

import org.apache.spark.sql.catalyst.expressions.{ AggregateWindowFunction, AttributeReference, ExpectsInputTypes, Expression, Literal }
import org.apache.spark.sql.{ functions, Column }
import org.apache.spark.sql.types.{ AbstractDataType, DataType, MapType, StringType }

case class TestUserClassUDWF(mergeMap: Expression) extends AggregateWindowFunction with ExpectsInputTypes {

  override def dataType: DataType = MapType(StringType, StringType)

  override val initialValues: Seq[Expression] = emptyMap :: Nil

  private val emptyMap = Literal.create(Map.empty[String, String], MapType(StringType, StringType))

  val current = AttributeReference("current", MapType(StringType, StringType), nullable = true)()

  override val evaluateExpression: Expression = aggBufferAttributes(0)

  private val update =
    functions.udf[Map[String, String], Map[String, String], Map[String, String]] { (input, buffer) =>
      var ret: Map[String, String] = buffer
      input.foreach { item =>
        ret = ret.updated(item._1, item._2)
      }
      ret
    }

  override val updateExpressions: Seq[Expression] = {
    val updateMap = update(new Column(mergeMap), new Column(current))
    updateMap.expr :: Nil
  }

  override def aggBufferAttributes: Seq[AttributeReference] = current :: Nil

  override def children: Seq[Expression] = Seq(mergeMap)

  override protected def withNewChildrenInternal(newChildren: IndexedSeq[Expression]): Expression = this

  override def inputTypes: Seq[DataType] = Seq(MapType(StringType, StringType))

}
