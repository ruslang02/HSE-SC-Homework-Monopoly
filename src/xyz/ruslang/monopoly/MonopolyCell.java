/**
 * @author <a href="mailto:rigarifullin@edu.hse.ru">Ruslan Garifullin</a>
 */
package xyz.ruslang.monopoly;

/**
 * Interface that describes all cells.
 */
public interface MonopolyCell {
    /**
     * Triggers the cell action (e.g. when the player steps on it)
     *
     * @param player Player to trigger the action for.
     */
    void trigger(MonopolyPlayer player);

    /**
     * Gives a symbolic presentation of a cell.
     *
     * @return The symbol.
     */
    char toSymbol();
}
