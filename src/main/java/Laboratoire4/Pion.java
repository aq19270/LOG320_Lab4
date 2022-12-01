package Laboratoire4;

public class Pion {

    public Pion clone() {
        return new Pion(x, y, color);
    }

    public enum colors {
        none(0),
        black(2),
        white(4);

        private int value;
        private colors(int val) {
            value = val;
        }

        public int getValue() {
            return value;
        }
    }

    private int x;
    private int y;
    private colors color;

    public Pion(int x, int y, colors color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public colors getColor() {
        return this.color;
    }

    public int getColorValue() {
        return this.color.getValue();
    }
}
