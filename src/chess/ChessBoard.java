package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import ai.minimax.Board;
import ai.minimax.MiniMax;
import chess.chesspiece.Bishop;
import chess.chesspiece.ChessPiece;
import chess.chesspiece.ChessPieceColor;
import chess.chesspiece.King;
import chess.chesspiece.Knight;
import chess.chesspiece.Pawn;
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
	
	private final Map<ChessPieceColor, Set<Integer>> colorToLocationsMap = initializeColorToLocationsMap();

	private final Map<ChessPieceColor, Integer> kingLocationMap = new HashMap<>();;
	
	private ChessPieceColor currentTurn = ChessPieceColor.WHITE;

	private final Map<ChessPieceColor, Boolean> inCheckMap = new HashMap<>();

	
	private Map<ChessPieceColor, Set<Integer>> initializeColorToLocationsMap() {
		
		Map<ChessPieceColor, Set<Integer>> result = new HashMap<>();
		for(ChessPieceColor c : ChessPieceColor.values())
			result.put(c, new HashSet<>());
		return result;
	}

	
	//so chessboard can only be instantiated by the ChessBoardFactory 
	ChessBoard()
	{
		kingLocationMap.put(ChessPieceColor.BLACK, convert_to_square_num(0,4));
		kingLocationMap.put(ChessPieceColor.WHITE, convert_to_square_num(7,4));
	}
	
	void populateColorLocationsMap()
	{
		for(Map.Entry<Integer, ChessPiece> entry : chessBoardMap.entrySet())
			colorToLocationsMap.get(entry.getValue().getColor()).add(entry.getKey());
	}
	
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

		Integer move = ChessBoard.convert_square_nums_to_move(currentPosition, intendedPosition);

		return legal_moves().contains(move);
	}

	

	//returns the desired String representation of the chessboard
	public String toString()
	{
		return ChessBoardStringify.convertBoardToString(this);
	}


	/**
	 * Method for retrieving a chess piece that is on the ChessBoard, at a specified row and column
	 *
	 * @param r the row, c the column 
	 * @return piece
	 */
	public ChessPiece getPiece(int r, int c)
	{
		int squareNum = ChessBoard.convert_to_square_num(r, c);
		return chessBoardMap.get(squareNum);
	}

	
	public void setPiece(int r, int c, ChessPiece piece)
	{
		int squareNum = ChessBoard.convert_to_square_num(r, c);
		chessBoardMap.put(squareNum, piece);
		
	}


	/**
	 * Method for clearing en passant of all Pawns on the board, by
	 * setting en passant to false.
	 *
	 */
	private void clearEnPassant()
	{
		Set<Integer> locations = colorToLocationsMap.get(currentTurn);
		for(int location : locations)
		{
			ChessPiece chesspiece = chessBoardMap.get(location);
			if(chesspiece instanceof Pawn)
			{
				((Pawn)(chesspiece)).setEn_passant(false);
			}
		}
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
	public boolean stalemateHasOccurred()
	{
		boolean inCheck = kingIsInCheck(currentTurn);
		if(inCheck)
			return false;
		ArrayList<Board<Integer>> successors = successors();
		return successors.isEmpty();
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
	public boolean checkmateHasOccurred()
	{
		boolean inCheck = kingIsInCheck(currentTurn);
		if(!inCheck)
			return false;
		ArrayList<Board<Integer>> successors = successors();
		return successors.isEmpty();
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
	public boolean kingIsInCheck(ChessPieceColor turn)
	{
		Boolean inCheck = inCheckMap.get(turn);
		if(inCheck!=null)
			return inCheck;
		
		int[] kingLocation = getKingLocation(turn);
		
		ChessPieceColor opponentColor = turn.getOtherColor();
		
		ChessPiece [] pieces = {new Pawn(this, turn), new King(this, turn, true), new Queen(this, turn), new Rook(this, turn), new Bishop(this, turn), new Knight(this, turn)};
		
		for(ChessPiece enemy : pieces)
		{
			ArrayList<Integer> destinations = enemy.computeAttackingPositions(kingLocation[0], kingLocation[1]);
			for(Integer position: destinations)
			{
				ChessPiece realPiece = chessBoardMap.get(position);
				if(realPiece !=null && realPiece.getClass().equals(enemy.getClass()) && realPiece.getColor().equals(opponentColor))
				{
					inCheckMap.put(turn, true);
					return true;
				}
			}
		}
		inCheckMap.put(turn, false);
		return false;	
	}

	public int [] getKingLocation(ChessPieceColor color)
	{
		return convert_square_num_to_coordinates(kingLocationMap.get(color));
	}

	@Override
	public boolean game_over() {
		return checkmateHasOccurred() || stalemateHasOccurred();
	}



	@Override
	public void perform_move(Integer move) {
		// TODO Auto-generated method stub

		int[] square_nums = convert_move_to_square_nums(move);
		int from = square_nums[0];
		int to = square_nums[1];
		
		performMove(from, to);
		
	}

	


	@Override
	public Board<Integer> copy() {
		
		ChessBoard copy = new ChessBoard();
		for(Map.Entry<Integer, ChessPiece> entry: chessBoardMap.entrySet())
			copy.chessBoardMap.put(entry.getKey(), entry.getValue().createDuplicate(copy));
		
		for(Entry<ChessPieceColor, Set<Integer>> entry: colorToLocationsMap.entrySet())
			copy.colorToLocationsMap.get(entry.getKey()).addAll(entry.getValue());
		
		copy.kingLocationMap.putAll(kingLocationMap);
		copy.currentTurn = currentTurn;
		return copy;
	}

	@Override
	public int evaluate(int depth) {
		
		return ChessBoardEvaluator.evaluateBoard(this, depth);
	}

	@Override
	public int get_current_turn() 
	{
		return currentTurn.ordinal();
	}

	@Override
	public ArrayList<Board<Integer>> recompute_successors() {
		ArrayList<Board<Integer>> successors = new ArrayList<>();
		List<Integer> legal_moves1 = legal_moves();
		if(!legal_moves1.isEmpty() && legal_moves1.get(0)==130)
		{
			legal_moves1.get(0);
		}
		List<Integer> legal_moves = legal_moves();
		
		for(Integer legal_move : legal_moves)
		{
			ChessBoard copy = (ChessBoard) copy();
			copy.perform_move(legal_move);
			
			if(copy.kingIsInCheck(ChessPieceColor.values()[this.get_current_turn()]))
				continue;
			
			successors.add(copy);
		}
		
		return successors;
	}

	@Override
	protected List<Integer> legal_moves() {
		Set<Integer> locationsOfMoveablePieces = colorToLocationsMap.get(currentTurn);
		List<Integer> legalMoves = new ArrayList<>();
		
		for(Integer squareNum : locationsOfMoveablePieces)
		{
			ChessPiece chessPiece = chessBoardMap.get(squareNum);
			int[] coordinates = convert_square_num_to_coordinates(squareNum);
			List<Integer> validDestinations = chessPiece.computeValidMoves(coordinates[0], coordinates[1]);
			
			List<Integer> validMoves = validDestinations.stream().map( to -> convert_square_nums_to_move(squareNum,to) ).collect( Collectors.toList() );
			
			legalMoves.addAll(validMoves);	
		}
		return legalMoves;
	}
	
	private String[] chess_board_id_String()
	{
		String [] result = new String[classicChessBoardDimension*classicChessBoardDimension+1];
		for(int i =0;i<result.length;i++)
			result[i] = "";
		for(Map.Entry<Integer, ChessPiece> entry : chessBoardMap.entrySet())
			result[entry.getKey()] = entry.getValue().toStateString();
		
		result[result.length-1] = currentTurn.getShorthand();
		return result;
	}

	@Override
	public int hashCode() {
		String [] boardIdRep = chess_board_id_String();
		
	    return Arrays.hashCode(boardIdRep);
	}
	

	public boolean equals(Object o)
	{
		if(!(o instanceof ChessBoard))
			return false;

		ChessBoard other = (ChessBoard)o;

		return other.get_current_turn() == get_current_turn() && chessBoardMap.equals(other.chessBoardMap);
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
	
	public void performMove(Integer from, Integer to)
	{
		//updates the locations
		
		ChessPiece pieceThatIsGoingToMove= chessBoardMap.get(from);
		int [] fromPosition = convert_square_num_to_coordinates(from), toPosition = convert_square_num_to_coordinates(to);
		
		SpecialMoves.makeSpecialChanges(this, pieceThatIsGoingToMove, fromPosition[0], fromPosition[1], toPosition[0],toPosition[1], Queen.class);
	
		for(Collection<Integer> locationSet : colorToLocationsMap.values())
			locationSet.remove(from);
		colorToLocationsMap.get(currentTurn).add(to);
		
		ChessPiece pieceToMove = chessBoardMap.remove(from);
		chessBoardMap.put(to, pieceToMove);

		
		clearEnPassant();
		
		changeTurn();
		
		inCheckMap.clear();
		
		resetSuccessors();
	}
	
	private void changeTurn()
	{
		currentTurn = currentTurn.getOtherColor();
	}
	
	public ChessPieceColor getCurrentPlayer()
	{
		return currentTurn;
	}
	
	@Override
	protected Integer getMove(Board<Integer> successor) 
	{
		Set<Integer> locationsOfMoveablePieces = colorToLocationsMap.get(currentTurn);
		
		for(Integer squareNum : locationsOfMoveablePieces)
		{
			ChessPiece chessPiece = chessBoardMap.get(squareNum);
			int[] coordinates = convert_square_num_to_coordinates(squareNum);
			List<Integer> validDestinations = chessPiece.computeValidMoves(coordinates[0], coordinates[1]);
			
			List<Integer> validMoves = validDestinations.stream().map( to -> convert_square_nums_to_move(squareNum,to) ).collect( Collectors.toList() );
			
			for(Integer move : validMoves)
			{
				Board<Integer> copy = copy();
				copy.perform_move(move);
				
				if(this.kingIsInCheck(ChessPieceColor.values()[this.get_current_turn()]))
					continue;
				
				if(copy.equals(successor))
					return move;
			}
		}
		return null;
	}
	
	public static int [] convert_move_to_square_nums(int move)
	{
		int [] result = {(move/100)%100, move%100};
		return result;
	}
	
	public static int convert_square_nums_to_move(int from, int to)
	{
		int result = from*100+to;
		return result;
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