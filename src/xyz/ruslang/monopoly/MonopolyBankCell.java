package xyz.ruslang.monopoly;

public class MonopolyBankCell implements MonopolyCell {
    private MonopolyBank bank;
    public MonopolyBankCell(MonopolyBank bank) {
        this.bank = bank;
    }
    @Override
    public void trigger(MonopolyPlayer player) {
        if (player instanceof MonopolyBotPlayer) return;
        double debt = bank.getDebt(player);
        if (debt > 0) {
            player.takeMoney(debt);
        }
    }

    @Override
    public char toSymbol() {
        return '$';
    }

    @Override
    public String toString() {
        return "Bank";
    }
}
