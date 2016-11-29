package org.xaguzman.tretonimos;

import java.util.ArrayList;
import java.util.List;

import org.xaguzman.Tile;

/**
 * Checks for L shaped tetronimos.
 * No matrix operations are used.
 * @author gdlxguzm
 *
 */
public class ShapeL implements TetronimoChecker{

	ArrayList<Tile> tiles = new ArrayList<>();
	List<Tile> tmp = new ArrayList<>();
	
	@Override
	public List<Tile> getTetronimoPieces(Tile tile) {
		tiles.clear();
		
		List<Tile> res = getVerticalL(tile);
		if ( res != null && res.size() == 4){
			tiles.addAll(res);
			return tiles;
		}
		
		res = getHorizontalL(tile);
		if (res != null && res.size() == 4){
			tiles.addAll(res);
			return tiles;
		}
		
		return tiles;
	}
	
	/*
	 * check if its a vertical L, either mirrored or not
	 *   a)		  b)      c)	  d)
	 * 0 1 0	0 1 0	1 1 0	0 1 1
	 * 0 1 0	0 1 0	0 1 0	0 1 0
	 * 0 1 1	1 1 0	0 1 0	0 1 0
	 */
	private List<Tile> getVerticalL(Tile t){
		tmp.clear();
		
		tmp.add(t);
		
		Tile topMostTile = t;
		Tile bottomMostTile = t;
	
		if ( Tile.allEqual(t, t.below) ){
			tmp.add(t.below);
			bottomMostTile = t.below;
			if ( Tile.allEqual(t, t.below.below) ){
				//tile passed is topmost block
				tmp.add(t.below.below);
				bottomMostTile = t.below.below;
			}else if (Tile.allEqual(t, t.above)){
				//tile passed is middle block
				tmp.add(t.above);
				topMostTile = t.above;
			}
		}else if(Tile.allEqual(t , t.above)){
			tmp.add(t.above);
			topMostTile = t.above;
			if ( Tile.allEqual(t, t.above.above) ){
				//tile passed is bottommost block
				tmp.add(t.above.above);
				topMostTile = t.above.above;
			}
		}else if (Tile.allEqual(t, t.left) ){
			tmp.add(t.left);
			if ( Tile.allEqual(t, t.left.above, t.left.above.above)){
				//passed tile is bottom - right
				//case a)
				tmp.add(t.left.above);
				tmp.add(t.left.above.above);
				return tmp;
			}
			
			if ( Tile.allEqual(t, t.left.below, t.left.below.below)){
				//passed tile is top - right
				//case d)
				tmp.add(t.left.below);
				tmp.add(t.left.below.below);
				return tmp;
			}
		}else if(Tile.allEqual(t,  t.right)){
			tmp.add(t.right);
			if ( Tile.allEqual(t, t.right.above, t.right.above.above)){
				//passed tile is bottom - left
				//case a)
				tmp.add(t.right.above);
				tmp.add(t.right.above.above);
				return tmp;
			}
			
			if ( Tile.allEqual(t, t.right.below, t.right.below.below)){
				//passed tile is top - left
				//case d)
				tmp.add(t.right.below);
				tmp.add(t.right.below.below);
				return tmp;
			}
		}
		
		if ( tmp.size() != 3)
			//not enough pieces to be a vertical L
			return tmp;
		
		if ( Tile.allEqual(t, bottomMostTile.right) ){
			//case a)
			tmp.add(bottomMostTile.right);
			return tmp;
		}
		
		if ( Tile.allEqual(t, bottomMostTile.left)){
			//case b)
			tmp.add(bottomMostTile.left);
			return tmp;
		}
		
		if ( Tile.allEqual(t, topMostTile.left)){
			//case c)
			tmp.add(topMostTile.left);
			return tmp;
		}
		if ( Tile.allEqual(t, topMostTile.right) ){
			//case d)			
			tmp.add(topMostTile.right);
			return tmp;
		}
			
		return tmp;
	}
	
	/*
	 * check if its an horizontal L, either mirrored or not
	 *   a)		  b)      c)	  d)
	 * 0 0 1	0 0 0	1 0 0	0 0 0
	 * 1 1 1	1 1 1	1 1 1	1 1 1
	 * 0 0 0	0 0 1	0 0 0	1 0 0
	 */
	private List<Tile> getHorizontalL(Tile t){
		tmp.clear();
		
		tmp.add(t);
		
		Tile leftMostTile = t;
		Tile rightMostTile = t;
	
		if ( Tile.allEqual(t, t.right) ){
			tmp.add(t.right);
			rightMostTile = t.right;
			if ( Tile.allEqual(t, t.right.right) ){
				//tile passed is leftMost block
				tmp.add(t.right.right);
				rightMostTile = t.right.right;
			}else if ( Tile.allEqual(t, t.left)){
				//tile passed is middle block
				tmp.add(t.left);
				leftMostTile = t.left;
			}
		}else if( Tile.allEqual(t, t.left)){
			tmp.add(t.left);
			leftMostTile = t.left;
			if ( Tile.allEqual(t, t.left.left) ){
				//tile passed is rightmost block
				tmp.add(t.left.left);
				leftMostTile = t.left.left;
			}
		}else if ( Tile.allEqual(t, t.below)){
			tmp.add(t.below);
			if (Tile.allEqual(t, t.below.left, t.below.left.left)){
				//tile is top-right
				//case a)
				tmp.add(t.below.left);
				tmp.add(t.below.left.left);
				return tmp;
			}else if( Tile.allEqual(t, t.below.right, t.below.right.right)){
				//tile is top-left
				//case c)
				tmp.add(t.below.right);
				tmp.add(t.below.right.right);
				return tmp;
			}
		}else if ( Tile.allEqual(t, t.above)){
			tmp.add(t.above);
			if (Tile.allEqual(t, t.above.left, t.above.left.left) ){
				//tile is bottom-right
				//case a)
				tmp.add(t.above.left);
				tmp.add(t.above.left.left);
				return tmp;
			}else if( Tile.allEqual(t, t.above.right, t.above.right.right)){
				//tile is bottom-left
				//case c)
				tmp.add(t.above.right);
				tmp.add(t.above.right.right);
				return tmp;
			}
		}
		
		if ( tmp.size() != 3)
			//not enough pieces to be an horizontal L
			return tmp;
		
		if ( Tile.allEqual(t, rightMostTile.above) ){
			//case a)
			tmp.add(rightMostTile.above);
			return tmp;
		}
		
		if ( Tile.allEqual(t, rightMostTile.below) ){
			//case b)
			tmp.add(rightMostTile.below);
			return tmp;
		}
		
		if ( Tile.allEqual(t, leftMostTile.above) ){
			//case c)
			tmp.add(leftMostTile.above);
			return tmp;
		}
		if ( Tile.allEqual(t, leftMostTile.below)){
			//case d)			
			tmp.add(leftMostTile.below);
			return tmp;
		}
			
		return tmp;
	}
	
}
