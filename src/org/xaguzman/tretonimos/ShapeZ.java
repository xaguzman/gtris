package org.xaguzman.tretonimos;

import java.util.ArrayList;
import java.util.List;

import org.xaguzman.Tile;

public class ShapeZ implements TetronimoChecker {

	List<Tile> tiles = new ArrayList<>();
	List<Tile> tmp = new ArrayList<>();
	
	@Override
	public List<Tile> getTetronimoPieces(Tile tile) {
		tiles.clear();
		
		tiles = getHorizontalZ(tile);
		if (tiles.size() == 4)
			return tiles;
		
		tiles = getVerticalZ(tile);
		
		return tiles;
	}
	
	/*
	 * 	a)		b)
	 *  1 1 0	0 1 1
	 *  0 1 1	1 1 0
	 */
	private List<Tile> getHorizontalZ(Tile t){
		tmp.clear();
		
		//case a)
		if ( Tile.allEqual(t, t.left, t.below, t.below.right)){
			tmp.add(t);
			tmp.add(t.left);
			tmp.add(t.below);
			tmp.add(t.below.right);
			return tmp;
		}
		
		if ( Tile.allEqual(t, t.right, t.right.below, t.right.below.right)){
			tmp.add(t);
			tmp.add(t.right);
			tmp.add(t.right.below);
			tmp.add(t.right.below.right);
			return tmp;
		}
		
		if ( Tile.allEqual(t, t.above, t.above.left, t.right)){
			tmp.add(t);
			tmp.add(t.above);
			tmp.add(t.above.left);
			tmp.add(t.right);
			return tmp;
		}
		
		if (Tile.allEqual(t, t.left, t.left.above, t.left.above.left)){
			tmp.add(t);
			tmp.add(t.left);
			tmp.add(t.left.above);
			tmp.add(t.left.above.left);
			return tmp;
		}

		//case b)
		if ( Tile.allEqual(t, t.right, t.below, t.below.left)){
			tmp.add(t);
			tmp.add(t.right);
			tmp.add(t.below);
			tmp.add(t.below.left);
			return tmp;
		}
		
		if ( Tile.allEqual(t, t.left, t.left.below, t.left.below.left)){
			tmp.add(t);
			tmp.add(t.left);
			tmp.add(t.left.below);
			tmp.add(t.left.below.left);
			return tmp;
		}
		
		if ( Tile.allEqual(t, t.left, t.above, t.above.right)){
			tmp.add(t);
			tmp.add(t.left);
			tmp.add(t.above);
			tmp.add(t.above.right);
			return tmp;
		}
		
		if ( Tile.allEqual(t, t.right, t.right.above, t.right.above.right)){
			tmp.add(t);
			tmp.add(t.right);
			tmp.add(t.right.above);
			tmp.add(t.right.above.right);
			return tmp;
		}
		return tmp;
	}
	

	/*
	 * 	a)		b)
	 * 1 0 		0 1 
	 * 1 1 		1 1 
	 * 0 1    	1 0 
	 */
	private List<Tile> getVerticalZ(Tile t){
		tmp.clear();
		
		//case a)
		if ( Tile.allEqual(t, t.below, t.below.right, t.below.right.below)){
			tmp.add(t);
			tmp.add(t.below);
			tmp.add(t.below.right);
			tmp.add(t.below.right.below);
			return tmp;
		}
		
		if ( Tile.allEqual(t, t.above, t.right, t.right.below)){
			tmp.add(t);
			tmp.add(t.above);
			tmp.add(t.right);
			tmp.add(t.right.below);
			return tmp;
		}
		
		if ( Tile.allEqual(t, t.left, t.left.above, t.below)){
			tmp.add(t);
			tmp.add(t.left);
			tmp.add(t.left.above);
			tmp.add(t.below);
			return tmp;
		}
		
		if ( Tile.allEqual(t, t.above, t.above.left, t.above.left.above)){
			tmp.add(t);
			tmp.add(t.above);
			tmp.add(t.above.left);
			tmp.add(t.above.left.above);
			return tmp;
		}
		
		
		//case b)
		if ( Tile.allEqual(t, t.below, t.below.left, t.below.left.below)){
			tmp.add(t);
			tmp.add(t.below);
			tmp.add(t.below.left);
			tmp.add(t.below.left.below);
			return tmp;
		}
		
		if ( Tile.allEqual(t, t.above, t.left, t.left.below)){
			tmp.add(t);
			tmp.add(t.above);
			tmp.add(t.left);
			tmp.add(t.left.below);
			return tmp;
		}
		
		if ( Tile.allEqual(t, t.right, t.right.above, t.below)){
			tmp.add(t);
			tmp.add(t.right);
			tmp.add(t.right.above);
			tmp.add(t.below);
			return tmp;
		}
		
		if ( Tile.allEqual(t, t.above, t.above.right, t.above.right.above)){
			tmp.add(t);
			tmp.add(t.above);
			tmp.add(t.above.right);
			tmp.add(t.above.right.above);
			return tmp;
		}
		return tmp;
	}
}
