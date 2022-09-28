package cn.github.note.flink.calcite

import org.apache.calcite.DataContext
import org.apache.calcite.adapter.java.JavaTypeFactory
import org.apache.calcite.linq4j.{ AbstractEnumerable, Enumerable, Enumerator }
import org.apache.calcite.rel.`type`.{ RelDataType, RelDataTypeFactory }
import org.apache.calcite.schema.impl.{ AbstractSchema, AbstractTable }
import org.apache.calcite.schema.{ ScannableTable, Schema, SchemaFactory, SchemaPlus, Table }
import org.apache.calcite.sql.`type`.SqlTypeName
import org.apache.flink.calcite.shaded.com.google.common.collect.ImmutableMap

import java.io.{ BufferedReader, File, FileReader }
import java.util
import org.apache.calcite.util._

import java.net.URLDecoder
import java.sql.DriverManager
import java.util.Properties
import scala.collection.mutable

class CsvSchemaFactory extends SchemaFactory {

  override def create(schemaPlus: SchemaPlus, s: String, map: util.Map[String, AnyRef]): Schema =
    new CsvSchema(map.get("dataFile").toString)

}

class CsvSchema(dataFile: String) extends AbstractSchema {
  override def getTableMap: util.Map[String, Table] = {
    val source                                       = Sources.of(this.getClass.getResource(dataFile))
    val builder: ImmutableMap.Builder[String, Table] = ImmutableMap.builder[String, Table]()
    val head                                         = dataFile.split("\\.").head
    builder.put(head, new CsvTable(source))
    builder.build()
  }
}

class CsvTable(source: Source) extends AbstractTable with ScannableTable {

  override def scan(dataContext: DataContext): Enumerable[Array[AnyRef]] = {
    new AbstractEnumerable[Array[AnyRef]] {
      override def enumerator(): Enumerator[Array[AnyRef]] = new CsvEnumerator(source)
    }
  }

  /**
    * 获取字段类型
    * @param relDataTypeFactory
    * @return
    */
  override def getRowType(relDataTypeFactory: RelDataTypeFactory): RelDataType = {
    val javaTypeFactory = relDataTypeFactory.asInstanceOf[JavaTypeFactory]
    val map             = mutable.Map.empty[String, RelDataType]
    try {
      val reader               = new BufferedReader(new FileReader(source.file()))
      val line                 = reader.readLine()
      val lines: Array[String] = line.split(",")
      lines.foreach { column =>
        val cols             = column.split(":")
        val (name, dataType) = (cols.head, cols.last)
        map += (name -> javaTypeFactory.createSqlType(SqlTypeName.get(dataType)))
      }
    } catch {
      case e: Throwable =>
        throw e
    }
    val (names, dataTypes) = map.toArray.unzip
    javaTypeFactory.createStructType(Pair.zip(names, dataTypes))
  }

}

class CsvEnumerator[T](source: Source) extends Enumerator[T] {
  val br: BufferedReader = new BufferedReader(source.reader())
  var value: T           = _

  override def current(): T = value

  override def moveNext(): Boolean = {
    try {
      val line = br.readLine()
      if (line == null) return false
      value = line.split(",").asInstanceOf[T]
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        return false
    }
    true
  }

  override def reset(): Unit = println("报错了")

  override def close(): Unit = ???
}

object Client {

  def main(args: Array[String]): Unit = {
    val model = URLDecoder.decode(this.getClass.getResource("/model.json").toString, "UTF-8")
    val prop  = new Properties()
    prop.put("model", model.replace("file:", ""))
    val conn = DriverManager.getConnection("jdbc:calcite:", prop)
    println(conn)
    val statement = conn.createStatement()
    val sql       = "select * from test_cav.test01"
    val set       = statement.executeQuery(sql)
    while (set.next()) {
      println(set.getString(1))
    }
  }

}
