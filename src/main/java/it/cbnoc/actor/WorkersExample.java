package it.cbnoc.actor;

import com.fpinjava.common.List;
import com.fpinjava.common.Result;
import com.fpinjava.state.SimpleRNG;

import java.util.concurrent.Semaphore;

public class WorkersExample {

	private static final Semaphore semaphore = new Semaphore(1);
	private static int listLegth = 200_000;
	private static int workers = 16;
	private static final List<Integer> testList = //List.list(1,2,3);
		SimpleRNG.doubles(listLegth, new SimpleRNG.Simple(3))._1.map(x -> (int) (x * 30)).reverse();

	static long ms = System.currentTimeMillis();

	public static void main(String[] args) throws InterruptedException {
		semaphore.acquire();

		final AbstractActor<Result<List<Integer>>> client =
			new AbstractActor<Result<List<Integer>>>("Client", Actor.Type.SERIAL) {

				@Override
				public void onReceive(
					Result<List<Integer>> message,
					Result<Actor<Result<List<Integer>>>> sender) {

					message.forEachOrFail(WorkersExample::processSuccess)
						.forEach(WorkersExample::processFailure);
					System.out.println(System.currentTimeMillis() - ms);
					semaphore.release();

				}
			};

		final Manager manager =
			new Manager("Manager", testList, client, workers);

		manager.start();
		semaphore.acquire();
	}

	private static void processFailure(String s) {
		System.out.println(s);
	}

	private static void processSuccess(List<Integer> integerList) {
		System.out.println("Result: " + integerList.takeAtMost(40));
	}
}
