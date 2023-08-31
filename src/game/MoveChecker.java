package game;

import entitiy.Cell;
import entitiy.CellStatus;

import java.util.ArrayList;

import static Constant.Constants.*;

public class MoveChecker {

    private Cell[][] cells;

    private Cell currentCell;

    private CellStatus currentTurn = CellStatus.LIGHT;

    public MoveChecker(Cell[][] cells){
        this.cells = cells;
    }

    public CellStatus getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(CellStatus currentTurn) {
        this.currentTurn = currentTurn;
    }

    //this function will need to be extended to include "king" pieces and jumps
    public ArrayList<Cell> findPotentialMoves(Cell cell){
        ArrayList<Cell> potentialMoves = new ArrayList<>();
        currentCell  = cell;
        Cell potential;
        int direction = cell.getValue() == CellStatus.DARK ? +1: -1;
        for (int i = -1; i < 3; i+=2) {
            potential = isOnBoard(cell.getRow() + direction,cell.getColumn()+i);
            if (potential != null){
                if(potential.getValue() == CellStatus.EMPTY){
                    potentialMoves.add(potential);
                }
            }
        }
        jumpChecker(cell,potentialMoves);
        return potentialMoves;
    }

    public ArrayList<Cell> findPotentialKingMoves(Cell cell){
        ArrayList<Cell> potentialMoves = new ArrayList<>();
        currentCell  = cell;
        Cell potential;
        for (int i = -1; i < 3; i+=2) {
            for (int j = -1; j < 3; j+=2) {
                potential = isOnBoard(cell.getRow() + i, cell.getColumn() + j);
                if(potential != null){
                    if(potential.getValue() == CellStatus.EMPTY){
                        potentialMoves.add(potential);
                    }
                }
            }
        }
        kingJumpChecker(cell,potentialMoves);
        return potentialMoves;
    }

    public void jumpChecker(Cell cell, ArrayList<Cell> potentialMoves){
        Cell next;
        Cell jumpSpot;
        int direction = cell.getValue() == CellStatus.DARK ? +1: -1;
        for (int i = -1; i < 3; i+=2) {
            next = isOnBoard(cell.getRow() + direction,cell.getColumn()+i);
            if (next != null && opCheck(next)){
                jumpSpot = isOnBoard(next.getRow() + direction, next.getColumn()+i);
                System.out.println(jumpSpot + " jumpspot");
                if (jumpSpot != null && jumpSpot.getValue() == CellStatus.EMPTY){
                    potentialMoves.add(jumpSpot);
                    jumpSpot.addJumps(next,cell);
                    System.out.println(jumpSpot.getJumpedCells());
                    jumpChecker(jumpSpot,potentialMoves);
                }
            }
        }
    }

    public void kingJumpChecker(Cell cell, ArrayList<Cell> potentialMoves){
        Cell next;
        Cell jumpSpot;
        for (int i = -1; i < 3; i+=2) {
            for (int j = -1; j < 3; j+=2) {
                next = isOnBoard(cell.getRow() + i, cell.getColumn() +j);
                if (next != null && opCheck(next)){
                    jumpSpot = isOnBoard(next.getRow() + 1, next.getColumn() + j);
                    if (jumpSpot != null && jumpSpot.getValue() == CellStatus.EMPTY){
                        potentialMoves.add(jumpSpot);
                        jumpSpot.addJumps(next,cell);
                        jumpChecker(jumpSpot,potentialMoves);
                    }
                }

            }
        }
    }


    //This function checks to see if the observed cell is the opposite colour to
    // the cell that is trying to move.
    public boolean opCheck(Cell potential){
        if (currentTurn != potential.getValue() && potential.getValue() != CellStatus.EMPTY){
            return true;
        }
        return false;
    }

    public void movePiece(Cell grey){

        if(currentCell.isKing()){
            currentCell.setKing(false);
            grey.setKing(true);
        }

        if(grey.getRow() == 0){
            if (currentCell.getValue() == CellStatus.LIGHT){
                grey.setKing(true);
            }
        } else if (grey.getRow() == 7) {
            if(currentCell.getValue() == CellStatus.DARK){
                grey.setKing(false);
            }

        }
        currentCell.setValue(CellStatus.EMPTY);
        grey.setValue(currentTurn);
        changeTurn();
    }

    public void changeTurn(){
        if (currentTurn == CellStatus.LIGHT){
            currentTurn = CellStatus.DARK;
        }else{
            currentTurn = CellStatus.LIGHT;
        }
    }
    public void colourPieces(ArrayList<Cell> cells, CellStatus colour) {
        for (int i = 0; i < cells.size(); i++){//BUG: Removed the -1 from cells.size
            cells.get(i).setValue(colour);
        }
    }
    public void clearJumps(ArrayList<Cell> cells){
        for (int i = 0; i < cells.size(); i++){//BUG: Removed the -1 from cells.size
            cells.get(i).clearJumps();
        }
    }
    private Cell isOnBoard(int row, int column){
        if (column >=0 && column < BOARD_SIZE && row >=0 && row < BOARD_SIZE){
            return cells[row][column];
        }
        return null;
    }

}
