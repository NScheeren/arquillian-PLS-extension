package src.main.impl.extension;

public class ArquillianPerformanceLoadStresstestInitializer {

	public ArquillianPerformanceLoadStresstestInitializer(Class<?> testClass) {

		if (ArquillianPerformanceLoadStresstestCheck.isPerformanceLoadStressTest(testClass)
				&& ArquillianPerformanceLoadStresstestCheck.hasRunAsClientAnnotation(testClass)
				&& ArquillianPerformanceLoadStresstestCheck.hasDeploymentAnnotation(testClass)) {

			// parameter und methode?
			ArquillianPerformanceLoadStresstestMethodExecutor executor = new ArquillianPerformanceLoadStresstestMethodExecutor(
					testClass, method);
			executor.invoke(parameters);

		}

	}

}
