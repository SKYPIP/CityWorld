package me.daddychurchill.CityWorld.Plats;

import java.util.Random;

import me.daddychurchill.CityWorld.PlatMaps.PlatMap;
import me.daddychurchill.CityWorld.Support.ByteChunk;
import me.daddychurchill.CityWorld.Support.Direction.StairWell;
import me.daddychurchill.CityWorld.Support.RealChunk;
import me.daddychurchill.CityWorld.Support.SurroundingFloors;

import org.bukkit.Material;

public class PlatOfficeBuilding extends PlatBuilding {

	protected final static int FloorHeight = PlatMap.FloorHeight;
	protected final static int stairWallMaterialIsWallMaterialOdds = 2; // stair walls are the same as walls 1/n of the time
	protected final static int buildingWallInsettedOdds = 2; // building walls inset as they go up 1/n of the time
	protected final static int buildingWallInsettedMinHeight = 8; // minimum building height before insetting is allowed
	protected final static int buildingWallInsettedMinMidPoint = 2; // lowest point of inset
	protected final static int buildingWallInsettedMinHighPoint = buildingWallInsettedMinHeight; // lowest highest point of inset
	
	protected Material wallMaterial;
	protected Material ceilingMaterial;
	protected Material glassMaterial;
	protected Material stairMaterial;
	protected Material stairWallMaterial;
	protected Material doorMaterial;
	
	//TODO columns height
	//TODO rounded building corners (if connected N and E and have a quarter arc of a building)
	//TODO roof fixtures (peak, antenna, air conditioning, stairwells access, penthouse, castle trim, etc.

	protected int insetWallNS;
	protected int insetWallEW;
	protected int insetCeilingNS;
	protected int insetCeilingEW;
	protected boolean insetInsetted;
	protected int insetInsetMidAt;
	protected int insetInsetHighAt;

	public PlatOfficeBuilding(Random rand, int maxHeight, int maxDepth, 
			int overallIdenticalHeightsOdds, 
			int overallSimilarHeightsOdds,
			int overallSimilarRoundedOdds) {
		super(rand, maxHeight, maxDepth, overallIdenticalHeightsOdds, 
				overallSimilarHeightsOdds, overallSimilarRoundedOdds);

		// nudge in a bit
		insetWallNS = rand.nextInt(2) + 1; // 1 or 2
		insetWallEW = insetWallNS;//rand.nextInt(2) + 1;
		insetCeilingNS = insetWallNS + rand.nextInt(3) - 1; // -1, 0 or 1 -> 0, 1, 2
		insetCeilingEW = insetCeilingNS;//insetWallEW + rand.nextInt(3) - 1;
		
		// nudge in a bit more as we go up
		insetInsetMidAt = 1;
		insetInsetHighAt = 1;
		insetInsetted = height >= buildingWallInsettedMinHeight && rand.nextInt(buildingWallInsettedOdds) == 0;
		if (insetInsetted) {
			insetInsetMidAt = Math.max(buildingWallInsettedMinMidPoint, rand.nextInt(buildingWallInsettedMinHeight));
			insetInsetHighAt = Math.max(insetInsetMidAt + 1, rand.nextInt(buildingWallInsettedMinHeight));
		}
		
		// what is it made of?
		wallMaterial = pickWallMaterial(rand);
		ceilingMaterial = pickCeilingMaterial(rand);
		glassMaterial = pickGlassMaterial(rand);
		stairMaterial = pickStairMaterial(wallMaterial);
		doorMaterial = Material.WOOD_DOOR;
		
		// what are the walls of the stairs made of?
		if (rand.nextInt(stairWallMaterialIsWallMaterialOdds) == 0)
			stairWallMaterial = wallMaterial;
		else
			stairWallMaterial = pickStairWallMaterial(wallMaterial);

		// Fix up any material issues
		// thin glass should not be used with ceiling inset, it looks goofy
		// thin glass should not be used with double-step walls, the glass does not align correctly
		if (glassMaterial == Material.THIN_GLASS) {
			insetCeilingNS = Math.min(insetCeilingNS, insetWallNS);
			insetCeilingEW = Math.min(insetCeilingEW, insetWallEW);
			if (wallMaterial == Material.DOUBLE_STEP)
				glassMaterial = Material.GLASS;
		}
	}

