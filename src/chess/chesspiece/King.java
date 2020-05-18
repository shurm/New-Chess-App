package chess.chesspiece;

import java.util.ArrayList;

import chess.ChessBoard;

public class King extends ChessPiece{
	//Variable which tells if King has been moved before, because if it has then it cannot perform castling
    private boolean moved=false;

    /*
         getter and setter
     */
    public boolean hasBeenMoved()
    {
        return moved;
    }
    public void setMoved(boolean moved)
    {
        this.moved=moved;
    }

    public King(ChessBoard board, ChessPieceColor type)
    {
        super(board, type, "K");
    }

    private void tryToCastle(ArrayList<Point> newPoints,Point p1)
    {
        //if King has been moved before we cant castle
        if(hasBeenMoved())
            return;

        if(this.isBlack())
        {
            //Log.v("tag","right rook");
            //are we castling with Rook on the right side
            if(areCastlingConditionsGood(new Point(0,7)))
                castlingHelper(newPoints,p1,new Point(0,6), 1, ChessPieceTypes.white);

           // Log.v("tag","left rook");
            //are we castling with Rook on the left side
            if(areCastlingConditionsGood(new Point(0,0)))
                castlingHelper(newPoints,p1,new Point(0,2), -1, ChessPieceTypes.white);
        }
        else
        {
           // Log.v("tag","right rook");
            //are we castling with Rook on the right side
            if(areCastlingConditionsGood(new Point(7,7)))
                castlingHelper(newPoints,p1,new Point(7,6), 1, ChessPieceTypes.black);

           // Log.v("tag","left rook");
            //are we castling with Rook on the left side
            if(areCastlingConditionsGood(new Point(7,0)))
                castlingHelper(newPoints,p1,new Point(7,2), -1, ChessPieceTypes.black);
        }

    }

    private void castlingHelper(ArrayList<Point> newPoints,Point p1,Point p2, int inc, String enemyType)
    {
        if(!kingCrossingCheck(p1, p2,  inc, enemyType))
            return;

        if(emptySquaresCheck(p1,  inc))
            newPoints.add(p2);
    }

    private boolean emptySquaresCheck(Point p1, int inc) {
        int squaresInBetween;
        if(inc==1)
        {
            squaresInBetween=2;
        }
        else
        {
            squaresInBetween=3;
        }

        //checks if the squares Between the King and the Rook are unoccupied
        Point p=new Point(p1.getRow(),p1.getColumn()+inc);
        for(int a=0;a<squaresInBetween;a++,p.setColumn(p.getColumn()+inc) )
        {
            if(getBoard().getPiece(p)!=null)
            {
                //.v("tag","There is a piece in between the rook and king");
                return false;
            }
        }
        return true;
    }

    private boolean kingCrossingCheck(Point p1,Point p2, int inc, String enemyType)
    {
        //checks if the King every crosses a Point that is "in check"
        for(Point p=new Point(p1.getRow(),p1.getColumn());true;p.setColumn(p.getColumn()+inc))
        {
            if(InCheck.areEnemiesAttackingPoint(getBoard(), enemyType, p))
            {
                //Log.v("tag","King crosses in check square.");
                return false;
            }
            if(p.equals(p2))
                break;
        }
        return true;
    }

    // if the Rook was moved before.
    private boolean areCastlingConditionsGood(Point rookPosition)
    {
        Point rook=rookPosition;
        if(getBoard().getPiece(rook)==null || !(getBoard().getPiece(rook) instanceof Rook)
                || ((Rook)getBoard().getPiece(rook)).hasBeenMoved()) {
            //Log.v("tag","Rook test failed");
            return false;
        }

        return true;
    }

    @Override
    public ArrayList<Integer> generatePoints(int r, int c)
    {
        ArrayList<Integer> newPoints = new ArrayList<>();
        
		int[] directions = {1,-1};
		for(int direction : directions)
			ChessPiece.checkBoundsAndAdd(newPoints, getBoard(), getColor(), r+direction, c);
		
		for(int direction : directions)
			ChessPiece.checkBoundsAndAdd(newPoints, getBoard(), getColor(), r, c+direction);

		for(int rDirection : directions)
			for(int cDirection : directions)
				ChessPiece.checkBoundsAndAdd(newPoints,  getBoard(), getColor(), r+rDirection, c+cDirection);
		
        tryToCastle(newPoints, p);

        return newPoints;
    }
   
    
	@Override
	public ChessPiece createDuplicate(ChessBoard newBoard) {
		 King king =new King(newBoard, getColor());
        king.moved=moved;
        return king;
	}
}
