import java.util.Random;

/**
 * Manages the behavior related to bombs in a game. This includes
 * determining whether a square contains a bomb and counting consecutive bombs in all directions
 * from a given square.
 */
class BombBehavior {
    // Random number generator for bomb placement.
    private static final Random random = new Random();

    // Flag indicating whether this particular behavior instance represents a bomb.
    private final boolean hasBomb;

    // Maximum number of steps to count in any direction for consecutive bombs.
    private static final int MAX_STEPS = 8;

    /**
     * Constructor for BombBehavior.
     *
     * @param mineProbability Probability (1 in mineProbability) that this instance represents a bomb.
     */
    public BombBehavior(int mineProbability) {
        // Determine if this instance represents a bomb based on the given probability.
        this.hasBomb = random.nextInt(mineProbability) == 0;
    }

    /**
     * Checks whether this instance represents a bomb.
     *
     * @return true if it's a bomb, false otherwise.
     */
    public boolean hasBomb() {
        return hasBomb;
    }

    /**
     * Counts all consecutive bombs in every direction from a given square until a
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

        // Iterate through each direction to count consecutive bombs.
        for (int[] offset : directionOffsets) {
            bombCount += countBombsInDirection(board, xLocation, yLocation, offset[0], offset[1]);
        }

        return bombCount;
    }

    /**
     * Counts the number of consecutive bombs in a given direction from the starting square,
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
        int steps = 0; // Counter for the number of steps taken in this direction.
        int checkX = xLocation + deltaX;
        int checkY = yLocation + deltaY;

        // Keep checking in this direction for bombs, but stop if a non-bomb is encountered,
        // the edge of the board is reached, or the maximum step count is reached.
        while (steps < MAX_STEPS) {
            GameSquare square = board.getSquareAt(checkX, checkY);

            // Stop the count if the square is not a BombSquare or if it doesn't contain a bomb.
            if (!(square instanceof BombSquare) || !((BombSquare) square).hasBomb()) {
                break;
            }

            // Increment the bomb count and proceed to the next square in this direction.
            bombCount++;
            checkX += deltaX;
            checkY += deltaY;
            steps++; // Increment the step counter.
        }

        return bombCount;
    }
}
