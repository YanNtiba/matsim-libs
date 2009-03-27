/**
 * 
 */
package playground.yu.analysis;

import org.matsim.core.api.network.Link;
import org.matsim.core.api.population.Leg;
import org.matsim.core.api.population.Person;
import org.matsim.core.api.population.Plan;
import org.matsim.core.api.population.PlanElement;
import org.matsim.core.utils.geometry.CoordUtils;
import org.matsim.population.algorithms.AbstractPersonAlgorithm;
import org.matsim.population.algorithms.PlanAlgorithm;
import org.matsim.roadpricing.RoadPricingScheme;

/**
 * prepares the data to compare with "BFS/ARE: Mikrozensus zum Verkehrsverhalten
 * 2005" (Kanton Zurich)
 * 
 * @author yu
 * 
 */
public class MZComparisonData extends AbstractPersonAlgorithm implements
		PlanAlgorithm {
	public static boolean isInRange(Link loc, RoadPricingScheme toll) {
		return toll.getLinks().contains(loc);
	}

	private double carDist, ptDist, wlkDist,
			otherDist,
			count,// counts all persons in population
			carDist_toll, ptDist_toll, wlkDist_toll,
			otherDist_toll,
			count_toll,// counts all inhabitants in toll area
			carTime, ptTime, wlkTime, otherTime, carTime_toll, ptTime_toll,
			wlkTime_toll, otherTime_toll
			// , linearDist, linearDist_toll
			;
	private RoadPricingScheme toll;
	private boolean inTollRange;

	public MZComparisonData(RoadPricingScheme toll) {
		this.toll = toll;
		reset(0);
	}

	@Override
	public void run(Person person) {
		count++;
		Plan pl = person.getSelectedPlan();
		inTollRange = isInRange(pl.getFirstActivity().getLink(), toll);
		if (inTollRange)
			count_toll++;
		run(pl);
	}

	public void run(Plan plan) {
		for (PlanElement pe : plan.getPlanElements()) {
			if (pe instanceof Leg) {
				double legDist = ((Leg) pe).getRoute().getDistance();// leg
				// distance
				// [m]
				String legMode = ((Leg) pe).getMode().name();
				double legTime = ((Leg) pe).getTravelTime() / 60.0;// travel
				// time
				// [min]
				double legLinearDist = CoordUtils.calcDistance(plan
						.getPreviousActivity((Leg) pe).getCoord(), plan
						.getNextActivity((Leg) pe).getCoord());// leg linear
				// distance [m]
				/*
				 * linearDist += legLinearDist; if (inTollRange) linearDist_toll
				 * += legLinearDist;
				 */
				if (legMode.equals("car")) {
					carDist += legDist;
					carTime += legTime;
					if (inTollRange) {
						carDist_toll += legDist;
						carTime_toll += legTime;
					}
				} else if (legMode.equals("pt")) {
					ptDist += legDist;
					ptTime += legTime;
					if (inTollRange) {
						ptDist_toll += legDist;
						ptTime_toll += legTime;
					}
				} else if (legMode.equals("walk")) {
					legDist = legLinearDist * 1.5;
					wlkDist += legDist;
					wlkTime += legTime;
					if (inTollRange) {
						wlkDist_toll += legDist;
						wlkTime_toll += legTime;
					}
				} else {
					otherDist += legDist;
					otherTime += legTime;
					if (inTollRange) {
						otherDist_toll += legDist;
						otherTime_toll += legTime;
					}
				}
			}
		}
	}

	public void reset(int iteration) {
		carDist = 0;
		ptDist = 0;
		wlkDist = 0;
		otherDist = 0;
		count = 0;
		carDist_toll = 0;
		ptDist_toll = 0;
		wlkDist_toll = 0;
		otherDist_toll = 0;
		count_toll = 0;
	}

	// ---------------events handle-------------------------------
	// getter -- Daily Distance
	public double getAvgDailyDistance_car_m() {
		return carDist / count;
	}

	public double getAvgTollDailyDistance_car_m() {
		return carDist_toll / count_toll;
	}

	public double getAvgDailyDistance_pt_m() {
		return ptDist / count;
	}

	public double getAvgTollDailyDistance_pt_m() {
		return ptDist_toll / count_toll;
	}

	public double getAvgDailyDistance_walk_m() {
		return wlkDist / count;
	}

	public double getAvgTollDailyDistance_walk_m() {
		return wlkDist_toll / count_toll;
	}

	public double getAvgDailyDistance_other_m() {
		return otherDist / count;
	}

	public double getAvgTollDailyDistance_other_m() {
		return otherDist_toll / count_toll;
	}

	// getter -- Daily En Route Time
	public double getAvgDailyEnRouteTime_car_min() {
		return carTime / count;
	}

	public double getAvgTollDailyEnRouteTime_car_min() {
		return carTime_toll / count_toll;
	}

	public double getAvgDailyEnRouteTime_pt_min() {
		return ptTime / count;
	}

	public double getAvgTollDailyEnRouteTime_pt_min() {
		return ptTime_toll / count_toll;
	}

	public double getAvgDailyEnRouteTime_walk_min() {
		return wlkTime / count;
	}

	public double getAvgTollDailyEnRouteTime_walk_min() {
		return wlkTime_toll / count_toll;
	}

	public double getAvgDailyEnRouteTime_other_min() {
		return otherTime / count;
	}

	public double getAvgTollDailyEnRouteTime_other_min() {
		return otherTime_toll / count_toll;
	}
	/*
	 * // getter -- Leg Linear Distance public double
	 * getAvgLegLinearDistance_m() { return linearDist / count; }
	 * 
	 * public double getAvgTollLegLinearDistance_m() { return linearDist_toll /
	 * count_toll; }
	 * 
	 * // getter -- Work Home Linear Distance public double
	 * getAvgWorkHomeLinearDistance_m() { return 0.0; }
	 * 
	 * public double getAvgTollWorkHomeLinearDistance_m() { return 0.0; }
	 */
}
