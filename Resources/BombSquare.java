import java.util.Random;

/**
 * This class represents a square on a game board that may contain a bomb.
 * It extends GameSquare and defines the behavior and properties of a bomb
 * square.
 */
public class BombSquare extends GameSquare {
	private static final String IMAGE_PATH = "images/";
	private static final int MINE_PROBABILITY = 10;
	private static final Random random = new Random(); // Shared Random instance

	private final boolean hasBomb;
	private boolean isRevealed = false;

	private static final int[][] directionOffsets = { // Direction offsets array
			{ -1, -1 }, { -1, 0 }, { -1, 1 },
			{ 0, -1 }, 			   { 0, 1 },
			{ 1, -1 }, { 1, 0 },   { 1, 1 }
	};

	/**
	 * Constructor for BombSquare that initializes the square with an image and
	 * determines if it has a bomb.
	 *
	 * @param x     the x-coordinate on the game board
	 * @param y     the y-coordinate on the game board
	 * @param board the game board this square belongs to
	 */
	public BombSquare(int x, int y, GameBoard board) {
		super(x, y, IMAGE_PATH + "blank.png", board);
		this.hasBomb = random.nextInt(MINE_PROBABILITY) == 0;
	}

	/**
	 * Handles the click action on the square, revealing the square's content.
	 */
	@Override
	public void clicked() {
		if (isRevealed) {
			return;
		}
		isRevealed = true;
		setImage(IMAGE_PATH + (hasBomb ? "bomb.png" : countConsecutiveBombs() + ".png"));
		if (!hasBomb && countConsecutiveBombs() == 0) {
			revealSurroundingSquares();
		}
	}

	/**
	 * Counts all consecutive bombs in every direction from this square until a
	 * non-bomb square is found.
	 *
	 * @return the total count of consecutive bombs in all directions.
	 */
	private int countConsecutiveBombs() {
		int bombCount = 0;

		// Check each direction for consecutive bombs.
		for (int[] offset : directionOffsets) {
			bombCount += countBombsInDirection(offset[0], offset[1]);
		}

		return bombCount;
	}

	/**
	 * Counts the number of consecutive bombs in a given direction from this square.
	 *
	 * @param deltaX The x-direction offset.
	 * @param deltaY The y-direction offset.
	 * @return The count of consecutive bombs in the direction until a non-bomb
	 *         square is found.
	 */
	private int countBombsInDirection(int deltaX, int deltaY) {
		int bombCount = 0;
		int checkX = xLocation + deltaX;
		int checkY = yLocation + deltaY;

		// Keep checking in this direction until we find a non-bomb or go out of bounds.
		while (true) {
			GameSquare square = board.getSquareAt(checkX, checkY);

			// Stop checking if out of bounds or not a BombSquare, or if it doesn't have a
			// bomb.
			if (!(square instanceof BombSquare) || !((BombSquare) square).hasBomb) {
				break;
			}

			// Increment the count and move to the next square in this direction.
			bombCount++;
			checkX += deltaX;
			checkY += deltaY;
		}

		return bombCount;
	}

	/**
	 * Reveals the surrounding squares if they are not bombs and not already
	 * revealed.
	 */
	private void revealSurroundingSquares() {
		for (int[] offset : directionOffsets) {
			GameSquare neighbor = board.getSquareAt(xLocation + offset[0], yLocation + offset[1]);
			if (neighbor instanceof BombSquare && !((BombSquare) neighbor).isRevealed) {
				neighbor.clicked();
			}
		}
	}

	public boolean hasBomb() {
		return hasBomb;
	}

	public boolean isRevealed() {
		return isRevealed;
	}
}
