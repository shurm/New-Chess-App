package chess.chesspiece;

import java.util.ArrayList;

import chess.ChessBoard;

public class Knight extends ChessPiece
{
    public Knight(ChessBoard board, ChessPieceColor type)
    {
        super(board,type, "N");
    }



    //A knight can travel 1 square vertically and 2 squares horizontally or visa-versa
    @Override
    public ArrayList<Integer> computeAttackingPositions(int r, int c)
    {
        ArrayList<Integer> newPoints= new ArrayList<>();

        int [] m1={1,-1};

        int [] m2={2,-2};

        knightHelper(newPoints,m1,m2, r,c);

        knightHelper(newPoints,m2,m1, r,c);

        return newPoints;
    }

    private void knightHelper(ArrayList<Integer> newPoints, int[] rDirections, int[] cDirections, int r, int c)
    {
    	for(int rDirection : rDirections)
			for(int cDirection : cDirections)
				ChessPiece.checkBoundsAndAdd(newPoints, getBoard(), getColor(), r+rDirection, c+cDirection);
		
    }

    
    @Override
    public ChessPiece createDuplicate(ChessBoard newBoard) 
    {
        return new Knight(newBoard, getColor());
    }
    
   
}
