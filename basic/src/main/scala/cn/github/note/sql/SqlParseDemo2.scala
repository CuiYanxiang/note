package cn.github.note.sql

import com.alibaba.druid.sql.SQLUtils
import com.alibaba.druid.sql.ast.SQLExpr
import com.alibaba.druid.sql.ast.expr._
import com.alibaba.druid.sql.ast.statement._
import com.alibaba.druid.sql.visitor.SchemaStatVisitor
import com.alibaba.druid.util.JdbcConstants

import scala.collection.JavaConverters._

object SqlParseDemo2 {
  val sql =
    """
      |select 
      |    gio_id, 
      |    arrayStringConcat(ids_$basic_userId, '||') as `id_$basic_userId`,
      |    arrayStringConcat(ids_phone, '||') as `id_phone`,
      |    arrayStringConcat(ids_$anonymous_user, '||') as `id_$anonymous_user`,
      |    tag_jc_md_cs_ljzb_sjsxint, arrayStringConcat(tag_asql, '||') as tag_asql,
      |    tag_submit,
      |    arrayStringConcat(tag_gq30tgmdsp, '||') as tag_gq30tgmdsp,
      |    tag_csjs, tag_cscs, tag_cscsss,
      |    arrayStringConcat(tag_cslbxbq, '||') as tag_cslbxbq,
      |    arrayStringConcat(tag_cslbxdz, '||') as tag_cslbxdz,
      |    arrayStringConcat(tag_1010lb, '||') as tag_1010lb, tag_1013lbsql,
      |    tag_1014lbsql, arrayStringConcat(tag_csdzlb, '||') as tag_csdzlb,
      |    tag_cscssssssss, tag_cscssssss, tag_aaaaaaa, tag_csdezx, usr_$wechat_city, usr_int1
      |from project_1.user_view
      |where seg_csdezx = 1 and account_id = '8f4a7bb4c03369cc'
      |""".stripMargin

  def main(args: Array[String]): Unit = {
    try {
      val selectStatement =
        SQLUtils.parseSingleStatement(sql, JdbcConstants.CLICKHOUSE).asInstanceOf[SQLSelectStatement]
      val queryBlock    = selectStatement.getChildren.get(0).asInstanceOf[SQLSelect].getQueryBlock
      var needAggregate = false
      val (idKeys, propKeys) = queryBlock.getSelectList.asScala
        .map { selectItem =>
          if (selectItem.getAlias != null) {
            val expr = selectItem.getExpr.toString
            if (expr.contains("ids")) {
              needAggregate = true
            }
            selectItem.getAlias.replace("\"", "")
          } else {
            selectItem.getExpr.toString
          }
        }
        .partition(selectString => selectString.contains("id_$"))
      if (propKeys.length > 10) {
        val whereConditions   = queryBlock.getWhere.getChildren
        val segmentConditions = whereConditions.get(0).asInstanceOf[SQLBinaryOpExpr]
        val segmentKey = segmentConditions.getChildren.get(0).asInstanceOf[SQLIdentifierExpr].toString
        val accountIdConditions = whereConditions.get(1).toString
        // TODO build final result sql
      }

    } catch {
      case e: Exception =>
        SQLUtils.parseSingleStatement(sql, JdbcConstants.CLICKHOUSE, false)
    }
  }
}
