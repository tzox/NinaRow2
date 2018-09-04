package Engine;

public class robotChoice {
    private int column;
    private boolean isPopuot;

    public robotChoice(int column, boolean isPopuot) {
        this.column = column;
        this.isPopuot = isPopuot;
    }

    public int getColumn() {
        return column;
    }

    public boolean isPopuot() {
        return isPopuot;
    }
}
