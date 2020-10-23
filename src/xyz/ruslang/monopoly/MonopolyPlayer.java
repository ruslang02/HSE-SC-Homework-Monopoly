/**
 * @author <a href="mailto:rigarifullin@edu.hse.ru">Ruslan Garifullin</a>
 */
package xyz.ruslang.monopoly;

import java.util.ArrayList;

/**
 * An abstract class that describes a monopoly player.
 */
public abstract class MonopolyPlayer {
    private final Main main;
    private final ArrayList<MonopolyShopCell> ownedShops = new ArrayList<>();
    private int money;
    private int position = 0;
    private final String name;

    public MonopolyPlayer(String name, Main main) {
        this.name = name;
        this.main = main;
        this.money = main.getDefaultMoney();
    }

    /**
     * Moves the player around the field
     *
     * @param distance Distance to move the player.
     * @return The position of the player after moving.
     */
    int move(int distance) {
        position = (getPosition() + distance) % main.getTotalCells();
        return getPosition();
    }

    /**
     * Adds the money value of the player.
     *
     * @param money Amount to add.
     */
    void giveMoney(double money) {
        this.money = this.getMoney() + (int) money;
    }

    /**
     * Subtracts the money value of the player and ends the game if the user is out.
     *
     * @param money Amount to subtract.
     */
    void takeMoney(double money) {
        this.money = this.getMoney() - (int) money;
        if (this.getMoney() <= 0) main.endGame(this);
    }

    /**
     * Adds a shop to the owned collection.
     *
     * @param shop Shop to add.
     */
    void addShop(MonopolyShopCell shop) {
        ownedShops.add(shop);
    }

    /**
     * @return Sum of all the shops purchased.
     */
    int getTotalCapital() {
        return ownedShops.stream().mapToInt(MonopolyShopCell::getPrice).sum();
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * @return Amount of money user has.
     */
    public int getMoney() {
        return money;
    }

    /**
     * @return Position of the player.
     */
    public int getPosition() {
        return position;
    }
}
