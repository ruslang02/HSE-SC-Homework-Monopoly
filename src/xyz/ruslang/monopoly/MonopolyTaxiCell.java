/**
 * @author <a href="mailto:rigarifullin@edu.hse.ru">Ruslan Garifullin</a>
 */
package xyz.ruslang.monopoly;

public class MonopolyTaxiCell implements MonopolyCell {
    @Override
    public void trigger(MonopolyPlayer player) {
        int taxiDistance = (int) (Math.random() * 3 + 3);
        player.move(taxiDistance);
        System.out.printf("Player %s was shifted forward by %d cells.\n", player, taxiDistance);
    }

    @Override
    public char toSymbol() {
        return 'T';
    }

    @Override
    public String toString() {
        return "Taxi";
    }
}
