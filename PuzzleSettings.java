import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PuzzleSettings extends JFrame {
    private JComboBox<String> boardSizeComboBox;
    private JButton chooseImageButton;
    private JCheckBox numberingCheckBox; 
    private JLabel selectedImageLabel;
    private String selectedImagePath;
    private int boardSize = 4; // Default to 4x4 grid
    private boolean numberingEnabled = false;

    public PuzzleSettings() {
        setTitle("Puzzle Settings");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 1 ));
        setFont(new Font("Arial", Font.BOLD, 50));


        // Board size selection
        JLabel boardSizeLabel = new JLabel("Select Board Size:");
        boardSizeComboBox = new JComboBox<>(new String[]{"3x3", "4x4"});
        boardSizeComboBox.setSelectedIndex(0); // Default to 4x4

        // Image selection button
        JLabel imageLabel = new JLabel("Choose Image:");
        chooseImageButton = new JButton("Select Image");
        selectedImageLabel = new JLabel("No image selected");


        chooseImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    selectedImagePath = selectedFile.getAbsolutePath();
                    selectedImageLabel.setText(selectedFile.getName());
                }
            }
        });

        // Numbering hint checkbox
        numberingCheckBox = new JCheckBox("Enable numbering hints");

        // Save/Apply button
        JButton applyButton = new JButton("Apply Settings");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardSize = boardSizeComboBox.getSelectedIndex() == 0 ? 3 : 4;
                numberingEnabled = numberingCheckBox.isSelected();
                if (selectedImagePath != null) {
                    new SlidingPuzzleGame(selectedImagePath, boardSize, numberingEnabled);
                } else {
                    String defaultImagePath = getClass().getResource("tom&jerry.jpg").getPath();
                    new SlidingPuzzleGame(defaultImagePath, boardSize, numberingEnabled);

                }
                dispose(); // Close settings window after applying settings
            }
        });

        // Add components to frame
        add(boardSizeLabel);
        add(boardSizeComboBox);
        add(imageLabel);
        add(chooseImageButton);
        add(selectedImageLabel);
        add(numberingCheckBox);
        add(applyButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PuzzleSettings::new);
    }
}
