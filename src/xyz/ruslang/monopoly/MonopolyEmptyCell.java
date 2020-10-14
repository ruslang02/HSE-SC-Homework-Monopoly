package xyz.ruslang.monopoly;

public class MonopolyEmptyCell implements MonopolyCell {
    @Override
    public void trigger(MonopolyPlayer player) {
        System.out.println("Just relax there.");
    }

    @Override
    public char toSymbol() {
        return 'E';
    }

    @Override
    public String toString() {
        return "Empty";
    }
}
