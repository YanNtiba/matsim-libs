package org.matsim.contrib.drt.extension.preplanned.optimizer.offlineOptimization.basicStructures;

import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;

public record OnlineVehicleInfo(DvrpVehicle vehicle, Link currentLink, double divertableTime) {
}
