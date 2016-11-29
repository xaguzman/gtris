package org.xaguzman;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.xaguzman.BoardListener.EventType;
import org.xaguzman.tretonimos.ShapeL;
import org.xaguzman.tretonimos.ShapeLine;
import org.xaguzman.tretonimos.ShapeSquare;
import org.xaguzman.tretonimos.ShapeZ;
import org.xaguzman.tretonimos.TetronimoChecker;

public class Board {
	static final int COLUMNS = 10; //tile-wise
	static final int ROWS = 21; //tile-wise
	
	Tile[][] tiles;
	
	
	static float BLOCK_FALL_SPEED = 1f / 3f; //gravity: 3 blocks / sec
	static final float TIMER_SPAWNTIMEMODIFY = 120; //modify every in milliseconds
	float timeSinceLastSpawnModify; //last time the spawn time was modified
	static final float SPAWNTIMEMODIFIER = 0.85f; //percentage to modify the spawn time by
	
	float newSpawnTime; //time that should elapse for each new spawn. in seconds
	float timeSinceLastSpawn; //time last spawn happened, in seconds
	
	float gravityUpdateAccum;
	Random random = new Random();
	int cursorRow = 4;
	int cursorColumn = COLUMNS / 2;
	
	boolean isFull; //condition for defeat
	List<TetronimoChecker> tetronimos;
	public Tile selectedTile;
	long score;
	float playTimeAccum;
	int playTimeMinutes = 0;
	int level = 1;
	List<BoardListener> listeners = new ArrayList<>();
	
	public Board(BoardListener... listeners){
		newSpawnTime = 2;
		tiles = new Tile[ROWS][COLUMNS];
		tetronimos = new ArrayList<>();
		tetronimos.add(new ShapeL());
		tetronimos.add(new ShapeLine());
		tetronimos.add(new ShapeSquare());
		tetronimos.add(new ShapeZ());
		
		for(BoardListener l : listeners)
			this.listeners.add(l);
		
		for ( int r = 0; r < ROWS; r++){
			tiles[r] = new Tile[COLUMNS];
			for ( int c = 0; c < COLUMNS; c++){
				Tile t = new Tile(r, c);
				tiles[r][c] = t;
				if (r < 20){
					//set value different than neighbors to try to avoid having 
					//tetronimos formed when board shows
					Tile tmp = r > 0 && tiles[r-1][c].value == Tile.EMPTY ? tiles[r-1][c] : t;
					tmp.value = random.nextInt(6);
					if( tmp.row > 0 ){
						if ( c == 0){
							while (tmp.value == tiles[tmp.row-1][tmp.col].value)
								tmp.value = random.nextInt(6);
						}else{
							while (tmp.value == tiles[tmp.row-1][c].value || tmp.value == tiles[tmp.row][c-1].value)
								tmp.value = random.nextInt(6);
						}
					}
				}
			}
		}
		for ( int r = 0; r < ROWS; r++){
			for ( int c = 0; c < COLUMNS; c++){
				tiles[r][c].below = r > 0 ? tiles[r-1][c] : Tile.UNDEFINED;
				tiles[r][c].above = r < ROWS - 1 ? tiles[r+1][c] : Tile.UNDEFINED;
				tiles[r][c].left = c > 0 ? tiles[r][c-1] : Tile.UNDEFINED;
				tiles[r][c].right = c < COLUMNS - 1 ? tiles[r][c+1] : Tile.UNDEFINED;
			}
		}
		
	}
	
	public void update(float delta){
		
		if ( isFull )
			return;
		
		int topRowFilledCount = 0;
		for (int c = 0 ; c < COLUMNS; c++){
			if (tiles[ROWS-2][c].value != Tile.EMPTY && tiles[ROWS-2][c].isStatic())
				topRowFilledCount++;
		}
		if (topRowFilledCount == COLUMNS){
			isFull = true;
			for(BoardListener l : listeners)
				l.onEvent(EventType.BoardFull);
			//lost
			return;
		}
			
		
		timeSinceLastSpawnModify += delta;
		timeSinceLastSpawn += delta;
		gravityUpdateAccum += delta;
		
		if (timeSinceLastSpawnModify > TIMER_SPAWNTIMEMODIFY){
			newSpawnTime *= SPAWNTIMEMODIFIER;
			level++;
			timeSinceLastSpawnModify = 0;
		}
		
		if (gravityUpdateAccum >= BLOCK_FALL_SPEED){
			gravityUpdateAccum = 0;
			applyGravity();
		}
		
		if ( timeSinceLastSpawn > newSpawnTime){
			timeSinceLastSpawn = 0;
			addNewBlocks();
		}
		
		if (playTimeAccum >= 60){
			playTimeMinutes++;
			playTimeAccum = 0;
		}else{
			playTimeAccum += delta;
		}
		
	}
	
	//drops blocks one row, if below cell is empty
	private void applyGravity(){
		for (int r = 1; r < ROWS; r++){
			for (int c = 0; c < COLUMNS; c++){
				if ( tiles[r][c].value == Tile.EMPTY || tiles[r][c].isStatic() )
					continue;
				
				tiles[r-1][c].value = tiles[r][c].value;
				tiles[r][c].value = Tile.EMPTY;
								
				if ( r - 1 >= 0 && tiles[r-1][c].isStatic() ){
					//block landed, check for tetronimo
					checkTetronimo(tiles[r-1][c]);
				}
				
			}
		}
	}
	
	//if a tetronimo was formed around the given tile, remove it
	public synchronized void checkTetronimo(Tile t){
		
		List<Tile> tilesToClear = null;
		for(TetronimoChecker checker : tetronimos){
			tilesToClear = checker.getTetronimoPieces(t);
			if ( tilesToClear.size() == 4)
				break;
		}
		
		if ( tilesToClear.size() > 0 ){
			for(Tile tile : tilesToClear){
				tile.value = Tile.EMPTY;
			}
			score += 2 + playTimeMinutes;
			for(BoardListener l : listeners)
				l.onEvent( EventType.ScoreIncreased);
		}
	}

	private void addNewBlocks() {
		int column = random.nextInt(COLUMNS-1);
		int triesLeft = 10; //try 10 more times in case we got a busy cell
		
		while (tiles[ROWS-1][column].isStatic() && triesLeft > 0){
			//new position, since that column is filled
			column = random.nextInt(COLUMNS-1);
			triesLeft--;
		}
		
		//try to find two empty columns by brute force
		if (tiles[ROWS-1][column].isStatic() && tiles[ROWS-1][column + 1].isStatic()){
			for (int i = 0; i< COLUMNS -1; i++)
				if (!tiles[ROWS-1][i+1].isStatic()){
					column = i;
				}
		}
				
		//if we were able to place the first block, add it
		if ( !tiles[ROWS-1][column].isStatic() ){
			int block1 = random.nextInt(5) + 1;
			tiles[ROWS-1][column].value = block1;
		}
		
		//if we were able to place the second block, add it
		if (!tiles[ROWS-1][column+1].isStatic()){
			int block2 = random.nextInt(5) + 1;
			tiles[ROWS-1][column+1].value = block2;
			//System.out.println(String.format("New blocks at columns %d and %d", column, column+1) );
		}		
	}
	
	
}
