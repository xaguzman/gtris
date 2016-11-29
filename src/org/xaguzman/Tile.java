package org.xaguzman;

public class Tile{
	public int row;
	public int col;
	public int value;
	
	static public int EMPTY = 0;
	static public int RED = 1;
	static public int GREEN = 2;
	static public int YELLOW = 3;
	static public int BLUE = 4;
	static public int PINK = 5;
	
	static final int WIDTH = 32;
	static final int HEIGHT = 32;
	
	static Tile UNDEFINED ;
	static{
		UNDEFINED = new Tile(-1, -1);
		UNDEFINED.left = UNDEFINED;
		UNDEFINED.right = UNDEFINED;
		UNDEFINED.above = UNDEFINED;
		UNDEFINED.below = UNDEFINED;
	}
	
	public Tile below, above, left, right;
	
	public int x, y;
	
	public Tile(int row, int col){
		this.row = row;
		this.col = col;
	}
	
	public boolean isStatic(){
		return row == 0 || below.value != EMPTY ;
	}
	
	public static boolean allEqual(Tile... tiles){
		int v = tiles[0].value;
		
		for (int i = 0; i < tiles.length ; i++)
			if (tiles[i].value != v || !tiles[i].isStatic() || tiles[i] == UNDEFINED)
				return false;
		
		return true;
	}
}
