package chess.chesspiece;

import java.util.ArrayList;

import chess.ChessBoard;

public class Bishop extends ChessPiece
{
    public Bishop(ChessBoard board, ChessPieceColor type)
    {
        super(board,type,"B");
    }

    @Override
    public ArrayList<Integer> generatePoints(int r, int c)
    {
    	ArrayList<Integer> result = ChessPiece.generateDiagonalMoves(getBoard(), getColor(), r, c);
		return result;
    }


    @Override
    public ChessPiece createDuplicate(ChessBoard newBoard)
    {
        return new Bishop(newBoard, this.getColor());
    }
}
