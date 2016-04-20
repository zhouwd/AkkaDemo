package com.joe.akka.thread;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.Creator;

/*
 *
 *
 */
public class PI {
	public static void main(String[] args) {
		PI pi = new PI();
		pi.calculate(10, 100000, 100000);
	}

	public void calculate(final int nrOfWorkers, final int nrOfElements, final int nrOfMessages) {
		// Create an Akka system
		ActorSystem system = ActorSystem.create("PiSystem");

		// create the result listener, which will print the result and shutdown the system
		final ActorRef listener = system.actorOf(Props.create(Listener.class), "listener");

		// create the master
		ActorRef master = system.actorOf(Props.create(Master.class, (Creator<Master>) () -> new Master(nrOfWorkers, nrOfMessages, nrOfElements, listener)), "master");

		// start the calculation
		master.tell(new Calculate(), ActorRef.noSender());

	}
}
