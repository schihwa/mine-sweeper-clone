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
			{ 0, -1 }, { 0, 1 },
			{ 1, -1 }, { 1, 0 }, { 1, 1 }
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
		setImage(IMAGE_PATH + (hasBomb ? "bomb.png" : countSurroundingBombs() + ".png"));
		if (!hasBomb && countSurroundingBombs() == 0) {
			revealSurroundingSquares();
		}
	}

	/**
	 * Counts the number of bombs in the surrounding squares.
	 *
	 * @return the number of surrounding bombs
	 */
	private int countSurroundingBombs() {
		int bombCount = 0;
		for (int[] offset : directionOffsets) {
			bombCount += checkBombAt(xLocation + offset[0], yLocation + offset[1]) ? 1 : 0;
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

	/**
	 * Checks if there is a bomb at the specified location.
	 *
	 * @param x the x-coordinate of the square to check
	 * @param y the y-coordinate of the square to check
	 * @return true if there is a bomb, false otherwise
	 */
	private boolean checkBombAt(int x, int y) {
		GameSquare square = board.getSquareAt(x, y);
		return square instanceof BombSquare && ((BombSquare) square).hasBomb;
	}

	public boolean hasBomb() {
		return hasBomb;
	}

	public boolean isRevealed() {
		return isRevealed;
	}
}
