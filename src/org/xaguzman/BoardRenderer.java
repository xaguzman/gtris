package org.xaguzman;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BoardRenderer {

	Board board;
	Image[] blocks;
	Image cursor;
	static int CURSOR_HEIGHT = 36;
	
	public BoardRenderer(Board board) throws IOException{
		this.board = board;
		blocks = new Image[5];

		blocks[Tile.RED-1] = ImageIO.read(BoardRenderer.class.getResource("/red.png"));
		blocks[Tile.BLUE-1] = ImageIO.read(BoardRenderer.class.getResource("/blue.png"));
		blocks[Tile.YELLOW-1] = ImageIO.read(BoardRenderer.class.getResource("/yellow.png"));
		blocks[Tile.GREEN-1] = ImageIO.read(BoardRenderer.class.getResource("/green.png"));
		blocks[Tile.PINK-1] = ImageIO.read(BoardRenderer.class.getResource("/pink.png"));
		cursor = ImageIO.read(BoardRenderer.class.getResource("/cursor.png"));
	}
	
	public void render(Graphics g){
		int x, y;			
		
		for(int row = 0; row < board.tiles.length - 1 ; row++){
			for ( int col = 0; col < board.tiles[row].length; col++){
				//skip the last row, since thats where we spawn new blocks, it shouldn't be rendered
				Tile tile = board.tiles[row][col]; 
				if (tile.value == Tile.EMPTY)
					continue;
				
				x = col * Tile.WIDTH;
				y = Game.BOARD_HEIGHT - (row * Tile.HEIGHT) - Tile.HEIGHT;
				
				if (!tile.isStatic()){
					y += Tile.HEIGHT / ( Board.BLOCK_FALL_SPEED / board.gravityUpdateAccum ) ;
				}
				
				g.drawImage(blocks[tile.value-1], x, y , null);
			}
		}
		
		if ( board.selectedTile == null){
			x = board.cursorColumn * Tile.WIDTH;
			y = Game.BOARD_HEIGHT - (board.cursorRow  * Tile.HEIGHT);
			g.drawImage(cursor, x - 2, y - CURSOR_HEIGHT, null );
		}else{
			x = board.cursorColumn * Tile.WIDTH;
			y = Game.BOARD_HEIGHT - (board.cursorRow  * Tile.HEIGHT) - 4;
			g.drawImage(cursor, x - 4, y - CURSOR_HEIGHT, 42, 42, null );
		}
	}
}
