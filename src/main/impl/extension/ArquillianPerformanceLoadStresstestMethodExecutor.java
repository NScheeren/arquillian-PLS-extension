package main.impl.extension;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.test.spi.TestMethodExecutor;

import main.impl.annotations.Iterationcount;
import main.impl.annotations.TimeDisplaced;
import main.impl.annotations.Usercount;
import main.impl.resultobjects.ArquillianPerformanceLoadStresstestMethodResult;

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
		// final TestResult result = null;

		int iterationParameter = method.getAnnotation(Iterationcount.class).value();
		int userCountParameter = method.getAnnotation(Usercount.class).value();
		int timeDisplaced = method.getAnnotation(TimeDisplaced.class).value();
		final long waitTime = timeDisplaced * 1000l;

		ArquillianPerformanceLoadStresstestMethodResult methodResult = new ArquillianPerformanceLoadStresstestMethodResult(
				method.getName(), userCountParameter, iterationParameter, timeDisplaced);

		for (Integer loopcounter = 0; loopcounter < iterationParameter; loopcounter++) {
			 ExecutorService executor = Executors.newFixedThreadPool(userCountParameter);
			List<Future<long>> results = new ArrayList<Future<long>>();

			for (int threadcounterForCreatingThreads = 0; threadcounterForCreatingThreads < userCountParameter;
					threadcounterForCreatingThreads++) {

				Callable callableForThreads = new Callable() {
					@Override
					public Object call() throws Exception {
						wait(waitTime);
						long start = System.currentTimeMillis();
						try {
							method.invoke(method.getClass());
						} catch (Exception e) {
							e.printStackTrace();
							return 0;
						}
						long stop = System.currentTimeMillis();
						long duration = stop - start; //Problem bei Tageswechsel
						return duration;
					}
				};

				results = executor.submit(callableForThreads);


			}

			for (int resultcounter = 0; resultcounter < results.size(); resultcounter++) {
				methodResult.setDuration(iterationParameter, userCountParameter, results.get(resultcounter).get());
			}

			methodResult.saveData("C:\\");


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
