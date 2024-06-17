package org.matsim.application.prepare.freight.tripGeneration;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.*;
import org.matsim.application.options.LanduseOptions;
import org.matsim.core.population.PopulationUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * The {@code FreightAgentGenerator} creates freight-agent plans using ketten-2010 data.
 */
public class FreightAgentGenerator {
    private final LocationCalculator locationCalculator;
    private final DepartureTimeCalculator departureTimeCalculator;
    private final NumOfTripsCalculator numOfTripsCalculator;
    private final PopulationFactory populationFactory;
    private final Network network;

	/**
	 * @param network network xml-file
	 * @param shpPath path to shapefile containing NUTS-regions
	 * @param landUse
	 * @param averageLoad Average load of a truck
	 * @param workingDays Working days per year
	 * @param sample MATSIM agent sample (0.0-1.0)
	 * @throws IOException if the given path is wrong or file is currently not available
	 */
    public FreightAgentGenerator(Network network, Path shpPath, LanduseOptions landUse, double averageLoad, int workingDays, double sample) throws IOException {
        this.locationCalculator = new DefaultLocationCalculator(network, shpPath, landUse);
        this.departureTimeCalculator = new DefaultDepartureTimeCalculator();
        this.numOfTripsCalculator = new DefaultNumberOfTripsCalculator(averageLoad, workingDays, sample);
        this.populationFactory = PopulationUtils.getFactory();
        this.network = network;
    }

	/**
	 * This constructor allows to use an external lookup table
	 * @param network network xml-file
	 * @param shpPath path to shapefile containing NUTS-regions
	 * @param lookupTablePathString URI-path to the lookup table (for local files start with "file:/")
	 * @param landUse
	 * @param averageLoad Average load of a truck
	 * @param workingDays Working days per year
	 * @param sample MATSIM agent sample (0.0-1.0)
	 * @throws IOException if the given path is wrong or file is currently not available
	 */
	public FreightAgentGenerator(Network network, Path shpPath, String lookupTablePathString, LanduseOptions landUse, double averageLoad, int workingDays, double sample) throws IOException {
		this.locationCalculator = new DefaultLocationCalculator(network, shpPath, lookupTablePathString, landUse);
		this.departureTimeCalculator = new DefaultDepartureTimeCalculator();
		this.numOfTripsCalculator = new DefaultNumberOfTripsCalculator(averageLoad, workingDays, sample);
		this.populationFactory = PopulationUtils.getFactory();
		this.network = network;
	}

