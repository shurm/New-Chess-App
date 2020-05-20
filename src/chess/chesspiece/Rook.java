package chess.chesspiece;

import java.util.ArrayList;

import chess.ChessBoard;

public class Rook extends ChessPiece{
	   //checks if rook piece has been moved, because if it has it cannot castle with the King
    private boolean moved = false;

    /*
        getter and setter
     */
    public boolean hasBeenMoved()
    {
        return moved;
    }

    public Rook(ChessBoard board, ChessPieceColor type)
    {
        super(board,type, "R");
    }


    @Override
    public ArrayList<Integer> computeAttackingPositions(int r, int c)
    {
    	ArrayList<Integer> result = ChessPiece.generateVerticalAndHortionzalMoves(getBoard(), getColor(), r, c);
		return result;
    }
  

    @Override
    public ChessPiece createDuplicate(ChessBoard newBoard)
    {
        Rook rook = new Rook(newBoard, getColor());
        rook.moved=moved;
        return rook;
    }

	public void setMoved(boolean b) {
		moved = true;
	}
	
	@Override
    public String toStateString()
    {
    	 return toString()+booleanTag(moved);
    }
}
