package driver;

import java.util.Scanner;

import chess.ChessBoard;
import chess.ChessBoardFactory;

public class Driver {

	public static void main(String[] args) {
		
		ChessBoard board = ChessBoardFactory.createNewChessBoard();
		Scanner keyboard = new Scanner(System.in);
		
		while(!board.game_over())
		{
			System.out.println(board.toString());
			System.out.println(board.getCurrentPlayer().name()+" to move:");
			
			
			
			String input = keyboard.nextLine();
			input = input.trim().toLowerCase();
			int r1 = '8'- input.charAt(1), c1 = input.charAt(0)-'a', r2 = '8'-input.charAt(4), c2 = input.charAt(3)-'a'; 
			//board.p
			//System.out
			if(!board.isValidMove(r1, c1, r2, c2))
			{
				System.out.println("Error, invalid move");
				continue;
			}
			int from = ChessBoard.convert_to_square_num(r1, c1), to = ChessBoard.convert_to_square_num(r2, c2);
			
			board.performMove(from, to);
			
			Integer move = board.getBestMove();
			board.perform_move(move);
		}
		keyboard.close();
	}
}
