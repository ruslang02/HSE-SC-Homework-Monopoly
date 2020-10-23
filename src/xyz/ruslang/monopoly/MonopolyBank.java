/**
 * @author <a href="mailto:rigarifullin@edu.hse.ru">Ruslan Garifullin</a>
 */
package xyz.ruslang.monopoly;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Singleton, the big rich bank.
 */
public class MonopolyBank {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final double creditCoeff;
    private final double debtCoeff;
    private MonopolyPlayer player;
    private double debt;

    public MonopolyBank(double creditCoeff, double debtCoeff) {
        this.creditCoeff = creditCoeff;
        this.debtCoeff = debtCoeff;
    }

    /**
     * Retrieves the debt of the player provided.
     *
     * @param player Player to get debt of.
     * @return The debt.
     */
    public double getDebt(MonopolyPlayer player) {
        if (player != this.player) return 0;
        return debt;
    }

    /**
     * Calculates the max credit that can be given to the player.
     *
     * @param player Player wanting to get the credit.
     * @return The credit value.
     */
    private double getMaxCredit(MonopolyPlayer player) {
        return creditCoeff * player.getTotalCapital();
    }

    /**
     * Clears the debt value of the player.
     *
     * @param player The player to clear debt of.
     */
    public void clearDebt(MonopolyPlayer player) {
        if (player == this.player) debt = 0;
    }

    /**
     * Offers player a credit.
     *
     * @param player Player to offer the credit.
     */
    public void offerCredit(MonopolyPlayer player) {
        int request = 0;
        while (request <= 0 || request > getMaxCredit(player)) {
            System.out.printf("Would you like to get a credit? Maximum amount you can obtain is $%d. Enter 0 or skip input: ", (int) getMaxCredit(player));
            try {
                request = Integer.parseInt("0" + reader.readLine());
                if (request == 0) return;
            } catch (NumberFormatException ignored) {
                System.out.println("Incorrect input, not providing a credit.");
                return;
            } catch (Exception ignored) {
                return;
            }
        }
        debt = request * debtCoeff;
        player.giveMoney(request);
        this.player = player;
    }
}
