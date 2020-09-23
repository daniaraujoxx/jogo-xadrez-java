package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	
	//metodo initialSetup = na hora que foir iniciada a partida
	//vai ser criado um tabuleiro (board) 8 por 8 e chama o  initialSetup() e coloca o R da classe rook na posição informada

	public ChessMatch() {
		board = new Board(8, 8);
		 initialSetup();
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}
	
	//mover peças
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		//convertendo essas duas posições (source, target) para posições da matriz
		Position source = sourcePosition.toPosition(); //posição de origem
		Position target = targetPosition.toPosition(); //posição de destino
		validateSourcePosition(source);
		Piece capturedPiece = makeMove(source, target); //makeMove realiza o movimento da peça
		return (ChessPiece)capturedPiece; //tem que fazer dowcasting pq a peça capturada é do tipo piece
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source); //retirou peça que estava na posição de origem
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target); // remove a possivel peça que esteja na posição de destino e ela por padrão será a peça capturada
		return capturedPiece;
		
		//esse metodo remove a peça de origem (para move-la) e também pode remover a peça de destino, como se o jogador tivesse capturado a peça do oponente
	}
	
	
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsApice(position)) {
			throw new ChessException("Não existe peca na posicao de origem!");
		}
	}
	
	
	
	
	
	//esse metodo recebe as coordenadas do xadrez
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}
	//metodo responsavel por iniciar a partida de xadrez, colocando as peças no tabuleiro
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