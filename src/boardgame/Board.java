package boardgame;

public class Board {
	private int rows;
	private int columns;
	private Piece [][] pieces;
	public Board(int rowns, int columns) {
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}
	
	

}
