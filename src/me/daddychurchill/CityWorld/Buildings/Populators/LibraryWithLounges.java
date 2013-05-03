package me.daddychurchill.CityWorld.Buildings.Populators;

import me.daddychurchill.CityWorld.Buildings.Rooms.LibraryDoubleRoom;
import me.daddychurchill.CityWorld.Buildings.Rooms.LibrarySingleRoom;
import me.daddychurchill.CityWorld.Buildings.Rooms.LibraryStudyRoom;
import me.daddychurchill.CityWorld.Buildings.Rooms.LoungeChairsRoom;
import me.daddychurchill.CityWorld.Buildings.Rooms.LoungeCouchRoom;
import me.daddychurchill.CityWorld.Buildings.Rooms.LoungeEllCouchRoom;
import me.daddychurchill.CityWorld.Buildings.Rooms.LoungeGameRoom;
import me.daddychurchill.CityWorld.Buildings.Rooms.LoungeQuadRoom;
import me.daddychurchill.CityWorld.Buildings.Rooms.LoungeTrioRoom;
import me.daddychurchill.CityWorld.Plugins.RoomProvider;

public class LibraryWithLounges extends RoomProvider {

	public LibraryWithLounges() {
		super();

		roomTypes.add(new LibrarySingleRoom());
		roomTypes.add(new LibraryDoubleRoom());
		roomTypes.add(new LibraryStudyRoom());
		
		roomTypes.add(new LoungeEllCouchRoom());
		roomTypes.add(new LoungeTrioRoom());
		roomTypes.add(new LoungeQuadRoom());
		roomTypes.add(new LoungeChairsRoom());
		roomTypes.add(new LoungeGameRoom());
		roomTypes.add(new LoungeCouchRoom());
	}

}
