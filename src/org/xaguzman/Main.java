package org.xaguzman;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.xaguzman.Sound.Volume;

public class Main {

	public static void main (String[] args) throws IOException{
		final Game gtris = new Game();
		JFrame window = new JFrame();
		window.setLayout(new BorderLayout(5, 0));
		window.add(gtris, BorderLayout.CENTER);
		window.add(new GameInfo(gtris), BorderLayout.SOUTH);
		window.setTitle("Gtris - Xavier Guzman");
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
		//gtris.start();
	}
	
	@SuppressWarnings("serial")
	public static class GameInfo extends JPanel{
		
		public GameInfo(final Game game){
			JButton start = new JButton("Play");
			start.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						game.start();
						if (!game.hasFocus())
							game.requestFocusInWindow();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			
			final JButton musiconoff = new JButton("Turn music OFF");
			musiconoff.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Sound.volume = Sound.volume == Volume.OFF ? Volume.ON : Volume.OFF;
					if (!game.hasFocus())
						game.requestFocusInWindow();
					
					String text = Sound.volume == Volume.OFF ? Volume.ON.toString() : Volume.OFF.toString();
					musiconoff.setText("Turn music " + text);
				}
			});
			
			add(start);
			add(musiconoff);
		}
	}
}
