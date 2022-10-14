package cn.github.note.flink.calcite

import org.apache.calcite.config.Lex
import org.apache.calcite.sql.dialect.OracleSqlDialect
import org.apache.calcite.sql.parser.SqlParser
import org.apache.calcite.sql.parser.SqlParser.Config

object CalciteSqlParser {

  def main(args: Array[String]): Unit = {
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
         |""".stripMargin

    val config  = SqlParser.configBuilder().setLex(Lex.MYSQL).build()
    val parser  = SqlParser.create(sql, config)
    val sqlNode = parser.parseQuery()
    println(sqlNode.toSqlString(OracleSqlDialect.DEFAULT))
  }

}
