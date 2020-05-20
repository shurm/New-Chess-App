package chess;

import chess.chesspiece.Bishop;
import chess.chesspiece.ChessPieceColor;
import chess.chesspiece.King;
import chess.chesspiece.Knight;
import chess.chesspiece.Pawn;
import chess.chesspiece.Queen;
import chess.chesspiece.Rook;

/**
 * This is the ChessBoardFactory class it is used to create ChessBoard Objects
 *
 * This class's main purpose was to modularize/ divide up the code,
 * also prevents the ChessBoard.java file from being super long
 *
 * @author Michael Shur
 *
 */
public class ChessBoardFactory
{
    /**
     * @return a new ChessBoard containing chess pieces in the starting positions
     */
    public static ChessBoard createNewChessBoard()
    {
        ChessBoard newBoard = new ChessBoard();

        ChessPieceColor[] allcolors = ChessPieceColor.values();
        for(ChessPieceColor color: allcolors)
        	generatePiecesForAPlayer(newBoard, color);

        newBoard.populateColorLocationsMap();
        
        return newBoard;
    }

    private static void generatePiecesForAPlayer(ChessBoard newBoard, ChessPieceColor color)
    {
        generate8Pawns(newBoard,color);
        generateRooks(newBoard,color);
        generateKnights(newBoard,color);
        generateBishops(newBoard,color);
        generateQueen(newBoard,color);
        generateKing(newBoard,color);
    }

    private static void generateKing(ChessBoard newBoard, ChessPieceColor color)
    {
        int rowNumber=getStartingRow(color);
        newBoard.setPiece(rowNumber, 4, new King(newBoard, color));
    }

    private static void generateQueen(ChessBoard newBoard, ChessPieceColor color)
    {
        int rowNumber=getStartingRow(color);
        newBoard.setPiece(rowNumber, 3, new Queen(newBoard, color));
    }

    private static void generateBishops(ChessBoard newBoard, ChessPieceColor color)
    {
        int rowNumber=getStartingRow(color), d = 2;
        newBoard.setPiece(rowNumber, d, new Bishop(newBoard, color));
        newBoard.setPiece(rowNumber, ChessBoard.classicChessBoardDimension-1-d, new Bishop(newBoard, color));
    }

    private static void generateKnights(ChessBoard newBoard, ChessPieceColor color)
    {
        int rowNumber=getStartingRow(color), d = 1;
        newBoard.setPiece(rowNumber, d, new Knight(newBoard, color));
        newBoard.setPiece(rowNumber, ChessBoard.classicChessBoardDimension-1-d, new Knight(newBoard, color));
    }

    private static void generateRooks(ChessBoard newBoard, ChessPieceColor color)
    {
        int rowNumber=getStartingRow(color), d = 0;
        newBoard.setPiece(rowNumber, d, new Rook(newBoard, color));
        newBoard.setPiece(rowNumber, ChessBoard.classicChessBoardDimension-1-d, new Rook(newBoard, color));
    }

    private static void generate8Pawns(ChessBoard newBoard, ChessPieceColor color)
    {
        int rowNumber=getStartingRow(color)+2*color.ordinal()-1;

        for(int c=0;c<ChessBoard.classicChessBoardDimension;c++)
        	newBoard.setPiece(rowNumber, c, new Pawn(newBoard, color));
        
    }
    
    private static int getStartingRow(ChessPieceColor color)
    {
        return (color.getOtherColor().ordinal())*(ChessBoard.classicChessBoardDimension-1);
    }
}