package entitiy;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static Constant.Constants.*;

public class Cell {
    private boolean king = false;

    private ArrayList<Cell> jumpedCells = new ArrayList<>();

    private CellStatus value;// determines weather or not there is a checker on that space

    private JButton jButton;// is the button that is on that space, creates click listner and what displays the colour


    private Color cellColour;// the colour of the cell

    private int row;

    private int column;

    public Cell(CellStatus value, JButton jButton,Color cellColour, int row, int column){
        this.value = value;
        this.jButton = jButton;
        this.cellColour = cellColour;
        this.row = row;
        this.column = column;
    }

    public String toString(){
        return row + "," + column + " " + value + " " + cellColour + " " + king;
    }

    public void addJumps(Cell jumped, Cell previous){
        jumpedCells.addAll(previous.jumpedCells);
        jumpedCells.add(jumped);
    }

    public void clearJumps(){
        jumpedCells.clear();
    }

    public ArrayList<Cell> getJumpedCells(){
        return jumpedCells;
    }
    public CellStatus getValue(){return value;}

    public int getRow(){return row;}

    public int getColumn(){return column;}

    public boolean isKing(){
        return king;
    }

    public void setKing(boolean new_king){
        king = new_king;
    }

    public void setValue(CellStatus value){
        this.value = value;
        switch (value){
            case EMPTY:
                if (this.row + this.column %2 == 0){
                    this.jButton.setBackground(DARK_COLOUR);
                }else{
                    this.jButton.setBackground(LIGHT_COLOUR);
                }
                break;
            case LIGHT:
                if(isKing()){
                    this.jButton.setBackground(LIGHT_KING_CHECK);

                }else {
                    this.jButton.setBackground(LIGHT_CHECK);
                }
                break;

            case DARK:
                if (isKing()){
                    this.jButton.setBackground(DARK_KING_CHECK);
                }else{
                    this.jButton.setBackground(DARK_CHECK);
                }
                break;


            case GRAY:
                this.jButton.setBackground(Color.GRAY);



            default:
                break;
        }
    }


}
