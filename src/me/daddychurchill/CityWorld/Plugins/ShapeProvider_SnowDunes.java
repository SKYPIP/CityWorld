package me.daddychurchill.CityWorld.Plugins;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import me.daddychurchill.CityWorld.WorldGenerator;
import me.daddychurchill.CityWorld.Context.SnowDunes.SnowDunesConstructionContext;
import me.daddychurchill.CityWorld.Context.SnowDunes.SnowDunesFarmContext;
import me.daddychurchill.CityWorld.Context.SnowDunes.SnowDunesHighriseContext;
import me.daddychurchill.CityWorld.Context.SnowDunes.SnowDunesLowriseContext;
import me.daddychurchill.CityWorld.Context.SnowDunes.SnowDunesMidriseContext;
import me.daddychurchill.CityWorld.Context.SnowDunes.SnowDunesNatureContext;
import me.daddychurchill.CityWorld.Context.SnowDunes.SnowDunesNeighborhoodContext;
import me.daddychurchill.CityWorld.Context.SnowDunes.SnowDunesParkContext;
import me.daddychurchill.CityWorld.Context.SnowDunes.SnowDunesRoadContext;
import me.daddychurchill.CityWorld.Plats.PlatLot;
import me.daddychurchill.CityWorld.Support.ByteChunk;
import me.daddychurchill.CityWorld.Support.CachedYs;
import me.daddychurchill.CityWorld.Support.Odds;
import me.daddychurchill.CityWorld.Support.RealChunk;

public class ShapeProvider_SnowDunes extends ShapeProvider_Normal {

	public final static Material floodMat = Material.SNOW_BLOCK;
	public final static byte floodId = (byte) Material.SNOW_BLOCK.getId();
	
	protected int floodY;
	
	private SimplexOctaveGenerator duneFeature1;
	private SimplexOctaveGenerator duneFeature2;
//	private SimplexOctaveGenerator duneNoise;
	
	private final static int featureOctaves = 2;
	private final static int featureVerticalScale = 4;
	private final static double featureFrequency = 1.50;
	private final static double featureAmplitude = 1;
	private final static double featureHorizontalScale = 1.0 / 32.0;
	
//	private final static int noiseOctaves = 16;
//	private final static int noiseVerticalScale = 3;
//	private final static double noiseFrequency = 1.50;
//	private final static double noiseAmplitude = 0.70;
//	private final static double noiseHorizontalScale = 1.0 / 64.0;
	
	public ShapeProvider_SnowDunes(WorldGenerator generator, Odds odds) {
		super(generator, odds);
		
		floodY = seaLevel + (featureVerticalScale / 2 * 3);

		long seed = generator.getWorldSeed();
		duneFeature1 = new SimplexOctaveGenerator(seed + 20, featureOctaves);
		duneFeature1.setScale(featureHorizontalScale);
		duneFeature2 = new SimplexOctaveGenerator(seed + 30, featureOctaves * 2);
		duneFeature2.setScale(featureHorizontalScale);
//		duneNoise = new SimplexOctaveGenerator(seed + 40, noiseOctaves);
//		duneNoise.setScale(noiseHorizontalScale);
	}

	@Override
	public void allocateContexts(WorldGenerator generator) {
		if (!contextInitialized) {
			natureContext = new SnowDunesNatureContext(generator);
			roadContext = new SnowDunesRoadContext(generator);
			
			parkContext = new SnowDunesParkContext(generator);
			highriseContext = new SnowDunesHighriseContext(generator);
			constructionContext = new SnowDunesConstructionContext(generator);
			midriseContext = new SnowDunesMidriseContext(generator);
			municipalContext = midriseContext;
			lowriseContext = new SnowDunesLowriseContext(generator);
			industrialContext = lowriseContext;
			neighborhoodContext = new SnowDunesNeighborhoodContext(generator);
			farmContext = new SnowDunesFarmContext(generator);
			
			contextInitialized = true;
		}
	}
	
	@Override
	public String getCollectionName() {
		return "SnowDunes";
	}
	
	@Override
	public double findPerciseFloodY(WorldGenerator generator, int blockX, int blockZ) {
		
		// shape the noise
//		double noiseY = 0;//duneNoise.noise(blockX, blockZ, noiseFrequency, noiseAmplitude, true);
		double featureY = duneFeature1.noise(blockX, blockZ, featureFrequency, featureAmplitude, true) -
						  Math.abs(duneFeature2.noise(blockX + 20, blockZ + 20, featureFrequency, featureAmplitude, true));
		
		return floodY + (featureY * featureVerticalScale);// + (noiseY * noiseVerticalScale);	
	}
	
