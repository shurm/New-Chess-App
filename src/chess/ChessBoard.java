package chess;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ai.minimax.Board;
import ai.minimax.MiniMax;
import chess.chesspiece.Bishop;
import chess.chesspiece.ChessPiece;
import chess.chesspiece.ChessPieceColor;
import chess.chesspiece.King;
import chess.chesspiece.Knight;
import chess.chesspiece.Queen;
import chess.chesspiece.Rook;

/**
 * public class which simulates a real life ChessBoard
 *
 * @author Michael Shur
 *
 */

public class ChessBoard extends Board<Integer> 
{
	public static final int classicChessBoardDimension=8;

	private final Map<Integer, ChessPiece> chessBoardMap = new HashMap<>();

	private final Map<ChessPieceColor, Integer> kingLocationMap = Map.of(ChessPieceColor.BLACK, convert_to_square_num(0,4),
			ChessPieceColor.WHITE, convert_to_square_num(7,4));
	
	private final Collection<ChessPiece> pawnsThatCanEnPassant= new ArrayList<>();
	private ChessPieceColor currentTurn = ChessPieceColor.WHITE;
	
	public boolean isValidMove(int r1, int c1, int r2, int c2)
	{
		int currentPosition = convert_to_square_num(r1,c1), intendedPosition = convert_to_square_num(r2,c2);
		//bounds check
		if(intendedPosition<0)
			return false;
		ChessPiece pieceAtCurrentPosition = chessBoardMap.get(currentPosition);

		//checks if a piece is at your original point
		if(pieceAtCurrentPosition == null )
			return false;

		//if white player tries to move a black piece or vice-versa
		if(pieceAtCurrentPosition.getColor()!=currentTurn)
			return false;

		ChessPiece pieceAtIntendedPosition = chessBoardMap.get(intendedPosition);

		//check if another one the player's pieces already occupies the intended position 
		if(pieceAtIntendedPosition!=null && pieceAtIntendedPosition.getColor()==currentTurn)
			return false;

		return pieceAtCurrentPosition.isValidMove(r1, c1, r2, c2);
	}

	//returns the desired String representation of the chessboard
	public String toString()
	{
		return ChessBoardStringify.convertBoardToString(this);
	}


	/**
	 * Method for retrieving a chess piece that is on the ChessBoard, at Point P
	 * as a parameter.
	 *
	 * @param p
	 * @return piece at Point p
	 */
	public ChessPiece getPiece(int r, int c)
	{
		int squareNum = ChessBoard.convert_to_square_num(r, c);
		return chessBoardMap.get(squareNum);
	}

	public void setPiece(int r, int c, ChessPiece piece)
	{
		int squareNum = ChessBoard.convert_to_square_num(r, c);
		if(squareNum<0)
			return;
		chessBoardMap.put(squareNum, piece);
	}


	/**
	 * Method for clearing en passant of all Pawns on the board, by
	 * setting en passant to false.
	 *
	 */
	private void clearEnPassant()
	{
		pawnsThatCanEnPassant.clear();
	}

	/**
	 * Method for checking whether or not a stalemate has occurred
	 *
	 *
	 * @param king Position of the king that is in possible jeopardy
	 * @param enemyType the type the enemy pieces
	 * @return true if statemate has occurred
	 * @return false otherwise
	 */
	public boolean stalemateHasOccurred(Point king, String enemyType)
	{
		if(checkHasOccurred(king, enemyType))
			return false;

		if(BlockCheck.areAbleToBlockCheck(this,king, enemyType))
			return false;

		return true;
	}

	/**
	 * Method for checking whether or not checkmate has occurred
	 *
	 *
	 * @param king Position of the king that is in possible jeopardy
	 * @param enemyType the type the enemy pieces
	 * @return true if checkmate has occurred
	 * @return false otherwise
	 */
	public boolean checkmateHasOccurred(Point king, String enemyType)
	{
		if(!checkHasOccurred(king, enemyType))
			return false;

		if(BlockCheck.areAbleToBlockCheck(this,king, enemyType))
			return false;

		return true;
	}


	/**
	 * Method for checking whether or not a check has occurred
	 *
	 *
	 * @param king Position of the king that is in possible jeopardy
	 * @param enemyType the type the enemy pieces
	 * @return true if check has occurred
	 * @return false otherwise
	 */
	public boolean checkHasOccurred(Point king, String enemyType)
	{
		return InCheck.areEnemiesAttackingPoint(this, enemyType,king);
	}


