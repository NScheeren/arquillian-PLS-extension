package main.impl.resultobjects;

import java.util.ArrayList;
import java.util.List;

import main.impl.annotations.PerformanceLoadStresstest;

public class ArquillianPerformanceLoadStresstestClassResult {

	public String testClassName;

	public PerformanceLoadStresstest annotation;

	public List<ArquillianPerformanceLoadStresstestMethodResult> methodResults;

	public ArquillianPerformanceLoadStresstestClassResult(String name /* , PerformanceLoadStresstest annotation */) {
		this.testClassName = name;
		// this.annotation = annotation;
		methodResults = new ArrayList<ArquillianPerformanceLoadStresstestMethodResult>();

	}

	public String getTestClassName() {
		return testClassName;
	}

	public void setTestClassName(String testClassName) {
		this.testClassName = testClassName;
	}

	public PerformanceLoadStresstest getAnnotation() {
		return annotation;
	}

	public void setAnnotation(PerformanceLoadStresstest annotation) {
		this.annotation = annotation;
	}

	public List<ArquillianPerformanceLoadStresstestMethodResult> getMethodResult() {
		return methodResults;
	}

	public void setMethodResult(List<ArquillianPerformanceLoadStresstestMethodResult> methodResult) {
		this.methodResults = methodResult;
	}

	public void addMethodResult(ArquillianPerformanceLoadStresstestMethodResult methodResult) {
		this.methodResults.add(methodResult);
	}

}
