package xyz.ruslang.monopoly;

import java.util.Scanner;

public class MonopolyMain {
    public static void main(String[] args) {
        int width, height, money;
        try {
            width = parseInt(args[1], 6, 30);
            height = parseInt(args[2], 6, 30);
            money = parseInt(args[3], 500, 15e3);
        } catch (NumberFormatException exception) {
            System.err.println(exception.getMessage());
            return;
        }
        new MonopolyMain(width, height, money);
    }

    private static int parseInt(String value, double lower, double higher) {
        int result = Integer.parseInt(value);
        if (result < lower || result > higher)
            throw new NumberFormatException("One of the input values were out of bounds.");
        return result;
    }

    private MonopolyCell[] fields;

    private final Scanner sc = new Scanner(System.in);

    private final int height;
    private final int width;
    private double creditCoeff;
    private double debtCoeff;
    private double penaltyCoeff;
    private double money;

    

    public MonopolyMain(int width, int height, int money) {
        this.width = width;
        this.height = height;
        this.money = money;

        config();
        fields = new MonopolyCell[width * 2 + height * 2 - 4];
        generateField();
    }

    private void config() {
        creditCoeff = Math.random() * 0.202 + 0.002;
        System.out.printf("Your credit coefficient rate is %d.", creditCoeff);

        debtCoeff = Math.random() * 2 + 1;
        System.out.printf("Your debt coefficient rate is %d.", debtCoeff);

        penaltyCoeff = Math.random() * 0.11 + 0.01;
        System.out.printf("Your penalty coefficient rate is %d.", penaltyCoeff);
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
        placeBankCells();
    }

    private void placeEmptyCells() {
        fields[0] = new MonopolyEmptyCell();
        fields[width - 1] = new MonopolyEmptyCell();
        fields[width - 1 + height - 1] = new MonopolyEmptyCell();
        fields[2 * (width - 1) + height - 1] = new MonopolyEmptyCell();
    }

    private void placeBankCells() {
        for (int i = 0; i < 4; i++) placeBankCell(i);
    }
    private void placeBankCell(int modifier) {
        int index;
        do {
            index = (int) Math.floor(Math.random() * ((modifier % 2 == 0 ? width : height) - 2));
            if (modifier == 0) index += 1;
            if (modifier >= 1) index += width;
            if (modifier >= 2) index += height;
            if (modifier >= 3) index += width;
        } while (fields[index] == null);
        fields[index] = new MonopolyBankCell();
    }
}
