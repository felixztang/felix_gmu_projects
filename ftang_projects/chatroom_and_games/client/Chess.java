/*
 *
 */
package client;
import java.util.List;
//
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//import java.util.Scanner;

/**
 *
 * @author Felix
 */
public class Chess extends Game //TODO: make King tile red when in check? handle checkmate better; DO GUI STUFF PLEASE; THREEFOLD REPETITION/STALEMATE?
{
    private int turn; // Index of the user whos turn it is
    private int winner; // Index of the user who won
    private List<User> players;
    private int myTurn = -1;
    
  
    JFrame frame;
    JPanel panel;
    JButton buttonGrid[][] = new JButton[8][8];
    
    @Override
    public void receiveMove(String move)
    {
        String player = (turn == 0 ? "WHITE" : "BLACK");
        
        if(turn != myTurn) {
        	enableButtons(true);
        }
        
        parseMove(move, player);
    }
    
    @Override
    public User getWinner()
    {
        return players.get(winner);
    }
            
    /**
     * 
     * 
     * 
     */
    
    private chessPiece[] board;
    private int selectedPiece;  // will store whichever piece is to be moved
    /** ------------------------
        ------------------------ */
    private class chessPiece
    {
        private char name;      // p, r, b, n, q, k
        private int tile;
        /**
         *  56 57 58 59 60 61 62 63      A8 B8 C8 D8 E8 F8 G8 H8
         *  48 49 50 51 52 53 54 55      A7 B7 C7 D7 E7 F7 G7 H7
         *  40 41 42 43 44 45 46 47      A6 B6 C6 D6 E6 F6 G6 H6
         *  32 33 34 35 36 37 38 39      A5 B5 C5 D5 E5 F5 G5 H5
         *  24 25 26 27 28 29 30 31      A4 B4 C4 D4 E4 F4 G4 H4
         *  16 17 18 19 20 21 22 23      A3 B3 C3 D3 E3 F3 G3 H3
         *  8  9  10 11 12 13 14 15      A2 B2 C2 D2 E2 F2 G2 H2
         *  0  1  2  3  4  5  6  7       A1 B1 C1 D1 E1 F1 G1 H1
         */
        private boolean movedYet;   //false unless the piece hasn't yet moved
        private boolean enPassant;  //false unless the piece is a pawn that *just* moved 2 tiles
        private String color;   //"WHITE" or "BLACK"
        
        private chessPiece(char name, int tile, String color)
        {
            this.name = name;
            this.tile = tile;
            this.movedYet = false;
            this.color = color;
            this.enPassant = false;
        }
        
        /*private chessPiece(char name, int tile, String color, boolean movedYet)
        {
            this.name = name;
            this.tile = tile;
            this.movedYet = movedYet;
            this.color = color;
            this.enPassant = false;
        }*/
        
        private chessPiece(chessPiece copyTarget)
        {
            this.name = copyTarget.getName();
            this.tile = copyTarget.getTile();
            this.movedYet = true;
            this.color = copyTarget.getColor();
            this.enPassant = false;
        }
        
        private String getColor()   { return color; }
        private char getName()      { return name; }
        //private boolean getMovedYet()   { return movedYet; }
        private boolean getEnPassant()  { return enPassant; }
        private int getTile()       { return tile; }
        
        private void setName(char name)  { this.name = name; }
        private void setTile(int tile)   { this.tile = tile; }
        private void setEnPassant(boolean enPassant)    { this.enPassant = enPassant;}
        
