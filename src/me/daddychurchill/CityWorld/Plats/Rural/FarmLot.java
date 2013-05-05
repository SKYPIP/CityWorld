package me.daddychurchill.CityWorld.Plats.Rural;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;

import me.daddychurchill.CityWorld.WorldGenerator;
import me.daddychurchill.CityWorld.Context.DataContext;
import me.daddychurchill.CityWorld.Plats.ConnectedLot;
import me.daddychurchill.CityWorld.Plats.PlatLot;
import me.daddychurchill.CityWorld.Support.ByteChunk;
import me.daddychurchill.CityWorld.Support.Odds;
import me.daddychurchill.CityWorld.Support.PlatMap;
import me.daddychurchill.CityWorld.Support.RealChunk;
import me.daddychurchill.CityWorld.Support.SurroundingLots;

public class FarmLot extends ConnectedLot {

	//TODO Tree farm?
	//TODO Apple farm?
	//TODO Cocoa farm?
	//TODO PPPwPPPPPPwPPP based wheat/flower/grass/mushroom/netherwart/dead/fallow
	//TODO SPSwSPSSPSwSPS based pumpkin/melon/dead/fallow
	//TODO wPPwPPwwPPwPPw based cane/dead/fallow
	
	private boolean directionNorthSouth;
	private Material cropType;
	private double oddsOfCrop = DataContext.oddsExtremelyLikely;

	public FarmLot(PlatMap platmap, int chunkX, int chunkZ) {
		super(platmap, chunkX, chunkZ);
		
		style = LotStyle.STRUCTURE;
		
		directionNorthSouth = chunkOdds.flipCoin();
		
		// crop type please
		if (platmap.generator.worldEnvironment == Environment.NETHER)
			if (platmap.generator.settings.includeDecayedNature)
				cropType = getDecayedNetherCrop();
			else
				cropType = getNetherCrop();
		else
			if (platmap.generator.settings.includeDecayedNature)
				cropType = getDecayedNormalCrop();
			else
				cropType = getNormalCrop();

		// decayed world?
		if (platmap.generator.settings.includeDecayedNature)
			oddsOfCrop = DataContext.oddsSomewhatUnlikely;
	}

	@Override
	public PlatLot newLike(PlatMap platmap, int chunkX, int chunkZ) {
		return new FarmLot(platmap, chunkX, chunkZ);
	}

	@Override
	public boolean makeConnected(PlatLot relative) {
		boolean result = super.makeConnected(relative);
		
		// other bits
		if (result && relative instanceof FarmLot) {
			FarmLot relativeFarm = (FarmLot) relative;
			
			directionNorthSouth = relativeFarm.directionNorthSouth;
			cropType = relativeFarm.cropType;
		}
		return result;
	}

	private final static byte isolationNormalId = (byte) Material.LOG.getId();
	private final static byte isolationNetherId = (byte) Material.NETHER_BRICK.getId();
	
	private final static Material matWater = Material.STATIONARY_WATER;
	private final static Material matSoil = Material.SOIL;
	private final static Material matSand = Material.SAND;
	private final static Material matMycel = Material.MYCEL;
	private final static Material matDirt = Material.DIRT;
	private final static Material matSoul = Material.SOUL_SAND;
	private final static Material matAir = Material.AIR;
	private final static Material matTrellis = Material.WOOD;
	private final static Material matPole = Material.FENCE;

	private final static Material cropFallow = Material.AIR;
	private final static Material cropYellowFlower = Material.YELLOW_FLOWER;
	private final static Material cropRedFlower = Material.RED_ROSE;
	private final static Material cropWheat = Material.CROPS;
	private final static Material cropPumpkin = Material.PUMPKIN_STEM;
	private final static Material cropMelon = Material.MELON_STEM;
	private final static Material cropVine = Material.VINE;
	private final static Material cropTrellis = Material.WOOD;
	private final static Material cropSugarCane = Material.SUGAR_CANE_BLOCK;
	private final static Material cropGrass = Material.LONG_GRASS;
	private final static Material cropCactus = Material.CACTUS;
	private final static Material cropBrownMushroom = Material.BROWN_MUSHROOM;
	private final static Material cropRedMushroom = Material.RED_MUSHROOM;
	private final static Material cropNetherwart = Material.NETHER_WARTS;
	private final static Material cropDeadBush = Material.DEAD_BUSH; 
	private final static Material cropNone = Material.DIRT;
	private final static Material cropCarrot = Material.CARROT;
	private final static Material cropPotato = Material.POTATO;
	
