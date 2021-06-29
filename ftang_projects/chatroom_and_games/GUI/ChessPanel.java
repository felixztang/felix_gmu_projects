package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import client.Chess;
import client.Game;

public class ChessPanel extends JPanel{

	private Chess chess;
	private JButton buttonGrid[][] = new JButton[8][8];
	
	public ChessPanel(Game ch) {
		chess = (Chess)ch;
		
		setLayout(new java.awt.GridLayout(8, 8));
        for (int i = 7; i > -1; i--)
        {
            for (int j = 0; j < 8; j++)
            {
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                button.setName(i + "" + j); // 00 -- 88
                
                //logic for coloring pieces
                //also initializes placement of chess pieces
                if (j % 2 == i % 2) button.setBackground(Color.GRAY);
                button.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ chess.receiveInput(button);
                }
                });
                
                chess.setIcon(button, i*8 + j);
                
                button.setFont(new Font("Arial", Font.PLAIN, 40));
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                
                //buttons to buttongrid and panel
                add(button);
                buttonGrid[i][j] = button;
            }
        }
        chess.setButtonGrid(buttonGrid);
	}
	
}
