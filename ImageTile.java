import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class ImageTile extends JPanel {
    private BufferedImage image;
    private int originalPosition; // The position in the original image
    private boolean numberingEnabled;

    public ImageTile(BufferedImage image, int originalPosition, boolean numberingEnabled) {
        this.image = image;
        this.originalPosition = originalPosition;
        this.numberingEnabled = numberingEnabled;
        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 1, 1, getWidth(), getHeight(), this);

        // Draw numbering if enabled
        if (numberingEnabled) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString(String.valueOf(originalPosition + 1), 10, 20);
        }
    }

    public int getCorrectIndex() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCorrectIndex'");
    }
}