	@Override
	public int getBottomY(WorldGenerator generator) {
		return generator.streetLevel;
	}
	
	@Override
	protected void generateActualChunk(WorldGenerator generator, PlatMap platmap, ByteChunk chunk, BiomeGrid biomes, DataContext context, int platX, int platZ) {
		// look around
		SurroundingLots farms = new SurroundingLots(platmap, platX, platZ);
		
		// what type of ground do we have
		byte surfaceId = generator.oreProvider.surfaceId;
		byte groundId = generator.oreProvider.subsurfaceId;
		if (cropType == cropSugarCane || cropType == cropCactus)
			groundId = sandId;
		chunk.setLayer(generator.streetLevel - 1, 2, groundId);
		
		// in-between bits bits
		byte dividerId = isolationNormalId;
		if (generator.worldEnvironment == Environment.NETHER) {
			dividerId = isolationNetherId;
		}
		
		// draw the isolation blocks
		if (!farms.toNorth()) {
			chunk.setBlocks(1, 15, generator.streetLevel, 0, 1, dividerId);
			if (farms.toWest())
				chunk.setBlock(0, generator.streetLevel, 0, dividerId);
			else
				chunk.setBlock(0, generator.streetLevel, 0, surfaceId);
			if (farms.toEast())
				chunk.setBlock(15, generator.streetLevel, 0, dividerId);
			else
				chunk.setBlock(15, generator.streetLevel, 0, surfaceId);
		}
		if (!farms.toSouth()) {
			chunk.setBlocks(1, 15, generator.streetLevel, 15, 16, dividerId);
			if (farms.toWest())
				chunk.setBlock(0, generator.streetLevel, 15, dividerId);
			else
				chunk.setBlock(0, generator.streetLevel, 15, surfaceId);
			if (farms.toEast())
				chunk.setBlock(15, generator.streetLevel, 15, dividerId);
			else
				chunk.setBlock(15, generator.streetLevel, 15, surfaceId);
		}
		if (!farms.toWest()) {
			chunk.setBlocks(0, 1, generator.streetLevel, 1, 15, dividerId);
			if (farms.toNorth())
				chunk.setBlock(0, generator.streetLevel, 0, dividerId);
			else
				chunk.setBlock(0, generator.streetLevel, 0, surfaceId);
			if (farms.toSouth())
				chunk.setBlock(0, generator.streetLevel, 15, dividerId);
			else
				chunk.setBlock(0, generator.streetLevel, 15, surfaceId);
		}
		if (!farms.toEast()) {
			chunk.setBlocks(15, 16, generator.streetLevel, 1, 15, dividerId);
			if (farms.toNorth())
				chunk.setBlock(15, generator.streetLevel, 0, dividerId);
			else
				chunk.setBlock(15, generator.streetLevel, 0, surfaceId);
			if (farms.toSouth())
				chunk.setBlock(15, generator.streetLevel, 15, dividerId);
			else
				chunk.setBlock(15, generator.streetLevel, 15, surfaceId);
		}
	}
	
