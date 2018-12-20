package main.impl.extension;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;

import main.impl.annotations.PerformanceLoadStresstest;

public class ArquillianPerformanceLoadStresstestCheck {

	/**
	 * Checks whether either given class or its superclass are annotated with
	 * {@link PerformanceLoadStresstest} annotation indicating that the test is
	 * supposed to be run as a PLS-test.
	 */
	public static boolean isPerformanceLoadStressTest(Class<?> testClass) {
		Class<?> clazz = testClass;
		while (clazz != null) {
			if (clazz.isAnnotationPresent(PerformanceLoadStresstest.class)) {
				return true;
			}
			clazz = clazz.getSuperclass();
		}
		return false;
	}

	/**
	 * Checks whether either given class or its superclass are annotated with
	 * {@link RunAsClient} annotation.
	 */
	public static boolean hasRunAsClientAnnotation(Class<?> testClass) {
		Class<?> clazz = testClass;
		while (clazz != null) {
			if (clazz.isAnnotationPresent(RunAsClient.class)) {
				return true;
			}
			clazz = clazz.getSuperclass();
		}
		return false;
	}

	/**
	 * Checks whether either given class or its superclass has a defined Deployment
	 * annotated with {@link Deployment} annotation.
	 */
	public static boolean hasDeploymentAnnotation(Class<?> testClass) {
		Class<?> clazz = testClass;
		while (clazz != null) {
			if (clazz.isAnnotationPresent(Deployment.class)) {
				return true;
			}
			clazz = clazz.getSuperclass();
		}
		return false;
	}

}
