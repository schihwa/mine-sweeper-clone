/**
 * Represents a square on a game board that may contain a bomb.
 * It extends GameSquare and uses BombBehavior for bomb-related functionality.
 */
public class BombSquare extends GameSquare {
	// Path to the directory containing image assets.
	private static final String IMAGE_PATH = "images/";

	// The probability (1 in MINE_PROBABILITY) that this square will contain a bomb.
	private static final int MINE_PROBABILITY = 10;

	// BombBehavior instance to handle bomb-related logic.
	private BombBehavior bombBehavior;

	// Flag indicating whether this square has been revealed.
	private boolean isRevealed = false;

	// Offsets used to check the surrounding squares in all eight directions.
	private static final int[][] directionOffsets = {
			{ -1, -1 }, { -1, 0 }, { -1, 1 },
			{ 0, -1 }, { 0, 1 },
			{ 1, -1 }, { 1, 0 }, { 1, 1 }
	};

	/**
	 * Constructor for BombSquare that initializes the square with an image and
	 * determines if it has a bomb.
	 *
	 * @param x     the x-coordinate of the square on the game board.
	 * @param y     the y-coordinate of the square on the game board.
	 * @param board the game board on which this square is placed.
	 */
	public BombSquare(int x, int y, GameBoard board) {
		// Call to the superclass constructor to set the square's position and default
		// image.
		super(x, y, IMAGE_PATH + "blank.png", board);

		// Initialize the BombBehavior with the defined probability.
		this.bombBehavior = new BombBehavior(MINE_PROBABILITY);
	}

	/**
	 * Handles the click action on this square. It reveals the square and updates
	 * its image based on whether it contains a bomb or the count of adjacent bombs.
	 */
	@Override
	public void clicked() {
		// Check if the square is already revealed; if so, do nothing.
		if (isRevealed) {
			return;
		}

		// Mark the square as revealed.
		isRevealed = true;

		// Update the square's image. If it has a bomb, show the bomb image.
		if (bombBehavior.hasBomb()) {
			setImage(IMAGE_PATH + "bomb.png");
		} else {
			// Count consecutive bombs and set the corresponding image.
			int bombCount = bombBehavior.countConsecutiveBombs(board, xLocation, yLocation, directionOffsets);
			setImage(IMAGE_PATH + bombCount + ".png");

			// If there are no adjacent bombs, reveal surrounding squares.
			if (bombCount == 0) {
				revealSurroundingSquares();
			}
		}
	}

	private void revealSurroundingSquares() {
		for (int[] offset : directionOffsets) {
			GameSquare neighbor = board.getSquareAt(xLocation + offset[0], yLocation + offset[1]);

			// Check if the neighboring square is a BombSquare and if it's not already
			// revealed.
			if (neighbor instanceof BombSquare && !((BombSquare) neighbor).isRevealed()) {
				// Trigger the click action on the neighboring square.
				neighbor.clicked();
			}
		}
	}

	/**
	 * Returns whether this square contains a bomb.
	 *
	 * @return true if it contains a bomb, false otherwise.
	 */
	public boolean hasBomb() {
		return bombBehavior.hasBomb();
	}

	/**
	 * Returns whether this square has been revealed.
	 *
	 * @return true if it has been revealed, false otherwise.
	 */
	public boolean isRevealed() {
		return isRevealed;
	}
}
