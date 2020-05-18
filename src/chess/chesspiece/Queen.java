package chess.chesspiece;

import java.util.ArrayList;

import chess.ChessBoard;

public class Queen extends ChessPiece
{
    public Queen(ChessBoard board, ChessPieceColor type)
    {
        super(board,type,"Q");
    }

    @Override
    public ArrayList<Integer> generatePoints(int r, int c)
    {
    	ArrayList<Integer> result = ChessPiece.generateVerticalAndHortionzalMoves(getBoard(), getColor(), r, c);
    	ArrayList<Integer> result2 = ChessPiece.generateDiagonalMoves(getBoard(), getColor(), r, c);
    	
    	result.addAll(result2);
        return result;
    }

  

    @Override
    public ChessPiece createDuplicate(ChessBoard newBoard)
    {
        return new Queen(newBoard, getColor());
    }
}
