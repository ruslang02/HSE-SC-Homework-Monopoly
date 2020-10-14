package xyz.ruslang.monopoly;

public class MonopolyTaxiCell implements MonopolyCell {
    @Override
    public void trigger(MonopolyPlayer player) {
        int taxiDistance = (int) (Math.random() * 2 + 3);
        player.move(taxiDistance);
    }

    @Override
    public char toSymbol() {
        return 'T';
    }

    @Override
    public String toString() {
        return "Taxi";
    }
}
