package Laboratoire4;

public class Case {
    private Pion currentPion = null;
    public Case(Pion pion) {
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
    }

    public Case clone() {
        Case newCase = new Case(null);

        if (!this.isEmpty()) {
            newCase.setPion(this.currentPion.clone());
        }

        return newCase;
    }
}