        /**
         * 
         * @return an integer array of every valid move for this piece
         */
        private int[] getValidMoves(chessPiece[] board)
        {
            int[] validMoves = new int[35];
            int move = 0;   // keeps track of current place in validMoves array
            
            switch(name)
            {
                case 'p': { // PAWN
                            if (color.equals("WHITE")) {
                                if (tile+8 < 64 && board[tile+8] == null) validMoves[move++] = tile+8;  // up 1
                                if (!movedYet && move > 0 && board[tile+16] == null) validMoves[move++] = tile+16;  // up 2
                                if (tile%8 != 0 && tile < 56 && board[tile+7] != null && board[tile+7].getColor().equals("BLACK")) validMoves[move++] = tile+7; // attack up-left
                                if (tile%8 != 7 && tile < 56 && board[tile+9] != null && board[tile+9].getColor().equals("BLACK")) validMoves[move++] = tile+9; // attack up-right
                                if (tile%8 != 0 && tile < 56 && board[tile-1] != null && board[tile-1].getColor().equals("BLACK") && board[tile-1].getEnPassant()) validMoves[move++] = tile+7; // en passant up-left
                                if (tile%8 != 7 && tile < 56 && board[tile+1] != null && board[tile+1].getColor().equals("BLACK") && board[tile+1].getEnPassant()) validMoves[move++] = tile+9; // en passant up-right
                                return validMoves;
                            }
                            //color BLACK
                                if (tile-8 > -1 && board[tile-8] == null) validMoves[move++] = tile-8;    // down 1
                                if (!movedYet && move > 0 && board[tile-16] == null) validMoves[move++] = tile-16;  // down 2
                                if (tile%8 != 7 && tile > 7 && board[tile-7] != null && board[tile-7].getColor().equals("WHITE")) validMoves[move++] = tile-7; // attack down-right
                                if (tile%8 != 0 && tile > 7 && board[tile-9] != null && board[tile-9].getColor().equals("WHITE")) validMoves[move++] = tile-9; // attack down-left
                                if (tile%8 != 7 && tile > 7 && board[tile+1] != null && board[tile+1].getColor().equals("WHITE") && board[tile+1].getEnPassant()) validMoves[move++] = tile-7; // en passant down-right
                                if (tile%8 != 0 && tile > 7 && board[tile-1] != null && board[tile-1].getColor().equals("WHITE") && board[tile-1].getEnPassant()) validMoves[move++] = tile-9; // en passant down-left
                                return validMoves;
                        }
                case 'q':   // QUEEN will get its moves from rook and bishop
                case 'r': { // ROOK
                            for (int i = tile+8; i < 64; i+=8)    // check UP
                            {
                                if (board[i] == null) validMoves[move++] = i;
                                else {
                                    if (!board[i].getColor().equals(color)) validMoves[move++] = i;
                                    break; // reached a piece, kill the loop
                                }
                            }
                            for (int i = tile-8; i > -1; i-=8)  // check DOWN
                            {
                                if (board[i] == null) validMoves[move++] = i;
                                else {
                                    if (!board[i].getColor().equals(color)) validMoves[move++] = i;
                                    break;
                                }
                            }
                            for (int i = tile+1; i%8 != 0 && i < 64; i++) // check RIGHT
                            {
                                if (board[i] == null) validMoves[move++] = i;
                                else {
                                    if (!board[i].getColor().equals(color)) validMoves[move++] = i;
                                    break;
                                }
                            }
                            for (int i = tile-1; i%8 != 7 && i > -1; i--) // check LEFT
                            {
                                if (board[i] == null) validMoves[move++] = i;
                                else {
                                    if (!board[i].getColor().equals(color)) validMoves[move++] = i;
                                    break;
                                }
                            }
                            if (name == 'r') return validMoves; // end of rook moves, queen still needs bishop moves
                        }
                case 'b': { // BISHOP
                            for (int i = tile+7; i%8 != 7 && i < 64; i+=7)    // check UP-LEFT
                            {
                                if (board[i] == null) validMoves[move++] = i;
                                else {
                                    if (!board[i].getColor().equals(color)) validMoves[move++] = i;
                                    break;
                                }
                            }
                            for (int i = tile+9; i%8 != 0 && i < 64; i+=9)  // check UP-RIGHT
                            {
                                if (board[i] == null) validMoves[move++] = i;
                                else {
                                    if (!board[i].getColor().equals(color)) validMoves[move++] = i;
                                    break;
                                }
                            }
                            for (int i = tile-9; i%8 != 7 && i > -1; i-=9)  // check DOWN-LEFT
                            {
                                if (board[i] == null) validMoves[move++] = i;
                                else {
                                    if (!board[i].getColor().equals(color)) validMoves[move++] = i;
                                    break;
                                }
                            }
                            for (int i = tile-7; i%8 != 0 && i > -1; i-=7)  // check DOWN-RIGHT
                            {
                                if (board[i] == null) validMoves[move++] = i;
                                else {
                                    if (!board[i].getColor().equals(color)) validMoves[move++] = i;
                                    break;
                                }
                            }
                            return validMoves;  // end of queen and bishop moves
                        }
                case 'n': { //KNIGHT
                            if (tile%8 > 1 && tile < 56 && (board[tile+6] == null || !board[tile+6].getColor().equals(color))) validMoves[move++] = tile+6;   //UP-LEFT-LEFT
                            if (tile%8 != 0 && tile < 48 && (board[tile+15] == null || !board[tile+15].getColor().equals(color))) validMoves[move++] = tile+15; //UP-UP-LEFT
                            if (tile%8 != 7 && tile < 48 && (board[tile+17] == null || !board[tile+17].getColor().equals(color))) validMoves[move++] = tile+17; //UP-UP-RIGHT
                            if (tile%8 < 6 && tile < 56 && (board[tile+10] == null || !board[tile+10].getColor().equals(color))) validMoves[move++] = tile+10;  //UP-RIGHT-RIGHT
                            if (tile%8 < 6 && tile > 7 && (board[tile-6] == null || !board[tile-6].getColor().equals(color))) validMoves[move++] = tile-6;    //DOWN-RIGHT-RIGHT
                            if (tile%8 != 7 && tile > 15 && (board[tile-15] == null || !board[tile-15].getColor().equals(color))) validMoves[move++] = tile-15; //DOWN-DOWN-RIGHT
                            if (tile%8 != 0 && tile > 15 && (board[tile-17] == null || !board[tile-17].getColor().equals(color))) validMoves[move++] = tile-17; //DOWN-DOWN-LEFT
                            if (tile%8 > 1 && tile > 7 && (board[tile-10] == null || !board[tile-10].getColor().equals(color))) validMoves[move++] = tile-10;   //DOWN-LEFT-LEFT
                            break;
                        }
                case 'k': { //KING
                            if (tile+8 < 64 && (board[tile+8] == null || !board[tile+8].getColor().equals(color))) validMoves[move++] = tile+8;   //UP
                            if (tile-8 > -1 &&  (board[tile-8] == null || !board[tile-8].getColor().equals(color))) validMoves[move++] = tile-8;   //DOWN
                            if (tile%8 != 7) {
                                if (board[tile+1] == null || !board[tile+1].getColor().equals(color)) validMoves[move++] = tile+1;    //RIGHT
                                if (tile > 7 &&  (board[tile-7] == null || !board[tile-7].getColor().equals(color))) validMoves[move++] = tile-7;  //DOWN-RIGHT
                                if (tile < 56 &&  (board[tile+9] == null || !board[tile+9].getColor().equals(color))) validMoves[move++] = tile+9; //UP-RIGHT
                            }
                            if (tile%8 != 0) {
                                if (board[tile-1] == null || !board[tile-1].getColor().equals(color)) validMoves[move++] = tile-1;    //LEFT
                                if (tile > 7 &&  (board[tile-9] == null || !board[tile-9].getColor().equals(color))) validMoves[move++] = tile-9;  //DOWN-LEFT
                                if (tile < 56 &&  (board[tile+7] == null || !board[tile+7].getColor().equals(color))) validMoves[move++] = tile+7; //UP-LEFT
                            }
                            //Castling
                            if (!movedYet && board[tile-1] == null && board[tile-2] == null && board[tile-3] == null && !board[tile-4].movedYet) validMoves[move++] = tile-2;   // QUEENSIDE
                            if (!movedYet && board[tile+1] == null && board[tile+2] == null && !board[tile+3].movedYet) validMoves[move++] = tile+2;    // KINGSIDE
                                
                            break;
                        }
            }
            return validMoves;
        }
        
