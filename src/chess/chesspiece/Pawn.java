package chess.chesspiece;

import java.util.ArrayList;

import chess.ChessBoard;

public class Pawn extends ChessPiece
{

    //variable which states whether this Pawn may be taken via en passant
    private boolean en_passant=false;

    /*
         getter and setter
     */
    public boolean isEn_passant() {
        return en_passant;
    }
    public void setEn_passant(boolean en_passant) {
        this.en_passant= en_passant;
    }


    public Pawn(ChessBoard board, ChessPieceColor type)
    {
        super(board,type,"p");
    }

    @Override
    public ArrayList<Integer> computeAttackingPositions(int r, int c)
    {
        ArrayList<Integer> newPoints=new ArrayList<>();
        
        int rowDelta = computeDirection();
        
        int [] array={-1,1};

        int nextRow = r+rowDelta;
        //diagonally takes enemy piece
        for(int cDelta : array)
        {
        	int c1 = c+cDelta;
        	ChessPiece chessPiece = getBoard().getPiece(nextRow, c1);
            
            if(chessPiece!=null && chessPiece.getColor()!=getColor())
                newPoints.add(ChessBoard.convert_to_square_num(nextRow, c1));
        }

        
       
        return newPoints;
    }

    @Override
    public ArrayList<Integer> computeValidMoves(int r, int c)
    {
    	ArrayList<Integer> result = computeAttackingPositions(r,c);
    	
    	int rowDelta = computeDirection();
        moveStraight(result,  r, c, rowDelta);
              
        int [] array={-1,1};

        int nextRow = r+rowDelta;
      //try en_passant
        for(int cDelta : array)
        {
        	int c1 = c+cDelta;
        	ChessPiece empty = getBoard().getPiece(nextRow, c1);
        	ChessPiece potentialPawn = getBoard().getPiece(r, c1);
            if(empty!=null || !(potentialPawn instanceof Pawn) || potentialPawn.getColor()==getColor())
                continue;
            
            if(((Pawn)(potentialPawn)).isEn_passant())
            	result.add(ChessBoard.convert_to_square_num(nextRow, c1));
        }
		return result;

    }
   

    private void moveStraight(ArrayList<Integer> newPoints,int r, int c, int rInc )
    {   
        int nextRow = r+rInc;
        if(getBoard().getPiece(nextRow, c)!=null)
        	return;
        
        newPoints.add(ChessBoard.convert_to_square_num(nextRow, c));

        int startingRow = computeStartingRow();
        nextRow = r+2*rInc;
        
        //try moving forward 2 squares
        if(r==startingRow && getBoard().getPiece(nextRow, c)==null)
        	newPoints.add(ChessBoard.convert_to_square_num(nextRow, c));

    }
    
    @Override
    public ChessPiece createDuplicate(ChessBoard newBoard) {
        Pawn pawn = new Pawn(newBoard, getColor());
        pawn.en_passant=this.en_passant;
        return pawn;
    }
    
    private int computeStartingRow()
    {
    	//white = 6
    	//black = 1
    	int n = getColor().ordinal();
    	
    	return (-1*(ChessBoard.classicChessBoardDimension-1))*(n-1) + 1*(n-1) + 1*(n);
    	
    }
    
    private int computeDirection()
    {
    	int n = getColor().ordinal();
    	
    	return 2*n-1;
    }
    
    @Override
    public String toStateString()
    {
    	 return toString()+booleanTag(en_passant);
    }
}
