package chess.chesspiece;

import java.util.ArrayList;

import chess.ChessBoard;

public class Pawn extends ChessPiece{

    //variable which states whether this Pawn may be taken via en passant
    private boolean en_passant=false;

    /*
         getter and setter
     */
    public boolean isEn_passant() {
        return en_passant;
    }
    public void setEn_passant(boolean en_passant) {
        this.en_passant= en_passant;
    }


    public Pawn(ChessBoard board, ChessPieceColor type)
    {
        super(board,type,"p");
    }

    @Override
    public ArrayList<Integer> generatePoints(int r, int c)
    {
        ArrayList<Integer> newPoints=new ArrayList<>();
        if(isBlack())
        {
            pawnMoves(newPoints, p, 1, 1);
        }
        else
        {
            pawnMoves(newPoints, p, 6, -1);
        }
        return newPoints;
    }


    private void pawnMoves(ArrayList<Integer> newPoints, Point p, int startingRow,int rInc)
    {
        moveStraight(newPoints,  p,  startingRow, rInc);

        Point newPoint;
        int [] array={-1,1};

        //diagonally takes enemy piece
        for(int a=0;a<array.length;a++)
        {
            newPoint=new Point(p.getRow()+rInc,p.getColumn()+array[a]);
            if(!getBoard().pointOutOfBounds(newPoint) && getBoard().hasEnemyPiece(newPoint,this))
                newPoints.add(newPoint);
        }

        //try en_passant
        for(int a=0;a<array.length;a++)
        {
            Point p1=new Point(p.getRow(),p.getColumn()+array[a]);
            Point p2=new Point(p.getRow()+rInc,p.getColumn()+array[a]);
            if(!getBoard().pointOutOfBounds(p1) && getBoard().hasEnemyPiece(p1, this) && getBoard().getPiece(p1) instanceof Pawn && ((Pawn)getBoard().getPiece(p1)).isEn_passant())
                newPoints.add(p2);
        }

    }

    private void moveStraight(ArrayList<Integer> newPoints,Point p,int startingRow, int rInc )
    {
        Point newPoint;

        //try moving forward 1 square
        newPoint=new Point(p.getRow()+rInc,p.getColumn());
        if(getBoard().pointOutOfBounds(newPoint) || getBoard().objectIsOnChessBoard(newPoint))
            return;

        newPoints.add(newPoint);

        //try moving forward 2 squares
        if(p.getRow()==startingRow)
        {
            newPoint=new Point(startingRow+2*rInc,p.getColumn());

            if(!getBoard().objectIsOnChessBoard(newPoint))
                newPoints.add(newPoint);
        }
    }
    @Override
    public ChessPiece createDuplicate(ChessBoard newBoard) {
        Pawn pawn = new Pawn(newBoard, getColor());
        pawn.en_passant=this.en_passant;
        return pawn;
    }
}
