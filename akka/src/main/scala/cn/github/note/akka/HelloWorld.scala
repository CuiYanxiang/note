package cn.github.note.akka

import akka.actor.{ Actor, ActorSystem, Props }

class HelloActor extends Actor {
  override def receive: Receive = {
    case "hello" => println("hello world ！")
    case _       => println("你好！")
  }
}

object test extends App {
  val system     = ActorSystem("HelloSystem")
  val helloActor = system.actorOf(Props[HelloActor])
  helloActor ! "hello"
  helloActor ! "你好"

}
