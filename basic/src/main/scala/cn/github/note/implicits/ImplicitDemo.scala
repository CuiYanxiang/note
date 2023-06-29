package cn.github.note.implicits

import java.util.Locale

object ImplicitDemo {

  def main(args: Array[String]): Unit = {
    test1("hello word")(Locale.CHINA)

    val fun = test2("hello-") _
    println(fun("world"))

    implicit val china = Locale.UK
    test1("hello world2")

    /**
      * 使用implicitly自动搜索隐式参数，因为implicit si: Show[Int], ss: Show[String]都是在Show的伴生对象中。
      * 调用Person的方法手动获取show对象
      * 若Show伴生对象无si ss隐式参数，则implicitly会无法编译
      */
//    val show1 = Person.PersonShow(si = implicitly, ss = implicitly)
//    val ret1  = show1.show(Person("bob", 25))
//    println(ret1)

//    val show2: Show2[Person2] = implicitly[Show2[Person2]]
//    val ret2                  = show2.show(Person2("JOIN", 25))
//    println(ret2)

//    val show3 = Person3.PersonShow3 //这里不用使用参数也可以，隐式参数在Show3伴生对象中
//    val ret3  = show3.show(Person3("tony", 25))
//    println(ret3)

//    implicit val stringShow: Show4[Int] = (s: Int) => (s * 2).toString
//    implicit val intShow: Show4[String] = (s: String) => s
//    val show4                           = Person4.PersonShow4
//    val ret4                            = show4.show(Person4("jack", 10))
//    println(ret4)

    //在这里，使用def与泛型，只需要定义一个隐式值
//    implicit def typeShow[T]: Show5[T] = (s: T) => s"hello implicit 5 ${s.toString}"
    //此时根据最相近匹配，会继续使用 stringShow2 intShow2的，typeShow隐式无效
//    implicit val stringShow2: Show5[Int] = (s: Int) => (s - 10).toString
//    implicit val intShow2: Show5[String] = (s: String) => s + "_good"
//    val show5                            = Person5.PersonShow5
//    val ret5                             = show5.show(Person5("kaer", 22))
//    println(ret5)

//    implicit def typeShow2[T]: Show6[T] = (s: T) => s"hello implicit 6 ${s.toString.toUpperCase}"
//    val show6                           = Person6.PersonShow6
//    val ret6                            = show6.show(Person6("yuie", 28))
//    println(ret6)

  }

  def test1(x: String)(implicit locale: Locale): Unit = {
    def test1Helper(args: String)(implicit locale: Locale): Unit = {
      println(s"args=$args, locale=$locale")
    }
    test1Helper(x)
  }

  def test2(x: String)(y: String): Unit = {
    println(s"x=$x, y=$y")
  }

}
