/**
 * @author <a href="mailto:rigarifullin@edu.hse.ru">Ruslan Garifullin</a>
 */
package xyz.ruslang.monopoly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Entry class.
 */
public class Main {
    private final int height;
    private final int width;
    private final MonopolyPlayer[] players = new MonopolyPlayer[2];
    private final MonopolyCell[] cells;
    private final MonopolyBank sberbank;
    private final int money;
    private double creditCoeff;
    private double debtCoeff;
    private double penaltyCoeff;
    private MonopolyPlayer currentPlayer;

    /**
     * Instance constructor.
     *
     * @param width  Width of the field.
     * @param height Height of the field.
     * @param money  Amount of money given to every player on start.
     */
    public Main(int width, int height, int money) {
        this.width = width;
        this.height = height;
        this.money = money;
        this.players[0] = new MonopolyHumanPlayer(this);
        this.players[1] = new MonopolyBotPlayer(this);
        config();
        cells = new MonopolyCell[width * 2 + height * 2 - 4];
        sberbank = new MonopolyBank(creditCoeff, debtCoeff);
        generateField();
        currentPlayer = players[(int) Math.round(Math.random())];
        System.out.printf("First player is %s.", currentPlayer.toString());
        while (!checkEnd()) nextRound();
    }

    /**
     * Entry point.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        int width = 8, height = 6, money = 5000;
        if (args.length > 0) {
            try {
                width = parseInt(args[0], 6, 30);
                height = parseInt(args[1], 6, 30);
                money = parseInt(args[2], 500, 15e3);
            } catch (Exception ignored) {
                System.err.println("Incorrect command line arguments.\nUsage: app.jar <width> <height> <money>\n6 <= width <= 30, 6 <= height <= 30, 500 <= money <= 15000");
                return;
            }
        }

        new Main(width, height, money);
    }

    /**
     * Parses number from string in given boundaries.
     *
     * @param value String to process.
     * @param lower Lower boundary.
     * @param upper Upper boundary.
     * @return Number from the string.
     */
    private static int parseInt(String value, double lower, double upper) {
        int result = Integer.parseInt(value);
        if (result < lower || result > upper)
            throw new NumberFormatException("One of the input values were out of bounds.");
        return result;
    }

    public int getDefaultMoney() {
        return money;
    }

    public int getTotalCells() {
        return cells.length;
    }

    private void displayPlayers() {
        for (MonopolyPlayer player : players)
            System.out.printf("Player %s. Money: $%d. Debt: $%d. Capital: $%d.\n",
                    player, player.getMoney(), (int) sberbank.getDebt(player), player.getTotalCapital()
            );
        System.out.println();
    }

    private void nextRound() {
        System.out.println("\n");
        displayPlayers();
        renderField();
        int dieOne = (int) (Math.random() * 6) + 1;
        int dieTwo = (int) (Math.random() * 6) + 1;
        System.out.printf("The dice rolled %d %d.\n", dieOne, dieTwo);
        int i = -1;
        i = currentPlayer.move(dieOne + dieTwo);
        do {
            i = currentPlayer.getPosition();
            renderField();
            System.out.printf("Player %s stepped on a %s cell.\n", currentPlayer, cells[i]);
            cells[i].trigger(currentPlayer);
        } while (i != currentPlayer.getPosition());
        System.out.printf("Player %s is in cell #%d and has $%d.\n", currentPlayer, i, currentPlayer.getMoney());
        currentPlayer = players[currentPlayer instanceof MonopolyHumanPlayer ? 1 : 0];
        System.out.printf("Next player is %s. Press ENTER to pass the turn", currentPlayer.toString());
        try {
            System.in.read();
        } catch (Exception ignored) {
        }
    }

    /**
     * @return Whether the game ended
     */
    private boolean checkEnd() {
        for (MonopolyPlayer player : players) if (player.getMoney() < 0) return true;
        return false;
    }

    /**
     * Ends the game and exits the app.
     *
     * @param loser The player that lost.
     */
    public void endGame(MonopolyPlayer loser) {
        MonopolyPlayer winner = players[loser instanceof MonopolyBotPlayer ? 0 : 1];
        System.out.printf("Player %s lost! Player %s won with $%d balance and $%d capital.",
                loser, winner, winner.getMoney(), winner.getTotalCapital());
        System.exit(0);
    }

