package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		
		
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		List<ChessPiece> captured = new ArrayList<>(); //passando no argumento ali embaixo

		while (true) {
			try {
				UI.clearScreen();
				UI.printMatch(chessMatch, captured);
				System.out.println();
				System.out.print("Origem: ");
				ChessPosition source = UI.readChessPosition(sc);
				
				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces(), possibleMoves); //imprime o tabuleiro só que colorindo as posições que a peça pode ir
				System.out.println();
				System.out.println("Destino: ");
				ChessPosition target = UI.readChessPosition(sc);
					
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
				//sempre que eu executar um movimento de xadrez (chessMatch)e retornar uma possivel peca capturedPiece faz:
				if(capturedPiece != null) {
					captured.add(capturedPiece); 
					//sempre que executar um movimento e esse movimento resultar em uma peca capturada, add ela na lista de pecas capturadas 
				}
			}
			catch (ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine(); // para o programa aguardar eu apertar enter
				
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine(); // para o programa aguardar eu apertar enter
				
			}
;		}

	}
}