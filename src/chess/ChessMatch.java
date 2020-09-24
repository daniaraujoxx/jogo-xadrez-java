package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;

	private List<Piece> piecesOntheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	// metodo initialSetup = na hora que foir iniciada a partida
	// vai ser criado um tabuleiro (board) 8 por 8 e chama o initialSetup() e coloca
	// o R da classe rook na posi��o informada

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}

	public int getTurn() {
		return turn;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public boolean getCheck() {
		return check;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
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

	// essa funçao serve para que na program impimir as posições possiveis a partir
	// de uma posição de origem
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}

	// mover pecas
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		// convertendo essas duas posi��es (source, target) para posi��es da matriz
		Position source = sourcePosition.toPosition(); // posi��o de origem
		Position target = targetPosition.toPosition(); // posi��o de destino
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target); // makeMove realiza o movimento da pe�a

		if (testeCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("Voca nao pode se colocar em check");
		}

		check = (testeCheck(opponent(currentPlayer))) ? true : false; // expressao ternaria
		//se a jogada que eu fiz deixou meu oponente em checkmate o jogo vai ter que acabar
		if(testCkeckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else { //senao a partida continua
			nextTurn();

		}
		
		return (ChessPiece) capturedPiece; // tem que fazer dowcasting pq a pe�a capturada � do tipo piece
	}

	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source); // retirou pe�a que estava na posi��o de origem
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target); // remove a possivel peca que esteja na posi��o de destino e ela por padrao ser�
										// a peca capturada

		if (capturedPiece != null) {
			piecesOntheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return capturedPiece;

		// esse metodo remove a pe�a de origem (para move-la) e tamb�m pode remover a
		// pe�a de destino, como se o jogador tivesse capturado a pe�a do oponente
	}

	// desfazer movimento para nao poder se colocar em cheque
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		Piece p = board.removePiece(target); // tira a peca que mexeu la do destino
		board.placePiece(p, source); // devolve a peca para a posicao de origem

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOntheBoard.add(capturedPiece);
		}

	}

	private void validateSourcePosition(Position position) {
		if (!board.thereIsApice(position)) {
			throw new ChessException("Nao existe peca na posicao de origem!");
		}
		if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) { // se essa cor for diferente do jogador
																				// atual, entao eh uma peca do
																				// adversario
			throw new ChessException("Essa peca nao eh sua!");

		}
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("Nao existe movimentos possiveis para a peca escolhida");
		}
	}

	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) { // se pra peça de origem a posição de destino não é um
															// movimento possivel significa que voce não pode mexer pra
															// lá
			throw new ChessException("A peca escolhida nao pode se mover para a posicao de destino!");

		}

	}

	// troca de turno
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
		// se o jogador for branco então na proxima vai ser o jogador preto
	}

	// devolve o oponente de uma cor
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	// localiza o rei de uma determinada cor
	// varre as pecas do jogo para localizar o rei de determinada cor
	private ChessPiece king(Color color) { // procura na lista de pecas em jogos qual eh o rei dessa cor que foi passada
											// como argumento
		List<Piece> list = piecesOntheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		// toda peca x tal que seja da cor que foi passada como argumento
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece) p;
			}
		}
		throw new IllegalStateException("Nao existe o rei " + color + " no tabuleiro");

	}

	// testa se o rei da cor x esta em check
	private boolean testeCheck(Color color) {
		Position KingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOntheBoard.stream()
				.filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());
		// pra cada peca contida nessa lista do oponente, vai ter que testar se alguma
		// dessas pecas levam ate o rei
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[KingPosition.getRow()][KingPosition.getColumn()]) {
				return true;

			}

		}

		return false;
	}

	private boolean testCkeckMate(Color color) {
		if (!testeCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOntheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) { // se existir alguma peca p nessa lista que possua um moimento que tira do
								// check, retorna falso, se nao tiver nenhum movimento retira do check
			boolean[][] mat = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) { // vai percorrer as linhas da matriz
				for (int j = 0; j < board.getColumns(); j++) { // percorrer colunas
					if (mat[i][j]) { // essa posicao tira do check? testar
						Position source = ((ChessPiece) p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testeCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}

			}

		}

		return true;
	}

	// esse metodo recebe as coordenadas do xadrez
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOntheBoard.add(piece);
	}

	// metodo responsavel por iniciar a partida de xadrez, colocando as pe�as no
	// tabuleiro
	private void initialSetup() {
		placeNewPiece('h', 7, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE));

		placeNewPiece('b', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 8, new King(board, Color.BLACK));
	}
}