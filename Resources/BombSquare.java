/**
 * Represents a square on a game board that may contain a bomb.
 * It extends GameSquare and uses BombBehavior for bomb-related functionality.
 */
public class BombSquare extends GameSquare {
    private static final String IMAGE_PATH = "images/";
    private static final int MINE_PROBABILITY = 10;

    private BombBehavior bombBehavior;
    private boolean isRevealed = false;

    private static final int[][] directionOffsets = {
            { -1, -1 }, { -1, 0 }, { -1, 1 },
            { 0, -1 }, { 0, 1 },
            { 1, -1 }, { 1, 0 }, { 1, 1 }
    };

    public BombSquare(int x, int y, GameBoard board) {
        super(x, y, IMAGE_PATH + "blank.png", board);
        this.bombBehavior = new BombBehavior(MINE_PROBABILITY);
    }

    @Override
    public void clicked() {
        if (isRevealed) {
            return;
        }
        isRevealed = true;
        // Determine the image based on whether the square has a bomb or not
        if (bombBehavior.hasBomb()) {
            setImage(IMAGE_PATH + "bomb.png");
        } else {
            int bombCount = bombBehavior.countConsecutiveBombs(board, xLocation, yLocation, directionOffsets);
            setImage(IMAGE_PATH + bombCount + ".png");
        }
        // Further click logic can be implemented here
    }

    public boolean hasBomb() {
        return bombBehavior.hasBomb();
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    // Additional methods can be added here as needed
}