        public String toString()    //TODO: change this, currently just for testing
        {
            String ret = ". ";
            
            switch(name)
            {
                case 'p':
                    if (color.equals("WHITE")) ret = "p ";
                    else ret = "0 ";
                    break;
                case 'r':
                    if (color.equals("WHITE")) ret = "r ";
                    else ret = "3 ";
                    break;
                case 'n':
                    if (color.equals("WHITE")) ret = "n ";
                    else ret = "5 ";
                    break;
                case 'b':
                    if (color.equals("WHITE")) ret = "b ";
                    else ret = "4 ";
                    break;
                case 'q':
                    if (color.equals("WHITE")) ret = "q ";
                    else ret = "2 ";
                    break;
                case 'k':
                    if (color.equals("WHITE")) ret = "k ";
                    else ret = "1 ";
            }
            
            return ret;
        }
    }
    /** ------------------------
        ------------------------ */
    
    public Chess(int gameType, RoomManager rm, Room room)
    {
        super(gameType, rm, room);
        selectedPiece = -1;
        turn = 0;
        board = new chessPiece[64];
        resetBoard();
    }
    
    public void setIcon(JButton button, int index)
    {
    	
    	chessPiece piece = board[index];
        if (piece == null)
        {
            button.setIcon(null);
            return;
        }
        
        String imgTitle = (piece.getColor().toLowerCase()); // "white" or "black"
        
        char pieceName = piece.getName();
        switch (pieceName)
        {
            case 'p':
                imgTitle = imgTitle + "P.png";
                break;
            case 'r':
                imgTitle = imgTitle + "R.png";
                break;
            case 'n':
                imgTitle = imgTitle + "N.png";
                break;
            case 'b':
                imgTitle = imgTitle + "B.png";
                break;
            case 'q':
                imgTitle = imgTitle + "Q.png";
                break;
            case 'k':
                imgTitle = imgTitle + "K.png";
                break;
        }
        
        ClassLoader classLoader = Chess.class.getClassLoader();
        Icon icon = new ImageIcon(classLoader.getResource("client/"+imgTitle));
        button.setIcon(icon);
        
    }
    