	@Override
	protected void generateActualBlocks(WorldGenerator generator, PlatMap platmap, RealChunk chunk, DataContext context, int platX, int platZ) {
		int croplevel = generator.streetLevel + 1;
		
		boolean fallowField = cropType == cropFallow;
		
		// waterless crops
		if (cropType == cropCactus)
			plowField(generator, chunk, chunkOdds, croplevel, matSand, 0, matSand, cropType, 0, 2, 2, 3);
		else if (cropType == cropVine)
			buildVineyard(generator, chunk, chunkOdds, croplevel, cropType);
		else if (cropType == cropTrellis)
			buildVineyard(generator, chunk, chunkOdds, croplevel, cropType);
		else if (cropType == cropBrownMushroom)
			plowField(generator, chunk, chunkOdds, croplevel, matMycel, 0, matAir, cropType, 0, 1, 2, 1);
		else if (cropType == cropRedMushroom)
			plowField(generator, chunk, chunkOdds, croplevel, matMycel, 0, matAir, cropType, 0, 1, 2, 1);
		else if (cropType == cropNetherwart)
			plowField(generator, chunk, chunkOdds, croplevel, matSoul, 0, matAir, cropType, chunkOdds.getRandomInt(4), 1, 2, 1);
		else if (cropType == cropDeadBush)
			plowField(generator, chunk, chunkOdds, croplevel, matDirt, 0, matAir, cropType, 0, 1, 2, 1);
		else {
			
			// watered crops
			if (generator.settings.includeAbovegroundFluids) {
				if (cropType == cropYellowFlower || cropType == cropRedFlower)
					plowField(generator, chunk, chunkOdds, croplevel, matSoil, 8, matWater, cropType, 0, 1, 2, 1);
				else if (cropType == cropGrass)
					plowField(generator, chunk, chunkOdds, croplevel, matSoil, 8, matWater, cropType, 1, 1, 2, 1);
				else if (cropType == cropWheat || cropType == cropCarrot || cropType == cropPotato)
					plowField(generator, chunk, chunkOdds, croplevel, matSoil, 8, matWater, cropType, 2 + chunkOdds.getRandomInt(6), 1, 2, 1);
				else if (cropType == cropPumpkin || cropType == cropMelon)
					plowField(generator, chunk, chunkOdds, croplevel, matSoil, 8, matWater, cropType, 2 + chunkOdds.getRandomInt(6), 1, 3, 1);
				else if (cropType == cropSugarCane)
					plowField(generator, chunk, chunkOdds, croplevel, matSand, 0, matWater, cropType, 0, 1, 2, 3);
				else if (cropType == cropNone)
					plowField(generator, chunk, chunkOdds, croplevel, matSoil, 8, matWater, matAir, 0, 1, 2, 1);
				else
					fallowField = true;
			} else {
				if (cropType == cropNone)
					plowField(generator, chunk, chunkOdds, croplevel, matDirt, 0, matAir, matAir, 0, 1, 2, 1);
				else
					fallowField = true;
			}
		}
		
		// just in case nothing happened
		if (fallowField)
			plowField(generator, chunk, chunkOdds, croplevel, matDirt, 0, matAir, cropType, 0, 1, 2, 1);
	}

	private void plowField(WorldGenerator generator, RealChunk chunk, Odds odds, int croplevel, 
			Material matRidge, int datRidge, Material matFurrow, 
			Material matCrop, int datCrop,
			int stepRow, int stepCol, int maxHeight) {
		
		// convert the data types
		byte byteRidge = (byte) datRidge;
		byte byteFurrow = (byte) 0;
		byte byteCrop = (byte) datCrop;
		
		// do the deed
		if (directionNorthSouth) {
			for (int x = 1; x < 15; x += stepCol) {
				chunk.setBlocks(x, x + 1, croplevel - 1, croplevel, 1, 15, matRidge, byteRidge, false);
				if (stepCol > 1)
					chunk.setBlocks(x + 1, x + 2, croplevel - 1, croplevel, 1, 15, matFurrow, byteFurrow, false);
				for (int z = 1; z < 15; z += stepRow)
					if (chunkOdds.playOdds(oddsOfCrop))
						chunk.setBlocks(x, croplevel, croplevel + 1 + odds.getRandomInt(maxHeight), z, matCrop, byteCrop, false);
			}
		} else {
			for (int z = 1; z < 15; z += stepCol) {
				chunk.setBlocks(1, 15, croplevel - 1, croplevel, z, z + 1, matRidge, byteRidge, false);
				if (stepCol > 1)
					chunk.setBlocks(1, 15, croplevel - 1, croplevel, z + 1, z + 2, matFurrow, byteFurrow, false);
				for (int x = 1; x < 15; x += stepRow)
					if (chunkOdds.playOdds(oddsOfCrop))
						chunk.setBlocks(x, croplevel, croplevel + 1 + odds.getRandomInt(maxHeight), z, matCrop, byteCrop, false);
			}
		}
	}
	
