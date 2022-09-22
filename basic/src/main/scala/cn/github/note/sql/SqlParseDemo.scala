package cn.github.note.sql

import com.alibaba.druid.sql.SQLUtils
import com.alibaba.druid.sql.ast.SQLExpr
import com.alibaba.druid.sql.ast.expr._
import com.alibaba.druid.sql.ast.statement._
import com.alibaba.druid.sql.visitor.SchemaStatVisitor
import com.alibaba.druid.util.JdbcConstants

import scala.collection.JavaConverters._

object SqlParseDemo {
  val sql =
    s"""
       |SELECT
       |   a.id id,
       |   a.`name`as name,
       |   a.age as age,
       |   a.score
       |FROM `user` a
       |LEFT JOIN order_info o
       |ON a.id = o.user_id
       |WHERE a.id = 1 AND a.name = 'xxx' AND a.age = (SELECT age FROM age_table age WHERE age.user_id = 1)
       |GROUP BY a.id, a.score
       |HAVING MAX(a.id) > 1
       |ORDER BY a.id DESC,a.score ASC
       |LIMIT 3, 10
       |""".stripMargin
  def main(args: Array[String]): Unit = {
    try {
      val statements = SQLUtils.parseSingleStatement(sql, JdbcConstants.MYSQL, false)
      statements match {
        //  查询
        case query: SQLSelectStatement =>
          val sqlSelectQuery: SQLSelectQuery = query.getSelect.getQuery
          sqlSelectQuery match {
            // 非union查询
            case sqlSelectQueryBlock: SQLSelectQueryBlock =>
              val selectList = sqlSelectQueryBlock.getSelectList
              println(s"字段信息：[${selectList.toArray.mkString(", ")}]")
              val table: SQLTableSource = sqlSelectQueryBlock.getFrom
              println(s"表信息：$table")
              table match {
                // 普通单表
                case sqlExprTable: SQLExprTableSource =>
                  println(s"当前为单表，表名为: ${sqlExprTable.getName}")
                  println(s"别名为: ${sqlExprTable.getAlias}")
                case sqlJoinTable: SQLJoinTableSource =>
                  println(s"join表，类型为: ${sqlJoinTable.getJoinType}")
                  println(s"join条件为: ${sqlJoinTable.getCondition}")
                case sqlSubQueryrTable: SQLSubqueryTableSource =>
                  // 子查询作为表
                  sqlSubQueryrTable.getAttributes.asScala.toMap.foreach(println)
                case _ =>
                  println("ERROR ~")
              }
              // 获取where条件
              val where: SQLExpr = sqlSelectQueryBlock.getWhere
              where match {
                //二元表达式
                case sqlBinaryOpExpr: SQLBinaryOpExpr =>
                  val left     = sqlBinaryOpExpr.getLeft
                  val right    = sqlBinaryOpExpr.getRight
                  val operator = sqlBinaryOpExpr.getOperator
                  println(s"where db类型: ${sqlBinaryOpExpr.getDbType}")
                  println(s"wbere 字段: ${left.toString} , 值: ${right.toString}")
                  println(s"where operator 类型: ${operator.name}")
                case sqlInSubQueryExpr: SQLInSubQueryExpr =>
                  println(s"子查询: ${sqlInSubQueryExpr.getSubQuery}")
                case _ =>
              }
              println()
              // 获取分组
              val groupBy: SQLSelectGroupByClause = sqlSelectQueryBlock.getGroupBy
              println("分组信息: ========")
              groupBy.getItems.forEach(println)
              println(s"分组条件: ${groupBy.getHaving.toString}")
              println()
              // 获取排序
              val orderBy = sqlSelectQueryBlock.getOrderBy
              println("排序信息: ========")
              orderBy.getItems.forEach { item =>
                println(s"排序字段: ${item.getExpr}")
                println(s"排序类型: ${item.getType.name}")
              }
              println()
              // 获取分页
              val limit = sqlSelectQueryBlock.getLimit
              println("分页信息: ========")
              val rowCount: SQLExpr = limit.getRowCount
              val offset            = limit.getOffset
              println(s"偏移量: $offset")
              println(s"获取偏移量: $rowCount")

            case sqlSelectQuery: SQLUnionQuery =>
            // todo union query
            case _ =>
          }
        case explain: SQLExplainStatement =>
        case insert: SQLInsertStatement   =>
        case _                            =>
      }

      println()
      println()
      println("================分割线===============")
      val visitor: SchemaStatVisitor = SQLUtils.createSchemaStatVisitor(JdbcConstants.MYSQL)
      statements.accept(visitor)
      println(s"使用visitor数据表: ${visitor.getTables}")
      visitor.getColumns.forEach { column =>
        if (column.isSelect) {
          println(s"查询的字段: ${column.getFullName} , ${column.getName}")
        }
      }
      println(s"使用visitor条件: ${visitor.getConditions}")
      println(s"使用visitor分组: ${visitor.getGroupByColumns}")
      println(s"使用visitor排序: ${visitor.getOrderByColumns}")
      val queryBlock = statements.asInstanceOf[SQLSelectStatement].getSelect.getQuery.asInstanceOf[SQLSelectQueryBlock]
      queryBlock.addCondition("a.score = 100")
      queryBlock.removeCondition("a.id = 1")
      println(s"更改条件后sql：$queryBlock")

    } catch {
      case e: Exception =>
        SQLUtils.parseSingleStatement(sql, JdbcConstants.CLICKHOUSE, false)
    }
  }
}
