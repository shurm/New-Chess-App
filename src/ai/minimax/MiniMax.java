package ai.minimax;

import java.util.ArrayList;

public class MiniMax<T> 
{
	private static final int MAX =1000, MIN =-1000;
	private static final MaxMinFunction [] max_and_min_functions = {(a,b) -> Math.max(a,b), (a,b) -> Math.min(a,b)};

	private static final long INTERVAL_TO_WAIT = 1000;
	
	private Board<T> board;
	public MiniMax(Board<T> board) {
		this.board = board;
	}

	public T getBestMove()
	{
		long starttime = System.currentTimeMillis();
		MiniMaxNode<T> bestMove = new MiniMaxNode<>(0); 
		int depth = 1;
		
		while(true)
		{
			MiniMaxNode<T> result = alphaBetaMinimax(MIN, MAX, depth, board, starttime);
			if(result==null)
				break;
			//System.out.println("depth explored: "+result.move);
			bestMove = result;
			depth++;
		}
		
		System.out.println("depth explored: "+depth);
		
		return board.getMove(bestMove.move);
	}

	private MiniMaxNode<T> alphaBetaMinimax(int alpha, int beta, int depth, Board<T> current_board, long starttime)
	{
		//times up stop searching
		if((System.currentTimeMillis()-starttime)>=INTERVAL_TO_WAIT)
			return null;
			
		//hit a leaf node (evaluate the board)
		if(depth == 0 || current_board.game_over()) 
			return new MiniMaxNode<>(current_board.evaluate(depth));

		int [] alpha_beta = {alpha, beta};


		ArrayList<Board<T>> successors = current_board.successors();
		
		MiniMaxNode<T> bestMove = new MiniMaxNode<T>(new int[] {MIN, MAX}[current_board.get_current_turn()]);
		
		for(Board<T> successor : successors)
		{

			MiniMaxNode<T> bestMoveReturned = alphaBetaMinimax(alpha, beta, depth-1, successor, starttime);
			if(bestMoveReturned==null)
				return null;
			
			
			MaxMinFunction function_to_use = max_and_min_functions[current_board.get_current_turn()];

			int bestVal = function_to_use.operation(bestMove.rating, bestMoveReturned.rating);
			if(bestVal!=bestMove.rating)
			{
				bestMove.rating = bestVal;
				bestMove.move = successor;
			}

			//Set alpha or beta
			alpha_beta[current_board.get_current_turn()] = function_to_use.operation(alpha_beta[current_board.get_current_turn()], bestVal);

			alpha = alpha_beta[0];
			beta = alpha_beta[1];
			if(beta <= alpha)
				break;

		}
		//makes sure the best move gets tried first the next time around
		int i = successors.indexOf(bestMove.move);
		successors.set(i, successors.get(0));
		successors.set(0, bestMove.move);
		
		
		return bestMove;
	}  
	
	public static class MiniMaxNode<T>
	{
		int rating;
		Board<T> move;

		public MiniMaxNode(int rating)
		{
			this.rating=rating;
		}

		public String toString()
		{
			return "{ rating: "+rating+", move: "+move.toString()+"}";
		}
	}

	public interface MaxMinFunction {
		int operation(int a, int b); 
	}
}
