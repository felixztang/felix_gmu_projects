package client;

import javax.swing.JButton;

public class TicTacToe extends Game {

	private enum TYPE{
		  X,O
	}
	
	private Piece board[][] = new Piece[3][3];
	private JButton buttonGrid[][];
	private TYPE turn = TYPE.X;
	private TYPE myMove;
	private int boardSize = 3;
	
	public TicTacToe(int gameType, RoomManager rm, Room room){
		super(gameType, rm, room);
	}
	
	//checks to see if a move is valid by detecting if there's a piece there already
	  public boolean isValidMove(int r, int c)
	  {
	    if(board[r][c] == null)
	      return true;
	    else
	      return false;
	  }
	  
	  //recieves move from above
	  public void receiveMove(String str)
	  {
	    char[] holder = str.toCharArray();
	    char charType = holder[0];
	    int coordRow = Character.getNumericValue(holder[1]);
	    int coordCol = Character.getNumericValue(holder[2]);
	    if((charType == 'X' && myMove != TYPE.X) || (charType == 'O' && myMove != TYPE.O)) {
	    	myMove = null;
	    	enableButtons(true);
	    }
	    doMove(coordRow,coordCol);
	    
	  }
	  
	  //sends move to above
	  public void sendMove(JButton button)
	  {
	    String move = "tictactoe," + turn + "" + button.getName();
	    sendMove(move);
	  }
	  
	  //executes the move recieved by recieve move
	  public void doMove(int r, int c)
	  {
	    Piece piece = new Piece(turn);
	    setPiece(piece,r,c);
	    buttonGrid[r][c].setText(turn.toString());
	    changeTurn();
	    if(checkWin(TYPE.X) || checkWin(TYPE.O))
	    {
	      resetBoard();
	    }
	  }
	  
	//changes the turn to whatever the opposite is
	  public void changeTurn(){
	    if(turn == TYPE.X)
	      turn = TYPE.O;
	    else if(turn == TYPE.O)
	      turn = TYPE.X;
	    else
	      turn = TYPE.X;
	  }
	  
	  /*BOARD AND MOVE LOGIC HANDLED BELOW THIS POINT
	   * NO REASON TO LOOK HERE AS THERE'S NOTHING IMPORTANT OR TOO INVOLVED IN 
	   * TIC TAC TOE
	   * */
	  
	  //resets boards and button texts
	  public void resetBoard()
	  {
	    for(int i = 0; i < boardSize; i++){
	      for(int j = 0; j < boardSize; j++)
	      {
	        removePiece(i,j);
	        buttonGrid[i][j].setText("");
	      }
	    }
	    turn = TYPE.X;
	    myMove = null;
	    enableButtons(true);
	  }
	  
	  public void enableButtons(boolean e) {
		  for(int i = 0; i<boardSize; i++)
		    {
		      for(int j = 0; j < boardSize; j++)
		      {
		        buttonGrid[i][j].setEnabled(e);
		      }
		    }
	  }
	  
	  //checks if board has 9 pieces on it
	  public boolean isBoardFull()
	  {
	    for(int i = 0; i<boardSize; i++)
	    {
	      for(int j = 0; j < boardSize; j++)
	      {
	        if(board[i][j] == null)
	          return false;
	      }
	    }
	    
	    resetBoard();
	    return true;
	  }
	  
	  
	  //returns reference to a piece but does not remvoe it
	  public Piece getPiece(int i, int j){
	    return board[i][j];
	  }
	  
	  //places a piece if location is valid and empty
	  public boolean setPiece(Piece piece, int r, int c)
	  {
	    if(r >= boardSize || c >= boardSize || r < 0 || c < 0)
	    {
	      return false;
	    }
	    else if(board[r][c] != null)
	    {
	      return false;
	    }
	    else
	    {
	      board[r][c] = piece;
	      isBoardFull();
	      return true;
	    }
	  }
	  
	  //removes a piece and makes null position
	  public boolean removePiece(int r, int c)
	  {
	    if(r >= boardSize || c >= boardSize || r < 0 || c < 0)
	    {
	      return false;
	    }
	    else
	    {
	      board[r][c] = null;
	      return true;
	    }
	  
	  }
	  
	  //Checks for all types of wins and will return true if the type is a win
	  public boolean checkWin(TYPE type)
	  {
	    boolean winCon = (checkColWin(type) || checkRowWin(type) || checkDiaWin(type));
	    if(winCon)
	      System.out.println("Congrats " +type+ " is the winner!\n\n");
	    return winCon;
	  }
	  
	  //specifically checks for collumn win
	  public boolean checkColWin(TYPE type)
	  {
	    for(int i = 0; i < boardSize; i++)
	    {
	      if(board[0][i] instanceof Piece && board[1][i] instanceof Piece && board[2][i] instanceof Piece)
	      {
	        if(board[0][i].type == type  && board[1][i].type == type  && board[2][i].type == type)
	        {
	          return true;
	        }
	      }
	    }
	    return false;
	  }
	  
	  //specifically checks for row wins
	  public boolean checkRowWin(TYPE type)
	  {
	    for(int i = 0; i < boardSize; i++)
	    {
	      if(board[i][0] instanceof Piece && board[i][1] instanceof Piece && board[i][2] instanceof Piece)
	      {
	        if(board[i][0].type == type  && board[i][1].type == type  && board[i][2].type == type)
	        {
	          return true;
	        }
	      }
	    }
	    return false;
	  }
	  
	  //specifically checks diagonals win
	  public boolean checkDiaWin(TYPE type)
	  {
	    if(board[0][0] instanceof Piece && board[1][1] instanceof Piece && board[2][2] instanceof Piece)
	    {
	      if(board[0][0].type == type && board[1][1].type == type && board[2][2].type == type)
	        return true;
	    }
	    else if(board[0][2] instanceof Piece && board[1][1] instanceof Piece && board[2][0] instanceof Piece)
	    {
	      if(board[0][2].type == type && board[1][1].type == type && board[2][0].type == type)
	        return true;
	    }
	    return false;
	  
	  }
	  
	  //handles logic for button clicked
	  public void buttonClicked(JButton button)
	  {
	    char[] holder = button.getName().toCharArray();
	    int coordRow = Character.getNumericValue(holder[0]);
	    int coordCol = Character.getNumericValue(holder[1]);
	    if(isValidMove(coordRow,coordCol) && !isBoardFull()){
//	      Piece piece = new Piece(turn);
//	      setPiece(piece,coordRow,coordCol);
//	      button.setText(String.valueOf(piece.getType()));
//	      changeTurn();
	    	myMove = turn;
	      sendMove(button);
	      //doMove(coordRow,coordCol);
	      enableButtons(false);
	      
	    }
	    
	    //sendMove(button);
	  }
	  
	  public void setButtons(JButton[][] buttons) {
		  buttonGrid = buttons;
	  }

	  public class Piece
	  {
	    TYPE type;
	    
	    public Piece(){
	      type = TYPE.X;
	    }
	    
	    public Piece(TYPE type)
	    {
	      this.type = type;
	    }
	    
	    public Piece(char charType)
	    {
	      if(charType == 'X')
	        this.type = TYPE.X;
	      else if(charType == 'O')
	        this.type = TYPE.O;
	      else
	        this.type = null;
	    }
	    public void setType(TYPE type){
	      this.type = type;
	    }

	    public TYPE getType(){
	      return this.type;
	    }
	    
	    @Override
	    public String toString(){
	      if (this.type == TYPE.X)
	        return "X";
	      else if(this.type == TYPE.O)
	        return "O";
	      else
	        return "-";
	    }

	  }
}
