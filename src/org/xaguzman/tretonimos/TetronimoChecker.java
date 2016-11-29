package org.xaguzman.tretonimos;

import java.util.List;

import org.xaguzman.Tile;

public interface TetronimoChecker {
	/**
	 * Gets the tetronimo to which a tile belongs.  
	 * @param tile The tile to check
	 * @return A list containing allthe pieces forming the tetronimo which contains the passed in tile. Empty
	 * list means no tetronimo could be found around the given tile.
	 */
	List<Tile> getTetronimoPieces(Tile tile);
}
