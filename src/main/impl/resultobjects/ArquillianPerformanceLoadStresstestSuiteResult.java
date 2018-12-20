package main.impl.resultobjects;

import java.util.HashMap;
import java.util.Map;

public class ArquillianPerformanceLoadStresstestSuiteResult {

	public String name;
	public Map<String, ArquillianPerformanceLoadStresstestClassResult> classResults;

	public ArquillianPerformanceLoadStresstestSuiteResult(String name) {
		this.name = name;
		this.classResults = new HashMap<String, ArquillianPerformanceLoadStresstestClassResult>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, ArquillianPerformanceLoadStresstestClassResult> getClassResults() {
		return classResults;
	}

}
