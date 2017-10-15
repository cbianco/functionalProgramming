package it.cbnoc.test;

import com.fpinjava.common.Result;
import it.cbnoc.actor.AbstractActor;
import it.cbnoc.actor.Actor;
import it.cbnoc.actor.Player;

import java.util.concurrent.Semaphore;

public class TestActor {

	private static Semaphore semaphore = new Semaphore(1);

	public static void main(String[] args) throws InterruptedException {
		Actor<Integer> referee =
			new AbstractActor<Integer>("Referee", Actor.Type.SERIAL) {
				@Override
				public void onReceive(Integer message, Result<Actor<Integer>> sender) {
					System.out.println("Game ended after " + message + " shots");
					semaphore.release();
				}
			};

		Actor<Integer> player1 =
			new Player("Player1", "ping", referee);

		Actor<Integer> player2 =
			new Player("Player2", "pong", referee);

		semaphore.acquire();
		player1.tell(1, Result.success(player2));
		semaphore.acquire();
	}
}
