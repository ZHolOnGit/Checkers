package ui;

import entitiy.Cell;
import entitiy.CellStatus;
import game.MoveChecker;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

import static Constant.Constants.*;

public class MainPanel extends JFrame {

    private MoveChecker moveChecker;

    ArrayList<Cell> greyCells = new ArrayList<>();

    public MainPanel(){
        setSize(400,400);
        setLocationRelativeTo(null);
        addComponentsToPlane();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
    }

    private void addComponentsToPlane() {
        GridLayout gridLayout = new GridLayout(BOARD_SIZE,BOARD_SIZE);
        this.setLayout(gridLayout);

        Cell[][] cells = new Cell[BOARD_SIZE][BOARD_SIZE];
        moveChecker = new MoveChecker(cells);
        // for loop to set the background colours of the board
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Cell tempEl;
                JButton button;
                if ((i + j) % 2 == 0){
                    button = setUpElementDesign(DARK_COLOUR);
                    tempEl = new Cell(CellStatus.EMPTY, button, DARK_COLOUR,i,j);
                }else{
                    button = setUpElementDesign(LIGHT_COLOUR);
                    tempEl = new Cell(CellStatus.EMPTY, button, LIGHT_COLOUR,i,j);
                }
                cells[i][j] = tempEl;
                button.addActionListener(e -> onClick(tempEl));

            }
        }
        // Adds the checkers onto the board
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if ((i+j) % 2 != 0){
                    if (i < 3){
                        cells[i][j].setValue(CellStatus.DARK);
                    } else if (i >4) {
                        cells[i][j].setValue(CellStatus.LIGHT);
                    }
                }
            }

        }
    }

    //This function will do 3 different things depending on the state of the button is clicked
    //1. if the button is a checker, it will check if the current turn corresponds to the checker that is
    //clicked, if the turn is correct then it will generate the possible moves for that checker (first just the diagonals, sort out the jumps later)
    // it will then colour the potential moves grey.
    //2. If the button is grey it will move the previously clicked checker to that spot
    //3. if the button is a empty area it will clear all grey cells and clear the current checker that was clicked if one
    //exists
    private void onClick(Cell tempEl) {
        System.out.println(tempEl + " Clicked");

        if (tempEl.getValue() == CellStatus.GRAY){
            moveChecker.movePiece(tempEl);
            greyCells.remove(tempEl);
            moveChecker.colourPieces(greyCells,CellStatus.EMPTY);
            moveChecker.colourPieces(tempEl.getJumpedCells(),CellStatus.EMPTY);
            moveChecker.clearJumps(greyCells);
            greyCells.clear();
        }
        if(greyCells.size() > 0){
            moveChecker.colourPieces(greyCells,CellStatus.EMPTY);
            greyCells.clear();
        }

        if (tempEl.getValue() == CellStatus.DARK ||tempEl.getValue() == CellStatus.LIGHT){
            if (tempEl.getValue() == moveChecker.getCurrentTurn()){
                if (tempEl.isKing()){
                    greyCells = moveChecker.findPotentialKingMoves(tempEl);
                }else{
                    greyCells = moveChecker.findPotentialMoves(tempEl);
                }

                moveChecker.colourPieces(greyCells,CellStatus.GRAY);
            }
        }

    }
    private JButton setUpElementDesign(Color cellColour){
        JPanel panel = new JPanel();
        this.add(panel);
        panel.setBorder(new LineBorder(Color.BLACK));

        JButton button = new JButton("");// Alternate jank soloution would be to have the checkers denoted by text
        button.setPreferredSize(new Dimension(60,60));
        button.setBackground(cellColour);
        panel.setBackground(cellColour);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setOpaque(true);
        panel.add(button);

        return button;
    }
}
