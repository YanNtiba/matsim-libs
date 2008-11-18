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
package playground.dgrether.functions;

import org.apache.log4j.Logger;
import org.matsim.utils.charts.XYLineChart;


/**
 * @author dgrether
 *
 */
public class FunctionPlotter {

	private static final Logger log = Logger.getLogger(FunctionPlotter.class);

	private XYLineChart chart;

	private Function function;

	public FunctionPlotter() {
		this.chart = new XYLineChart("function", "x", "y");
		this.function = new Function();
		plotFunction();
	}


	public void plotFunction() {
		this.chart.addSeries(this.function.getTitle(), this.function.computeXValues(0, 5) ,this.function.computeYValues(0, 5));
		this.chart.saveAsPng("./output/plot.png", 800, 600);
		log.info("function plotted successfully");
	}




	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new FunctionPlotter();
	}

}
