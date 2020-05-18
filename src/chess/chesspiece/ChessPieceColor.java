package chess.chesspiece;

public enum ChessPieceColor
{
	WHITE("w"),BLACK("b");
	
	private String shorthand;
	
	private ChessPieceColor(String shorthand)
	{
		this.shorthand = shorthand;
	}

	public String getShorthand() {
		return shorthand;
	}	
}
