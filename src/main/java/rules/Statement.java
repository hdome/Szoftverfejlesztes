package rules;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Data
@Slf4j
public class Statement {

    public static final int[][] START = {
            {2, 1, 2, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 2, 1, 2}
    };
    public Statement() {
        this(START);
    }
    public Statement(int[][] board){
        if(!isValidBoard(board)){
            throw new IllegalArgumentException();
        }
        initBoard(board);
    }

    private boolean isValidBoard(int[][] board){
        if(board == null || board.length != 5){
            return false;
        }
        int countRed=0;
        int countBlue=0;
        for(int[] row:board){
            if(row == null || row.length !=4){
                return false;
            }
            for(int a:row){
                if(a<0 || a>Piece.values().length){
                    return false;
                }
                countBlue += (a==2)?1:0;
                countRed += (a==1)?1:0;
            }
        }
        return countBlue == 4 && countRed == 4;
    }


    private void initBoard(int[][] board){
        this.board = new Piece[5][4];
        for(int i=0; i<5;i++){
            for(int j=0; j<4; j++){
                this.board[i][j] = Piece.of(board[i][j]);
            }
        }
    }

    @Setter(AccessLevel.NONE)
    private Piece turn = Piece.BLUE;

    @Setter(AccessLevel.NONE)
    private Piece winner;

    @Setter(AccessLevel.NONE)
    private Piece[][] board;

    private void nextTurn(){
        if(this.turn == Piece.RED){
            this.turn = Piece.BLUE;
        } else{
            this.turn = Piece.RED;
        }
    }

    public void moveTo(int row, int column, Moves moves){
        List<Moves> directions = whereToMove(row, column);
        if(this.board[row][column]!=this.turn){
            throw new IllegalArgumentException();
        }
        if(!directions.contains(moves)){
            throw new IllegalArgumentException();
        }
        log.info("The {} piece at ({},{}) is moved to {}", this.board[row][column].name(), row, column, moves);
        swapPieces(row, column, moves);
        nextTurn();
    }

    private void swapPieces(int row, int column, Moves moves){
        Piece temp;
        int newRow = row + moves.getRowChange();
        int newColumn = column + moves.getColChange();
        temp = this.board[row][column];
        this.board[row][column] = this.board[newRow][newColumn];
        this.board[newRow][newColumn] = temp;

    }

    private boolean canMoveUp(int row, int column){
        if(this.board[row][column]==Piece.EMPTY){
            throw new IllegalArgumentException();
        }
        return (row-1)>=0 && (row-1)<5 && this.board[row-1][column]==Piece.EMPTY;
    }

    private boolean canMoveDown(int row, int column){
        if(this.board[row][column]==Piece.EMPTY){
            throw new IllegalArgumentException();
        }
        return (row+1)>=0 && (row+1)<5 && this.board[row+1][column]==Piece.EMPTY;
    }

    private boolean canMoveRight(int row, int column){
        if(this.board[row][column]==Piece.EMPTY){
            throw new IllegalArgumentException();
        }
        return (column+1)>=0 && (column+1)<4 && this.board[row][column+1]==Piece.EMPTY;
    }

    private boolean canMoveLeft(int row, int column){
        if(this.board[row][column]==Piece.EMPTY){
            throw new IllegalArgumentException();
        }
        return (column-1)>=0 && (column-1)<4 && this.board[row][column-1]==Piece.EMPTY;
    }

    public List<Moves> whereToMove(int row, int column){
        List<Moves> directions = new ArrayList<>();

        if(canMoveUp(row, column)){
            directions.add(Moves.UP);
        }
        if(canMoveDown(row, column)){
            directions.add(Moves.DOWN);
        }
        if(canMoveRight(row, column)){
            directions.add(Moves.RIGHT);
        }
        if(canMoveLeft(row, column)){
            directions.add(Moves.LEFT);
        }

        return directions;
    }

    public boolean isEnd() {
        return isThreeInARow() || isThreeInAColumn() || isThreeInDiagonal();
    }

    private boolean isThreeInARow(){
        for(Piece[] row:this.board){
            for(int i=0; i<2; i++){
                if(row[i]==row[i+1] && row[i]==row[i+2] && row[i]!=Piece.EMPTY){
                    this.winner = row[i];
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isThreeInAColumn(){
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                if(this.board[i][j]==this.board[i+1][j] && this.board[i][j]==this.board[i+2][j] && this.board[i][j]!=Piece.EMPTY){
                    this.winner = this.board[i][j];
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isThreeInDiagonal(){
        for(int i=0; i<3; i++) {
            for (int j = 0; j < 4; j++) {
                if(j+2<4){
                    if(this.board[i][j]==this.board[i+1][j+1] && this.board[i][j]==this.board[i+2][j+2] && this.board[i][j]!=Piece.EMPTY){
                        winner = this.board[i][j];
                        return true;
                    }
                }
                if(j-2>=0){
                    if(this.board[i][j]==this.board[i+1][j-1] && this.board[i][j]==this.board[i+2][j-2] && this.board[i][j]!=Piece.EMPTY){
                        winner = this.board[i][j];
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
