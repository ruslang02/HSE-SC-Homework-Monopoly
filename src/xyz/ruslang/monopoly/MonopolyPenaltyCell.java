/**
 * @author <a href="mailto:rigarifullin@edu.hse.ru">Ruslan Garifullin</a>
 */
package xyz.ruslang.monopoly;

public class MonopolyPenaltyCell implements MonopolyCell {
    private final double penaltyCoeff;

    public MonopolyPenaltyCell(double penaltyCoeff) {
        this.penaltyCoeff = penaltyCoeff;
    }

    /**
     * Charges the penalty from the player.
     *
     * @param player Player to trigger the action for.
     */
    @Override
    public void trigger(MonopolyPlayer player) {
        int penalty = (int) (player.getMoney() * penaltyCoeff);
        System.out.printf("Player %s will be charged $%d.\n", player, penalty);
        player.takeMoney(penalty);
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
