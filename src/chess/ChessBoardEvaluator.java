package chess;

import java.util.Map;

import chess.chesspiece.Bishop;
import chess.chesspiece.ChessPiece;
import chess.chesspiece.ChessPieceColor;
import chess.chesspiece.King;
import chess.chesspiece.Knight;
import chess.chesspiece.Pawn;
import chess.chesspiece.Queen;
import chess.chesspiece.Rook;

public class ChessBoardEvaluator
{
	private static final Map<Class<? extends ChessPiece>, Integer> materialMap = Map.of(King.class, 0, Pawn.class,1, Bishop.class,3, Knight.class,3,Rook.class,5,Queen.class,9);
	public static int evaluateBoard(ChessBoard board, int depth)
	{
		int sign = turnToSign(board.getCurrentPlayer());
		int maxValue = 1000;
		if(board.checkmateHasOccurred())
			return sign*maxValue*depth;
		if(board.stalemateHasOccurred())
			return sign*maxValue/2*depth;
		
		int score = 0;
		for(Map.Entry<Integer, ChessPiece> entry: board.boardEntrySet())
		{
			ChessPiece chessPiece = entry.getValue();
			sign = turnToSign(chessPiece.getColor());
			int materialValue = materialMap.get(chessPiece.getClass())*sign;
			score+=materialValue;
		}
		
		return score;
	}
	
	public static int turnToSign(ChessPieceColor color)
	{
		return -2*color.ordinal()+1;
	}
}