    public void setIcons()
    {
        for (int i = 0; i < 64; i++)
        {
            setIcon(buttonGrid[i/8][i%8], i);
        }
    }
    
    /*private void printToGame(String s)  // TODO: change print location???
    {
        System.out.print(s);
    }
    
    private void printlnToGame(String s)    // TODO: change print location???
    {
        System.out.println(s);
    }*/
    
    public void receiveInput(JButton button)
    {
        char[] holder = button.getName().toCharArray();
        int coordRow = Character.getNumericValue(holder[0]);
        int coordCol = Character.getNumericValue(holder[1]);
        int tile = coordRow*8 + coordCol;   // 0 -- 63
        
        // TODO: check if it is this player's turn
        String player = (turn == 0 ? "WHITE" : "BLACK");
        
        // NO SELECTION YET: select piece to move
        if(selectedPiece < 0) {
        	if (board[tile] != null && board[tile].getColor().equals(player))
        	{
        		selectedPiece = tile;
        	}
        	return;
        }
        
        // ALREADY HAVE SELECTION: try to move already-selected piece
        if (!parseMove(selectedPiece, tile, player))
        {
            selectedPiece = -1;
            return; // return on failed move
        }
        
        // SEND MOVE
        char firstFile = (char) ((selectedPiece % 8) + 65);    // A -- H
        int firstRank = (selectedPiece / 8) + 1;               // 1 -- 8
        char secondFile = (char) ((tile % 8) + 65);            // A -- H
        int secondRank = (tile / 8) + 1;                       // 1 -- 8
        String move = "" + firstFile + "" + firstRank + " " + secondFile + "" + secondRank;
        myTurn = turn;
        sendMove("chess,"+move); // this is convoluted but I want to reuse old code due to time restraints and it shouldn't be *that* demanding
        enableButtons(false);
        selectedPiece = -1; // deselect piece, move already successful
    }
    
