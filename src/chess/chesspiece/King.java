package chess.chesspiece;

import java.util.ArrayList;

import chess.ChessBoard;

public class King extends ChessPiece
{
	//Variable which tells if King has been moved before, because if it has then it cannot perform castling
    private boolean moved=false;

    /*
         getter and setter
     */
    public boolean hasBeenMoved()
    {
        return moved;
    }
    public void setMoved(boolean moved)
    {
        this.moved=moved;
    }

    public King(ChessBoard board, ChessPieceColor type)
    {
        this(board, type, false);
    }

    public King(ChessBoard board, ChessPieceColor type, boolean b) {
    	super(board, type, "K");
    	setMoved(b);
	}
   
    @Override
    public ArrayList<Integer> computeAttackingPositions(int r, int c)
    {
        ArrayList<Integer> newPoints = new ArrayList<>();
        
        //up and down
		int[] directions = {1,-1};
		for(int direction : directions)
			ChessPiece.checkBoundsAndAdd(newPoints, getBoard(), getColor(), r+direction, c);
		
		//left and right
		for(int direction : directions)
			ChessPiece.checkBoundsAndAdd(newPoints, getBoard(), getColor(), r, c+direction);

		//Diagonal
		for(int rDirection : directions)
			for(int cDirection : directions)
				ChessPiece.checkBoundsAndAdd(newPoints,  getBoard(), getColor(), r+rDirection, c+cDirection);
		
		
        return newPoints;
    }
    @Override
    public ArrayList<Integer> computeValidMoves(int r, int c)
    {
    	ArrayList<Integer> result = computeAttackingPositions(r,c);
    	
    	//if the king was moved before or if it is currently in check we cant castle
    	if(moved || getBoard().kingIsInCheck(getColor()))
    		return result;
    	   	
    	//try to castle on the left side
    	if(passesCastlingConditions(r,c,0,-1))
    		result.add(ChessBoard.convert_to_square_num(r, c-2));
    	
    	//try to castle on the right side
    	if(passesCastlingConditions(r,c,ChessBoard.classicChessBoardDimension-1,1))
    		result.add(ChessBoard.convert_to_square_num(r, c+2));
    	
        //tryToCastle(newPoints, p);

		return result;

    }
    
	private boolean passesCastlingConditions(int kingRow, int kingCol, int rookCol, int direction) 
	{
		//checks if rook has been moved
		ChessPiece possibleRook = getBoard().getPiece(kingRow, rookCol);
		if(!(possibleRook instanceof Rook))
			return false;
		Rook rook = (Rook)(possibleRook);
		if(rook.hasBeenMoved())
			return false;
		
		//checks if the squares inbetween are empty
		for(int c = kingCol+direction;c!=rookCol;c+=direction)
			if(getBoard().getPiece(kingRow, c)!=null)
				return false;
		
		//the squares the king passes over cannot be in check
		int kingDestinationCol = kingCol+2*direction;
		for(int c = kingCol+direction;c!=kingDestinationCol+direction;c+=direction)
		{
			ChessBoard copy = (ChessBoard) getBoard().copy();
			ChessPiece king = copy.removePiece(kingRow, kingCol);
			copy.setPiece(kingRow, c, king);
			copy.setKingPosition(getColor(), kingRow, c);
			
			if(copy.kingIsInCheck(king.getColor()))
				return false;
		}
			
		
		return true;
	}
	
	@Override
	public ChessPiece createDuplicate(ChessBoard newBoard) {
		King king =new King(newBoard, getColor());
        king.moved=moved;
        return king;
	}
	
	@Override
	public String toStateString()
    {
    	 return toString()+booleanTag(moved);
    }
}
