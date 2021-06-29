package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class chatGUI{
  chatLoginScreen log;
  
  public chatGUI(){
    log = new chatLoginScreen();
  }

  void start(){
  
  }
  public static void main(String[] args){
    chatGUI gui = new chatGUI();
    gui.start();
   
  }
  
 //a test message to print out to check button fuctionality
  public static void testMessage()
  {
    JOptionPane.showMessageDialog(null, "You have reached the test message");
  }
}

  