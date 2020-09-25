package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {
//o peao so pode se mover pra frente uma casa e na primeira vez que le for se mexer ele pode mover duas casas
	// se tiver uma peca adversaria na diagonal, ele pode se mover na diagonal tb

	private ChessMatch chessMatch;
	
	public Pawn(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

		Position p = new Position(0, 0);

		if (getColor() == Color.WHITE) { // se a peca for branca o peao se move pra cima
			p.setValues(position.getRow() - 1, position.getColumn());
			// se a posicao de uma linha acima do peao branco existir e estiver vazia, ele
			// pode ir para cima
			if (getBoard().positionExists(p) && !getBoard().thereIsApice(p)) { // significa que o peao pode ir para essa
																				// posicao
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() - 2, position.getColumn());
			Position p2 = new Position(position.getRow() - 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsApice(p) && getBoard().positionExists(p2)
					&& !getBoard().thereIsApice(p2) && getMoveCount() == 0) { // se for a primeira jogada pode ir para
																				// duas casas acima
				mat[p.getRow()][p.getColumn()] = true;
			}
			// diagonal
			p.setValues(position.getRow() - 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && isThereOpponetPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}

			//diagonal
			p.setValues(position.getRow() - 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && isThereOpponetPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			//movimento especial en passan pecas brancas
			if(position.getRow() == 3) {
				Position left = new Position(position.getRow(), position.getColumn() -1);
				if(getBoard().positionExists(left) && isThereOpponetPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
					mat[left.getRow() -1][left.getColumn()] = true; //move uma linha acima
				}
				Position rigth = new Position(position.getRow(), position.getColumn() -1);
				if(getBoard().positionExists(rigth) && isThereOpponetPiece(rigth) && getBoard().piece(rigth) == chessMatch.getEnPassantVulnerable()) {
					mat[rigth.getRow() -1][rigth.getColumn()] = true; //move uma linha acima
				}
			}
		} 
		
		else {
			//pecas pretas andam pra baixo, por isso sempre +1
			p.setValues(position.getRow() + 1, position.getColumn());
			
			if (getBoard().positionExists(p) && !getBoard().thereIsApice(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() + 2, position.getColumn());
			Position p2 = new Position(position.getRow() + 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsApice(p) && getBoard().positionExists(p2)
					&& !getBoard().thereIsApice(p2) && getMoveCount() == 0) { 
				mat[p.getRow()][p.getColumn()] = true;
			}
			// diagonal
			p.setValues(position.getRow() + 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && isThereOpponetPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}

			//diagonal
			p.setValues(position.getRow() + 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && isThereOpponetPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			//movimento especial en passan pecas pretas
			if(position.getRow() == 3) {
				Position left = new Position(position.getRow(), position.getColumn() -1);
				if(getBoard().positionExists(left) && isThereOpponetPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
					mat[left.getRow() +1][left.getColumn()] = true; //move uma linha acima
				}
				Position rigth = new Position(position.getRow(), position.getColumn() -1);
				if(getBoard().positionExists(rigth) && isThereOpponetPiece(rigth) && getBoard().piece(rigth) == chessMatch.getEnPassantVulnerable()) {
					mat[rigth.getRow() +1][rigth.getColumn()] = true; //move uma linha acima
				}
			}
			
		}
		
		return mat;
	}
	
	@Override
	public String toString() {
		return "P";
	}

}
