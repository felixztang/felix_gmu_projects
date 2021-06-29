package GUI;

import javax.swing.*;

import client.Game;
import client.RoomManager;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

class chatTabs{

  JTabbedPane tPane;
  RoomManager roomManager;
  ArrayList<RoomPanel> roomPanels;
  boolean running = true;
  
  public chatTabs(String username){
	roomManager = new RoomManager(username);
	
	roomPanels = new ArrayList<RoomPanel>();
	
    tPane = new JTabbedPane();
    JPanel newChatPanel = new JPanel(false);
    newChatPanel.setLayout(new FlowLayout());
    tPane.addTab("New Room", newChatPanel);
    
    setupNewRoom(newChatPanel);
  }
  
  public void setupNewRoom(JPanel panel) {
	  new Thread(){
		    public void run() {
		    	updateClient();
		    }
		}.start();
	  
	  panel.removeAll();
	  
	  JButton host = new JButton("Host Room");
	  JButton join = new JButton("Join Room");
	  panel.add(host);
	  panel.add(join);
	    
	  ActionListener a = new ActionListener()
	  {
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
	      hostRoom(panel);
	        
	    }
	  };
	  host.addActionListener(a);
	    
	  ActionListener b = new ActionListener()
	  {
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
	        joinRoom(panel);
	    }
	  };
	  join.addActionListener(b);
	  
	  panel.revalidate();
	  panel.repaint();
  }
  
  public void hostRoom(JPanel panel) {
	  panel.removeAll();
	  
	  JLabel label = new JLabel("Port:");
	  JTextField tf = new JTextField(32);
	  panel.add(label);
	  panel.add(tf);
	  
	  JButton go = new JButton("Go");
	  panel.add(go);
	  ActionListener a = new ActionListener()
	    {
	      @Override
	      public void actionPerformed(ActionEvent e)
	      {
	    	  roomManager.hostRoom(Integer.parseInt(tf.getText()));
	    	  addRoom("localhost");
	    	  setupNewRoom(panel);
	      }
	    };
	    go.addActionListener(a);
	    tf.addActionListener(a);
	    
	    JButton back = new JButton("Back");
		panel.add(back);
		ActionListener b = new ActionListener(){
			@Override
		    public void actionPerformed(ActionEvent e)
		    {
				setupNewRoom(panel);
		    }
		};
		back.addActionListener(b);
	  
	  panel.revalidate();
	  panel.repaint();
  }
  
  public void joinRoom(JPanel panel) {
	  panel.removeAll();
	  
	  JLabel labelIP = new JLabel("IP:");
	  JTextField tfIP = new JTextField(32);
	  panel.add(labelIP);
	  panel.add(tfIP);
	  
	  JLabel labelPort = new JLabel("Port:");
	  JTextField tfPort = new JTextField(32);
	  panel.add(labelPort);
	  panel.add(tfPort);
	  
	  JButton go = new JButton("Go");
	  panel.add(go);
	  ActionListener a = new ActionListener()
	    {
	      @Override
	      public void actionPerformed(ActionEvent e)
	      {
	    	  roomManager.joinRoom(tfIP.getText(), Integer.parseInt(tfPort.getText()));
	    	  addRoom(tfIP.getText());
	    	  setupNewRoom(panel);
	      }
	    };
	    go.addActionListener(a);
	    tfIP.addActionListener(a);
	    tfPort.addActionListener(a);
	    
	    JButton back = new JButton("Back");
		panel.add(back);
		ActionListener b = new ActionListener(){
			@Override
		    public void actionPerformed(ActionEvent e)
		    {
				setupNewRoom(panel);
		    }
		};
		back.addActionListener(b);
	  
	  panel.revalidate();
	  panel.repaint();
  }
  
  public void addRoom(String name) {
	  RoomPanel roomPanel = new RoomPanel();
	  tPane.addTab(name, roomPanel);
	  roomPanels.add(roomPanel);
	  tPane.setSelectedComponent(roomPanel);
  }
  
  public void updateClient() {
		while(running) {
			for(int i = 0; i < roomManager.getNumberOfRooms(); i++) {
				if(roomManager.receiveMessage(i)) {
					leave(i);
					break;
				}
				roomManager.acceptNewConnections(i);
				if(roomPanels.size() > i) {
					String[] messages = roomPanels.get(i).GetMessages();
					for(int j = 0; j < messages.length; j++) {
						roomManager.sendMessage("chat,"+messages[j], i, false);
					}
					String[] userList = roomManager.getUsers(i);
					roomPanels.get(i).UpdateUserList(userList);
					String[] chatLog = roomManager.getMessageLog(i);
					if (chatLog.length > 0) {
						roomPanels.get(i).UpdateChatLog(chatLog);
					}
					
					Integer[] games = roomPanels.get(i).GetGames();
					for(int j = 0; j < games.length; j++) {
						roomManager.startGame(i, (int)games[j]);
					}
					for(int j = 0; j < 4; j++) {
						Game g = roomManager.getGame(i, j);
						if(g != null) {
							roomPanels.get(i).SetGame(g, j);
						}
					}
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
  
  public void leave(int roomNumber) {
	  tPane.remove(roomNumber+1);
	  roomPanels.remove(roomNumber);
  }
  
  public void close() {
  	for(int i = 0; i < roomManager.getNumberOfRooms(); i++) {
  		roomManager.leaveRoom(i);
  	}
  	running = false;
  }
}