
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Collections;

public class SlidingPuzzleGame extends JFrame {
    private JPanel puzzlePanel;
    private BufferedImage fullImage;
    private int gridSize;
    private ImageTile[][] tiles;
    private int emptyRow, emptyCol;
    private static final String DEFAULT_IMAGE_PATH = "tom&jerry.jpg"; // Update with your default image path

    public SlidingPuzzleGame(String imagePath, int gridSize, boolean numberingEnabled) {
        this.gridSize = gridSize;
        this.fullImage = loadImage(imagePath);
        
        if (this.fullImage == null) {
            JOptionPane.showMessageDialog(this, "Failed to load image, using default image.");
            this.fullImage = loadImage(DEFAULT_IMAGE_PATH);
        }

        createPuzzle(fullImage.getWidth() / gridSize, numberingEnabled);
        setUpPuzzlePanel();
    }

    private BufferedImage loadImage(String path) {
        try {
            File imageFile = new File(path);
            if (imageFile.exists()) {
                return ImageIO.read(imageFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if image loading fails
    }

    private void createPuzzle(int tileSize, boolean numberingEnabled) {
        tiles = new ImageTile[gridSize][gridSize];
        ArrayList<ImageTile> tileList = new ArrayList<>();

        // Create tiles
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (row == gridSize - 1 && col == gridSize - 1) {
                    tiles[row][col] = null; // Leave empty tile
                    emptyRow = row;
                    emptyCol = col;
                } else {
                    BufferedImage subImage = fullImage.getSubimage(col * tileSize, row * tileSize, tileSize, tileSize);
                    ImageTile tile = new ImageTile(subImage, row * gridSize + col, numberingEnabled);
                    tiles[row][col] = tile;
                    tileList.add(tile);

                    // Add a mouse listener to detect clicks on the tiles
                    tile.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            handleTileClick(tile);
                        }
                    });
                }
            }
        }

        // Shuffle tiles
        Collections.shuffle(tileList);
        
        // Add shuffled tiles to the puzzle
        int index = 0;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (row == gridSize - 1 && col == gridSize - 1) {
                    // Leave empty space for the last tile
                    tiles[row][col] = null; 
                } else {
                    tiles[row][col] = tileList.get(index++);
                }
            }
        }
    }

    private void setUpPuzzlePanel() {
        puzzlePanel = new JPanel(new GridLayout(gridSize, gridSize));
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (tiles[row][col] != null) {
                    puzzlePanel.add(tiles[row][col]);
                } else {
                    puzzlePanel.add(new JPanel()); // Empty space for the last tile
                }
            }
        }
        add(puzzlePanel, BorderLayout.CENTER);
        setTitle("Sliding Puzzle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Handle tile clicks and swapping
    private void handleTileClick(ImageTile clickedTile) {
        int clickedRow = -1;
        int clickedCol = -1;

        // Find the clicked tile's position in the grid
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (tiles[row][col] == clickedTile) {
                    clickedRow = row;
                    clickedCol = col;
                }
            }
        }

        // Check if the clicked tile is adjacent to the empty space
        if (isAdjacent(clickedRow, clickedCol, emptyRow, emptyCol)) {
            // Swap the clicked tile with the empty space
            tiles[emptyRow][emptyCol] = clickedTile;
            tiles[clickedRow][clickedCol] = null;

            emptyRow = clickedRow;
            emptyCol = clickedCol;

            // Repaint the puzzle panel after the swap
            puzzlePanel.removeAll();
            setUpPuzzlePanel();
            puzzlePanel.revalidate();
            puzzlePanel.repaint();

            // Check if the puzzle is solved
            if (isPuzzleSolved()) {
                JOptionPane.showMessageDialog(this, "Congratulations! You've solved the puzzle!");
            }
        }
    }

    // Check if two tiles are adjacent
    private boolean isAdjacent(int row1, int col1, int row2, int col2) {
        return (Math.abs(row1 - row2) == 1 && col1 == col2) || (Math.abs(col1 - col2) == 1 && row1 == row2);
    }

    // Check if the puzzle is solved
    private boolean isPuzzleSolved() {
        int index = 0;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (tiles[row][col] != null && tiles[row][col].getCorrectIndex() != index) {
                    return false;
                }
                index++;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PuzzleSettings());
    }
}