    /** Logistics to perform at the end of each turn, put into a single function to simplify sender/receiver end.
     * 
     * @param player the current player, "WHITE" or "BLACK".
     */
    private void endTurnLogistics(String player)
    {
        for (chessPiece piece : board)
        {
            if (piece != null && !piece.getColor().equals(player)) piece.setEnPassant(false);
            if (piece != null && piece.getName() == 'p' && (piece.getTile() > 55 || piece.getTile() < 8)) parsePromotion("Q", piece.getTile()); // auto-promote queen, for simplicity
        }
        if (detectCheckMate(board, (player.equals("WHITE") ? "BLACK" : "WHITE"))) winner = turn;    // current player has checkmated the other
        turn = 1 - turn;    // SWITCH SIDES
        setIcons();
    }
    
    /** Parse move function meant to be used on the current player's end in conjunction with receiveInput.
     * 
     * @param currTile the tile that has already been selected, which the piece is moving from.
     * @param nextTile the tile that had just been selected, that the piece is moving to.
     * @param player the current player, "WHITE" or "BLACK".
     * @return true if the move was parsed successfully.
     */
    private boolean parseMove(int currTile, int nextTile, String player)
    {
        if (board[currTile] == null || !board[currTile].getColor().equals(player)) return false;   // not the player's piece
        
        for (int move : board[currTile].getValidMoves(board))   // search valid moves for the requested move
        {
            if (move == nextTile)   // move exists for current piece
            {
                chessPiece[] potentialBoard = new chessPiece[64];
                for (int i = 0; i < 64; i++) if (board[i] != null) // copy the board state
                {
                    potentialBoard[i] = new chessPiece(board[i]);
                }
                movePiece(potentialBoard, currTile, nextTile);
                if (detectCheck(potentialBoard, (player.equals("WHITE") ? "WHITE" : "BLACK"))) return false;    // would put current player in check
                
                //movePiece(board, currTile, nextTile);   // valid move, actually commit to it
                
                return true;    // move was made successfully
            }
        }
        return false;   // move does not exist for current piece
    }
    
    /*public void playChess() // assuming the board in 'this' has already been created
    {
        Scanner type = new Scanner(System.in);
        String currPlayer = "WHITE";
        String command = "";
        
        while (!detectCheckMate(board, "WHITE") && !detectCheckMate(board, "BLACK"))
        {
            // MAKE MOVE
            do
            {
                printlnToGame('\u000C' + this.toString());  // print board
                printlnToGame("\n\n" + (currPlayer.equals("WHITE") ? "White" : "Black") + ", please enter a valid move (ex. \"E2 E4\").\n");
                if (detectCheck(board, currPlayer)) printlnToGame(currPlayer + " is in check.\n");
                command = type.nextLine();
            } while (!parseMove(command, currPlayer));  // while invalid move
            
            // PROMOTE PAWN (if necessary)
            int promotionTile = -1;
            for (int i = 0; i < 64; i++) if (((currPlayer.equals("WHITE") && i > 55) || currPlayer.equals("BLACK") && i < 8) && board[i] != null && board[i].getName() == 'p') promotionTile = i;
            if (promotionTile > -1) do
            {
                printlnToGame('\u000C' + this.toString());  // print board
                printlnToGame("\n\n" + (currPlayer.equals("WHITE") ? "White" : "BLACK") + ", please enter a valid promotion (\"B\", \"N\", \"R\", \"Q\").");
                command = type.nextLine();
            } while (!parsePromotion(command, promotionTile));  // while invalid promotion
            
            // REMOVE EN PASSANT FROM ENEMY PIECES
            for (chessPiece piece : board) if (piece != null && !piece.getColor().equals(currPlayer)) piece.setEnPassant(false);
            
            // SWAP PLAYERS
            currPlayer = (currPlayer.equals("WHITE") ? "BLACK" : "WHITE");
        }
        
        printlnToGame('\u000C' + this.toString());
        printlnToGame("\n\n" + (detectCheckMate(board, "WHITE") ? "Black" : "White") + " wins!");    // end game message
    }*/
    
