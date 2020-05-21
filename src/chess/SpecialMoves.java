package chess;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import chess.chesspiece.Bishop;
import chess.chesspiece.ChessPiece;
import chess.chesspiece.ChessPieceColor;
import chess.chesspiece.King;
import chess.chesspiece.Pawn;
import chess.chesspiece.Rook;

public class SpecialMoves 
{
	private static final Map<Class<? extends ChessPiece>, ChangeChessBoard> PIECE_SPECIFIC_CHANGES = Map.of(King.class, new KingChanges(),
																										Rook.class, new RookChanges(),
																										Pawn.class, new PawnChanges());
	private static final ChangeChessBoard DEFAULT_CHANGES = (board, p, r1,c1,r2,c2, classType) -> {};
	
    static void makeSpecialChanges(ChessBoard board, ChessPiece chessPiece, int r1, int c1, int r2, int c2, 
    		Class<? extends ChessPiece> pawnPromotionClass)
    {
    	ChangeChessBoard changesToMake = PIECE_SPECIFIC_CHANGES.getOrDefault(chessPiece.getClass(), DEFAULT_CHANGES);
    	
    	changesToMake.change(board, chessPiece, r1, c1, r2, c2, pawnPromotionClass);
    }
    
   
    private static interface ChangeChessBoard{
    	void change(ChessBoard board, ChessPiece piece, int r1, int c1, int r2, int c2, Class<? extends ChessPiece> pawnPromotionClass);
    }
    
    private static class KingChanges implements ChangeChessBoard
    {

		@Override
		public void change(ChessBoard board, ChessPiece king, int r1, int c1, int r2, int c2,
				Class<? extends ChessPiece> pawnPromotionClass) {
			board.setKingPosition(king.getColor(), r2, c2);
		       
	        //this King can no longer be castled
	        ((King)(king)).setMoved(true);

	        
	        //handle castling
	        Map<Integer, Integer> rookMap = Map.of(2, ChessBoard.convert_to_square_num(r2,ChessBoard.classicChessBoardDimension-1), 
	        		-2, ChessBoard.convert_to_square_num(r2,0));
	        
	        Integer rookPosition = rookMap.get(c2-c1);
	        if(rookPosition!=null)
	        {
	        	int [] rookStartingPosition = ChessBoard.convert_square_num_to_coordinates(rookPosition);
	        	Rook rook= (Rook)board.removePiece(rookStartingPosition[0], rookStartingPosition[1]);
	        	rook.setMoved(true);
	        	
	        	int cDelta = (c2-c1)/2;
	        	
	        	board.setPiece(r1, c1+cDelta, rook);
	        }
		}	
    }
    
    private static class RookChanges implements ChangeChessBoard
    {
		@Override
		public void change(ChessBoard board, ChessPiece rook, int r1, int c1, int r2, int c2,
				Class<? extends ChessPiece> pawnPromotionClass) {
			((Rook)rook).setMoved(true);
		}
    }
    
    private static class PawnChanges implements ChangeChessBoard
    {

		@Override
		public void change(ChessBoard board, ChessPiece pawn, int r1, int c1, int r2, int c2,
				Class<? extends ChessPiece> pawnPromotionClass) {
			//prepare for future en passant

	        if(Math.abs(r2-r1)==2)
	            ((Pawn)(pawn)).setEn_passant(true);
	        
	        // pawn promotion
	        else if(r2%(ChessBoard.classicChessBoardDimension-1)==0)
	        {
	        	ChessPiece changedPiece = null;
				try {
					changedPiece = pawnPromotionClass.getDeclaredConstructor(ChessBoard.class, ChessPieceColor.class).newInstance(board, pawn.getColor());
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            board.setPiece(r1, c1, changedPiece);
	        }
	        //en passant happened
	        else if( (Math.abs(r2-r1) & Math.abs(c2-c1))==1 && board.getPiece(r2,c2)==null)
	        	board.removePiece(r1, c2);
		}
    }
}
