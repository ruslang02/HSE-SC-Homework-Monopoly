package xyz.ruslang.monopoly;

public class MonopolyBank {
    private MonopolyPlayer player;
    private double debt;

    private double creditCoeff;
    private double debtCoeff;

    public MonopolyBank(double creditCoeff, double debtCoeff) {
        this.creditCoeff = creditCoeff;
        this.debtCoeff = debtCoeff;
    }

    public double getDebt(MonopolyPlayer player) {
        if (player != this.player) return 0;
        return debt;
    }
}
