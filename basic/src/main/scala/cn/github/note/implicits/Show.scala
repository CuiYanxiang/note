package cn.github.note.implicits

trait Show[T] {
  def show(t: T): String
}

object Show {
  implicit def IntShow: Show[Int] = (i: Int) => (i + 1).toString

  implicit def StringShow: Show[String] = (s: String) => s.toUpperCase
}

case class Person(name: String, age: Int)

object Person {
  def PersonShow(implicit si: Show[Int], ss: Show[String]): Show[Person] =
    //使用了隐式si ss，会处理Int和String类型
    (p: Person) => "Person(name=" + ss.show(p.name) + ", age=" + si.show(p.age) + ")"
}

trait Show2[T] {
  def show(t: T): String
}

object Show2 {
  //与val相同，但是def支持泛型，都称为隐式值，当在参数列表时，称为隐式参数
  implicit def IntShow: Show2[Int] = (i: Int) => (i - 1).toString

  implicit def StringShow: Show2[String] = (s: String) => s.toLowerCase

}
case class Person2(name: String, age: Int)

object Person2 {
  //对于PersonShow2本身也是隐式参数的，可以直接使用隐式方法implicitly，自动得到Show[Person2]对象
  implicit def PersonShow2(implicit si: Show2[Int], ss: Show2[String]): Show2[Person2] =
    (p: Person2) => s"Person2(name=${ss.show(p.name)}, age=${si.show(p.age)})"
}

trait Show3[T] {
  //与val相同，但是def支持泛型，都称为隐式值，当在参数列表时，称为隐式参数
  def show(t: T): String
}

object Show3 {
  implicit def IntShow: Show3[Int] = (i: Int) => i.toString

  implicit def StringShow: Show3[String] = (s: String) => s
}

case class Person3(name: String, age: Int)

object Person3 {
  def PersonShow3(implicit si: Show3[Int], ss: Show3[String]): Show3[Person3] =
    (p: Person3) => "Person3(name=" + ss.show(p.name) + ", age=" + si.show(p.age) + ")"
}

trait Show4[T] {
  def show(t: T): String
}

case class Person4(name: String, age: Int)

object Person4 {
  def PersonShow4(implicit si: Show4[Int], ss: Show4[String]): Show4[Person4] =
    (p: Person4) => "Person4(name=" + ss.show(p.name) + ", age=" + si.show(p.age) + ")"
}

case class Person5(name: String, age: Int)

trait Show5[T] {
  def show(t: T): String
}

object Person5 {
  def PersonShow5(implicit si: Show5[Int], ss: Show5[String]): Show5[Person5] =
    (p: Person5) => "Person5(name=" + ss.show(p.name) + ", age=" + si.show(p.age) + ")"
}

trait Show6[T] {
  def show(t: T): String
}

case class Person6(name: String, age: Int)

object Person6 {
  def PersonShow6[T](implicit si: Show6[Int], ss: Show6[String]): Show6[Person6] =
    (p: Person6) => "Person6(name=" + ss.show(p.name) + ", age=" + si.show(p.age) + ")"
}
