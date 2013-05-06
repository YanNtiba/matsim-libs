/* *********************************************************************** *
 * project: org.matsim.*
 * BestPlanSelector.java
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

package org.matsim.core.replanning.selectors;

import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;

/**
 * Selects the plan with the best score from the existing plans of the person.
 *
 * @author mrieser
 */
public class BestPlanSelector implements PlanSelector {

	/**
	 * selects the plan with the highest score from the person
	 */
	@Override
	public Plan selectPlan(final Person person) {

		double maxScore = Double.NEGATIVE_INFINITY;
		Plan bestPlan = null;

		for (Plan plan : person.getPlans()) {
			Double score = plan.getScore();
			if ((score != null) && (score.doubleValue() > maxScore) && !score.isNaN() ) {
				maxScore = plan.getScore().doubleValue();
				bestPlan = plan;
			}
		}

		if (bestPlan == null && person.getPlans().size() > 0) {
			// it seems none of the plans has a real score... so just return the first one (if there is one)
			return person.getPlans().get(0);
		}
		return bestPlan;
	}

}
