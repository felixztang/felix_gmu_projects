package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;


import client.Game;
import client.checkers;
import client.checkers.Piece;
import client.checkers.TYPE;

public class CheckersPanel extends JPanel{
  
  private checkers check;
  private JButton buttonGrid[][] = new JButton[8][8];
  
  public CheckersPanel(Game ch) {
    check = (checkers)ch;
    setLayout(new java.awt.GridLayout(8, 8));
    //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    for(int i = 0; i < 8; i++)
    {
      for(int j = 0; j < 8; j++)
      {
        JButton button = new JButton();
        button.setBackground(Color.WHITE);
        button.setName(i + "" + j);
        
        //logic for coloring them
        //also initializes placement of checkers
        if(i%2 == 0)
        {
          if(j%2 == 0)
            button.setBackground(Color.WHITE);
          else
          {
            button.setBackground(Color.BLACK);
            button.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){
              check.buttonClick(button);
            }
            });
            if(i >= 0 && i <=2)
            {
              Piece piece = new Piece(TYPE.RED);
              //board[i][j] = piece;
              //setIcon(button,piece);
              check.setPiece(piece,i,j);
              check.setIcon(button,piece);
            }
            else if(i >= 5 && i <= 7)
            {
              Piece piece = new Piece(TYPE.BLACK);
//              board[i][j] = piece;
//              setIcon(button,piece);
              check.setPiece(piece,i,j);
              check.setIcon(button,piece);
            }
          }
          
        }
        else{
          if(j%2 == 1)
            button.setBackground(Color.WHITE);
          else
          {
            button.setBackground(Color.BLACK);
            button.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){
              check.buttonClick(button);
            }
            });
            if(i >= 0 && i <=2)
            {
              Piece piece = new Piece(TYPE.RED);
//              board[i][j] = piece;
//              setIcon(button,piece);
              check.setPiece(piece,i,j);
              check.setIcon(button,piece);
            }
            else if(i >= 5 && i <= 7)
            {
              Piece piece = new Piece(TYPE.BLACK);
//              board[i][j] = piece;
//              setIcon(button,piece);
              check.setPiece(piece,i,j);
              check.setIcon(button,piece);
            }
          }
        }
        button.setFont(new Font("Arial", Font.PLAIN, 40));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        //buttons to buttongrid and panel
        add(button);
        buttonGrid[i][j] = button;
      }
      check.setButtonGrid(buttonGrid);
    }
//    frame.add(panel);
//    frame.setSize(400,400);
//    frame.setVisible(true);
  }
  
}
