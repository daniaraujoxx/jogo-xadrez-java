package boardgame;

public class Board {

	private int rows;
	private int columns;
	private Piece[][] pieces;

	public Board(int rows, int columns) {
		if(rows < 1 || columns < 1) {
			throw new BoardException("Erro ao criar tabuleiro, é necessário pelo menos uma linha e uma coluna");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	
	public int getColumns() {
		return columns;
	}

	public Piece piece(int row, int column) {
		if(!positionExists(row, column)) {
			throw new BoardException("Posição invalida!");
		}
		return pieces[row][column];
	}

	public Piece piece(Position position) {
		if(!positionExists(position)) {
			throw new BoardException("Posição invalida!");
		}
		return pieces[position.getRow()][position.getColumn()];
	}

	public void placePiece(Piece piece, Position position) {
		if(thereIsApice(position)) {
			throw new BoardException("Já existe uma peça na posição: " + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		// pegando a matriz na posição dada e atribuir a ela a peça informada
		piece.position = position;
	}

	//metodo auxiliar
	private boolean positionExists(int row, int column) {
		return row >= 0 && row < rows && column >= 0 && column < columns;
	}
	
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}
	
	//testar se tem uma peça na posição informada
	public boolean thereIsApice(Position position) {
		if(!positionExists(position)) {
			throw new BoardException("Posição invalida!");
		}
		return piece(position) != null;
		// piece(position) esta declarado acima e irá retornar a peça da matriz em x posição, se essa peça for
		//diferente de nulo significa que tem uma peça nessa posição.
	}
}
