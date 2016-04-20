package com.joe.learn.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 *
 */
public class Main {
	public static void main(String[] args) throws InterruptedException {
		ActorSystem system = ActorSystem.create("akkaA");
		ActorRef rcActor = system.actorOf(Props.create(ActorLifeCycle.class), "helloWorld");
		rcActor.tell("hi", ActorRef.noSender());
//		Thread.sleep(3000);
		//Actor树
		//使用绝对路径查找helloWorld
//		ActorSelection as = system.actorSelection("/user/helloWorld");
		rcActor.tell("hello", ActorRef.noSender());

		rcActor.tell("remote", ActorRef.noSender());


	}
}