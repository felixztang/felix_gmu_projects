package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class chatMenubar{
  JMenuBar menuBar;
  JMenu menuHelp;
  JMenu menuSettings;
  JMenuItem item1;
  JMenuItem item2;
  JMenuItem item3;
  JMenuItem item4;
  JMenuItem item5;
  
  public chatMenubar()
  {
    //create menu bar and menus here
    menuBar = new JMenuBar();
    //add any menus that might be helpful
    menuHelp = new JMenu("Help");
    menuSettings = new JMenu("Settings");
    menuBar.add(menuHelp);
    menuBar.add(menuSettings);
    
    //Add menu items here
    item1 = new JMenuItem("Key Shortcut List");
    item2 = new JMenuItem("About :)");
    item3 = new JMenuItem("Resolution Settings");
    item4 = new JMenuItem("light mode");
    item5 = new JMenuItem("dark mode");
    menuHelp.add(item1);
    menuHelp.add(item2);
    menuSettings.add(item3);
    menuSettings.add(item4);
    menuSettings.add(item5);
    
    //blah
    //action Listener specifically for menus
    ActionListener menuListener = new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if(e.getSource() == item4)
        {
          chatWindows.lightMode();
          chatWindows.lightMode();
        }
        else if(e.getSource() == item5)
        {
          chatWindows.darkMode();
          chatWindows.darkMode();
        }
        else
        {
          chatGUI.testMessage();
        }
      }
    };
    item1.addActionListener(menuListener);
    item2.addActionListener(menuListener);
    item3.addActionListener(menuListener);
    item4.addActionListener(menuListener);
    item5.addActionListener(menuListener);
    
  }
  
}