    /** Parses a String instruction for the chess game and returns true (and performs the move) if the move can be made.
     *  This function is meant to be used on the non-player's end, in conjunction with receiveMove.
     * 
     * @param command move instruction starting and ending with a valid chess tile (A1 -- H8).
     * @param player the color of the player making this move, in all caps.
     * @return true if the move was successfully parsed and performed.
     */
    private boolean parseMove(String command, String player)
    {
        if (command.length() != 5) return false;    // invalid command length
        if (command.charAt(1) < '1' || command.charAt(1) > '8') return false;   // invalid rank for currTile
        if (command.charAt(4) < '1' || command.charAt(4) > '8') return false;   // invalid rank for nextTile
        
        int currTile;
        int nextTile;
        
        switch(command.charAt(0))
        {
            case 'A':
            case 'a':
                currTile =     (8 * (command.charAt(1) - 49));    // 8 for each rank, -48 for char conversion, -1 so it's 0--7 instead of 1--8
                break;
            case 'B':
            case 'b':
                currTile = 1 + (8 * (command.charAt(1) - 49));    // 1 for each file
                break;
            case 'C':
            case 'c':
                currTile = 2 + (8 * (command.charAt(1) - 49));
                break;
            case 'D':
            case 'd':
                currTile = 3 + (8 * (command.charAt(1) - 49));
                break;
            case 'E':
            case 'e':
                currTile = 4 + (8 * (command.charAt(1) - 49));
                break;
            case 'F':
            case 'f':
                currTile = 5 + (8 * (command.charAt(1) - 49));
                break;
            case 'G':
            case 'g':
                currTile = 6 + (8 * (command.charAt(1) - 49));
                break;
            case 'H':
            case 'h':
                currTile = 7 + (8 * (command.charAt(1) - 49));
                break;
            default:
                return false;   // invalid file
        }
        
        switch(command.charAt(3))
        {
            case 'A':
            case 'a':
                nextTile =     (8 * (command.charAt(4) - 49));    // 8 for each rank, -48 for char conversion
                break;
            case 'B':
            case 'b':
                nextTile = 1 + (8 * (command.charAt(4) - 49));    // 1 for each file
                break;
            case 'C':
            case 'c':
                nextTile = 2 + (8 * (command.charAt(4) - 49));
                break;
            case 'D':
            case 'd':
                nextTile = 3 + (8 * (command.charAt(4) - 49));
                break;
            case 'E':
            case 'e':
                nextTile = 4 + (8 * (command.charAt(4) - 49));
                break;
            case 'F':
            case 'f':
                nextTile = 5 + (8 * (command.charAt(4) - 49));
                break;
            case 'G':
            case 'g':
                nextTile = 6 + (8 * (command.charAt(4) - 49));
                break;
            case 'H':
            case 'h':
                nextTile = 7 + (8 * (command.charAt(4) - 49));
                break;
            default:
                return false;   // invalid file
        }
        
        if (board[currTile] == null || !board[currTile].getColor().equals(player)) return false;   // not the player's piece

        for (int move : board[currTile].getValidMoves(board))   // search valid moves for the requested move
        {
            if (move == nextTile)   // move exists for current piece
            {
                chessPiece[] potentialBoard = new chessPiece[64];
                for (int i = 0; i < 64; i++) if (board[i] != null) // copy the board state
                {
                    potentialBoard[i] = new chessPiece(board[i]);
                }
                movePiece(potentialBoard, currTile, nextTile);
                if (detectCheck(potentialBoard, (player.equals("WHITE") ? "WHITE" : "BLACK"))) return false;    // would put current player in check
                
                movePiece(board, currTile, nextTile);   // valid move, actually commit to it
                
                endTurnLogistics(player);
                
                return true;    // move was made successfully
            }
        }
        return false;   // move does not exist for current piece
    }
    
