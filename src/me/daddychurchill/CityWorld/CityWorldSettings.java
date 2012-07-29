package me.daddychurchill.CityWorld;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class CityWorldSettings {
	
	/* TODOs
	 * Add to bunkers
	 *   Farms
	 *   Floors and stairs where practical
	 *   Rounded tanks
	 *   Writing on the outside of buildings
	 */
	
	public Environment environment;
	public boolean darkEnvironment;
	
	public boolean includeRoads = true;
	public boolean includeRoundabouts = true;
	public boolean includeSewers = true;
	public boolean includeCisterns = true;
	public boolean includeBasements = true;
	public boolean includeMines = true;
	public boolean includeBunkers = true;
	public boolean includeBuildings = true;
	public boolean includeHouses = true;
	public boolean includeFarms = true;

	public boolean includeCaves = true;
	public boolean includeLavaFields = true;
	public boolean includeSeas = true;
	public boolean includeMountains = true;
	public boolean includeOres = true;
	
	public boolean treasuresInSewers = true;
	public boolean spawnersInSewers = true;
	public boolean treasuresInMines = true;
	public boolean spawnersInMines = true;
	public boolean treasuresInBunkers = true;
	public boolean spawnersInBunkers = true;
	
	public boolean includeUndergroundFluids = true;
	public boolean includeAbovegroundFluids = true;
	public boolean includeWorkingLights = true;
	public boolean includeWoolRoads = false;
	public boolean includeNamedRoads = true;
	public boolean includeDecayedRoads = false;
	public boolean includeDecayedBuildings = false;
	public boolean includeDecayedNature = false;
	public boolean includeTekkitMaterials = false;
	
	public int roadRange = Integer.MAX_VALUE;
	public boolean checkRoadRange = false;
	public int cityRange = Integer.MAX_VALUE;
	public boolean checkCityRange = false;
	
	private final static String tagIncludeRoads = "IncludeRoads";
	private final static String tagIncludeRoundabouts = "IncludeRoundabouts";
	private final static String tagIncludeSewers = "IncludeSewers";
	private final static String tagIncludeCisterns = "IncludeCisterns";
	private final static String tagIncludeBasements = "IncludeBasements";
	private final static String tagIncludeMines = "IncludeMines";
	private final static String tagIncludeBunkers = "IncludeBunkers";
	private final static String tagIncludeBuildings = "IncludeBuildings";
	private final static String tagIncludeHouses = "IncludeHouses";
	private final static String tagIncludeFarms = "IncludeFarms";

	private final static String tagIncludeCaves = "IncludeCaves";
	private final static String tagIncludeLavaFields = "IncludeLavaFields";
	private final static String tagIncludeSeas = "IncludeSeas";
	private final static String tagIncludeMountains = "IncludeMountains";
	private final static String tagIncludeOres = "IncludeOres";
	
	private final static String tagTreasuresInSewers = "TreasuresInSewers";
	private final static String tagSpawnersInSewers = "SpawnersInSewers";
	private final static String tagTreasuresInMines = "TreasuresInMines";
	private final static String tagSpawnersInMines = "SpawnersInMines";
	private final static String tagTreasuresInBunkers = "TreasuresInBunkers";
	private final static String tagSpawnersInBunkers = "SpawnersInBunkers";
	
	private final static String tagIncludeUndergroundFluids = "IncludeUndergroundFluids";
	private final static String tagIncludeAbovegroundFluids = "IncludeAbovegroundFluids";
	private final static String tagIncludeWorkingLights = "IncludeWorkingLights";
	private final static String tagIncludeWoolRoads = "IncludeWoolRoads";
	private final static String tagIncludeNamedRoads = "IncludeNamedRoads";
	private final static String tagIncludeDecayedRoads = "IncludeDecayedRoads";
	private final static String tagIncludeDecayedBuildings = "IncludeDecayedBuildings";
	private final static String tagIncludeDecayedNature = "IncludeDecayedNature";
	private final static String tagIncludeTekkitMaterials = "IncludeTekkitMaterials";
	
	private final static String tagRoadRange = "RoadRange";
	private final static String tagCityRange = "CityRange";
	
	Location center;
	
	public CityWorldSettings(CityWorld plugin, World world) {
		super();
		String worldname = world.getName();
		environment = world.getEnvironment();
		center = new Location(world, 0, 0, 0);
		
		// get the right defaults
		switch (environment) {
		case NORMAL:
			darkEnvironment = false;
			break;
		case NETHER:
			darkEnvironment = true;
			includeWorkingLights = false;
			includeDecayedRoads = true;
			includeDecayedBuildings = true;
			includeDecayedNature = true;
			break;
		case THE_END:
			darkEnvironment = true;
			includeMines = false;
			includeBunkers = false;
			includeHouses = false;
			includeFarms = false;
			includeLavaFields = false;
			break;
		}
		
		// add/get the configuration
		FileConfiguration config = plugin.getConfig();
		config.options().header("CityWorld Plugin Options");
		config.options().copyDefaults(true);
		
		// get the right section
		ConfigurationSection section = null;
		
		// see if we can find the specific world
		if (config.isConfigurationSection(worldname))
			section = config.getConfigurationSection(worldname);
		
		// if not then create it
		else {
			section = config.createSection(worldname);
			section.addDefault(tagIncludeRoads, includeRoads);
			section.addDefault(tagIncludeRoundabouts, includeRoundabouts);
			section.addDefault(tagIncludeSewers, includeSewers);
			section.addDefault(tagIncludeCisterns, includeCisterns);
			section.addDefault(tagIncludeBasements, includeBasements);
			section.addDefault(tagIncludeMines, includeMines);
			section.addDefault(tagIncludeBunkers, includeBunkers);
			section.addDefault(tagIncludeBuildings, includeBuildings);
			section.addDefault(tagIncludeHouses, includeHouses);
			section.addDefault(tagIncludeFarms, includeFarms);
			
			section.addDefault(tagIncludeCaves, includeCaves);
			section.addDefault(tagIncludeLavaFields, includeLavaFields);
			section.addDefault(tagIncludeSeas, includeSeas);
			section.addDefault(tagIncludeMountains, includeMountains);
			section.addDefault(tagIncludeOres, includeOres);
			
			section.addDefault(tagTreasuresInSewers, treasuresInSewers);
			section.addDefault(tagSpawnersInSewers, spawnersInSewers);
			section.addDefault(tagTreasuresInMines, treasuresInMines);
			section.addDefault(tagSpawnersInMines, spawnersInMines);
			section.addDefault(tagTreasuresInBunkers, treasuresInBunkers);
			section.addDefault(tagSpawnersInBunkers, spawnersInBunkers);
			
			section.addDefault(tagIncludeUndergroundFluids, includeUndergroundFluids);
			section.addDefault(tagIncludeAbovegroundFluids, includeAbovegroundFluids);
			section.addDefault(tagIncludeWorkingLights, includeWorkingLights);
			section.addDefault(tagIncludeWoolRoads, includeWoolRoads);
			section.addDefault(tagIncludeNamedRoads, includeNamedRoads);
			section.addDefault(tagIncludeDecayedRoads, includeDecayedRoads);
			section.addDefault(tagIncludeDecayedBuildings, includeDecayedBuildings);
			section.addDefault(tagIncludeDecayedNature, includeDecayedNature);
			section.addDefault(tagIncludeTekkitMaterials, includeTekkitMaterials);
			
			section.addDefault(tagRoadRange, roadRange);
			section.addDefault(tagCityRange, cityRange);
		}
		
		// did we get a section?
		if (section != null) {
			includeRoads = section.getBoolean(tagIncludeRoads, includeRoads);
			includeRoundabouts = section.getBoolean(tagIncludeRoundabouts, includeRoundabouts);
			includeSewers = section.getBoolean(tagIncludeSewers, includeSewers);
			includeCisterns = section.getBoolean(tagIncludeCisterns, includeCisterns);
			includeBasements = section.getBoolean(tagIncludeBasements, includeBasements);
			includeMines = section.getBoolean(tagIncludeMines, includeMines);
			includeBunkers = section.getBoolean(tagIncludeBunkers, includeBunkers);
			includeBuildings = section.getBoolean(tagIncludeBuildings, includeBuildings);
			includeHouses = section.getBoolean(tagIncludeHouses, includeHouses);
			includeFarms = section.getBoolean(tagIncludeFarms, includeFarms);
			
			includeCaves = section.getBoolean(tagIncludeCaves, includeCaves);
			includeLavaFields = section.getBoolean(tagIncludeLavaFields, includeLavaFields);
			includeSeas = section.getBoolean(tagIncludeSeas, includeSeas);
			includeMountains = section.getBoolean(tagIncludeMountains, includeMountains);
			includeOres = section.getBoolean(tagIncludeOres, includeOres);

			treasuresInSewers = section.getBoolean(tagTreasuresInSewers, treasuresInSewers);
			spawnersInSewers = section.getBoolean(tagSpawnersInSewers, spawnersInSewers);
			treasuresInMines = section.getBoolean(tagTreasuresInMines, treasuresInMines);
			spawnersInMines = section.getBoolean(tagSpawnersInMines, spawnersInMines);
			treasuresInBunkers = section.getBoolean(tagTreasuresInBunkers, treasuresInBunkers);
			spawnersInBunkers = section.getBoolean(tagSpawnersInBunkers, spawnersInBunkers);
			
			includeUndergroundFluids = section.getBoolean(tagIncludeUndergroundFluids, includeUndergroundFluids);
			includeAbovegroundFluids = section.getBoolean(tagIncludeAbovegroundFluids, includeAbovegroundFluids);
			includeWorkingLights = section.getBoolean(tagIncludeWorkingLights, includeWorkingLights);
			includeWoolRoads = section.getBoolean(tagIncludeWoolRoads, includeWoolRoads);
			includeNamedRoads = section.getBoolean(tagIncludeNamedRoads, includeNamedRoads);
			includeDecayedRoads = section.getBoolean(tagIncludeDecayedRoads, includeDecayedRoads);
			includeDecayedBuildings = section.getBoolean(tagIncludeDecayedBuildings, includeDecayedBuildings);
			includeDecayedNature = section.getBoolean(tagIncludeDecayedNature, includeDecayedNature);
			includeTekkitMaterials = section.getBoolean(tagIncludeTekkitMaterials, includeTekkitMaterials);
			
			roadRange = Math.min(Short.MAX_VALUE, Math.max(0, section.getInt(tagRoadRange, roadRange)));
			checkRoadRange = roadRange > 0 && roadRange < Short.MAX_VALUE;
			if (roadRange == 0) {
				includeRoads = false;
				includeSewers = false;
			}

			cityRange = Math.min(roadRange, Math.max(0, section.getInt(tagCityRange, cityRange)));
			checkCityRange = cityRange > 0 && cityRange < Short.MAX_VALUE;
			if (cityRange == 0) {
				includeCisterns = false;
				includeBasements = false;
				includeMines = false;
				includeBunkers = false;
				includeBuildings = false;
				includeHouses = false;
				includeFarms = false;
			}
		}
		
		// write it back out if needed
		plugin.saveConfig();
	}
	
	public boolean inRoadRange(int x, int z) {
		return !checkRoadRange || center.distance(new Location(center.getWorld(), x, 0, z)) <= roadRange;
	}
	
	public boolean inCityRange(int x, int z) {
		return !checkCityRange || center.distance(new Location(center.getWorld(), x, 0, z)) <= cityRange;
	}
}
