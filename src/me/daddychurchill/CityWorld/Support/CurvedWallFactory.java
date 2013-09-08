package me.daddychurchill.CityWorld.Support;

public class CurvedWallFactory extends MaterialFactory {

	public CurvedWallFactory(Odds odds, byte decayed) {
		super(odds, decayed);
	}

	public CurvedWallFactory(MaterialFactory other) {
		super(other);
	}

	@Override
	public void placeMaterial(SupportChunk chunk, byte primaryId, byte secondaryId, int x, int y1, int y2, int z) {
		super.placeMaterial(chunk, primaryId, secondaryId, pickMaterial(primaryId, secondaryId, x * z), x, y1, y2, z);
	}
}
