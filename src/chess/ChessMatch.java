package chess;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;	
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;

	
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

	public ChessPiece getEnPassantVulnerable() {
		return null;
	}

	public ChessPiece getPromoted() {
		return promoted;
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
		ChessPiece movedPiece = (ChessPiece) board.piece(target);

		//promocao especial
		promoted = null;
		if(movedPiece instanceof Pawn) {
			if((movedPiece.getColor() == Color.WHITE && target.getRow() == 0 || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7))){
				promoted = (ChessPiece)board.piece(target);
				promoted = replacePromotedPiece("Q"); //esta trocando o peao por uma peca mais poderosa
			}
		}
		
		check = (testeCheck(opponent(currentPlayer))) ? true : false; // expressao ternaria
		// se a jogada que eu fiz deixou meu oponente em checkmate o jogo vai ter que
		// acabar
		if (testCkeckMate(opponent(currentPlayer))) {
			checkMate = true;
		} else { // senao a partida continua
			nextTurn();
		}

		// movimento especial en passant
		if (movedPiece instanceof Pawn
				&& (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2))
			; // movimento inicial de peao com duas casas
		enPassantVulnerable = movedPiece;

		return (ChessPiece) capturedPiece; // tem que fazer dowcasting pq a pe�a capturada � do tipo piece
	}
	public ChessPiece replacePromotedPiece(String type) {
		if(promoted == null) {
			throw new IllegalStateException("Nao ha peca para ser promovida");
		}
		if(!type.equals("B") && !type.equals("C") && !type.equals("T") & !type.contentEquals("Q")) { //equals = pra comparar se um tipo eh igual a outro
			throw new InvalidParameterException("Valor invalido");
	}
	Position pos = promoted.getChessPosition().toPosition(); //removeu a peca que tava na posicao
	Piece p = board.removePiece(pos); //guardei na variavel p
	piecesOntheBoard.remove(p);
	
	ChessPiece newPiece = newPiece(type, promoted.getColor()); 
	board.placePiece(newPiece, pos);
	piecesOntheBoard.add(newPiece);
	
	return newPiece;
	
	}
	//metodo auxiliar para fazer a troca de peca
	private ChessPiece newPiece(String type, Color color) {
		if(type.equals("B")) return new Bishop(board, color);
		if(type.equals("C")) return new Knight(board, color);
		if(type.equals("Q")) return new Queen(board, color);
		return new Rook(board, color);

	}

	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece) board.removePiece(source); // retirou pe�a que estava na posi��o de origem
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target); // remove a possivel peca que esteja na posi��o de destino e ela por padrao ser�
										// a peca capturada

		if (capturedPiece != null) {
			piecesOntheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}

		// roque pequeno para a direita
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) { // significa que o rei andou duas casa
																					// para a direita, ou seja foi um
																					// roque pequeno
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3); // posicao de origem da torre
			Position targetT = new Position(source.getRow(), source.getColumn() + 1); // posicao de destino da torre
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount(); // incrementar a quantidade de movimentos
		}

		// roque grande move para esquerda
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) { // significa que o rei andou duas casa
																					// para a direita, ou seja foi um
																					// roque pequeno
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4); // posicao de origem da torre
			Position targetT = new Position(source.getRow(), source.getColumn() - 1); // posicao de destino da torre
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount(); // incrementar a quantidade de movimentos
		}

		// movimento especial en passant
		// so pode andar na diagonal numa casa vazia se for um en passant
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == null) { // o meu peao andou na diagonal e
																						// nao capturou peca? Foi en
																						// passant
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());
				} else {
					pawnPosition = new Position(target.getRow() - 1, target.getColumn()); //posicao da peca capturada
				}
				capturedPiece = board.removePiece(pawnPosition); //remove a peca
				capturedPieces.add(capturedPiece); //add ela a lista de pecas capturadas
				piecesOntheBoard.remove(capturedPiece);
				
			}

		}

		return capturedPiece;

		// esse metodo remove a pe�a de origem (para move-la) e tamb�m pode remover a
		// pe�a de destino, como se o jogador tivesse capturado a pe�a do oponente
	}

	// desfazer movimento para nao poder se colocar em cheque
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) board.removePiece(target); // tira a peca que mexeu la do destino
		p.decreaseMoveCount();
		board.placePiece(p, source); // devolve a peca para a posicao de origem

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOntheBoard.add(capturedPiece);
		}

		// roque grande
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {

			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}

		// roque grande move para esquerda
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		//
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) { 							
				ChessPiece pawn = (ChessPiece)board.removePiece(target);
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Position(3, target.getColumn());
				} else {
					pawnPosition = new Position(4, target.getColumn()); //posicao da peca capturada
				}
				board.placePiece(pawn, pawnPosition);
				
				
			}

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
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this /* alto referencia */));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK, this /* alto referencia */));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this /* alto referencia */));
		placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
	}
}