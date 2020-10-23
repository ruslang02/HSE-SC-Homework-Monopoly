/**
 * @author <a href="mailto:rigarifullin@edu.hse.ru">Ruslan Garifullin</a>
 */
package xyz.ruslang.monopoly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MonopolyShopCell implements MonopolyCell {
    private final int cellIndex;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final double improvementCoeff = Math.random() * (2 - 0.1) + 0.1;
    private final double compensationCoeff = Math.random() * (1 - 0.1) + 0.1;
    private int N = (int) (Math.random() * (500 - 50 + 1)) + 50;
    private int K = (int) ((Math.random() * (0.9 - 0.5) + 0.5) * N);
    private char ownerSymbol = 'S';
    private MonopolyPlayer owner;

    public MonopolyShopCell(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    /**
     * Manages the player stepping on the shop cell.
     *
     * @param player Player to trigger the action for.
     */
    @Override
    public void trigger(MonopolyPlayer player) {
        System.out.printf("Player %s is in shop cell %d. Shop's price: $%d. Compensation price: $%d.\n", player, cellIndex, N, K);
        if (ownerSymbol == 'S') {
            triggerBuy(player);
            return;
        }
        System.out.printf("Shop is owned by %s.\n", owner);
        int upgradePrice = (int) (N * improvementCoeff);
        boolean isHuman = player instanceof MonopolyHumanPlayer;
        if (ownerSymbol == 'M' && isHuman) {
            if (player.getMoney() < upgradePrice) {
                System.out.println("You do not have enough money to upgrade the shop.");
                return;
            }
            System.out.printf("Do you wish to upgrade it for $%d? [Y/n]", upgradePrice);
            String input = "y";
            try {
                input = reader.readLine();
            } catch (IOException ignored) {
            }
            if (input.length() == 0 || input.toLowerCase().trim().equals("y")) processUpgrade();
            else System.out.println("You refused to upgrade the shop.");
        } else if ((ownerSymbol != 'M') == isHuman) {
            System.out.printf("Player %s must pay $%d to the player %s.\n", player, K, owner);
            player.takeMoney(K);
            owner.giveMoney(K);
        } else if (Math.random() >= 0.5 && player.getMoney() >= upgradePrice) processUpgrade();
    }

    /**
     * Processes the upgrade of the shop from the owner's wallet.
     */
    private void processUpgrade() {
        int upgradePrice = (int) (N * improvementCoeff);
        owner.takeMoney(upgradePrice);
        N += upgradePrice;
        K += K * compensationCoeff;
        System.out.printf("The shop was upgraded. Now it costs $%d and makes $%d of profit.\n", N, K);
    }

    /**
     * @return The price of the shop.
     */
    public int getPrice() {
        return N;
    }

    /**
     * Attempts to purchase the shop.
     *
     * @param player Player to process purchase from.
     */
    private void triggerBuy(MonopolyPlayer player) {
        System.out.println("This shop has no owner.");
        if (player.getMoney() < N) {
            System.out.println("Not enough money to buy it.");
            return;
        }
        if (player instanceof MonopolyHumanPlayer) {
            System.out.printf("Would you like to buy if for $%d? [Y/n] ", N);
            String input = "y";
            try {
                input = reader.readLine();
            } catch (IOException ignored) {
            }
            if (input.length() == 0 || input.toLowerCase().trim().equals("y")) processPurchase(player);
        } else {
            boolean willBuy = (int) (Math.round(Math.random())) == 1;
            if (willBuy) processPurchase(player);
            else {
                System.out.printf("Player %s decided to not buy it.\n", player);
            }
        }
    }

    /**
     * Processes the payment for the shop.
     *
     * @param player Player to process purchase from.
     */
    private void processPurchase(MonopolyPlayer player) {
        player.takeMoney(N);
        player.addShop(this);
        System.out.printf("Player %s bought the shop for $%d.\n", player, N);
        ownerSymbol = player instanceof MonopolyBotPlayer ? 'O' : 'M';
        owner = player;
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
