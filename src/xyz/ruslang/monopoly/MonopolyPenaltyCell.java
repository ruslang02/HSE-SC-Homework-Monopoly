package xyz.ruslang.monopoly;

public class MonopolyPenaltyCell implements MonopolyCell {
    private double penaltyCoeff;

    public MonopolyPenaltyCell(double penaltyCoeff) {
        this.penaltyCoeff = penaltyCoeff;
    }
    @Override
    public void trigger(MonopolyPlayer player) {
        player.takeMoney(player.money * penaltyCoeff);
    }

    @Override
    public char toSymbol() {
        return '%';
    }

    @Override
    public String toString() {
        return "Penalty";
    }
}
