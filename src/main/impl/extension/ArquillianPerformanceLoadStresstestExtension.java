package src.main.impl.extension;

import org.jboss.arquillian.core.spi.LoadableExtension;

public class ArquillianPerformanceLoadStresstestExtension implements LoadableExtension {

	@Override
	public void register(ExtensionBuilder builder) {

		builder.observer(ArquillianPerformanceLoadStresstestMethodExecutor.class);
		builder.observer(ArquillianPerformanceLoadStresstestCheck.class);

	}

}
