package rules;

public enum Piece {
    EMPTY,
    RED,
    BLUE;

    public static Piece of(int value){
        if(value<0 || value>=values().length){
            throw new IllegalArgumentException();
        }

        return values()[value];
    }

    public int getValue(){
        return ordinal();
    }

    public String toString(){
        return Integer.toString(ordinal());
    }
}
