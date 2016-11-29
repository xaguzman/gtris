package org.xaguzman.tretonimos;

import java.util.ArrayList;
import java.util.List;

import org.xaguzman.Tile;

public class ShapeSquare implements TetronimoChecker{

	List<Tile> tiles = new ArrayList<>();
	@Override
	public List<Tile> getTetronimoPieces(Tile tile) {
		tiles.clear();
		
		//bottom left
		if ( Tile.allEqual(tile, tile.above, tile.right, tile.above.right) ){
			tiles.add(tile);
			tiles.add(tile.above);
			tiles.add(tile.right);
			tiles.add(tile.above.right);
			return tiles;
		}
		
		//bottom right
		if ( Tile.allEqual(tile, tile.above, tile.left, tile.above.left) ){
				tiles.add(tile);
				tiles.add(tile.above);
				tiles.add(tile.left);
				tiles.add(tile.above.left);
				return tiles;
		}
		
		//top left
		if ( Tile.allEqual(tile, tile.below, tile.right, tile.below.right) ){
				tiles.add(tile);
				tiles.add(tile.below);
				tiles.add(tile.right);
				tiles.add(tile.below.right);
				return tiles;

		}
		
		//top right
		if ( Tile.allEqual(tile, tile.below, tile.left, tile.below.left) ){
				tiles.add(tile);
				tiles.add(tile.below);
				tiles.add(tile.left);
				tiles.add(tile.below.left);
				return tiles;
		}
		
		return tiles;
	}

}
