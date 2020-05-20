package driver;

import java.util.Scanner;

import chess.ChessBoard;
import chess.ChessBoardFactory;
import chess.chesspiece.ChessPieceColor;

public class Driver {

	public static void main(String[] args) {
		
		ChessBoard board = ChessBoardFactory.createNewChessBoard();

		while(!board.game_over())
		{
			System.out.println(board.toString());
			System.out.println(board.getCurrentPlayer().name()+" to move:");
			Scanner keyboard = new Scanner(System.in);
			
			String input = keyboard.nextLine();
			input = input.trim();
			String [] encodedMove = input.split(" ");
			
			//board.p
			//System.out
			
			
		}
	}

}
