package IA;

import java.util.ArrayList;

import javax.swing.tree.TreeNode;

import Graphics.GraphicBoard;
import src.Board;
import src.Cell;
import src.ChessPiece;

public class IA {
    Board b;
    GraphicBoard gb;

    int color;

    public IA(Board b, GraphicBoard gb , int color) {
        this.b = b;
        this.color = color;
        this.gb = gb;
    }

    

    

    private ArrayList<ChessPiece> getAllMovablePieces(int color){
        ArrayList<ChessPiece> pieces = new ArrayList<>();
        for(int i = 0; i < b.getBoard().length; i++){
            for(int j = 0; j < b.getBoard()[i].length; j++){
                ChessPiece p = b.getBoard()[i][j].getPiece();
                if(p != null && p.getColor() == color && p.hasPossibleMoves()){
                    pieces.add(p);
                }
            }
        }
        return pieces;
    }



    public void doSomething(){
        //System.out.println("IA is thinking...");
        ArrayList<ChessPiece> pieces = getAllMovablePieces(color);
        System.out.println("IA found " + pieces.size() + " pieces");
        int max = Integer.MIN_VALUE;
        
        ArrayList<ChessPiece> bestPieces = new ArrayList<>();
        ArrayList<Cell> bestCells = new ArrayList<>();
        ArrayList<Cell> originalCells = new ArrayList<>();
        for(ChessPiece p : pieces){
            ArrayList<Cell> possibleMoves = p.getPossibleMoves();
            //System.out.println("IA found " + possibleMoves.size() + " possible moves for " + p);
            for(Cell c : possibleMoves){
                Board clone = b.clone();
                ChessPiece piece = clone.getCell(p.getCell().getI(), p.getCell().getJ()).getPiece();
                piece.move(clone.getCell(c.getI(), c.getJ()));
                int score = evaluate(clone, color);
                //System.out.println(score);
                if(score > max){
                    max = score;
                    bestPieces.clear();
                    bestCells.clear();
                    originalCells.clear();
                    bestPieces.add(piece);
                    bestCells.add(c);
                    originalCells.add(p.getCell());
                }else if(score == max){
                    bestPieces.add(piece);
                    bestCells.add(c);
                    originalCells.add(p.getCell());
                }
            }
        }

        int index = (int)(Math.random() * bestPieces.size());
        ChessPiece bestPiece = bestPieces.get(index);
        Cell bestCell = bestCells.get(index);
        Cell startCell = originalCells.get(index);


        
        b.getCell(startCell.getI(), startCell.getJ()).getPiece().move(b.getCell(bestCell.getI(), bestCell.getJ()));



        

        gb.repaint();     
    }

    //make minimax algorithm
    public int minimax(BoardNode tree, int depth, boolean isMaximisingPlayer){
        if(depth == 0){
            return evaluate(tree.getBoard(), color);
        }

        if(isMaximisingPlayer){
            int bestScore = Integer.MIN_VALUE;
            for(BoardNode child : tree.getChildren()){
                int score = minimax(child, depth - 1, false);
                bestScore = Math.max(score, bestScore);
            }
            return bestScore;
        }else{
            int bestScore = Integer.MAX_VALUE;
            for(BoardNode child : tree.getChildren()){
                int score = minimax(child, depth - 1, true);
                bestScore = Math.min(score, bestScore);
            }
            return bestScore;
        }
    }
    

    public static int evaluate(Board b, int color) {
        int score = 0;
        
        score += evaluateColor(b, color);
        score -= evaluateColor(b, color == ChessPiece.WHITE_COLOR ? ChessPiece.BLACK_COLOR : ChessPiece.WHITE_COLOR);

        return score;
    }

    private static int evaluateColor(Board b, int color){
        int score = 0;
        Cell[][] board = b.getBoard();

        //count the values of the pieces
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                ChessPiece p = board[i][j].getPiece();
                if(p != null && p.getColor() == color){
                    ChessPiece piece = board[i][j].getPiece();
                    score += piece.getValue();
                    score +=  piece.getPositionFactor()[piece.getCell().getI()][piece.getCell().getJ()];
                }
            }
        }


        return score;
    }

    
    
    
    
    public int evaluate(int color){
        return evaluate(b, color);
    }

     

}