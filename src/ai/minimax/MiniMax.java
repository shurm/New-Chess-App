package ai.minimax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MiniMax<T> 
{
	//maximum number of successors a board should have (limit the game tree and speed up search)
	private static final int MAX_SUCCESSORS = 5;
	
	private static final int MAX =1000, MIN =-1000;
	private static final MaxMinFunction [] max_and_min_functions = {(a,b) -> Math.max(a,b), (a,b) -> Math.min(a,b)};

	private static final Comparator [] COMPARATORS = {Board.DESCENDING_ORDER,Board.ASCENDING_ORDER};
	private static final long INTERVAL_TO_WAIT = 1000;
	
	private Board<T> board;
	public MiniMax(Board<T> board) {
		this.board = board;
	}

	public T getBestMove()
	{
		
		long starttime = System.currentTimeMillis();
		Integer bestMove = null; 
		int depth = 1;
		
		while(true)
		{
			Integer result = alphaBetaMinimax(MIN, MAX, depth, board, starttime);
			if(result==null)
				break;
			//System.out.println("depth explored: "+result.move);
			bestMove = result;
			depth++;
		}
		
		System.out.println("depth explored: "+depth);
		
		return board.getMove(board.getSuccessors().get(0));
	}

	private Integer alphaBetaMinimax(int alpha, int beta, int depth, Board<T> current_board, long starttime)
	{
		//times up stop searching
		if((System.currentTimeMillis()-starttime)>=INTERVAL_TO_WAIT)
			return null;
			
		//hit a leaf node (evaluate the board)
		if(depth == 0 || current_board.game_over()) 
			return current_board.evaluate(depth);
		

		int [] alpha_beta = {alpha, beta};


		ArrayList<Board<T>> successors = current_board.successors();
		
		
		int rating = new int[] {MIN, MAX}[current_board.get_current_turn()];
		current_board.setRating(rating);
		
		int i;
		for(i =0;i<successors.size() && beta > alpha;i++)
		{
			Board<T> successor = successors.get(i);
			Integer bestMoveReturned = alphaBetaMinimax(alpha, beta, depth-1, successor, starttime);
			if(bestMoveReturned==null)
				return null;
			
			successor.setRating(bestMoveReturned);
			MaxMinFunction function_to_use = max_and_min_functions[current_board.get_current_turn()];

			int bestVal = function_to_use.operation(current_board.getRating(), successor.getRating());
			if(bestVal!=current_board.getRating())
			{
				current_board.setRating(bestVal);
			}

			//Set alpha or beta
			alpha_beta[current_board.get_current_turn()] = function_to_use.operation(alpha_beta[current_board.get_current_turn()], bestVal);

			alpha = alpha_beta[0];
			beta = alpha_beta[1];

		}
		
		//remove successors that were pruned
		i = successors.size() - i;
		while (i>0)
		{
			successors.remove(successors.size()-1);
			i--;
		}
		
		
		Comparator c = COMPARATORS[current_board.get_current_turn()];
		Collections.sort(successors, c);
		
		if(successors.size()>MAX_SUCCESSORS)
			successors.subList(MAX_SUCCESSORS, successors.size()).clear();
		
		
		return current_board.getRating();
	}  
	
	

	public interface MaxMinFunction {
		int operation(int a, int b); 
	}
}
