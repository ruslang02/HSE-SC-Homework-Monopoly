/**
 * @author <a href="mailto:rigarifullin@edu.hse.ru">Ruslan Garifullin</a>
 */
package xyz.ruslang.monopoly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        int width = 8, height = 6, money = 5000;
        try {
            width = parseInt(args[0], 6, 30);
            height = parseInt(args[1], 6, 30);
            money = parseInt(args[2], 500, 15e3);
        } catch (Exception ignored) {
        }
        new Main(width, height, money);
    }

    private static int parseInt(String value, double lower, double higher) {
        int result = Integer.parseInt(value);
        if (result < lower || result > higher)
            throw new NumberFormatException("One of the input values were out of bounds.");
        return result;
    }

    private MonopolyCell[] cells;
    private MonopolyBank sberbank;

    private final Scanner sc = new Scanner(System.in);

    private final int height;
    private final int width;
    private double creditCoeff;
    private double debtCoeff;
    private double penaltyCoeff;
    private double money;


    public Main(int width, int height, int money) {
        this.width = width;
        this.height = height;
        this.money = money;

        config();
        cells = new MonopolyCell[width * 2 + height * 2 - 4];
        sberbank = new MonopolyBank(creditCoeff, debtCoeff);
        generateField();
    }

    private void config() {
        creditCoeff = Math.random() * 0.202 + 0.002;
        System.out.printf("Your credit coefficient rate is %f.\n", creditCoeff);

        debtCoeff = Math.random() * 2 + 1;
        System.out.printf("Your debt coefficient rate is %f.\n", debtCoeff);

        penaltyCoeff = Math.random() * 0.11 + 0.01;
        System.out.printf("Your penalty coefficient rate is %f.\n", penaltyCoeff);
    }

    /*
        private int inputInt(String helper) {
            return inputInt(helper, 6, 30);
        }

        private int inputInt(String helper, int lower, int higher) {
            int result = lower - 1;
            do {
                try {
                    System.out.printf("Enter %s: ", helper);
                    result = sc.nextInt();
                } catch (Exception ignored) {
                }
            } while (result < lower || result > higher);
            return result;
        }
    */
    private void generateField() {
        placeEmptyCells();
        System.out.println(Arrays.toString(cells));
        placeBankCells();
        System.out.println(Arrays.toString(cells));
        placeTaxiCells();
        System.out.println(Arrays.toString(cells));
        placePenaltyCells();
        System.out.println(Arrays.toString(cells));
        placeShopCells();
        System.out.println(Arrays.toString(cells));
        renderField();
    }

    private void renderField() {
        for (int i = 0; i < width; i++) {
            System.out.print(cells[i].toSymbol());
        }
        System.out.println();
        for (int i = 1; i < height - 1; i++) {
            System.out.print(cells[cells.length - i].toSymbol());
            char[] spaces = new char[width - 2];
            Arrays.fill(spaces, ' ');
            System.out.print(spaces);
            System.out.println(cells[width + i - 1].toSymbol());
        }
        for (int i = 0; i < width; i++) {
            System.out.print(cells[width + height + i - 2].toSymbol());
        }
    }

    private void placeEmptyCells() {
        cells[0] = new MonopolyEmptyCell();
        cells[width - 1] = new MonopolyEmptyCell();
        cells[width - 1 + height - 1] = new MonopolyEmptyCell();
        cells[2 * (width - 1) + height - 1] = new MonopolyEmptyCell();
    }

    private void placeBankCells() {
        int[] indexes = {
                getRandomInt(width - 2, 1),
                getRandomInt(height - 2, width),
                getRandomInt(width - 2, height),
                getRandomInt(height - 2, width)
        };
        for (int index : indexes) cells[index] = new MonopolyBankCell(sberbank);
    }

    private int getRandomInt(int length, int begin) {
        int rnd;
        do {
            rnd = (int) Math.floor(Math.random() * length) + begin;
        } while (cells[rnd] != null);
        return rnd;
    }

    private void placeShopCells() {
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] == null)
                cells[i] = new MonopolyShopCell();
        }
    }

    private ArrayList<Integer> getRandomIndexes() {
        int[] amounts = IntStream.generate(() -> getRandomInt(2, 0)).limit(4).toArray();
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < amounts[0]; i++) indexes.add(getRandomInt(width - 2, 1));
        for (int i = 0; i < amounts[1]; i++) indexes.add(getRandomInt(height - 2, width));
        for (int i = 0; i < amounts[2]; i++) indexes.add(getRandomInt(width - 2, height));
        for (int i = 0; i < amounts[3]; i++) indexes.add(getRandomInt(height - 2, width));
        return indexes;
    }

    private void placeTaxiCells() {
        for (int index : getRandomIndexes()) cells[index] = new MonopolyTaxiCell();
    }

    private void placePenaltyCells() {
        for (int index : getRandomIndexes()) cells[index] = new MonopolyPenaltyCell(penaltyCoeff);
    }
}
