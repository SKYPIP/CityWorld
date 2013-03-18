package me.daddychurchill.CityWorld.Rooms;

import org.bukkit.Material;

import me.daddychurchill.CityWorld.Support.Direction.Facing;
import me.daddychurchill.CityWorld.Support.Odds;
import me.daddychurchill.CityWorld.Support.RealChunk;

public class DebugRoom extends PlatRoom {

	public DebugRoom() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(RealChunk chunk, Odds odds, int floor, int x, int y,
			int z, int width, int height, int depth, Facing sideWithWall,
			Material materialWall, Material materialGlass) {
		switch (sideWithWall) {
		case NORTH:
			chunk.setBlocks(x, x + width, y, y + height, z, z + 1, materialWall);
			break;
		case SOUTH:
			chunk.setBlocks(x, x + width, y, y + height, z + depth - 1, z + depth, materialWall);
			break;
		case WEST:
			chunk.setBlocks(x, x + 1, y, y + height, z, z + depth, materialWall);
			break;
		case EAST:
			chunk.setBlocks(x + width - 1, x + width, y, y + height, z, z + depth, materialWall);
			break;
		}
	}

}
