package xyz.ruslang.monopoly;

public class MonopolyShopCell implements MonopolyCell {
    private char ownerSymbol = 'S';

    @Override
    public void trigger(MonopolyPlayer player) {
        if (player instanceof MonopolyHumanPlayer) {
            ownerSymbol = 'M';
        } else {
            ownerSymbol = 'O';
        }
    }

    @Override
    public char toSymbol() {
        return ownerSymbol;
    }

    @Override
    public String toString() {
        return "Shop";
    }
}