    /** Parses a String (character) instruction for promotion within the chess game, returning true on successful promotion.
     * 
     * @param command the String containing a single character labeling the piece to promote to.
     * @param promotionTile the tile where the promotion is to take place.
     * @return true if the promotion was successfully parsed and performed.
     */
    private boolean parsePromotion(String command, int promotionTile)
    {
        if (command.length() != 1) return false;    // not a char
        
        switch(command.charAt(0))
        {
            case 'B':
            case 'b':
                board[promotionTile].setName('b');
                return true;
            case 'N':
            case 'n':
                board[promotionTile].setName('n');
                return true;
            case 'R':
            case 'r':
                board[promotionTile].setName('r');
                return true;
            case 'Q':
            case 'q':
                board[promotionTile].setName('q');
                return true;
            default:
                return false;   // no bishop/knight/rook/queen requested
        }
    }
    
    /**
     * Resets the board to its initial state.
     */
    private void resetBoard()
    {
        for (int i = 8; i < 16; i++) board[i] = new chessPiece('p', i, "WHITE");    // START WHITE
        board[0] = new chessPiece('r', 0, "WHITE");
        board[1] = new chessPiece('n', 1, "WHITE");
        board[2] = new chessPiece('b', 2, "WHITE");
        board[3] = new chessPiece('q', 3, "WHITE");
        board[4] = new chessPiece('k', 4, "WHITE");
        board[5] = new chessPiece('b', 5, "WHITE");
        board[6] = new chessPiece('n', 6, "WHITE");
        board[7] = new chessPiece('r', 7, "WHITE"); // END WHITE
        
        for (int i = 16; i < 48; i++) board[i] = null;  // MIDDLE
        
        for (int i = 48; i < 56; i++) board[i] = new chessPiece('p', i, "BLACK");   // START BLACK
        board[56] = new chessPiece('r', 56, "BLACK");
        board[57] = new chessPiece('n', 57, "BLACK");
        board[58] = new chessPiece('b', 58, "BLACK");
        board[59] = new chessPiece('q', 59, "BLACK");
        board[60] = new chessPiece('k', 60, "BLACK");
        board[61] = new chessPiece('b', 61, "BLACK");
        board[62] = new chessPiece('n', 62, "BLACK");
        board[63] = new chessPiece('r', 63, "BLACK");   // END BLACK
    }
    
    /**
     * modifying currentBoard directly, moves a piece from currentTile to newTile.
     * clears currentTile
     * 
     * @param currentBoard the current state of the board
     * @param currentTile the current tile of the piece to be moved
     * @param newTile the tile for the piece to be moved to
     */
    private void movePiece(chessPiece[] currentBoard, int currentTile, int newTile)
    {
        //System.out.println("Moving " + currentTile + " to " + newTile); //TODO: delete DEBUG
        /*System.out.println("" + board[currentTile].getName());
        System.out.println("" + board[currentTile].getTile());
        System.out.println("" + (board[currentTile].getMovedYet() ? "True" : "False"));
        System.out.println("" + board[currentTile].getColor());*/
        /*char name = currentBoard[currentTile].getName();
        int tile = currentBoard[currentTile].getTile();
        String color = currentBoard[currentTile].getColor();*/
        
        // CASTLING - MOVING ROOK
        if (currentBoard[currentTile].getName() == 'k' && (currentTile - newTile == 2 || newTile - currentTile == 2))
        {
            switch(newTile)
            {
                case 2: // WHITE QUEENSIDE
                    movePiece(currentBoard, 0, 3);
                    break;
                case 6: // WHITE KINGSIDE
                    movePiece(currentBoard, 7, 5);
                    break;
                case 58:    // BLACK QUEENSIDE
                    movePiece(currentBoard, 56, 59);
                    break;
                case 62:    // BLACK KINGSIDE
                    movePiece(currentBoard, 63, 61);
            }
        }
        
        // EN PASSANT - EN PASSANT CAPTURE
        if (currentBoard[currentTile].getName() == 'p')
        {
            if (newTile == currentTile + 7 && currentBoard[currentTile-1] != null && currentBoard[currentTile-1].getEnPassant()) currentBoard[currentTile-1] = null;    // UP-LEFT
            if (newTile == currentTile + 9 && currentBoard[currentTile+1] != null && currentBoard[currentTile+1].getEnPassant()) currentBoard[currentTile+1] = null;    // UP-RIGHT
            if (newTile == currentTile - 7 && currentBoard[currentTile+1] != null && currentBoard[currentTile+1].getEnPassant()) currentBoard[currentTile+1] = null;    // DOWN-RIGHT
            if (newTile == currentTile - 9 && currentBoard[currentTile-1] != null && currentBoard[currentTile-1].getEnPassant()) currentBoard[currentTile+1] = null;    // DOWN-LEFT
        }
        
        // MOVE PIECE
        currentBoard[newTile] = new chessPiece(currentBoard[currentTile]);  // duplicates the piece to newTile
        currentBoard[newTile].setTile(newTile); // fixes the tile noted within the piece's chessPiece variable
        currentBoard[currentTile] = null;   // clears the old tile
        
        // EN PASSANT - MARK MOVING PAWN
        if (currentBoard[newTile] != null && currentBoard[newTile].getName() == 'p' && (currentTile - newTile == 16 || newTile - currentTile == 16)) currentBoard[newTile].setEnPassant(true);
    }
    
