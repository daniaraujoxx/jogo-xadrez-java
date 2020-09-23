package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;

	// metodo initialSetup = na hora que foir iniciada a partida
	// vai ser criado um tabuleiro (board) 8 por 8 e chama o initialSetup() e coloca
	// o R da classe rook na posi��o informada

	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}
	
	//essa funçao serve para que na program impimir as posições possiveis a partir de uma posição de origem
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}

	// mover pe�as
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		// convertendo essas duas posi��es (source, target) para posi��es da matriz
		Position source = sourcePosition.toPosition(); // posi��o de origem
		Position target = targetPosition.toPosition(); // posi��o de destino
		validateSourcePosition(source);
		validateTargetPosition(source, target);

		Piece capturedPiece = makeMove(source, target); // makeMove realiza o movimento da pe�a
		return (ChessPiece) capturedPiece; // tem que fazer dowcasting pq a pe�a capturada � do tipo piece
	}

	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source); // retirou pe�a que estava na posi��o de origem
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target); // remove a possivel pe�a que esteja na posi��o de destino e ela por padr�o ser�
										// a pe�a capturada
		return capturedPiece;

		// esse metodo remove a pe�a de origem (para move-la) e tamb�m pode remover a
		// pe�a de destino, como se o jogador tivesse capturado a pe�a do oponente
	}

	private void validateSourcePosition(Position position) {
		if (!board.thereIsApice(position)) {
			throw new ChessException("N�o existe peca na posicao de origem!");
		}
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("Nao existe movimentos possiveis para a peca escolhida");
		}
	}

	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) { // se pra peça de origem a posição de destino não é um
															// movimento possivel significa que voce não pode mexer pra
															// lá
			throw new ChessException("A peca escolhida não pode se mover para a posicao de destino!");

		}

	}

	// esse metodo recebe as coordenadas do xadrez
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}

	// metodo responsavel por iniciar a partida de xadrez, colocando as pe�as no
	// tabuleiro
	private void initialSetup() {
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
		placeNewPiece('c', 2, new Rook(board, Color.WHITE));
		placeNewPiece('d', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new King(board, Color.WHITE));

		placeNewPiece('c', 7, new Rook(board, Color.BLACK));
		placeNewPiece('c', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}