	/**
	 * Creates freight-agent plans using a ketten-2010 data-entry.
	 * @param tripRelation Entry from ketten-2010 table in form of a {@link TripRelation} object, used as a base for plan-generation.
	 * @param tripRelationId Names the ids of the generated Persons
	 * @return A list of freight-agents ({@link Person}) with finished plans between two NUTS regions.
	 */
    public List<Person> generateRoadFreightAgents(TripRelation tripRelation, String tripRelationId) {
        List<Person> freightAgents = new ArrayList<>();
        String preRunMode = tripRelation.getModePreRun();
        String mainRunMode = tripRelation.getModeMainRun();
        String postRunMode = tripRelation.getModePostRun();

        if (!preRunMode.equals("2") && !mainRunMode.equals("2") && !postRunMode.equals("2")) {
            return freightAgents; // This trip relation is irrelevant as it does not contain any freight by road
        }

        int numOfTrips = numOfTripsCalculator.calculateNumberOfTrips(tripRelation.getTonsPerYearMainRun(), tripRelation.getGoodsTypeMainRun());
        for (int i = 0; i < numOfTrips; i++) {
            // pre-run
            if (preRunMode.equals("2")) {
				// Initialize Person object
                Person person = populationFactory.createPerson(Id.createPersonId("freight_" + tripRelationId + "_" + i + "_pre"));
                Plan plan = populationFactory.createPlan();
                double departureTime = departureTimeCalculator.getDepartureTime();

				// Add start-activity (type: "freight_start") into plan
                Id<Link> startLinkId = locationCalculator.getLocationOnNetwork(tripRelation.getOriginCell());
                Activity startAct = populationFactory.createActivityFromLinkId("freight_start", startLinkId);
                startAct.setCoord(network.getLinks().get(startLinkId).getToNode().getCoord());
                startAct.setEndTime(departureTime);
                plan.addActivity(startAct);

				// Add freight-leg into plan
                Leg leg = populationFactory.createLeg("freight");
                plan.addLeg(leg);

				// Add end-activity (type: "freight_start") into plan
                Id<Link> endLinkId = locationCalculator.getLocationOnNetwork(tripRelation.getOriginCellMainRun());
                Activity endAct = populationFactory.createActivityFromLinkId("freight_end", endLinkId);
                endAct.setCoord(network.getLinks().get(endLinkId).getToNode().getCoord());
                plan.addActivity(endAct);

                person.addPlan(plan);
                person.getAttributes().putAttribute("trip_type", "pre-run");
				LongDistanceFreightUtils.writeCommonAttributesV1(person, tripRelation, tripRelationId);

                freightAgents.add(person);
            }

            // main-run
            if (mainRunMode.equals("2")) {
				// Initialize Person object
                Person person = populationFactory.createPerson(Id.createPersonId("freight_" + tripRelationId + "_" + i + "_main"));
                Plan plan = populationFactory.createPlan();
                double departureTime = departureTimeCalculator.getDepartureTime();

				// Add start-activity (type: "freight_start") into plan
                Id<Link> startLinkId = locationCalculator.getLocationOnNetwork(tripRelation.getOriginCellMainRun());
                Activity startAct = populationFactory.createActivityFromLinkId("freight_start", startLinkId);
                startAct.setEndTime(departureTime);
                startAct.setCoord(network.getLinks().get(startLinkId).getToNode().getCoord());
                plan.addActivity(startAct);

				// Add freight-leg into plan
                Leg leg = populationFactory.createLeg("freight");
                plan.addLeg(leg);

				// Add end-activity (type: "freight_start") into plan
                Id<Link> endLinkId = locationCalculator.getLocationOnNetwork(tripRelation.getDestinationCellMainRun());
                Activity endAct = populationFactory.createActivityFromLinkId("freight_end", endLinkId);
                endAct.setCoord(network.getLinks().get(endLinkId).getToNode().getCoord());
                plan.addActivity(endAct);

                person.addPlan(plan);
                person.getAttributes().putAttribute("trip_type", "main-run");
				LongDistanceFreightUtils.writeCommonAttributesV1(person, tripRelation, tripRelationId);

                freightAgents.add(person);
            }

            // post-run
            if (postRunMode.equals("2")) {
				// Initialize Person object
				Person person = populationFactory.createPerson(Id.createPersonId("freight_" + tripRelationId + "_" + i + "_post"));
                Plan plan = populationFactory.createPlan();
                double departureTime = departureTimeCalculator.getDepartureTime();

				// Add start-activity (type: "freight_start") into plan
				Id<Link> startLinkId = locationCalculator.getLocationOnNetwork(tripRelation.getDestinationCellMainRun());
                Activity startAct = populationFactory.createActivityFromLinkId("freight_start", startLinkId);
                startAct.setCoord(network.getLinks().get(startLinkId).getToNode().getCoord());
                startAct.setEndTime(departureTime);
                plan.addActivity(startAct);

				// Add freight-leg into plan
                Leg leg = populationFactory.createLeg("freight");
                plan.addLeg(leg);

				// Add end-activity (type: "freight_start") into plan
				Id<Link> endLinkId = locationCalculator.getLocationOnNetwork(tripRelation.getDestinationCell());
                Activity endAct = populationFactory.createActivityFromLinkId("freight_end", endLinkId);
                endAct.setCoord(network.getLinks().get(endLinkId).getToNode().getCoord());
                plan.addActivity(endAct);

                person.addPlan(plan);
                person.getAttributes().putAttribute("trip_type", "post-run");
				LongDistanceFreightUtils.writeCommonAttributesV1(person, tripRelation, tripRelationId);

                freightAgents.add(person);
            }
        }

        return freightAgents;
    }

		//TODO integrate Terminals
		//TODO überlegen ob man start/End Locations eventuell bei einigen Gütergruppen gleich lassen
		//TODO eventuell Attribute nur für Daten dieser Trips hinzufügen (nur pre, main, post)
		//TODO link of end of pre run should be link of start of main run

