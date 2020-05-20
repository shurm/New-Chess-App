package chess;

import java.util.Map;

import chess.chesspiece.ChessPiece;

public class ChessBoardStringify {

	public static String convertBoardToString(ChessBoard board)
	{
		String[][] result= new String[ChessBoard.classicChessBoardDimension+1][ChessBoard.classicChessBoardDimension+1];

		Map<Integer, String> stringMap = Map.of(0,"  ", 1, "##");

		for(int r=0;r<ChessBoard.classicChessBoardDimension;r++)
		{
			for(int c=0;c<ChessBoard.classicChessBoardDimension;c++)
			{
				int key = (r%2)^(c%2);
				String square = stringMap.get(key);
				result[r][c] = square;
			}
			result[r][ChessBoard.classicChessBoardDimension] = (ChessBoard.classicChessBoardDimension-r)+"\n";
		}
		for(int c=0;c<ChessBoard.classicChessBoardDimension;c++)
			result[ChessBoard.classicChessBoardDimension][c] = " "+(char)('a'+c);
		
		result[ChessBoard.classicChessBoardDimension][ChessBoard.classicChessBoardDimension] = "";

		for(Map.Entry<Integer, ChessPiece> entry:board.boardEntrySet())
		{
			int squareNum = entry.getKey();
			int r = squareNum/ChessBoard.classicChessBoardDimension, c = squareNum%ChessBoard.classicChessBoardDimension;
			
			ChessPiece chesspiece = entry.getValue();
			result[r][c] = chesspiece.toString();
		}
		
		StringBuilder returnString = new StringBuilder();
		for(String[] row: result)
			returnString.append(String.join(" ", row));
		
		return returnString.toString();
	}
}