	private void buildVineyard(WorldGenerator generator, RealChunk chunk, Odds odds, int cropLevel, Material matCrop) {
		int stepCol = 3;
		if (directionNorthSouth) {
			for (int x = 1; x < 15; x += stepCol) {
				chunk.setBlocks(x, cropLevel, cropLevel + 4, 1, matPole);
				chunk.setBlocks(x, cropLevel, cropLevel + 4, 14, matPole);
				chunk.setBlocks(x, x + 1, cropLevel + 3, cropLevel + 4, 2, 14, matTrellis);
				if (matCrop != cropTrellis) {
					if (chunkOdds.playOdds(oddsOfCrop)) {
						for (int z = 2; z < 14; z++) {
							chunk.setBlocks(x - 1, x, cropLevel + 1 + odds.getRandomInt(3), cropLevel + 4, z, z + 1, matCrop, (byte) 8);
							chunk.setBlocks(x + 1, x + 2, cropLevel + 1 + odds.getRandomInt(3), cropLevel + 4, z, z + 1, matCrop, (byte) 2);
						}
					}
				}
			}
		} else {
			for (int z = 1; z < 15; z += stepCol) {
				chunk.setBlocks(1, cropLevel, cropLevel + 4, z, matPole);
				chunk.setBlocks(14, cropLevel, cropLevel + 4, z, matPole);
				chunk.setBlocks(2, 14, cropLevel + 3, cropLevel + 4, z, z + 1, matTrellis);
				if (matCrop != cropTrellis) {
					if (chunkOdds.playOdds(oddsOfCrop)) {
						for (int x = 2; x < 14; x++) {
							chunk.setBlocks(x, x + 1, cropLevel + 1 + odds.getRandomInt(3), cropLevel + 4, z - 1, z, matCrop, (byte) 1);
							chunk.setBlocks(x, x + 1, cropLevel + 1 + odds.getRandomInt(3), cropLevel + 4, z + 1, z + 2, matCrop, (byte) 4);
						}
					}
				}
			}
		}
	}
	
	private Material getNormalCrop() {
		switch (chunkOdds.getRandomInt(14)) {
		case 1:
			return cropYellowFlower;
		case 2:
			return cropRedFlower;
		case 3:
			return cropPumpkin;
		case 4:
			return cropMelon;
		case 5:
			return cropVine;
		case 6:
			return cropGrass;
		case 7:
			return cropSugarCane;
		case 8:
			return cropCactus;
		case 9:
			return cropFallow;
		case 10:
			return cropCarrot;
		case 11:
			return cropPotato;
		case 12:
			return cropNone;
		default:
			return cropWheat;
		}
	}

	private Material getDecayedNormalCrop() {
		switch (chunkOdds.getRandomInt(5)) {
		case 1:
			return cropTrellis;
		case 2:
			return cropFallow;
		case 3:
			return cropNone;
		default:
			return cropDeadBush;
		}
	}

	private Material getNetherCrop() {
		switch (chunkOdds.getRandomInt(6)) {
		case 1:
			return cropBrownMushroom;
		case 2:
			return cropRedMushroom;
		case 3:
			return cropNetherwart;
		case 4:
			return cropDeadBush;
		default:
			return cropFallow;
		}
	}

	private Material getDecayedNetherCrop() {
		switch (chunkOdds.getRandomInt(10)) {
		case 1:
			return cropBrownMushroom;
		case 2:
			return cropRedMushroom;
		case 3:
			return cropNetherwart;
		case 4:
			return cropDeadBush;
		default:
			return cropFallow;
		}
	}
}
