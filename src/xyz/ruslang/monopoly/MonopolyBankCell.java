/**
 * @author <a href="mailto:rigarifullin@edu.hse.ru">Ruslan Garifullin</a>
 */
package xyz.ruslang.monopoly;

/**
 * The Bank cell, connects to the host bank.
 */
public class MonopolyBankCell implements MonopolyCell {
    private final MonopolyBank bank;

    public MonopolyBankCell(MonopolyBank bank) {
        this.bank = bank;
    }

    /**
     * Offers a bank credit or retrieves the debt for the player.
     *
     * @param player Player to trigger the action for.
     */
    @Override
    public void trigger(MonopolyPlayer player) {
        if (player instanceof MonopolyBotPlayer) {
            System.out.println("Player is a bot, can't provide a credit.");
            return;
        }
        int debt = (int) bank.getDebt(player);
        if (debt > 0) {
            System.out.printf("Player %s has a debt of $%d. Processing payment...\n", player, debt);
            player.takeMoney(debt);
            bank.clearDebt(player);
        } else {
            bank.offerCredit(player);
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
