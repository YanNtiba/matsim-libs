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
package playground.dgrether.analysis.runscompare;

import playground.dgrether.DgPaths;


/**
 * @author dgrether
 *
 */
public class IvtChScenarioPathsSetter implements ScenarioPathsSetter {

	public IvtChScenarioPathsSetter(){}

	public String getCountsFile() {
		return DgPaths.IVTCHCOUNTS;
	}

	public String getNetworkFile() {
		return DgPaths.IVTCHNET;
	}

	public String getRoadPricingFile() {
		return DgPaths.IVTCHROADPRICING;
	}
	
	
}
