/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
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
package org.matsim.core.replanning;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.population.BasicPlan;
import org.matsim.api.core.v01.population.HasPlansAndId;
import org.matsim.core.replanning.modules.GenericPlanStrategyModule;
import org.matsim.core.replanning.selectors.GenericPlanSelector;
import org.matsim.core.replanning.selectors.RandomUnscoredPlanSelector;

/**
 * @author nagel
 *
 */
public class GenericPlanStrategyImpl<T extends BasicPlan> implements GenericPlanStrategy<T> {

	private GenericPlanSelector<T> planSelector = null;
	private GenericPlanStrategyModule<T> firstModule = null;
	private final ArrayList<GenericPlanStrategyModule<T>> modules = new ArrayList<GenericPlanStrategyModule<T>>();
	private final ArrayList<T> plans = new ArrayList<T>();
	private long counter = 0;
	private ReplanningContext replanningContext;
	private final static Logger log = Logger.getLogger(PlanStrategyImpl.class);

	/**
	 * Creates a new strategy using the specified planSelector.
	 *
	 * @param planSelector
	 */
	public GenericPlanStrategyImpl(final GenericPlanSelector<T> planSelector) {
		this.planSelector = planSelector;
	}

	public void addStrategyModule(final GenericPlanStrategyModule<T> module) {
		if (this.firstModule == null) {
			this.firstModule = module;
		} else {
			this.modules.add(module);
		}
	}
	
	public int getNumberOfStrategyModules() {
		if (this.firstModule == null) {
			return 0;
		}
		return this.modules.size() + 1; // we also have to count "firstModule", thus +1
	}
	
	@Override
	public void run(final HasPlansAndId<T> person) {
		this.counter++;
		
		// if there is at least one unscored plan, find that one:
//		T plan = ((PersonImpl) person).getRandomUnscoredPlan();
		T plan = new RandomUnscoredPlanSelector<T>().selectPlan(person) ;
		
		// otherwise, find one according to selector (often defined in PlanStrategy ctor):
		if (plan == null) {
			plan = this.planSelector.selectPlan(person);
		}
		
		// "select" that plan:
		if ( plan != null ) {
//			((PersonImpl) person).setSelectedPlan(plan);
			person.setSelectedPlan(plan);
		}
		else {
			log.error( planSelector+" returned no plan: not changing selected plan for person "+person );
		}


		// if there is a "module" (i.e. "innovation"):
		if (this.firstModule != null) {
			
			// set the working plan to a copy of the selected plan:
//			plan = ((PersonImpl) person).copySelectedPlan();
			plan = person.createCopyOfSelectedPlanAndMakeSelected();
			// (this makes, as a side effect, the _new_ plan selected)
			
			// add new plan to container that contains the plans that are handled by this PlanStrategy:
			this.plans.add(plan);

			// start working on this new plan:
			this.firstModule.handlePlan(plan);
		}

	}

	@Override
	public void init(ReplanningContext replanningContext0) {
		this.replanningContext = replanningContext0;
		if (this.firstModule != null) {
			this.firstModule.prepareReplanning(replanningContext0);
		}
	}

	@Override
	public void finish() {
		if (this.firstModule != null) {
			// finish the first module
				this.firstModule.finishReplanning();
			// now work through the others
			for (GenericPlanStrategyModule<T> module : this.modules) {
				module.prepareReplanning(replanningContext);
				for (T plan : this.plans) {
					module.handlePlan(plan);
				}
				module.finishReplanning();
			}
		}
		this.plans.clear();
		log.info("Plan-Strategy finished, " + this.counter + " plans handled. Strategy: " + this.toString());
		this.counter = 0;
	}

	@Override
	public String toString() {
		StringBuilder name = new StringBuilder(20);
		name.append(this.planSelector.getClass().getSimpleName());
		if (this.firstModule != null) {
			name.append('_');
			name.append(this.firstModule.getClass().getSimpleName());
			for (Object module : this.modules) {
				name.append('_');
				name.append(module.getClass().getSimpleName());
			}
		}
		return name.toString();
	}

	public GenericPlanSelector<T> getPlanSelector() {
		return planSelector;
	}
	

}