    /**
     * Assigns random configuration values for variables.
     */
    private void config() {
        creditCoeff = Math.random() * 0.198 + 0.002;
        System.out.printf("Your credit coefficient rate is %f.\n", creditCoeff);

        debtCoeff = Math.random() * 2 + 1;
        System.out.printf("Your debt coefficient rate is %f.\n", debtCoeff);

        penaltyCoeff = Math.random() * 0.09 + 0.01;
        System.out.printf("Your penalty coefficient rate is %f.\n", penaltyCoeff);

        System.out.printf("Every player gets $%d on start.\n", money);

        System.out.print("Press ENTER to start the game");
        try {
            System.in.read();
        } catch (Exception ignored) {
        }
    }

    /**
     * Fills the field with different cells.
     */
    private void generateField() {
        placeEmptyCells();
        placeBankCells();
        placeTaxiCells();
        placePenaltyCells();
        placeShopCells();
    }

    /**
     * Prints current player's symbol.
     *
     * @param index Current cell's index.
     * @return Whether the position was filled.
     */
    private boolean renderPosition(int index) {
        if (players[0].getPosition() == index) System.out.print('▉');
        else if (players[1].getPosition() == index) System.out.print('▓');
        else return false;
        return true;
    }

    /**
     * Renders the field as a rectangle.
     */
    private void renderField() {
        System.out.println("Current field:");
        for (int i = 0; i < width; i++) {
            if (!renderPosition(i)) System.out.print(cells[i].toSymbol());
        }
        System.out.println();
        for (int i = 1; i < height - 1; i++) {
            if (!renderPosition(cells.length - i)) System.out.print(cells[cells.length - i].toSymbol());
            System.out.print(" ".repeat(width - 2));
            if (!renderPosition(width + i - 1)) System.out.print(cells[width + i - 1].toSymbol());
            System.out.println();
        }
        for (int i = 2 * width + height - 3; i > width + height - 3; i--) {
            if (!renderPosition(i)) System.out.print(cells[i].toSymbol());
        }
        System.out.println("\n");
    }

    /**
     * PLaces empty cells on the corners of the field.
     */
    private void placeEmptyCells() {
        cells[0] = new MonopolyEmptyCell();
        cells[width - 1] = new MonopolyEmptyCell();
        cells[width - 1 + height - 1] = new MonopolyEmptyCell();
        cells[2 * (width - 1) + height - 1] = new MonopolyEmptyCell();
    }

    /**
     * Places bank cells on the field (one on each side).
     */
    private void placeBankCells() {
        int[] indexes = {
                getRandomIndex(width - 2, 1),
                getRandomIndex(height - 2, width),
                getRandomIndex(width - 2, width + height - 1),
                getRandomIndex(height - 2, 2 * width + height - 2)
        };
        for (int index : indexes) cells[index] = new MonopolyBankCell(sberbank);
    }

    /**
     * Randomly assigns a cell index that is not occupied yet.
     *
     * @param length Amount of randomizable elements.
     * @param begin  First element index.
     * @return The randomized value.
     */
    private int getRandomIndex(int length, int begin) {
        int rnd;
        int tries = 0;
        do {
            rnd = (int) Math.floor(Math.random() * length) + begin;
            tries++;
        } while (cells[rnd] != null && tries < length * 2);
        return rnd;
    }

    /**
     * Places shop cells on the rest of the positions.
     */
    private void placeShopCells() {
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] == null)
                cells[i] = new MonopolyShopCell(i);
        }
    }

    /**
     * Creates an array of random cell positions (max. 2 for each side).
     *
     * @return The ArrayList with all indexes.
     */
    private ArrayList<Integer> getRandomIndexes() {
        int[] amounts = IntStream.generate(() -> (int) (Math.random() * 3)).limit(4).toArray();
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < amounts[0]; i++) indexes.add(getRandomIndex(width - 2, 1));
        for (int i = 0; i < amounts[1]; i++) indexes.add(getRandomIndex(height - 2, width));
        for (int i = 0; i < amounts[2]; i++) indexes.add(getRandomIndex(width - 2, width + height - 1));
        for (int i = 0; i < amounts[3]; i++) indexes.add(getRandomIndex(height - 2, 2 * width + height - 2));
        return indexes;
    }

    /**
     * Places taxi cells (max. 2 on each side).
     */
    private void placeTaxiCells() {
        for (int index : getRandomIndexes()) cells[index] = new MonopolyTaxiCell();
    }

    /**
     * Places penalty cells (max. 2 on each side).
     */
    private void placePenaltyCells() {
        for (int index : getRandomIndexes()) cells[index] = new MonopolyPenaltyCell(penaltyCoeff);
    }
}
