package me.daddychurchill.CityWorld.Support;

public class OutsideWEWallFactory extends MaterialFactory {

	public OutsideWEWallFactory(Odds odds, byte decayed) {
		super(odds, decayed);
	}

	public OutsideWEWallFactory(MaterialFactory other) {
		super(other);
	}

	@Override
	public void placeMaterial(SupportChunk chunk, byte primaryId, byte secondaryId, int x, int y1, int y2, int z) {
		super.placeMaterial(chunk, primaryId, secondaryId, pickMaterial(primaryId, secondaryId, x), x, y1, y2, z);
	}
}
