package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import client.Game;
import client.TicTacToe;

public class TicTacToePanel extends JPanel{

	private JButton buttonGrid[][] = new JButton[3][3];
	private TicTacToe tttgame;
	
	public TicTacToePanel(Game game){
		tttgame = (TicTacToe)game;
		setLayout(new java.awt.GridLayout(3, 3));
	    for(int i = 0; i < 3; i++)
	    {
	      for(int j = 0; j < 3; j++)
	      {
	        JButton button = new JButton();
	        button.setBackground(Color.WHITE);
	        button.setFont(new Font("Arial", Font.PLAIN, 40));
	        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	        button.setName(i+ "" +j);
	        //button.setText(i+ "" +j);
	        //add action listener to each button
	        button.addActionListener( new ActionListener(){ public void actionPerformed(ActionEvent e){
	            tttgame.buttonClicked(button);
	          }
	        });

	        //add buttons to panel and grid
	        add(button);
	        buttonGrid[i][j] = button;
	      }
	    }
	    tttgame.setButtons(buttonGrid);
	}
	
}
