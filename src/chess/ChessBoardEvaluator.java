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
	private static final Map<Class<? extends ChessPiece>, Integer> materialMap = Map.of(King.class, 0, 
																						Pawn.class,1, 
																						Bishop.class,3,
																						Knight.class,3,
																						Rook.class,5,
																						Queen.class,9);

	private static final double [][] pawnEvalWhite =
		{
		        {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0},
		        {5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0},
		        {1.0,  1.0,  2.0,  3.0,  3.0,  2.0,  1.0,  1.0},
		        {0.5,  0.5,  1.0,  2.5,  2.5,  1.0,  0.5,  0.5},
		        {0.0,  0.0,  0.0,  2.0,  2.0,  0.0,  0.0,  0.0},
		        {0.5, -0.5, -1.0,  0.0,  0.0, -1.0, -0.5,  0.5},
		        {0.5,  1.0, 1.0,  -2.0, -2.0,  1.0,  1.0,  0.5},
		        {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0}
		};

		
	private static final double [][] knightEval =
		{
		        {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0},
		        {-4.0, -2.0,  0.0,  0.0,  0.0,  0.0, -2.0, -4.0},
		        {-3.0,  0.0,  1.0,  1.5,  1.5,  1.0,  0.0, -3.0},
		        {-3.0,  0.5,  1.5,  2.0,  2.0,  1.5,  0.5, -3.0},
		        {-3.0,  0.0,  1.5,  2.0,  2.0,  1.5,  0.0, -3.0},
		        {-3.0,  0.5,  1.0,  1.5,  1.5,  1.0,  0.5, -3.0},
		        {-4.0, -2.0,  0.0,  0.5,  0.5,  0.0, -2.0, -4.0},
		        {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0}
		};

	private static final double [][] bishopEvalWhite = {
			{ -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
			{-1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0},
			{ -1.0,  0.0,  0.5,  1.0,  1.0,  0.5,  0.0, -1.0},
			{ -1.0,  0.5,  0.5,  1.0,  1.0,  0.5,  0.5, -1.0},
			{ -1.0,  0.0,  1.0,  1.0,  1.0,  1.0,  0.0, -1.0},
			{-1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0, -1.0},
			{ -1.0,  0.5,  0.0,  0.0,  0.0,  0.0,  0.5, -1.0},
			{ -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0}
	};

		
	private static final double [][] rookEvalWhite = 
		{
			{  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0},
			{  0.5,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5},
			{ -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
			{ -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
			{ -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
			{ -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
			{ -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
			{  0.0,   0.0, 0.0,  0.5,  0.5,  0.0,  0.0,  0.0}
		};

		private static final double [][]  evalQueen =
			{
				{ -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0},
				{ -1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0},
				{ -1.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0},
				{ -0.5,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5},
				{  0.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5},
				{ -1.0,  0.5,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0},
				{ -1.0,  0.0,  0.5,  0.0,  0.0,  0.0,  0.0, -1.0},
				{ -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0}
			};

		private static final double [][]  kingEvalWhite = 
			{
				{ -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
				{ -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
				{ -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
				{ -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
				{ -2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
				{ -1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
				{  2.0,  2.0,  0.0,  0.0,  0.0,  0.0,  2.0,  2.0 },
				{  2.0,  3.0,  1.0,  0.0,  0.0,  1.0,  3.0,  2.0 }
			};
	private static final Map<Class<? extends ChessPiece>, double[][]> positionMap =
			Map.of(
					King.class, kingEvalWhite, 
					Pawn.class,pawnEvalWhite, 
					Bishop.class,bishopEvalWhite,
					Knight.class,knightEval,
					Rook.class,rookEvalWhite,
					Queen.class,evalQueen
				);
	public static int evaluateBoard(ChessBoard board, int depth)
	{
		int sign = turnToSign(board.getCurrentPlayer());
		int maxValue = 1000;
		if(board.checkmateHasOccurred())
			return sign*maxValue*(depth+1);
		if(board.stalemateHasOccurred())
			return sign*maxValue/2*(depth+1);
		
		Integer rating = board.getRating();
		if(rating!=null)
			return rating;
		
		int score = 0;
		for(Map.Entry<Integer, ChessPiece> entry: board.boardEntrySet())
		{
			int[] coordinates = ChessBoard.convert_square_num_to_coordinates(entry.getKey());
			ChessPiece chessPiece = entry.getValue();
			sign = turnToSign(chessPiece.getColor());
			int materialValue = materialMap.get(chessPiece.getClass());
			
			double [] [] positionTableForPiece = positionMap.get(chessPiece.getClass());
			
			int r = (coordinates[0]-(ChessBoard.classicChessBoardDimension-1)*chessPiece.getColor().ordinal())*(-2*chessPiece.getColor().ordinal()+1);
			int c = coordinates[1];
			score+=materialValue*10*sign+positionTableForPiece[r][c]*sign+board.successors().size()*sign;
		}
		//board.setStoredRating(score);
		return score;
	}
	
	public static int turnToSign(ChessPieceColor color)
	{
		return -2*color.ordinal()+1;
	}
}
