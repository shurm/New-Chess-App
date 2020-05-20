package chess.chesspiece;

import java.util.ArrayList;
import java.util.Map;

import chess.ChessBoard;

public abstract class ChessPiece {

    private ChessPieceColor color;

    private ChessBoard board;

    private String classSymbol;

    /**
     * Protected constructor for ChessPiece
     * @param board
     * @param type
     */
    protected ChessPiece(ChessBoard board, ChessPieceColor color, String classSymbol)
    {
        this.color=color;
        this.setBoard(board);
        this.classSymbol = classSymbol;
    }

    /**
     * abstract method that should be overridden by every class that extends EmptyChessPiece
     * @return a list of Points, the chess piece should be capable of attacking each of these positions
     */
    public abstract ArrayList<Integer> computeAttackingPositions(int r, int c);

    public ArrayList<Integer> computeValidMoves(int r, int c)
    {
    	return computeAttackingPositions(r, c);
    }

    /**
     * template method
     *
     * determines if this chess piece is able to move validly from point p1 to point p2
     *
     * @param p1
     * @param p2
     * @return true if this chess piece is able to move validly from point p1 to point p2
     * @return false otherwise
     */
    public boolean isValidMove(int r1, int c1, int r2, int c2)
    {
        ArrayList<Integer> pointsPieceIsAbleToGoTo=this.computeAttackingPositions(r1, c1);

        int targetSquareNum = ChessBoard.convert_to_square_num(r2, c2);
        for(Integer validPosition : pointsPieceIsAbleToGoTo)
        {
            if(validPosition == targetSquareNum)
                return true;
        }
        return false;
    }

    /**
     * abstract method that should be overridden by every class that extends EmptyChessPiece
     *
     * Creates a new Chess Piece
     *
     * the values of this piece's global variables should be copied into the new piece
     *
     * @return a list of Points, the chess piece should be capable of going to each one of these Points
     */
    public abstract ChessPiece createDuplicate(ChessBoard newBoard);


    /**
     *
     * @return a String representation of the chess piece
     */
    public String toString()
    {
    	 return color.getShorthand()+classSymbol;
    }

    public String toStateString()
    {
    	 return color.getShorthand()+classSymbol;
    }
    
	public ChessPieceColor getColor() {
		return color;
	}

	public ChessBoard getBoard() {
		return board;
	}

	public void setBoard(ChessBoard board) {
		this.board = board;
	}
	
	public boolean equals(Object o)
	{
		if(!(o instanceof ChessPiece))
			return false;
		ChessPiece other = (ChessPiece)o;
		return color.equals(other.color) && other.getClass().equals(this.getClass());
	}
	
	public static ArrayList<Integer> generateVerticalAndHortionzalMoves(ChessBoard board, ChessPieceColor color, int r, int c)
	{
		ArrayList<Integer> newPoints = new ArrayList<>();
		int[] directions = {1,-1};
		for(int direction : directions)
			generateLinearPoints(direction, 0, newPoints, board, color, r, c);
		
		for(int direction : directions)
			generateLinearPoints(0, direction, newPoints, board, color, r, c);
		return newPoints;
	}
	
	public static ArrayList<Integer> generateDiagonalMoves(ChessBoard board, ChessPieceColor color, int r, int c)
	{
		ArrayList<Integer> newPoints = new ArrayList<>();
		int[] directions = {1,-1};
		for(int rDirection : directions)
			for(int cDirection : directions)
				generateLinearPoints(rDirection, cDirection, newPoints, board, color, r, c);
		
		return newPoints;
	}
	
	private static void generateLinearPoints(int rInc, int cInc, ArrayList<Integer> newPoints, ChessBoard board, ChessPieceColor color, int r, int c)
    {
		while(true)
		{
			r+=rInc;
			c+=cInc;
			if(!checkBoundsAndAdd(newPoints, board, color, r, c))
				break;
			
			int squareLocation = ChessBoard.convert_to_square_num(r, c);
			newPoints.add(squareLocation);
		}
    }
	
	public static boolean checkBoundsAndAdd(ArrayList<Integer> newPoints, ChessBoard board, ChessPieceColor color, int r, int c)
	{
		int squareLocation = ChessBoard.convert_to_square_num(r, c);
		//if intended position is out of bounds
		if(squareLocation<0)
			return false;
		ChessPiece piece = board.getPiece(r,c);
		if(piece!=null)
		{
			//if the piece at new destination is different from the piece that wants to move
			if(piece.getColor() != color)
				newPoints.add(squareLocation);
			return false;
		}
		return true;
	}
	
	private static final Map<Boolean, String> booleanTagMap = Map.of(false, "", true,"*");
	public static String booleanTag(boolean b)
	{
		return booleanTagMap.get(b);
	}
}
