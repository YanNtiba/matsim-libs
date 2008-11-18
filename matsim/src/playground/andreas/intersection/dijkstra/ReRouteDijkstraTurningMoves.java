/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
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
package playground.andreas.intersection.dijkstra;

import org.matsim.network.NetworkLayer;
import org.matsim.population.algorithms.PlanAlgorithm;
import org.matsim.replanning.modules.MultithreadedModuleA;
import org.matsim.router.util.TravelCost;
import org.matsim.router.util.TravelTime;

/**
 * @author dgrether
 * 
 */
public class ReRouteDijkstraTurningMoves extends MultithreadedModuleA {

	TravelCost costCalculator = null;

	TravelTime timeCalculator = null;

	NetworkLayer network = null;

	public ReRouteDijkstraTurningMoves(final NetworkLayer network, final TravelCost costCalculator,
			final TravelTime timeCalculator) {
		this.network = NetworkWrapper.wrapNetwork(network);
		this.costCalculator = costCalculator;
		this.timeCalculator = timeCalculator;
	}

	/**
	 * @see org.matsim.replanning.modules.MultithreadedModuleA#getPlanAlgoInstance()
	 */
	@Override
	public PlanAlgorithm getPlanAlgoInstance() {
		return new PlansCalcRouteDijkstra(this.network, this.costCalculator, this.timeCalculator);
	}

}
