package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        GameView view = new GameView();
        view.printEmptyBoard();

        System.out.println("Enter the coordinates of the ship:");
        try (Scanner scanner = new Scanner(System.in)) {
            String inputStart = scanner.next();
            String inputEnd = scanner.next();

            Coordinate start = Coordinate.parse(inputStart);
            Coordinate end = Coordinate.parse(inputEnd);

            Ship ship = Ship.create(start, end);

            System.out.println("Length: " + ship.length());
            System.out.println("Parts: " + ship.getFormatedParts());

        } catch (IllegalArgumentException e) {
            System.out.println("Error! \n" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error! \nUnexpected input.");
        }
    }
}

class Ship {
    private final List<Coordinate> parts;

    private Ship(List<Coordinate> parts) {
        this.parts = parts;
    }

    public static Ship create(Coordinate start, Coordinate end) {
        if (start.row() != end.row() && start.col() != end.col()) {
            throw new IllegalArgumentException("Ship must be placed either horizontally or vertically");
        }

        List<Coordinate> calculatedParts = new ArrayList<>();

        int rowStep = Integer.compare(end.row(), start.row());
        int colStep = Integer.compare(end.col(), start.col());

        int length = Math.max(
                Math.abs(start.row() - end.row()),
                Math.abs(start.col() - end.col())
        ) + 1;

        int currentRow = start.row();
        int currentCol = start.col();

        for (int i = 0; i < length; i++) {
            calculatedParts.add(new Coordinate(currentRow, currentCol));
            currentRow += rowStep;
            currentCol += colStep;
        }

        return new Ship(calculatedParts);
    }

    public int length() {
        return parts.size();
    }

    public String getFormatedParts() {
        return parts.stream()
                .map(Coordinate::toNotation)
                .collect(Collectors.joining(" "));
    }
}

record Coordinate(int row, int col) {
    public static Coordinate parse(String input) {
        if (input == null || input.length() > 3 || input.length() < 2) {
            throw new IllegalArgumentException("Invalid coordinate format");
        }

        char rowChar = input.toUpperCase().charAt(0);
        if (rowChar < 'A' || rowChar > 'J') {
            throw new IllegalArgumentException("Row must be between A and J");
        }

        int row = rowChar - 'A';
        int col;

        try {
            col = Integer.parseInt(input.substring(1)) - 1;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Column must be a number");
        }

        if (col < 0 || col > 9) {
            throw new IllegalArgumentException("Column must be between 1 and 10");
        }

        return new Coordinate(row, col);
    }

    public String toNotation() {
        return String.format("%c%d", ('A' + row), col + 1);
    }
}

class GameView {
    public static final int BOARD_SIZE = 10;

    public void printEmptyBoard() {
        System.out.print(" ");
        for (int i = 1; i <= BOARD_SIZE; i++) {
            System.out.print(" " + i);
        }
        System.out.println();

        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((char) ('A' + i));
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(" ~");
            }
            System.out.println();
        }
    }
}