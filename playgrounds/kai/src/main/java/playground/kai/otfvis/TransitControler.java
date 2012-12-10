/* *********************************************************************** *
 * project: org.matsim.*
 * PtControler.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2009 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package playground.kai.otfvis;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.otfvis.OTFVis;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.MatsimConfigReader;
import org.matsim.core.config.groups.OTFVisConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.mobsim.framework.Mobsim;
import org.matsim.core.mobsim.framework.MobsimFactory;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.QSimFactory;
import org.matsim.vis.otfvis.OTFClientLive;
import org.matsim.vis.otfvis.OTFFileWriterFactory;
import org.matsim.vis.otfvis.OnTheFlyServer;

public class TransitControler {
	
	private static boolean useTransit = true ;

	public static void main(final String[] args) {
		//		args[0] = "/Users/nagel/kw/rotterdam/config.xml" ;
		Config config = new Config();
		config.addCoreModules();
		new MatsimConfigReader(config).readFile(args[0]);
		if ( useTransit ) {
			config.scenario().setUseTransit(true);
			config.scenario().setUseVehicles(true);
//		config.otfVis().setColoringScheme( OTFVisConfigGroup.COLORING_BVG ) ;
		}

		config.getQSimConfigGroup().setVehicleBehavior( QSimConfigGroup.VEHICLE_BEHAVIOR_TELEPORT ) ;

		Controler tc = new Controler(config) ;

		tc.setMobsimFactory(new MyMobsimFactory()) ;
		tc.addSnapshotWriterFactory("otfvis", new OTFFileWriterFactory());
		tc.setOverwriteFiles(true);
		//		tc.setCreateGraphs(false);

		tc.run();
	}

	static class MyMobsimFactory implements MobsimFactory {
		private boolean useOTFVis = true ;

		@Override
		public Mobsim createMobsim(Scenario sc, EventsManager eventsManager) {
			QSim qSim = (QSim) new QSimFactory().createMobsim(sc, eventsManager) ;
			

			if ( useOTFVis ) {
				// otfvis configuration.  There is more you can do here than via file!
				final OTFVisConfigGroup otfVisConfig = qSim.getScenario().getConfig().otfVis();
				otfVisConfig.setDrawTransitFacilities(false) ; // this DOES work
				//				otfVisConfig.setShowParking(true) ; // this does not really work
				otfVisConfig.setColoringScheme(OTFVisConfigGroup.ColoringScheme.bvg) ;


				OnTheFlyServer server = OTFVis.startServerAndRegisterWithQSim(sc.getConfig(), sc, eventsManager, qSim);
				OTFClientLive.run(sc.getConfig(), server);
			}
			//			if(this.useHeadwayControler){
			//				simulation.getQSimTransitEngine().setAbstractTransitDriverFactory(new FixedHeadwayCycleUmlaufDriverFactory());
			//				this.events.addHandler(new FixedHeadwayControler(simulation));		
			//			}

			return qSim ;
		}
	}
}
