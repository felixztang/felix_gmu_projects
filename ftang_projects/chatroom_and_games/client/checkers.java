package client;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class checkers extends Game{
  
  //ENUM FOR TYPES
  public enum TYPE{
  BLACK,RED,BLACKKING,REDKING
}
   
  JFrame frame;
  JPanel panel;
  //board is the object representation
  Piece board[][] = new Piece[8][8];
  //buttonGrid is visual representation
  JButton buttonGrid[][];
  
  public int turn;
  public int boardSize = 8;
  public int blackCheckers;
  public int redCheckers;
  public TYPE winner;
  public boolean inHand = false;
  public Piece inHandPiece;
  public int inHandPieceRow;
  public int inHandPieceCol;
  
  //icons
  private Icon red;
  private Icon redKing;
  private Icon black;
  private Icon blackKing;
  private int myTurn;

  //initialize game, we will have to change the constructor when merging with the main code
  public checkers(int gameType, RoomManager rm, Room room){
    
    super(gameType, rm, room);
    
    blackCheckers = 12;
    redCheckers = 12;
    
    ClassLoader classLoader = checkers.class.getClassLoader();
    red = new ImageIcon(classLoader.getResource("client/RED.png"));
    black = new ImageIcon(classLoader.getResource("client/BLACK.png"));
    redKing = new ImageIcon(classLoader.getResource("client/RED_KING.png"));
    blackKing = new ImageIcon(classLoader.getResource("client/BLACK_KING.png"));
    turn = 0;
  }
  
  /* BIG IMPORTANT THESE FUNCTIONS MUST BE IMPLEMENTED TO RUN
   * Gets a string that is identical to what you send from send move 
   */
  //
  public void receiveMove(String str){
     char[] holder = str.toCharArray();
     int startRow = Character.getNumericValue(holder[0]);
     int startCol = Character.getNumericValue(holder[1]);
     int finalRow = Character.getNumericValue(holder[2]);
     int finalCol = Character.getNumericValue(holder[3]);
     if(myTurn != turn) {
    	 enableButtons(true);
     }
     doMove(startRow, startCol, finalRow,finalCol);
  }
  
  // This sends move to room manager in string form
  public void sendMove(int r, int c, JButton button)
  {
    sendMove("checkers,"+ inHandPieceRow + inHandPieceCol+button.getName());
  }
  
  //execute the move passed in from recieveMove, it's not really necessary
  //BE CAREFUL NOT DO DO MOVE TWICE
  public void doMove(int startRow, int startCol, int finalRow, int finalCol){
    
	  
	  
//    //HAVE TO CHANGE THIS TO WORK WITH DO MOVE
	  if(inHandPiece == null) {
		  inHandPiece = board[startRow][startCol];
		  board[startRow][startCol] = null;
		  buttonGrid[startRow][startCol].setIcon(null);
		  isValidJump(inHandPiece, startRow, startCol, finalRow, finalCol);
	  }
	  
    board[finalRow][finalCol] = inHandPiece;
    setIcon(buttonGrid[finalRow][finalCol],inHandPiece);
    
    checkKing(inHandPiece,finalRow,finalCol);
    
    turn = 1 - turn;
    
    inHandPiece = null;
    inHand = false;
    inHandPieceRow = -1;
    inHandPieceCol = -1;
    
    winner = checkWin();
    if (winner != null)
    {
      System.out.println("Congrats " + winner + " is the winner!\n");
    }
  }
  
 
  //handles the event of a button click
  //to be clear white spaces do not have buttons with events
  public void buttonClick(JButton button){
    //Button name string to int coordinates to detirmine position of click
    char[] holder = button.getName().toCharArray();
    int coordRow = Character.getNumericValue(holder[0]);
    int coordCol = Character.getNumericValue(holder[1]);
    
    /*TWO DIFFERENT BEHAVIORS
     * If you have a piece in hand it will take the first branch, if not 
     * it will take the second branch likely resulting in putting something 
     * in your hand
     */
    
    if(inHand)
    {
      /* detirmines if a move is valid based on starting position remembered by
       * inHandPiece coords and the final destination position which will be button pressed
       * e.g. coordRow,coordCol
       * */
      if(isValidMove(inHandPiece,inHandPieceRow,inHandPieceCol,coordRow,coordCol))
      {
        //in the case of putting down a piece in the same place, it does not send move
        if(inHandPieceRow == coordRow && inHandPieceCol == coordCol)
        {
          board[coordRow][coordCol] = inHandPiece;
          setIcon(buttonGrid[coordRow][coordCol],inHandPiece); //potentially change
          inHandPiece = null;
          inHand = false;
          inHandPieceRow = -1;
          inHandPieceCol = -1;
        }
        
        //HAVE TO CHANGE THIS TO WORK WITH DO MOVE
//        board[coordRow][coordCol] = inHandPiece;
//        setIcon(buttonGrid[coordRow][coordCol],inHandPiece);
        
        else if(!(inHandPieceRow == coordRow && inHandPieceCol == coordCol))
        {
        	myTurn = turn;
        	enableButtons(false);
          sendMove(inHandPieceRow,inHandPieceCol,button);
        }
        
        //checkKing(board[coordRow][coordCol],coordRow,coordCol);  
      }
       
    }
    //if no piece in hand
    else
    {
      //if the location clicked on has an Piece on it, you will put it in your hand
      //However, this sends no move and simply prepares for the next click
      //which is presumably placing object
      if(board[coordRow][coordCol] instanceof Piece)
      {
    	  
    	if(((board[coordRow][coordCol].getType() == TYPE.BLACK || board[coordRow][coordCol].getType() == TYPE.BLACKKING) && turn == 0) ||
    			((board[coordRow][coordCol].getType() == TYPE.RED || board[coordRow][coordCol].getType() == TYPE.REDKING) && turn == 1)) {
    		inHand = true;
    		inHandPiece = board[coordRow][coordCol];
    		inHandPieceRow = coordRow;
    		inHandPieceCol = coordCol;
    		board[coordRow][coordCol] = null;
    		buttonGrid[coordRow][coordCol].setIcon(null);
    	}
      }
    }
  }
  
  //will set the icon of a button to the appropriate piece type
  public void setIcon(JButton button, Piece piece)
  {
    //button.setIcon(null);
    if(piece.getType() == TYPE.BLACK)
    {
      button.setIcon(black);
    }
    if(piece.getType() == TYPE.BLACKKING)
    {
      button.setIcon(blackKing);
    }
    if(piece.getType() == TYPE.RED)
    {
      button.setIcon(red);
    }
    if(piece.getType() == TYPE.REDKING)
    {
      button.setIcon(redKing);
    }
  }
  
  public boolean setPiece(Piece piece, int r, int c)
  {
    board[r][c] = piece;
    return true;
  }
  
  /*BELOW THIS POINT IS BOARD LOGIC 
   * ENTER AT YOUR OWN PERIL
   * */
  
  //should validate any move given the piece starting loca and final loca
  public boolean isValidMove(Piece piece, int startRow, int startCol, int finalRow, int finalCol)
  {
    if(piece.getType() == TYPE.RED)
    {
      return(isValidRed(piece,startRow,startCol,finalRow,finalCol) || isValidJump(piece,startRow,startCol,finalRow,finalCol));
    }
    else if(piece.getType() == TYPE.BLACK)
    {
      return(isValidBlack(piece,startRow,startCol,finalRow,finalCol) || isValidJump(piece,startRow,startCol,finalRow,finalCol));
    }
    else if(piece.getType() == TYPE.BLACKKING || piece.getType() == TYPE.REDKING)
    {
      return(isValidKingMove(startRow, startCol, finalRow,finalCol) || isValidJump(piece,startRow,startCol,finalRow,finalCol));
    }
    else
      return false;
  }
  
  /* specific logic for red, basic red pieces cannot move up the board only down
   * so this logic will hold for only basic red pieces
   * */
  public boolean isValidRed(Piece piece, int startRow, int startCol, int finalRow, int finalCol)
  {
    if(finalRow < startRow)
      return false;
    else if(finalRow >= startRow +2)
      return false;
    else if(finalCol >= startCol +2 || finalCol <= startCol -2)
      return false;
    else if(board[finalRow][finalCol] != null)
      return false;
    else
      return true;
  }
  
  /* specific logic for black piece, black pieces cannot move back down the board
   * so this logic will only hold for basic black pieces
   * */
  public boolean isValidBlack(Piece piece, int startRow, int startCol, int finalRow, int finalCol)
  {
    if(finalRow > startRow)
      return false;
    else if(finalRow <= startRow -2)
      return false;
    else if(finalCol >= startCol +2 || finalCol <= startCol -2)
      return false;
    else if(board[finalRow][finalCol] != null)
      return false;
    else
      return true;
  }
  
  /* should validate a move for both types of pieces and kings, so it has to be robust
   * first checks to make sure the jump is 2 grid boxes over and away from to make certain of diagonal movement
   * DOESN"T WORK RIGHT NOW
   * NEEDS TO REMOVE PIECES IT JUMPS OVER AND REMOVE THE ICON FROM THE BUTTON
   * */
  public boolean isValidJump(Piece piece, int startRow, int startCol, int finalRow, int finalCol)
  {
    
    if(finalRow == startRow +2 || finalRow == startRow - 2)
    {
      if(finalCol == startCol+2 || finalCol == startCol -2)
      { 
        if(board[finalRow][finalCol] == null)
        {
          if(checkOppositePiece(board[(startRow+finalRow)/2][(startCol+finalCol)/2],piece))
          {
            if(piece.getType() == TYPE.RED && finalRow < startRow)
              return false;
            else if(piece.getType() == TYPE.BLACK && finalRow > startRow)
              return false;
            else{
              removePiece(board[(startRow+finalRow)/2][(startCol+finalCol)/2],(startRow+finalRow)/2,(startCol+finalCol)/2);
              return true;
            }
          }
        }
      }
    }
    return false;
    
  }
  
  public boolean isValidKingMove(int startRow, int startCol, int finalRow, int finalCol){
    if(board[finalRow][finalCol] == null && (((startRow - finalRow == 1 || startRow - finalRow == -1) &&(startCol - finalCol == 1 ||  startCol - finalCol == -1))) || (startRow == finalRow && startCol == finalCol))
      return true;
    else
      return false;
  }
  
  //Will check if a piece is elligible to become a king based on type and position
  public boolean checkKing(Piece piece,int r, int c)
  {
    if(piece.getType() == TYPE.RED && r == 7)
    {
      piece.setType(TYPE.REDKING);
      makeKing(piece,r,c);
      return true;
    }
    else if(piece.getType() == TYPE.BLACK && r == 0)
    {
      piece.setType(TYPE.BLACKKING);
      makeKing(piece,r,c);
      return true;
    }
    else
      return false;
  }
  
  //will MAKE said piece king of type, called by checkKing()
  public boolean makeKing(Piece piece, int r, int c)
  {
    buttonGrid[r][c].setIcon(null);
    setIcon(buttonGrid[r][c],piece);
    return true;
  }
  
  //Remove piece object reference and icon representation TESTED SUCCESS
  public boolean removePiece(Piece piece, int r, int c)
  {
    boolean flag = false;
    if(piece.getType() == TYPE.BLACK)
    {
      blackCheckers--;
      flag = true;
      
    }
    else if(piece.getType() == TYPE.RED)
    {
      redCheckers--;
      flag = true;
    }
    board[r][c] = null;
    buttonGrid[r][c].setIcon(null);
    return flag;
  }
  
  //checks to see if pieces are opposite teams, eg black/red or red/black
  public boolean checkOppositePiece(Piece piece, Piece piece2)
  {
    if((piece.getType() == TYPE.BLACK || piece.getType() == TYPE.BLACKKING) && (piece2.getType() == TYPE.RED || piece2.getType() == TYPE.REDKING) )
    {
      return true;
    }
    else if((piece.getType() == TYPE.RED || piece.getType() == TYPE.REDKING) && (piece2.getType() == TYPE.BLACK || piece2.getType() == TYPE.BLACKKING))
    {
      return true;
    }
    else 
      return false;
    
  }
  
  public TYPE checkWin()
  {
    if(blackCheckers <= 0)
    {
      return TYPE.RED;
    }
    else if(redCheckers <= 0)
      return TYPE.BLACK;
    else
      return null;
  }
  
  public void setButtonGrid(JButton[][] buttons) {
	  buttonGrid = buttons;
  }
  
  public void enableButtons(boolean e) {
	  for(int i = 0; i<8; i++)
	    {
	      for(int j = 0; j < 8; j++)
	      {
	        buttonGrid[i][j].setEnabled(e);
	      }
	    }
  }
  
  /* NEED TO IMPLEMENT
   * 
   * WIN LOGIC
   * 
   * MULTIPLE JUMP LOGIC
   * */

//class Piece for piece logic
public static class Piece
{
  TYPE type;
  
  public Piece(){
    type = null;
  }
  
  public Piece(TYPE type)
  {
    this.type = type;
  }
  
  public Piece(char charType)
  {
    if(charType == 'b')
      this.type = TYPE.BLACK;
    else if(charType == 'B')
      this.type = TYPE.BLACKKING;
    else if(charType == 'r')
      this.type = TYPE.RED;
    else if(charType == 'R')
      this.type = TYPE.REDKING;
    else
      type = null;
  }
  public void setType(TYPE type){
    this.type = type;
  }

  public TYPE getType(){
    return this.type;
  }
  
  @Override
  public String toString(){
    if (this.type == TYPE.BLACK)
      return "b";
    else if(this.type == TYPE.BLACKKING)
      return "B";
    else if(this.type == TYPE.RED)
      return "r";
    else if(this.type == TYPE.REDKING)
      return "R";
    else
      return "-";
  }

}
  
}