package com.joe.learn.akka;

import akka.actor.UntypedActor;
import scala.Option;

/**
 * 生命周期 Demo
 */
class ActorLifeCycle extends UntypedActor {

	private int i;

	@Override
	public void preStart() {

		System.out.println("SEQ:" + (i++) +" ===preStart(启动前)");
	}

	@Override
	public void preRestart(Throwable reason, Option<Object> message) throws Exception {
		System.out.println("SEQ:" + (i++) +" ===preRestart(重启前)  message=" + message);
		super.preRestart(reason, message);
	}

	@Override
	public void postRestart(Throwable reason) throws Exception {
		System.out.println("SEQ:" + (i++) +" ===postRestart(清理)");
		super.postRestart(reason);
	}

	@Override
	public void postStop() throws Exception {
		System.out.println("SEQ:" + (i++) +" ===postStop(停止)");
		super.postStop();
	}

	@Override
	public void onReceive(Object msg) {
		System.out.println("SEQ:" + (i++) +" ===onReceive(接受时)");
		System.out.println("Content:"+msg);
		if (msg.toString().equals("hello")) {
			System.out.println("----" + msg);
			// getContext().stop(getSelf());
		} else if (msg.toString().equals("remote")) {
			System.out.println("****" + msg);
		} else {
			// 导致崩溃，actor会自动重启
			System.out.println(3 / 0);
			// unhandled(msg);
		}
	}
}