	/** Generates freight agents for a given freight demand data relation.
	 * Uses a {@link Person} as base to generate a List of freight agents.
	 * Required attribute-keys of {@link Person} are expected to be set: <i>mode_pre_run</i>, <i>mode_main-run</i>, <i>mode_post-run</i>, ... <br>
	 * Settings for Generation are saved in static {@link LongDistanceFreightUtils} attributes.
	 * @param freightDemandDataRelation
	 * @param tripRelationId
	 * @return
	 */
public List<Person> generateRoadFreightAgents(Person freightDemandDataRelation, String tripRelationId) {
	List<Person> freightAgents = new ArrayList<>();
	if (LongDistanceFreightUtils.getModePreRun(freightDemandDataRelation).equals(LongDistanceFreightUtils.LongDistanceTravelMode.road)) {
		String tripType = "pre-run";
		String fromCell = LongDistanceFreightUtils.getOriginCell(freightDemandDataRelation);
		String toCell = LongDistanceFreightUtils.getOriginCellMainRun(freightDemandDataRelation);
		double tonsPerYear = LongDistanceFreightUtils.getTonsPerYearPreRun(freightDemandDataRelation);
		String goodsType = String.valueOf(LongDistanceFreightUtils.getGoodsTypePreRun(freightDemandDataRelation));

		createFreightAgent(freightDemandDataRelation, tripRelationId, tripType, fromCell, toCell, tonsPerYear, goodsType, freightAgents);
	}
	if (LongDistanceFreightUtils.getModeMainRun(freightDemandDataRelation).equals(LongDistanceFreightUtils.LongDistanceTravelMode.road)) {
		String tripType = "main-run";
		String fromCell = LongDistanceFreightUtils.getOriginCellMainRun(freightDemandDataRelation);
		String toCell = LongDistanceFreightUtils.getDestinationCellMainRun(freightDemandDataRelation);
		double tonsPerYear = LongDistanceFreightUtils.getTonsPerYearMainRun(freightDemandDataRelation);
		String goodsType = String.valueOf(LongDistanceFreightUtils.getGoodsTypeMainRun(freightDemandDataRelation));

		createFreightAgent(freightDemandDataRelation, tripRelationId, tripType, fromCell, toCell, tonsPerYear, goodsType, freightAgents);
	}
	if (LongDistanceFreightUtils.getModePostRun(freightDemandDataRelation).equals(LongDistanceFreightUtils.LongDistanceTravelMode.road)) {
		String tripType = "post-run";
		String fromCell = LongDistanceFreightUtils.getDestinationCellMainRun(freightDemandDataRelation);
		String toCell = LongDistanceFreightUtils.getDestinationCell(freightDemandDataRelation);
		double tonsPerYear = LongDistanceFreightUtils.getTonsPerYearPostRun(freightDemandDataRelation);
		String goodsType = String.valueOf(LongDistanceFreightUtils.getGoodsTypePostRun(freightDemandDataRelation));

		createFreightAgent(freightDemandDataRelation, tripRelationId, tripType, fromCell, toCell, tonsPerYear, goodsType, freightAgents);
	}
	return freightAgents;
	}

	/** Generates all necessary freight agents for a given freight demand data relation.
	 * @param freightDemandDataRelation
	 * @param tripRelationId
	 * @param tripType
	 * @param fromCell
	 * @param toCell
	 * @param tonsPerYear
	 * @param goodsType
	 * @param freightAgents This list will be extended by the generated agents
	 */
	private void createFreightAgent(Person freightDemandDataRelation, String tripRelationId, String tripType, String fromCell, String toCell,
									double tonsPerYear, String goodsType, List<Person> freightAgents) {
		int numOfTrips = numOfTripsCalculator.calculateNumberOfTripsV2(tonsPerYear, goodsType);
		for (int i = 0; i < numOfTrips; i++) {
			Person person = populationFactory.createPerson(Id.createPersonId("freight_" + tripRelationId + "_" + i + "_" + tripType));
			double departureTime = departureTimeCalculator.getDepartureTime();

			Id<Link> startLinkId = locationCalculator.getLocationOnNetwork(fromCell);
			Id<Link> endLinkId = locationCalculator.getLocationOnNetwork(toCell);
			Plan plan = populationFactory.createPlan();

			Activity startAct = populationFactory.createActivityFromLinkId("freight_start", startLinkId);
			startAct.setCoord(network.getLinks().get(startLinkId).getToNode().getCoord());
			startAct.setEndTime(departureTime);
			plan.addActivity(startAct);

			PopulationUtils.createAndAddLeg(plan, "freight");
			PopulationUtils.createAndAddActivityFromLinkId(plan, "freight_end", endLinkId);

			person.addPlan(plan);
			LongDistanceFreightUtils.setTripType(person, tripType);
			freightDemandDataRelation.getAttributes().getAsMap().forEach((k, v) -> person.getAttributes().putAttribute(k, v));
			freightAgents.add(person);
		}
	}
	public String getVerkehrszelleOfLink(Id<Link> linkId) {
		return locationCalculator.getVerkehrszelleOfLink(linkId);
	}

	public interface LocationCalculator {
        Id<Link> getLocationOnNetwork(String verkehrszelle);
		String getVerkehrszelleOfLink(Id<Link> linkId);
	}

    public interface DepartureTimeCalculator {
        double getDepartureTime();
    }

    public interface NumOfTripsCalculator {
        int calculateNumberOfTrips(double tonsPerYear, String goodsType);
		int calculateNumberOfTripsV2(double tonsPerYear, String goodsType);

	}

}