    /**
     * 
     * @param potentialBoard the state to determine a check
     * @param color the color of the king to detect in check
     * @return true if (color) king is in check, given a potential board state
     */
    private boolean detectCheck(chessPiece[] potentialBoard, String color)
    {
        for (int kingTile = 0; kingTile < 64; kingTile++) if (potentialBoard[kingTile] != null && potentialBoard[kingTile].getName() == 'k' && potentialBoard[kingTile].getColor().equals(color))  // found (color) King
        {
            for (int enemyTile = 0; enemyTile < 64; enemyTile++) if (potentialBoard[enemyTile] != null && !potentialBoard[enemyTile].getColor().equals(color))    // found enemy piece
            {
                for (int move : potentialBoard[enemyTile].getValidMoves(potentialBoard)) if (move == kingTile) return true;    // enemy piece can move to king
            }
            
            return false;   // did not find an enemy piece that could attack the king
        }
        
        return false;   // TODO: error message? this should NOT be reached...
    }
    
    /**
     * determines if a given player (color) can make any moves that would take them out of check.
     * this function assumes the player has already been determined to be in check.
     * 
     * @param potentialBoard the state to determine a checkmate
     * @param color the color of the king to detect having been checkmated
     * @return true if (color) has been checkmated, given a potential board state
     */
    private boolean detectCheckMate(chessPiece[] potentialBoard, String color)
    {
        for (int tile = 0; tile < 64; tile++) if (potentialBoard[tile] != null && potentialBoard[tile].getColor().equals(color))    // find every friendly piece
        {
            for (int move : potentialBoard[tile].getValidMoves(potentialBoard)) // every friendly piece --> every possible move
            {
                chessPiece[] evenMorePotentialBoard = new chessPiece[64];
                for (int i = 0; i < 64; i++) if (potentialBoard[i] != null) // copy the board state
                {
                    evenMorePotentialBoard[i] = new chessPiece(potentialBoard[i]);
                }
                movePiece(evenMorePotentialBoard, tile, move);
                if (!detectCheck(evenMorePotentialBoard, color)) return false;
                
                // in the long run, this should return false if there are any
                // possible moves that result in (color) out of check
            }
        }
        
        return true;    // (color) can make no saving moves with their pieces, thus checkmate
    }
    
    public String toString()    //TODO: fix this toString as well
    {
        String str = "\n";
        for (int i = 56; i > -1; i++)
        {
            if (board[i] == null) str += ". ";
            else str += board[i].toString();
            if (i%8 == 7)
            {
                str += "\n";
                i -= 16;
            }
        }
        str += "\n";
        
        return str;
    }
    
    public void setButtonGrid(JButton[][] grid) {
    	buttonGrid = grid;
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
    
    
}
