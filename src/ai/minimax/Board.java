package ai.minimax;

import java.util.ArrayList;
import java.util.List;

public abstract class Board<T> 
{
	private ArrayList<Board<T>> successors = null;
	
	public abstract boolean game_over();

    public abstract void perform_move(T move);

    public abstract Board<T> copy();
           
    public abstract int evaluate(int depth);
    
    public abstract int get_current_turn();
    
    public abstract T getBestMove();
    
    protected abstract List<T> legal_moves();
    
    public final ArrayList<Board<T>> successors()
    {
    	if(successors!=null)
    		return successors;
    	
    	successors = recompute_successors();
    	return successors;
    }

	public abstract ArrayList<Board<T>> recompute_successors();

	protected abstract T getMove(Board<T> move);
}