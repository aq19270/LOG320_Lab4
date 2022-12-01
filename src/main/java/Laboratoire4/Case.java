package Laboratoire4;

public class Case {
    private Pion currentPion = null;
    private int x;
    private int y;

    public Case(int x, int y, Pion pion) {
        this.x = x;
        this.y = y;
        this.currentPion = pion;
    }

    public boolean isEmpty() {
        return currentPion == null;
    }

    public void emptyCase(){
        currentPion = null;
    }

    public Pion getPion() {
        return currentPion;
    }

    public void setPion(Pion pion) {
        currentPion = pion;
        pion.setX(this.x);
        pion.setY(this.y);
    }

    public Case clone() {
        Case newCase = new Case(this.x, this.y, null);

        if (!this.isEmpty()) {
            newCase.setPion(this.currentPion.clone());
        }

        return newCase;
    }
}