	@Override
	public int findFloodY(WorldGenerator generator, int blockX, int blockZ) {
		return NoiseGenerator.floor(findPerciseFloodY(generator, blockX, blockZ));
	}

	@Override
	public int findHighestFloodY(WorldGenerator generator) {
		return floodY + featureVerticalScale + noiseVerticalScale;
	}

	@Override
	public int findLowestFloodY(WorldGenerator generator) {
		return floodY;
	}

	@Override
	protected Biome remapBiome(WorldGenerator generator, PlatLot lot, Biome biome) {
		return Biome.ICE_MOUNTAINS;
	}

	@Override
	protected void generateStratas(WorldGenerator generator, PlatLot lot,
			ByteChunk chunk, int x, int z, byte substratumId, byte stratumId,
			int stratumY, byte subsurfaceId, int subsurfaceY, byte surfaceId,
			int coverY, byte coverId, boolean surfaceCaves) {

		// do the default bit
		actualGenerateStratas(generator, lot, chunk, x, z, substratumId, stratumId, stratumY, 
				subsurfaceId, subsurfaceY, surfaceId, surfaceCaves);
		
		// cover it up a bit
		actualGenerateSnow(generator, lot, chunk, x, z, subsurfaceY);
	}
	
	@Override
	protected void generateStratas(WorldGenerator generator, PlatLot lot,
			ByteChunk chunk, int x, int z, byte substratumId, byte stratumId,
			int stratumY, byte subsurfaceId, int subsurfaceY, byte surfaceId,
			boolean surfaceCaves) {

		// do the default bit
		actualGenerateStratas(generator, lot, chunk, x, z, substratumId, stratumId, stratumY, 
				subsurfaceId, subsurfaceY, surfaceId, surfaceCaves);
		
		// cover it up a bit
		actualGenerateSnow(generator, lot, chunk, x, z, subsurfaceY);
	}
	
	protected void actualGenerateSnow(WorldGenerator generator, PlatLot lot, ByteChunk chunk, int x, int z, int subsurfaceY) {
		int y = findFloodY(generator, chunk.getBlockX(x), chunk.getBlockZ(z));
		if (y > subsurfaceY) {
			y = chunk.findLastEmptyBelow(x, y + 1, z);
			if (!chunk.isPartialHeight(x, y - 1, z))
				chunk.setBlocks(x, subsurfaceY, y, z, floodId);
		}
	}
	
	private static int snowId = Material.SNOW.getId();
	
	@Override
	public void postGenerateBlocks(WorldGenerator generator, PlatLot lot, RealChunk chunk, CachedYs blockYs) {
		
		// add the snow
		ShapeProvider shape = generator.shapeProvider;
//		OreProvider ore = generator.oreProvider;
		
		// let the other guy do it's thing
		super.postGenerateBlocks(generator, lot, chunk, blockYs);

		// shape the world
		for (int x = 0; x < chunk.width; x++) {
			for (int z = 0; z < chunk.width; z++) {
				int surfaceY = blockYs.getBlockY(x, z);
				
				// where to drop the snow
				double floodY = shape.findPerciseFloodY(generator, chunk.getBlockX(x), chunk.getBlockZ(z));
				byte amount = (byte) NoiseGenerator.floor((floodY - Math.floor(floodY)) * 8.0);
				int y = NoiseGenerator.floor(floodY);
				if (surfaceY > floodY)
					y = surfaceY;
				
				// find the bottom
				y = chunk.findLastEmptyBelow(x, y + 1, z);
				if (chunk.isEmpty(x, y, z)) {
					if (!chunk.isPartialHeight(x, y - 1, z))
						chunk.setBlock(x, y, z, snowId, amount, false);
					y++;
				}
				
				// see if there is more we can do
				boolean thisOneEmpty = false;
				boolean lastOneSolid = false;
				while (amount > 0 && y < chunk.height) {
					y++;
					thisOneEmpty = chunk.isEmpty(x, y, z);
					if (lastOneSolid && thisOneEmpty) {
						if (!chunk.isPartialHeight(x, y - 1, z))
							chunk.setBlock(x, y, z, snowId, amount, false);
						amount--;
					}
					lastOneSolid = !thisOneEmpty;
				}
			}
		}
	}
	
//	public void dropSnow(WorldGenerator generator, RealChunk chunk, int x, int y, int z, byte level) {
//		y = chunk.findLastEmptyBelow(x, y + 1, z);
//		if (chunk.isEmpty(x, y, z))
//			chunk.setBlock(x, y, z, snowId, level, false);
//	}
}