	public boolean hasEnemyPiece(Point p, ChessPiece piece)
	{
		if(getPiece(p)==null)
			return false;
		if(getPiece(p).getType().equals(piece.getType()))
			return false;
		return true;
	}

	/**
	 * Checks if the move you just made puts your own king in check
	 *
	 * @param turn
	 * @param p1
	 * @param p2
	 * @return true if your king is now in check
	 * @return false otherwise
	 */
	public boolean kingIsInCheck( String turn, Point p1, Point p2)
	{
		ChessBoard newBoard=ChessBoardFactory.createNewChessBoard(this,p1,p2);

		Point kingLocation;
		String enemyType;
		if(turn.equals("White"))
		{
			kingLocation=newBoard.whiteKingLocation;
			enemyType=ChessPieceTypes.black;
		}
		else
		{
			kingLocation=newBoard.blackKingLocation;
			enemyType=ChessPieceTypes.white;
		}

		if(newBoard.checkHasOccurred(kingLocation, enemyType))
			return true;

		return false;
	}

	public Point getKingLocation(String type)
	{
		if(type.equals(ChessPieceTypes.white))
			return new Point(whiteKingLocation.getRow(),whiteKingLocation.getColumn());
		return new Point(blackKingLocation.getRow(),blackKingLocation.getColumn());
	}

	@Override
	public boolean game_over() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public List<Integer> legalmoves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void perform_move(Integer move) {
		// TODO Auto-generated method stub

		int squareNum = move%100;

		ChessPiece pieceThatIsGoingToMove= chessBoardMap.remove(squareNum);

		clearEnPassant();

		squareNum = (move/100)%100;

		chessBoardMap.put(squareNum, pieceThatIsGoingToMove);

		char c=' ';
		SpecialMoves.makeSpecialChanges(this, pieceThatIsGoingToMove, p1, p2, c);
	}

	@Override
	public Board<Integer> copy() {
		
		ChessBoard copy = new ChessBoard();
		for(Map.Entry<Integer, ChessPiece> entry: chessBoardMap.entrySet())
			copy.chessBoardMap.put(entry.getKey(), entry.getValue().createDuplicate(copy));
		
		copy.kingLocationMap.putAll(kingLocationMap);
		copy.currentTurn = currentTurn;
		return copy;
	}

	@Override
	public int evaluate(int depth) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_current_turn() 
	{
		return currentTurn.ordinal();
	}

	private String[] chess_board_id_String()
	{
		String [] result = new String[classicChessBoardDimension*classicChessBoardDimension];
		for(int i =0;i<result.length;i++)
			result[i] = "";
		for(chessBoardMap.entrySet())
		for(int r=0;r<classicChessBoardDimension;r++)
		{
			for(int c=0;c<classicChessBoardDimension;c++)
			{

				result[i] = chessBoard[r][c].
			}
		}
	}



	public boolean equals(Object o)
	{
		if(!(o instanceof ChessBoard) || o==null)
			return false;

		ChessBoard other = (ChessBoard)o;

		return chessBoardMap.equals(other.chessBoardMap);
	}

	@Override
	public Integer getBestMove() 
	{
		MiniMax<Integer> ai = new MiniMax<>(this);
		Integer move = ai.getBestMove();
		return move;
	}

	public Set<Map.Entry<Integer, ChessPiece>> boardEntrySet() 
	{	
		return chessBoardMap.entrySet();
	}
	
	public ChessPiece removePiece(int r, int c) {
		int square_num = convert_to_square_num(r,c);
		return chessBoardMap.remove(square_num);
	}
	
	public void setKingPosition(ChessPieceColor color, int r, int c)
	{
		int square_num = convert_to_square_num(r,c);
		kingLocationMap.put(color, square_num);
	}
	
	public static int [] convert_square_num_to_coordinates(int square_num)
	{
		int [] result = {square_num/classicChessBoardDimension, square_num%classicChessBoardDimension};
		return result;
	}
	
	public static int convert_to_square_num(int r, int c)
	{
		int largeNum = classicChessBoardDimension*classicChessBoardDimension;

		int squareId = (r%classicChessBoardDimension)*classicChessBoardDimension + c%classicChessBoardDimension - Math.abs(c)/classicChessBoardDimension*largeNum 
				- Math.abs(r)/classicChessBoardDimension*largeNum + ((int)Math.signum(r+1)-1)*largeNum + ((int)Math.signum(c+1)-1)*largeNum;
		return squareId;
	}
	
}