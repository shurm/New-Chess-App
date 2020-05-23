package ai.minimax;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class Board<T> 
{
	private ArrayList<Board<T>> successors = null;
	private Integer rating = null;

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

    protected void resetSuccessors() {
    	successors = null;
	}
    
    protected void setSuccessors(Board<T> board) {
    	successors = board.successors;
	}
    
    protected ArrayList<Board<T>> getSuccessors() {
    	return successors;
	}
    
    protected Board<T> successorsContains(Board<T> board) 
    {
    	if(successors==null)
    		return null;
    	int index = successors.indexOf(board);
    	if(index<0)
    		return null;
    	return successors.get(index);
	}
    
	public abstract ArrayList<Board<T>> recompute_successors();

	protected abstract T getMove(Board<T> move);

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}
	
	public static final Comparator<Board> ASCENDING_ORDER = new Comparator<Board>() {

		@Override
		public int compare(Board o1, Board o2) {
			return Integer.compare(o1.getRating(), o2.getRating());
		}
	}; 
	
	public static final Comparator<Board> DESCENDING_ORDER = new Comparator<Board>() {

		@Override
		public int compare(Board o1, Board o2) {
			return Integer.compare(o2.getRating(), o1.getRating());
		}
	}; 
	
}