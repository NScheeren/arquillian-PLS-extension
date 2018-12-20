package main.impl.extension;

import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.spi.annotation.SuiteScoped;

import main.impl.resultobjects.ArquillianPerformanceLoadStresstestClassResult;
import main.impl.resultobjects.ArquillianPerformanceLoadStresstestSuiteResult;

public class ArquillianPerformanceLoadStresstestInitializer {

	@Inject
	@SuiteScoped
	public InstanceProducer<ArquillianPerformanceLoadStresstestSuiteResult> suiteResultInstance;

	public ArquillianPerformanceLoadStresstestInitializer(Class<?> testClass) {

		if (ArquillianPerformanceLoadStresstestCheck.isPerformanceLoadStressTest(testClass)
				&& ArquillianPerformanceLoadStresstestCheck.hasRunAsClientAnnotation(testClass)
				&& ArquillianPerformanceLoadStresstestCheck.hasDeploymentAnnotation(testClass)) {

			ArquillianPerformanceLoadStresstestClassResult classResult = new ArquillianPerformanceLoadStresstestClassResult(
					testClass.getName() /* ,plsTest */);
			ArquillianPerformanceLoadStresstestSuiteResult suiteResult = suiteResultInstance.get();

			if (suiteResult == null) {
				suiteResult = new ArquillianPerformanceLoadStresstestSuiteResult(classResult.getTestClassName());
				suiteResultInstance.set(suiteResult);
			}
		}

	}

	public ArquillianPerformanceLoadStresstestSuiteResult getSuiteResult() {
		return (ArquillianPerformanceLoadStresstestSuiteResult) this.suiteResultInstance;
	}

}