	public void makeConnected(Random rand, PlatLot relative) {
		super.makeConnected(rand, relative);

		// other bits
		if (relative instanceof PlatOfficeBuilding) {
			PlatOfficeBuilding relativebuilding = (PlatOfficeBuilding) relative;

			// nudge in a bit
			insetWallNS = relativebuilding.insetWallNS;
			insetWallEW = relativebuilding.insetWallEW;
			insetCeilingNS = relativebuilding.insetCeilingNS;
			insetCeilingEW = relativebuilding.insetCeilingEW;
			
			// nudge in a bit more as we go up
			insetInsetted = relativebuilding.insetInsetted;
			insetInsetMidAt = relativebuilding.insetInsetMidAt;
			insetInsetHighAt = relativebuilding.insetInsetHighAt;
			
			// what is it made of?
			wallMaterial = relativebuilding.wallMaterial;
			ceilingMaterial = relativebuilding.ceilingMaterial;
			glassMaterial = relativebuilding.glassMaterial;
			stairMaterial = relativebuilding.stairMaterial;
			stairWallMaterial = relativebuilding.stairWallMaterial;
			doorMaterial = relativebuilding.doorMaterial;
		}
	}

	@Override
	public void generateChunk(PlatMap platmap, ByteChunk chunk, int platX, int platZ) {
		// check out the neighbors
		SurroundingFloors neighborBasements = getNeighboringBasementCounts(platmap, platX, platZ);
		SurroundingFloors neighborFloors = getNeighboringFloorCounts(platmap, platX, platZ);

		// starting with the bottom
		int lowestY = PlatMap.StreetLevel - FloorHeight * (depth - 1) - 3;
		generateBedrock(chunk, lowestY);
		
		// bottom most floor
		chunk.setBlocks(0, ByteChunk.Width, lowestY, lowestY + 1, 0, ByteChunk.Width, (byte) ceilingMaterial.getId());
		
		// below ground
		for (int floor = 0; floor < depth; floor++) {
			int floorAt = PlatMap.StreetLevel - FloorHeight * floor - 2;
			
			// one floor please
			drawWalls(chunk, floorAt, FloorHeight - 1, 0, 0, false,
					wallMaterial, wallMaterial, neighborBasements);
			drawCeilings(chunk, floorAt + FloorHeight - 1, 1, 0, 0, false,
					ceilingMaterial, neighborBasements);
			
			// one down, more to go
			neighborBasements.decrement();
		}

		// is rounding allowed?
		boolean allowRounded = rounded && 
				insetWallNS == insetWallEW && insetCeilingNS == insetCeilingEW;
		
		// insetting the inset
		int localInsetWallNS = insetWallNS;
		int localInsetWallEW = insetWallEW;
		int localInsetCeilingNS = insetCeilingNS;
		int localInsetCeilingEW = insetCeilingEW;

		// above ground
		for (int floor = 0; floor < height; floor++) {
			int floorAt = PlatMap.StreetLevel + FloorHeight * floor + 2;
			allowRounded = allowRounded && neighborFloors.isRoundable();

			// breath in?
			if (insetInsetted) {
				if (floor == insetInsetMidAt || floor == insetInsetHighAt) {
					localInsetWallNS++;
					localInsetWallEW++;
					localInsetCeilingNS++;
					localInsetCeilingEW++;
				}
			}
			
			// one floor please
			drawWalls(chunk, floorAt, FloorHeight - 1, 
					localInsetWallNS, localInsetWallEW, 
					allowRounded, wallMaterial, glassMaterial, neighborFloors);
			drawCeilings(chunk, floorAt + FloorHeight - 1, 1, 
					localInsetCeilingNS, localInsetCeilingEW, 
					allowRounded, ceilingMaterial, neighborFloors);

			// one down, more to go
			neighborFloors.decrement();
		}
	}
	
