package boardgame;

public class Board {

	private int rows;
	private int columns;
	private Piece[][] pieces;

	public Board(int rows, int columns) {
		if(rows < 1 || columns < 1) {
			throw new BoardException("Erro ao criar tabuleiro, eh necessario pelo menos uma linha e uma coluna");
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
			throw new BoardException("Posicao invalida!");
		}
		return pieces[row][column];
	}

	public Piece piece(Position position) {
		if(!positionExists(position)) {
			throw new BoardException("Posicao invalida!");
		}
		return pieces[position.getRow()][position.getColumn()];
	}

	public void placePiece(Piece piece, Position position) {
		if(thereIsApice(position)) {
			throw new BoardException("Ja existe uma peca na posicao: " + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		// pegando a matriz na posi��o dada e atribuir a ela a pe�a informada
		piece.position = position;
	}
	
	public Piece removePiece(Position position) {
		if(!positionExists(position)) {
			throw new BoardException("Posicao invalida!");

		}
		if(piece(position) == null) {
			return null;
		}
		Piece aux = piece(position);
		aux.position = null; //retira pe�a do tabuleiro
		pieces[position.getRow()][position.getColumn()] = null;
		return aux;
	}

	//metodo auxiliar
	private boolean positionExists(int row, int column) {
		return row >= 0 && row < rows && column >= 0 && column < columns;
	}
	
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}
	
	//testar se tem uma pe�a na posi��o informada
	public boolean thereIsApice(Position position) {
		if(!positionExists(position)) {
			throw new BoardException("Posicao invalida!");
		}
		return piece(position) != null;
		// piece(position) esta declarado acima e ir� retornar a pe�a da matriz em x posi��o, se essa pe�a for
		//diferente de nulo significa que tem uma pe�a nessa posi��o.
	}
}
