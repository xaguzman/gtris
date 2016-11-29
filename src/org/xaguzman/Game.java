package org.xaguzman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class Game extends JComponent implements Runnable, BoardListener {

	boolean isRunning;
	
	static final int BOARD_WIDTH = Tile.WIDTH * 10;
	static final int BOARD_HEIGHT = Tile.HEIGHT * 20; //don't draw the top row, we spawn blocks there
	static final int SCREEN_WIDTH = BOARD_WIDTH + 150;
	static final int SCREEN_HEIGHT = BOARD_HEIGHT;
	static final int MAX_FRAMESKIP = 10;
	Board board;
	BoardRenderer renderer;
	String scoreFormat = "Score: %s";
	String levelFormat = "Level %s";

	public Game() {
		setPreferredSize(new Dimension(SCREEN_WIDTH, BOARD_HEIGHT ));
		addKeyListener(input);
		Sound.values();
	}
	
	public void start() throws IOException{
		if( isRunning)
			return;
		
		isRunning = true;
		board = new Board(this);
		renderer = new BoardRenderer(board);
		
		new Thread(this).start();
	}
	
	public void stop(){
		isRunning = false;
	}
	
	public void run() {
        requestFocusInWindow();
        //for smoother rendering, using an intermediate image
        Image image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        
        Font uiFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        Font gameOverFont = new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 60);
        
        long lastUpdate = System.nanoTime();
        float nextRenderTime = 0;
        long renderTime = 1000000000 / 30;
        int frameSkipsLeft = 0;
        Color clearColor = Color.LIGHT_GRAY;
        float timeSinceLose = 0;

        while (isRunning) {
            Graphics g = image.getGraphics();
            g.setFont(uiFont);
            long now = System.nanoTime();
            float delta = (now - lastUpdate) / 1000000000f; //change the time to milliseconds isntead of nano seconds.
            lastUpdate = now;
            
            delta = Math.min(delta,  1 / 60f);
            
            board.update(delta);
            nextRenderTime -= delta;
            if ( nextRenderTime <= 0  || frameSkipsLeft == 0){
            	//clear the screen
            	g.setColor(clearColor);
            	g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
            	g.setColor(Color.WHITE);
            	g.fillRect(BOARD_WIDTH, 0, SCREEN_WIDTH - BOARD_WIDTH, SCREEN_HEIGHT);
            	g.setColor(Color.BLACK);
            	g.drawRect(BOARD_WIDTH, 0, SCREEN_WIDTH - BOARD_WIDTH, SCREEN_HEIGHT);
            	
            	//g.setColor(clearColor);
            	//g.fillRect(BOARD_WIDTH + 2, 5, SCREEN_WIDTH - BOARD_WIDTH - 4, 30);
            	//render board
            	renderer.render(g);
            	nextRenderTime = now + renderTime;
            	frameSkipsLeft = MAX_FRAMESKIP;
            	
            	//render info
            	g.setColor(Color.GRAY);
            	g.drawString(String.format(levelFormat, board.level), BOARD_WIDTH + 20, 20);
            	g.drawString("Score", BOARD_WIDTH + 20, 50);
            	g.drawString(String.valueOf(board.score), BOARD_WIDTH + 20, 68);
            	
            	if (board.isFull){
            		g.setFont(gameOverFont);
            		g.setColor(Color.BLACK);
            		
            		g.drawString("GAME OVER", 37, 303);
            		g.drawString("GAME OVER", 37, 297);
            		g.drawString("GAME OVER", 43, 303);
            		g.drawString("GAME OVER", 43, 297);
            		
            		g.setColor(Color.WHITE);
            		g.drawString("GAME OVER", 39, 301);
            		g.drawString("GAME OVER", 39, 299);
            		g.drawString("GAME OVER", 41, 301);
            		g.drawString("GAME OVER", 41, 299);
            		
            		g.setColor(Color.RED);
            		g.drawString("GAME OVER", 40, 300);
            	}
            	g.dispose();
            	                
            	//render to component
                g = getGraphics();
                g.drawImage(image, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
                g.dispose();
            }else{
            	frameSkipsLeft--;
            }
            
            if (board.isFull){
            	timeSinceLose += delta;
            }
            isRunning = timeSinceLose < 0.5f;
        } 
	}
	
	KeyAdapter input = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			if (board.isFull)
				return;
	
			Tile tileToCheck1 = null, tileToCheck2 =null;
			tileToCheck1 = board.selectedTile;
			
			switch (e.getKeyCode()){
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_S:
					if ( board.cursorRow == 0 )
						break;
						
					if (board.selectedTile != null){
						if ( board.selectedTile.below.isStatic() && board.selectedTile.below.value != Tile.EMPTY){
							int tmp = board.selectedTile.value;
							board.selectedTile.value = board.selectedTile.below.value;
							board.selectedTile.below.value = tmp;
							tileToCheck2 = board.selectedTile.below;
							board.selectedTile = null;
						}else{
							//play sound
							Sound.WRONG.play();
							board.selectedTile = null;
						}
					}
					Sound.MOVE.play();
					board.cursorRow--;
					break;
					
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_D:
					if ( board.cursorColumn == Board.COLUMNS - 1 )
						break;
					
					if (board.selectedTile != null){
						if ( board.selectedTile.right.isStatic() && board.selectedTile.right.value != Tile.EMPTY){
							int tmp = board.selectedTile.value;
							board.selectedTile.value = board.selectedTile.right.value;
							board.selectedTile.right.value = tmp;
							tileToCheck2 = board.selectedTile.right;
							board.selectedTile = null;
						}else{
							//play sound
							Sound.WRONG.play();
							board.selectedTile = null;
						}
					}
					Sound.MOVE.play();
					board.cursorColumn++;
					break;
					
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_A:
					if ( board.cursorColumn == 0 )
						break;
					
					if (board.selectedTile != null){
						if ( board.selectedTile.left.isStatic() && board.selectedTile.left.value != Tile.EMPTY){
							int tmp = board.selectedTile.value;
							board.selectedTile.value = board.selectedTile.left.value;
							board.selectedTile.left.value = tmp;
							tileToCheck2 = board.selectedTile.left;							
							board.selectedTile = null;
						}else{
							//play sound
							Sound.WRONG.play();
							board.selectedTile = null;
						}
					}
					Sound.MOVE.play();
					board.cursorColumn--;
					break;
					
				case KeyEvent.VK_UP:
				case KeyEvent.VK_W:
					if ( board.cursorColumn == Board.ROWS - 1 )
						break;
					
					if (board.selectedTile != null){
						if ( board.selectedTile.above.isStatic() && board.selectedTile.above.value != Tile.EMPTY){
							int tmp = board.selectedTile.value;
							board.selectedTile.value = board.selectedTile.above.value;
							board.selectedTile.above.value = tmp;
							tileToCheck2 = board.selectedTile.above;
							board.selectedTile = null;
						}else{
							//playsound
							Sound.WRONG.play();
							board.selectedTile = null;
						}
					}
					Sound.MOVE.play();
					board.cursorRow++;
					break;
					
				case KeyEvent.VK_SPACE:
					Tile targetTile = board.tiles[board.cursorRow][board.cursorColumn];
					if ( targetTile.isStatic() && targetTile.value != Tile.EMPTY ){
						board.selectedTile = board.tiles[board.cursorRow][board.cursorColumn];
					}else{
						Sound.WRONG.play();
					}
					break;
				case KeyEvent.VK_ESCAPE:
					board.selectedTile = null;
					break;
			}
			
			if ( tileToCheck1 != null)
				board.checkTetronimo(tileToCheck1);
			if ( tileToCheck2 != null)
				board.checkTetronimo(tileToCheck2);
		};
	};

	@Override
	public void onEvent(EventType event) {
		if (event == EventType.ScoreIncreased)
			Sound.SCORE.play();
		if ( event == EventType.BoardFull)
			Sound.GAMEOVER.play();
	}
}
