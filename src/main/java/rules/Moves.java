package rules;

public enum Moves {

    UP(-1, 0),
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, 1);

    private final int rowChange;
    private final int colChange;

    Moves(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    public int getRowChange() {
        return rowChange;
    }

    public int getColChange() {
        return colChange;
    }
    public static Moves of(int rowChange, int colChange) {
        for (var moves : values()) {
            if (moves.rowChange == rowChange && moves.colChange == colChange) {
                return moves;
            }
        }
        throw new IllegalArgumentException();
    }


}
