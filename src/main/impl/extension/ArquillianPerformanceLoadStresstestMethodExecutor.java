package main.impl.extension;

import java.lang.Thread.State;
import java.lang.reflect.Method;

import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.test.spi.TestMethodExecutor;
import org.jboss.arquillian.test.spi.TestResult;

import main.impl.annotations.Iterationcount;
import main.impl.annotations.TimeDisplaced;
import main.impl.annotations.Usercount;

public class ArquillianPerformanceLoadStresstestMethodExecutor implements TestMethodExecutor {

	private Object instance;
	private Method method;

	public ArquillianPerformanceLoadStresstestMethodExecutor(Object instance, Method method) {
		Validate.notNull(instance, "instance must not be null");
		Validate.notNull(method, "method must not be null");

		this.instance = instance;
		this.method = method;
	}

	@Override
	public void invoke(Object... parameters) {
		final Method method = this.method;
		checkAnnotationConfiguration(method);
		final TestResult result = null;

		Integer iterationParameter = method.getAnnotation(Iterationcount.class).value();
		Integer userCountParameter = method.getAnnotation(Usercount.class).value();

		// Array of the resultArrays
		TestResult[][] resultAll = new TestResult[iterationParameter][userCountParameter];

		// create as much Iterations as given in the IterationCount Annotation
		for (Integer loopcounter = 0; loopcounter < iterationParameter; loopcounter++) {

			Thread[] threadArray = new Thread[userCountParameter];
			ThreadGroup threadgroup = new ThreadGroup("ThreadGroup" + loopcounter);
			// create as much threads as given in the UserCount Annotation
			for (int threadcounterForCreatingThreads = 0; threadcounterForCreatingThreads < userCountParameter; threadcounterForCreatingThreads++) {

				Runnable runnableForThreads = new Runnable() {
					@Override
					public void run() {

						try {

							method.invoke(method.getClass());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};

				String nameForThread = "userThread" + threadcounterForCreatingThreads;
				threadArray[threadcounterForCreatingThreads] = new Thread(threadgroup, runnableForThreads,
						nameForThread);

			}

			if (method.getAnnotation(TimeDisplaced.class) == null) {
				// start all threads parallel
				for (int threadcounterForStartingThreads = 0; threadcounterForStartingThreads < userCountParameter; threadcounterForStartingThreads++) {
					threadArray[threadcounterForStartingThreads].run();
					resultAll[loopcounter][threadcounterForStartingThreads].setStart(System.currentTimeMillis());
				}
			} else {
				// start the threads with the given delay
				for (int threadcounterForStartingThreads = 0; threadcounterForStartingThreads < userCountParameter; threadcounterForStartingThreads++) {
					threadArray[threadcounterForStartingThreads].run();
					long waitTime = method.getAnnotation(TimeDisplaced.class).value() * 1000l;

					try {
						threadArray[threadcounterForStartingThreads].sleep(waitTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					resultAll[loopcounter][threadcounterForStartingThreads].setStart(System.currentTimeMillis());
				}
			}

			for (int threadcounterForStartingThreads = 0; threadcounterForStartingThreads < userCountParameter; threadcounterForStartingThreads++) {
				resultAll[loopcounter][threadcounterForStartingThreads].setEnd(0);
			}

			// take the dates when each thread finished running their methods

			while (threadgroup.activeCount() != 0)
				for (int threadcounterForStartingThreads = 0; threadcounterForStartingThreads < userCountParameter; threadcounterForStartingThreads++) {
					if (threadArray[threadcounterForStartingThreads].isAlive() == false
							&& resultAll[loopcounter][threadcounterForStartingThreads].getEnd() == 0) {
						// set end-time
						resultAll[loopcounter][threadcounterForStartingThreads].setEnd(System.currentTimeMillis());
						State state = threadArray[threadcounterForStartingThreads].getState();
						// save passed status if thread terminated
						if (state.equals(Thread.State.TERMINATED)) {
							resultAll[loopcounter][threadcounterForStartingThreads].setStatus(TestResult.Status.PASSED);
						} else {
							resultAll[loopcounter][threadcounterForStartingThreads].setStatus(TestResult.Status.FAILED);
							// throw error
						}
					}
				}
		}

	}

	protected void checkAnnotationConfiguration(Method method) throws RuntimeException {

		if (method.getAnnotations() == null) {
			throw new RuntimeException("Annotations are missing!");
		} else {
			if (method.getAnnotation(Usercount.class) == null || method.getAnnotation(Usercount.class).value() == 0)
				throw new RuntimeException("UserCount-Annotation is either missing or zero!");

			if (method.getAnnotation(Iterationcount.class) == null
					|| method.getAnnotation(Iterationcount.class).value() == 0)
				throw new RuntimeException("IterationCount-Annotation is either missing or zero!");

			if (method.getAnnotation(TimeDisplaced.class) == null)
				throw new RuntimeException("TimeDisplaced-Annotation is missing!");
			else {
				if (method.getAnnotation(TimeDisplaced.class) != null
						&& method.getAnnotation(TimeDisplaced.class).value() != 0
						&& method.getAnnotation(Usercount.class).value() < 2)
					throw new RuntimeException(
							"The Test can not be run in TimeDisplaced-Mode, because the UserCount is not at least two!");
			}
		}

	}

	@Override
	public Method getMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @Override public void invoke(Object... parameters) throws Throwable {
	 *
	 * }
	 */
}