	@Override
	public void generateBlocks(PlatMap platmap, RealChunk chunk, int platX, int platZ) {
		// check out the neighbors
		//SurroundingFloors neighborBasements = getNeighboringBasementCounts(platmap, platX, platZ);
		SurroundingFloors neighborFloors = getNeighboringFloorCounts(platmap, platX, platZ);
		
		// is rounding allowed and where are the stairs
		boolean allowRounded = rounded && 
				insetWallNS == insetWallEW && insetCeilingNS == insetCeilingEW && 
				neighborFloors.isRoundable();
		StairWell stairLocation = getStairWellLocation(allowRounded, neighborFloors);
		
		// bottom floor? if there is going to be stairs here, lets place some matching doors
		if (allowRounded) {
			//TODO drawCornerDoors
		} else {
			drawCenteredDoors(chunk, insetWallNS, RealChunk.Width - insetWallNS - 1, 
					  insetWallEW, RealChunk.Width - insetWallEW - 1,
					  PlatMap.StreetLevel + 2, FloorHeight,
					  !neighborFloors.toSouth(), !neighborFloors.toNorth(), 
					  !neighborFloors.toWest(), !neighborFloors.toEast(),
					  wallMaterial);
		}
		
		// work on the basement stairs first
		for (int floor = 0; floor < depth; floor++) {
			int y = PlatMap.StreetLevel - FloorHeight * floor - 2;
			
			// stairs?
			if (needStairsDown) {
				
				// top is special... but only if there are no stairs up
				if (floor == 0 && !needStairsUp) {
					drawStairsWalls(chunk, y, FloorHeight, insetWallNS, insetWallEW, stairLocation, stairWallMaterial, true, false);
				
				// all the rest of those lovely stairs
				} else {
					// place the stairs and such
					drawStairs(chunk, y, FloorHeight, insetWallNS, insetWallEW, stairLocation, stairMaterial);
						
					// plain walls please
					drawStairsWalls(chunk, y, FloorHeight, insetWallNS, insetWallEW, stairLocation, wallMaterial, false, floor == depth - 1);

					// pillars if no stairs here
					drawOtherPillars(chunk, y, FloorHeight, stairLocation, wallMaterial);
				}
			
			// if no stairs then
			} else {
				drawOtherPillars(chunk, y, FloorHeight, StairWell.CENTER, wallMaterial);
			}
		}
		
		// now the above ground floors
		if (needStairsUp) {
			for (int floor = 0; floor < height; floor++) {
				int y = PlatMap.StreetLevel + FloorHeight * floor + 2;
				
				// more stairs and such
				if (floor < height - 1)
					drawStairs(chunk, y, FloorHeight, insetWallNS, insetWallEW, stairLocation, stairMaterial);
				
				// fancy walls... maybe
				drawStairsWalls(chunk, y, FloorHeight, insetWallNS, insetWallEW, stairLocation, stairWallMaterial, floor == height - 1, false);
			}
		}
	}
	
	static protected Material pickWallMaterial(Random rand) {
		switch (rand.nextInt(12)) {
		case 1:
			return Material.COBBLESTONE;
		case 2:
			return Material.STONE;
		case 3:
			return Material.SMOOTH_BRICK;
		case 4:
			return Material.CLAY;
		case 5:
			return Material.IRON_BLOCK;
		case 6:
			return Material.BRICK;
		case 7:
			return Material.MOSSY_COBBLESTONE;
		case 8:
			return Material.DOUBLE_STEP;
		case 9:
			return Material.SANDSTONE;
		case 10:
			return Material.WOOD;
		case 11:
			return Material.WOOL;
		default:
			return Material.SAND;
		}
	}

	static protected Material pickStairMaterial(Material wall) {
		switch (wall) {
		case COBBLESTONE:
		case MOSSY_COBBLESTONE:
			return Material.COBBLESTONE_STAIRS;

		case STONE:
		case SMOOTH_BRICK:
		case CLAY:
		case IRON_BLOCK:
		case DOUBLE_STEP:
			return Material.SMOOTH_STAIRS;

		case WOOL:
		case BRICK:
			return Material.BRICK_STAIRS;
		
		default: // SANDSTONE, WOOD, SAND
			return Material.WOOD_STAIRS;
		}
	}

	static protected Material pickStairWallMaterial(Material wall) {
		switch (wall) {
		case COBBLESTONE:
		case MOSSY_COBBLESTONE:
		case STONE:
		case SMOOTH_BRICK:
			return Material.IRON_FENCE;

		case IRON_BLOCK:
		case DOUBLE_STEP:
		case WOOL:
		case BRICK:
			return Material.THIN_GLASS;
		
		default: // SANDSTONE, WOOD, SAND, CLAY
			return Material.FENCE;
		}
	}

	static protected Material pickCeilingMaterial(Random rand) {
		switch (rand.nextInt(11)) {
		case 1:
			return Material.COBBLESTONE;
		case 2:
			return Material.STONE;
		case 3:
			return Material.SMOOTH_BRICK;
		case 4:
			return Material.CLAY;
		case 5:
			return Material.IRON_BLOCK;
		case 6:
			return Material.BRICK;
		case 7:
			return Material.MOSSY_COBBLESTONE;
		case 8:
			return Material.DOUBLE_STEP;
		case 9:
			return Material.SANDSTONE;
		case 10:
			return Material.WOOD;
		default:
			return Material.WOOL;
		}
	}
}