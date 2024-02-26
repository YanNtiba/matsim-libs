package org.matsim.contrib.drt.estimator;

import com.google.inject.Binder;
import com.google.inject.Singleton;
import com.google.inject.multibindings.MapBinder;
import org.matsim.contrib.drt.analysis.DrtEventSequenceCollector;
import org.matsim.contrib.drt.estimator.impl.BasicDrtEstimator;
import org.matsim.contrib.drt.estimator.impl.PessimisticDrtEstimator;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeModule;
import org.matsim.contrib.dvrp.run.DvrpMode;
import org.matsim.contrib.dvrp.run.DvrpModes;
import org.matsim.core.modal.ModalProviders;

/**
 * Main module that needs to be installed if any estimator is to be used.
 */
public class DrtEstimatorModule extends AbstractDvrpModeModule {

	private final DrtConfigGroup drtCfg;
	private final DrtEstimatorParams params;

	public DrtEstimatorModule(String mode, DrtConfigGroup drtCfg, DrtEstimatorParams params) {
		super(mode);
		this.drtCfg = drtCfg;
		this.params = params;
	}

	public static Object bindEstimator(Binder binder, String mode){

		// TODO create helper method for binding

//		DvrpMode key = DvrpModes.mode(mode);
//		binder.bind(key)
//		ModalProviders.createProvider(mode, DvrpModes::mode);
		return null;
	}

	@Override
	public void install() {

		// DRT Estimators will be available as Map<DvrpMode, DrtEstimator>
		MapBinder.newMapBinder(this.binder(), DvrpMode.class, DrtEstimator.class)
			.addBinding(DvrpModes.mode(getMode()))
			.to(modalKey(DrtEstimator.class));

		addControlerListenerBinding().to(modalKey(DrtEstimator.class));

		bindModal(DrtEstimatorParams.class).toInstance(params);

		// Needs to run before estimators
		bindModal(DrtEstimateAnalyzer.class)
			.toProvider(
				modalProvider(getter -> new DrtEstimateAnalyzer(getter.getModal(DrtEstimator.class),
					getter.getModal(DrtEventSequenceCollector.class), getMode()))
			)
			.in(Singleton.class);

		addControlerListenerBinding().to(modalKey(DrtEstimateAnalyzer.class));

	}


}
