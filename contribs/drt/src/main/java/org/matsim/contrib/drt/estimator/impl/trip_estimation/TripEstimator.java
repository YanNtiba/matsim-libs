package org.matsim.contrib.drt.estimator.impl.trip_estimation;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.utils.collections.Tuple;
import org.matsim.core.utils.misc.OptionalTime;

public interface TripEstimator {
	Tuple<Double, Double> getAlphaBetaValues(Id<Link> fromLinkId, Id<Link> toLinkId, OptionalTime departureTime);

}
