package ai.minimax;

import java.util.List;

public abstract class Board<T> 
{
	public abstract boolean game_over();

    public abstract List<T> legalmoves();

    public abstract void perform_move(T move);

    public abstract Board<T> copy();
           
    public abstract int evaluate(int depth);
    
    public abstract int get_current_turn();
    
    public abstract T getBestMove();
}