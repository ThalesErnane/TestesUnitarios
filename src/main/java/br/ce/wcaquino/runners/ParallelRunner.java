package br.ce.wcaquino.runners;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;

public class ParallelRunner extends BlockJUnit4ClassRunner {

	public ParallelRunner(Class<?> klass) throws InitializationError {
		super(klass);
		setScheduler(new TreadPoll());
	}
	
	private static class TreadPoll implements RunnerScheduler {
		private ExecutorService executor;
		
		public TreadPoll() {
			executor = Executors.newFixedThreadPool(2);
		}

		public void schedule(Runnable run) {
			executor.submit(run);
		}

		public void finished() {
			executor.shutdown();
			
			try {
				executor.awaitTermination(10, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
			
		}
		
	}
	
	
}
