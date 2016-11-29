package org.xaguzman.tretonimos;

import java.util.ArrayList;
import java.util.List;

import org.xaguzman.Tile;

public class ShapeLine implements TetronimoChecker {

	List<Tile> tiles = new ArrayList<>();
	
	@Override
	public List<Tile> getTetronimoPieces(Tile tile) {
		tiles.clear();
		
		//vertical. Go to the tile with the same value 
		int count = 1;
		Tile iter = tile;
		while ( Tile.allEqual(iter, iter.below) && count < 4){
			iter = iter.below;
			count++;
		}
		
		if ( count == 4){
			tiles.add(iter);
			tiles.add(iter.above);
			tiles.add(iter.above.above);
			tiles.add(iter.above.above.above);
			return tiles;
		}
		
		count = 1;
		while ( count < 4 && Tile.allEqual(iter, iter.above) ){
			count++;
			iter = iter.above;
		}
		
		if ( count == 4){
			tiles.add(iter);
			tiles.add(iter.below);
			tiles.add(iter.below.below);
			tiles.add(iter.below.below.below);
			return tiles;
		}
		
		//horizontal
		count = 1;
		iter = tile;
		while (Tile.allEqual(iter, iter.left) && count < 4){
			iter = iter.left;
			count++;
		}
		
		if ( count == 4){
			tiles.add(iter);
			tiles.add(iter.right);
			tiles.add(iter.right.right);
			tiles.add(iter.right.right.right);
			return tiles;
		}
		
		count = 1;
		while ( count < 4 && Tile.allEqual(iter, iter.right) ){
			count++;
			iter = iter.right;
		}
		
		if ( count == 4){
			tiles.add(iter);
			tiles.add(iter.left);
			tiles.add(iter.left.left);
			tiles.add(iter.left.left.left);
			return tiles;
		}

		
		return tiles;
	}

}
