package GUI;

import javax.swing.*;

class chatWindows{
  chatMenubar mb;
  chatTabs tab;
  String username;
  
  chatWindows(String username){
    mb = new chatMenubar();
    tab = new chatTabs(username);
    
    JFrame frame = new JFrame("Chat Client");
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            if (JOptionPane.showConfirmDialog(frame, 
                "Are you sure you want to close this window?", "Close Window?", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
            	tab.close();
                System.exit(0);
            }
        }
    });
    frame.setSize(800, 800); 
    frame.setVisible(true);
    
    frame.getContentPane().add(tab.tPane);
  }
  
  
  public static void lightMode()
  {
    //frame.getContentPane().setBackground(Color.WHITE);
    //tPane.setBackground(Color.WHITE);
    //label.setBackground(Color.WHITE);
  }
  
  //turn the colors of the windows grey
  public static void darkMode()
  {
     
    //frame.getContentPane().setBackground(Color.GRAY);
    //tPane.setBackground(Color.GRAY);
    //label.setBackground(Color.GRAY);
    //tPane.setBackgroundAt(0,Color.GRAY);
  }
}