package com.joe.akka.thread;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.DefaultResizer;
import akka.routing.RoundRobinPool;
import scala.concurrent.duration.Duration;

class Master extends UntypedActor {

		private final int nrOfMessages;
		private final int nrOfElements;

		private double pi;
		private int nrOfResults;
		private final long start = System.currentTimeMillis();

		private final ActorRef listener;
		private final ActorRef workerRouter;

		public Master(final int nrOfWorkers, int nrOfMessages, int nrOfElements, ActorRef listener) {
			this.nrOfMessages = nrOfMessages;
			this.nrOfElements = nrOfElements;
			this.listener = listener;
			int lowerBound = 10;
			int upperBound = 20;
			DefaultResizer resizer = new DefaultResizer(lowerBound, upperBound);
			workerRouter = this.getContext().actorOf(Props.create(Worker.class).withRouter(new RoundRobinPool(nrOfWorkers).withResizer(resizer)),"workerRouter");
		}

		public void onReceive(Object message) {
			if (message instanceof Calculate) {
//				System.out.println("nrOfMessages="+nrOfMessages+";nrOfElements="+nrOfElements);
				for (int start = 0; start < nrOfMessages; start++) {
					workerRouter.tell(new Work(start, nrOfElements), getSelf());
				}
			} else if (message instanceof Result) {
				Result result = (Result) message;

				pi += result.getValue();
				nrOfResults += 1;
				if (nrOfResults == nrOfMessages) {
					// Send the result to the listener
					Duration duration = Duration.create(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
					listener.tell(new PiApproximation(pi, duration), getSelf());
					// Stops this actor and all its supervised children
					getContext().stop(getSelf());
				}
			} else {
				unhandled(message);
			}
		}
	}