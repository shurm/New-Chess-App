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
	
	public ChessPieceColor getOtherColor()
	{
		ChessPieceColor[] allcolors = values();
		
		return allcolors[(this.ordinal()+1)%allcolors.length];
	}
}
