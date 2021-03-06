package src.main.impl.extension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.event.suite.After;
import org.jboss.arquillian.test.spi.event.suite.Before;
import org.jboss.arquillian.test.spi.event.suite.Test;
import org.jboss.arquillian.transaction.impl.container.SecurityActions;
import org.jboss.arquillian.warp.impl.server.inspection.InspectionRegistry;

import src.main.impl.resultobjects.ArquillianPerformanceLoadStresstestSuiteResult;

public class ArquillianPerformanceLoadStresstestTestDriver {

	@Inject
	private Instance<InspectionRegistry> registry;

	@Inject
	private Event<Before> before;

	@Inject
	private Event<After> after;

	@Inject
	private Event<Test> test;

	public void fireTest(@Observes ArquillianPerformanceLoadStresstestEvent event) {

		for (final Object inspection : registry.getInspections()) {

			final List<Annotation> qualifiers = event.getQualifiers();

			if (qualifiers == null || qualifiers.size() == 0
					|| !isArquillianPerformanceLoadStresstestLifecycleEvent(qualifiers)) {
				throw new IllegalStateException("ERROR");
			}

			List<Method> methods = SecurityActions.getMethodsMatchingAllQualifiers(inspection.getClass(), qualifiers);

			for (final Method testMethod : methods) {
				ArquillianPerformanceLoadStresstestInitializer initializer = new ArquillianPerformanceLoadStresstestInitializer(
						testMethod.getDeclaringClass());

				ArquillianPerformanceLoadStresstestSuiteResult suiteResult = initializer.getSuiteResult();

				executeTest(inspection, testMethod);

				suiteResult.getClassResults();

			}
		}
	}

	private boolean isArquillianPerformanceLoadStresstestLifecycleEvent(List<Annotation> qualifiers) {
		for (Annotation qualifier : qualifiers) {
			if (!ArquillianPerformanceLoadStresstestCheck.checkAll(qualifier.annotationType())) {
				return false;
			}
		}

		return true;
	}

	private void executeTest(Object inspection, Method method) {
		before.fire(new Before(inspection, method));

		test.fire(new Test(new ArquillianPerformanceLoadStresstestMethodExecutor(inspection, method)));

		after.fire(new After(inspection, method));
	}

	private InspectionRegistry registry() {
		return registry.get();
	}
}
