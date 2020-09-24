package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Queen extends ChessPiece {

	public Queen(Board board, Color color) {
		super(board, color);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Q"; // essa letra vai aparecer no tabuleiro

	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

		Position p = new Position(0, 0);

		// acima da peça
		p.setValues(position.getRow() - 1, position.getColumn());
		while (getBoard().positionExists(p) && !getBoard().thereIsApice(p)) { // enquanto a posição p existir e nao
																				// tiver uma peça lá, marque como
																				// verdadeiro
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() - 1); // repete enquanto tiver casas vazias

		}
		if (getBoard().positionExists(p) && isThereOpponetPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		// abaixo da peça
		p.setValues(position.getRow(), position.getColumn() - 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsApice(p)) { // enquanto a posição p existir e nao
																				// tiver uma peça lá, marque como
																				// verdadeiro
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() - 1);
		}
		if (getBoard().positionExists(p) && isThereOpponetPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// direita
		p.setValues(position.getRow(), position.getColumn() + 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsApice(p)) { // enquanto a posição p existir e nao
																				// tiver uma peça lá, marque como
																				// verdadeiro
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() + 1); // repete enquanto tiver casas vazias

		}
		if (getBoard().positionExists(p) && isThereOpponetPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// pra baixo
		p.setValues(position.getRow() + 1, position.getColumn());
		while (getBoard().positionExists(p) && !getBoard().thereIsApice(p)) { // enquanto a posição p existir e nao
																				// tiver uma peça lá, marque como
																				// verdadeiro
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() + 1); // repete enquanto tiver casas vazias
		}
		// noroeste
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsApice(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() - 1);

		}
		if (getBoard().positionExists(p) && isThereOpponetPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		// nordeste
		p.setValues(position.getRow() - 1, position.getColumn() + 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsApice(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() + 1);
		}
		if (getBoard().positionExists(p) && isThereOpponetPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// sudeste
		p.setValues(position.getRow() + 1, position.getColumn() + 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsApice(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() + 1);

		}
		if (getBoard().positionExists(p) && isThereOpponetPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// sudoente
		p.setValues(position.getRow() + 1, position.getColumn() - 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsApice(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() - 1);
		}

		return mat;
	}

}
