package boardgame;

public abstract class Piece {

	protected Position position;
	private Board board;
	
	public Piece(Board board) {
		this.board = board;
		position = null;
	}

	protected Board getBoard() {
		return board;
	}

	public abstract boolean[][] possibleMoves();
	//metodo concreto que está utilizando o abstrato
	public boolean possibleMove(Position position) { //testar se a peça pode mover para uma determinada posição
		return possibleMoves()[position.getRow()][position.getColumn()];
		
	}
	
	//essa operação vai chamar o metodo abstrato possibleMove que vai retornar uma matriz de booleano
	//essa matriz será varrida pra saber se há pelo menos uma posição na matrizque seja verdadeira
	public boolean isThereAnyPossibleMove() {
		boolean[][] mat = possibleMoves();
		for (int i=0; i<mat.length; i++) {
			for(int j=0; j<mat.length; j++) {
				if(mat[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
}
