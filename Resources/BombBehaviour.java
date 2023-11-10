import java.util.Random;

/**
 * Handles the behavior associated with a bomb in the game,
 * such as determining if a square has a bomb and counting consecutive bombs.
 */
class BombBehavior {
    private static final Random random = new Random();
    private final boolean hasBomb;
    private static final int MAX_STEPS = 8;

    public BombBehavior(int mineProbability) {
        this.hasBomb = random.nextInt(mineProbability) == 0;
    }

    public boolean hasBomb() {
        return hasBomb;
    }

    /**
     * Counts all consecutive bombs in every direction from this square until a
     * non-bomb square is found or the maximum number of steps is reached.
     *
     * @param board The game board to check for bombs.
     * @param xLocation The x-coordinate of the starting square.
     * @param yLocation The y-coordinate of the starting square.
     * @param directionOffsets The offsets to check surrounding squares.
     * @return The total count of consecutive bombs in all directions.
     */
    public int countConsecutiveBombs(GameBoard board, int xLocation, int yLocation, int[][] directionOffsets) {
        int bombCount = 0;

        // Check each direction for consecutive bombs.
        for (int[] offset : directionOffsets) {
            bombCount += countBombsInDirection(board, xLocation, yLocation, offset[0], offset[1]);
        }

        return bombCount;
    }

    /**
     * Counts the number of consecutive bombs in a given direction from this square,
     * up to a maximum of 8 steps.
     *
     * @param board The game board to check for bombs.
     * @param xLocation The x-coordinate of the starting square.
     * @param yLocation The y-coordinate of the starting square.
     * @param deltaX The x-direction offset.
     * @param deltaY The y-direction offset.
     * @return The count of consecutive bombs in the direction until a non-bomb
     *         square is found or the maximum of 8 steps is reached.
     */
    private int countBombsInDirection(GameBoard board, int xLocation, int yLocation, int deltaX, int deltaY) {
        int bombCount = 0;
        int steps = 0; // Counter for the number of steps taken in the direction
        int checkX = xLocation + deltaX;
        int checkY = yLocation + deltaY;

        // Keep checking in this direction until we find a non-bomb, go out of bounds,
        // or reach 8 steps.
        while (steps < MAX_STEPS) {
            GameSquare square = board.getSquareAt(checkX, checkY);

            // Stop checking if out of bounds, not a BombSquare, or if it doesn't have a bomb.
            if (!(square instanceof BombSquare) || !((BombSquare) square).hasBomb()) {
                break;
            }

            // Increment the count and move to the next square in this direction.
            bombCount++;
            checkX += deltaX;
            checkY += deltaY;
            steps++; // Increment the steps counter
        }

        return bombCount;
    }
}
