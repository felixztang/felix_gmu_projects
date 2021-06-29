package GUI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

class chatLoginScreen{
  chatWindows cw;
  
  chatLoginScreen(){
    
    JFrame logframe = new JFrame("Welcome");
    logframe.setLayout(new FlowLayout());
    logframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    logframe.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            if (cw == null) {
            	System.exit(0);
            }
        }
    });
    logframe.setSize(800, 600);
    
    //buttons go here
    JLabel label = new JLabel("Enter Username: ");
	JTextField tf = new JTextField(32);
	JLabel error = new JLabel();
	
	logframe.add(label);
	logframe.add(tf);
	logframe.add(error);
	
	logframe.setVisible(true);
    
    ActionListener a = new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if(e.getActionCommand().contains(",")) {
        	error.setText("Usernames can't have commas!");
        }else if(e.getActionCommand().equals("")){
        	error.setText("Usernames can't be blank!");
        }else {
        cw = new chatWindows(e.getActionCommand());
        logframe.dispatchEvent(new WindowEvent(logframe, WindowEvent.WINDOW_CLOSING));
        }
      }
    };
    tf.addActionListener(a);
    
  }
}