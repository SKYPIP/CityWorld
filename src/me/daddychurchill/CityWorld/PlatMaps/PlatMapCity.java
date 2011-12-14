package me.daddychurchill.CityWorld.PlatMaps;

import java.util.Random;

import me.daddychurchill.CityWorld.Plats.PlatLot;
import me.daddychurchill.CityWorld.Plats.PlatOfficeBuilding;
import me.daddychurchill.CityWorld.Plats.PlatPark;
import me.daddychurchill.CityWorld.Plats.PlatUnfinishedBuilding;

import org.bukkit.World;

public class PlatMapCity extends PlatMapUrban {

	// Instance Constants
	public int floorsMaximumAbove;
	public int floorsMaximumBelow;
	
	// Class Odds
	static final public int overallParkOdds = 4; //5; // parks show up 1/n of the time
	static final public int overallIsolatedBuildingOdds = 3; // isolated buildings 1/n of the time
	static final public int overallIdenticalHeightsOdds = 2; // similar height 1/n of the time
	static final public int overallSimilarHeightsOdds = 2; // identical height 1/n of the time
	static final public int overallSimilarRoundedOdds = 1; // like rounding 1/n of the time

	public PlatMapCity(World world, Random random, int platX, int platZ) {
		super(world, random, platX, platZ);

		// calculate the extremes for this plat
		floorsMaximumAbove = 3 + platRand.nextInt(4) * 4;
		floorsMaximumBelow = 1 + platRand.nextInt(4);

		// backfill with buildings and parks
		for (int x = 0; x < Width; x++) {
			for (int z = 0; z < Width; z++) {
				PlatLot current = platLots[x][z];
				if (current == null) {

					// what to build?
					if (platRand.nextInt(overallParkOdds) == 0)
						current = new PlatPark(platRand);
					else
//						current = new PlatUnfinishedBuilding(platRand,
						current = new PlatOfficeBuilding(platRand,
								floorsMaximumAbove, floorsMaximumBelow, 
								overallIdenticalHeightsOdds, 
								overallSimilarHeightsOdds,
								overallSimilarRoundedOdds);
					
					/* for each plot
					 *   randomly pick a plattype
					 *   see if the "previous chunk" is the same type
					 *     if so make the new plattype connected to the previous type
					 *   if the new plot is shorter than the previous plot or
					 *   if the new plot is shallower than the previous slot
					 *     mark the previous plot to have stairs
					 *   if plot does not have neighbors yet
					 *     mark the plot to have stairs
					 */

					// see if the previous chunk is the same type
					PlatLot previous = null;
					if (x > 0 && current.isConnectable(platLots[x - 1][z])) {
						previous = platLots[x - 1][z];
					} else if (z > 0 && current.isConnectable(platLots[x][z - 1])) {
						previous = platLots[x][z - 1];
					}
					
					//TODO note the running size of the connected building

					// if there was a similar previous one then copy it... maybe
					if (previous != null
							&& platRand.nextInt(overallIsolatedBuildingOdds) != 0) {
						current.makeConnected(platRand, previous);
					}

					// remember what we did
					platLots[x][z] = current;
				}
			}
		}
		
		//TODO now go through all of the lots and for each connected one make sure there is 
		//     one vertical access point (stairs, ladders, etc.)
	}
}