package xyz.ruslang.monopoly;

public interface MonopolyCell {
    void trigger(MonopolyPlayer player);
    char toSymbol();
}
