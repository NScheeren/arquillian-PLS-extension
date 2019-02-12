package src.main.impl.extension;

import org.jboss.arquillian.core.spi.LoadableExtension;

public class ArquillianPerformanceLoadStresstestExtension implements LoadableExtension {

	@Override
	public void register(ExtensionBuilder builder) {

		// builder.observer evtl nur fuer Klassen, die Events observen?
		builder.observer(ArquillianPerformanceLoadStresstestMethodExecutor.class);
		builder.observer(ArquillianPerformanceLoadStresstestCheck.class);
		builder.observer(ArquillianPerformanceLoadStresstestInitializer.class);

	}

}
