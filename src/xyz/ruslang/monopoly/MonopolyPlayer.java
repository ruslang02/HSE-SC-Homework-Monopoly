package xyz.ruslang.monopoly;

public abstract class MonopolyPlayer {
    double money;
    int position = 0;
    String name;
    public MonopolyPlayer(String name) {
        this.name = name;
    }
    void move(int distance) {
        position += distance;
    }
    void takeMoney(double money) {
        this.money -= money;
        if (this.money <= 0) lose();
    }
    void lose() {
        System.out.println("Player " + name + " lost!");
    }
}
