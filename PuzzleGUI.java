import javax.swing.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PuzzleGUI {
    private LinePuzzle puzzle;
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel homePanel;
    private JPanel gamePanel;
    private JPanel buttonPanel;
    private JPanel difficultyPanel;
    private JPanel headerPanel;
    private JPanel panel;
    private JLabel correctIncorrectLabel;
    private List<Line> horizontalLines;
    private List<Integer> verticalLines;
    private int[] puzzleNumbers; 
    private JButton checkPuzzleButton;
    private JButton clearLinesButton;
    private JButton changeDifficultyButton;
    private JButton exitButton;
    private JButton solvePuzzleButton;
    private int puzzleDifficulty;
    private int score = 0; 
    private JLabel scoreLabel;

    private class Line {
        final int x1, y1, x2, y2;

        public Line(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public void draw(Graphics g) {
            g.drawLine(x1, y1, x2, y2);
        }
    }

    public PuzzleGUI() {
        puzzle = new LinePuzzle();
        horizontalLines = new ArrayList<>();
        verticalLines = new ArrayList<>();

        frame = new JFrame("Japanese Ladders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        homePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Japanese Ladders", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.insets = new Insets(5, 5, 5, 5); 
        homePanel.add(titleLabel, gbc);

        gbc.insets = new Insets(5, 0, 5, 0); 
        homePanel.add(Box.createVerticalStrut(20), gbc); 

        JButton startButton = new JButton("Start");
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startButton.addActionListener(e -> cardLayout.show(cardPanel, "Difficulty"));
        homePanel.add(startButton, gbc);

        difficultyPanel = new JPanel(new GridBagLayout());
        GridBagConstraints GBC = new GridBagConstraints();
        GBC.gridwidth = GridBagConstraints.REMAINDER;
        GBC.fill = GridBagConstraints.HORIZONTAL;

        JLabel difficultyLabel = new JLabel("Select a difficulty: ", SwingConstants.CENTER);
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, 24));
        GBC.insets = new Insets(5, 5, 5 , 5);
        difficultyPanel.add(difficultyLabel, GBC);

        GBC.insets = new Insets(5, 0, 5, 0);
        difficultyPanel.add(Box.createVerticalStrut(20), GBC);

        JButton easyButton = new JButton("Easy");
        easyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        easyButton.addActionListener(e -> {
            puzzleNumbers = puzzle.generatePuzzle(1);
            puzzleDifficulty = 1;
            cardLayout.show(cardPanel, "Game");
            setupNewPuzzle();
        });
        difficultyPanel.add(easyButton, GBC);

        JButton mediumButton = new JButton("Medium");
        mediumButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mediumButton.addActionListener(e -> {
            puzzleNumbers = puzzle.generatePuzzle(2);
            puzzleDifficulty = 2;
            cardLayout.show(cardPanel, "Game");
            setupNewPuzzle();
        });
        difficultyPanel.add(mediumButton, GBC);

        JButton hardButton = new JButton("Hard");
        hardButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hardButton.addActionListener(e -> {
            puzzleNumbers = puzzle.generatePuzzle(3);
            puzzleDifficulty = 3;
            cardLayout.show(cardPanel, "Game"); 
            setupNewPuzzle();
        });
        difficultyPanel.add(hardButton, GBC);

        gamePanel = new JPanel(new BorderLayout());
        headerPanel = new JPanel(new BorderLayout());

        exitButton = new JButton("   ← Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 15));
        exitButton.addActionListener(e -> exitApplication());
        exitButton.setBorder(null);
        exitButton.setContentAreaFilled(false);
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        headerPanel.add(exitButton, BorderLayout.WEST);

        correctIncorrectLabel = new JLabel("");
        correctIncorrectLabel.setHorizontalAlignment(JLabel.CENTER);
        correctIncorrectLabel.setFont(new Font("Arial", Font.BOLD, 15));
        correctIncorrectLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        headerPanel.add(correctIncorrectLabel, BorderLayout.CENTER);

        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setHorizontalAlignment(JLabel.RIGHT);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 15));
        scoreLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        headerPanel.add(scoreLabel, BorderLayout.EAST);

        gamePanel.add(headerPanel, BorderLayout.NORTH);
        initializeUI();

        cardPanel.add(homePanel, "Home");
        cardPanel.add(difficultyPanel, "Difficulty");
        cardPanel.add(gamePanel, "Game");

        frame.add(cardPanel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        cardLayout.show(cardPanel, "Home");
    }

    private void initializeUI() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawLines(g);
                for (Line line : horizontalLines) {
                    line.draw(g);
                }
            }
        };

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                handleMouseClick(e);
                panel.repaint();
            }
        });

        buttonPanel = new JPanel();
        gamePanel.add(buttonPanel, BorderLayout.SOUTH);

        checkPuzzleButton = new JButton("Check Puzzle");
        checkPuzzleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        checkPuzzleButton.addActionListener(e -> {
			try {
				checkPuzzle();
			} catch (InterruptedException e1) {

				e1.printStackTrace();
			}
		});
        buttonPanel.add(checkPuzzleButton);

        clearLinesButton = new JButton("Clear Lines");
        clearLinesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearLinesButton.addActionListener(e -> clearLines());
        buttonPanel.add(clearLinesButton);

        changeDifficultyButton = new JButton("Change Difficulty");
        changeDifficultyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        changeDifficultyButton.addActionListener(e -> cardLayout.show(cardPanel, "Difficulty"));
        buttonPanel.add(changeDifficultyButton);
        
        solvePuzzleButton = new JButton("Solve Puzzle");
        solvePuzzleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        solvePuzzleButton.addActionListener(e -> SolvePuzzle());
        buttonPanel.add(solvePuzzleButton);

        gamePanel.add(panel);
    }
    
    private void SolvePuzzle() {
        // Clear any existing lines
        horizontalLines.clear();

        // The correct order is the natural ascending order of numbers
        int[] correctOrder = IntStream.rangeClosed(1, puzzleNumbers.length).toArray();

        // Simulate the ascent to find the positions where horizontal lines should be drawn
        simulateAscent(correctOrder);

        // Redraw the panel to show the solution
        panel.repaint();
    }
    
    private void simulateAscent(int[] correctOrder) {
        // Array to hold the current index positions of the numbers
        int[] indices = new int[correctOrder.length];
        for (int i = 0; i < correctOrder.length; i++) {
            // Find the index of each number as per correctOrder in the currentPositions
            indices[i] = findIndex(puzzleNumbers, correctOrder[i]);
        }

        int yTop = 100;
        int yBottom = 200;
        int yCurrent = yTop;
        int yIncrement = (yBottom - yTop) / calculateMinimumLines(puzzleNumbers);

        // Iterate over each pair of vertical lines
        for (int i = 0; i < indices.length - 1; i++) {
            for (int j = 0; j < indices.length - i - 1; j++) {
                // Check if the numbers are out of order
                if (indices[j] > indices[j + 1]) {
                    // Draw a horizontal line between the two vertical lines
                    int x1 = verticalLines.get(j);
                    int x2 = verticalLines.get(j + 1);
                    horizontalLines.add(new Line(x1, yCurrent, x2, yCurrent));

                    // Swap the indices to reflect the crossing of the lines
                    int temp = indices[j];
                    indices[j] = indices[j + 1];
                    indices[j + 1] = temp;

                    // Increment yCurrent for the next line
                    yCurrent += yIncrement;
                }
            }
        }
    }

    // Helper method to find the index of a value in an array
    private int findIndex(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1; // Value not found
    }
    
    private void drawLines(Graphics g) {
        int panelWidth = panel.getWidth();
        int spacing = panelWidth / (puzzleNumbers.length + 1);
        int yTop = 90;
        int yBottom = 190;

        for (int i = 0; i < puzzleNumbers.length; i++) {
            int x = spacing * (i + 1);
            g.drawLine(x, yTop, x, yBottom);
            g.drawString(Integer.toString(i + 1), x - 3, yTop - 10); 
            g.drawString(Integer.toString(puzzleNumbers[i]), x - 3, yBottom + 20); 
            verticalLines.add(x);
        }
    }
    
    public int calculateMinimumLines(int[] lines) {
        int total = 0;
        for (int i = 0; i < lines.length; i++) {
            for (int j = i + 1; j < lines.length; j++) {
                if (lines[j] < lines[i]) {
                    total++;
                }
            }
        }
        return total;
    }

    private void clearLines() {
    	horizontalLines.clear();
    	panel.repaint();
    }

    private void exitApplication() {
    	System.exit(0);
    }

    private void handleMouseClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        int panelWidth = panel.getWidth();
        int spacing = panelWidth / (puzzleNumbers.length + 1);
        int yTop = 90;
        int yBottom = 190;

        if (mouseY >= yTop && mouseY <= yBottom) {

            for (int i = 0; i < puzzleNumbers.length - 1; i++) {
                int x1 = spacing * (i + 1);
                int x2 = spacing * (i + 2);

                if (mouseX > x1 && mouseX < x2) {

                    horizontalLines.add(new Line(x1, mouseY, x2, mouseY));
                    break;
                }
            }
        }
    }

    private void changeCorrectLabel(boolean correct) throws InterruptedException {
    	if (correct) {
    		correctIncorrectLabel.setText("Correct!");
    		correctIncorrectLabel.paintImmediately(correctIncorrectLabel.getVisibleRect());
    		panel.repaint();
    		TimeUnit.SECONDS.sleep(1);
    		correctIncorrectLabel.setText("");
    		panel.repaint();
    	} else {
    		correctIncorrectLabel.setText("Incorrect");
    		correctIncorrectLabel.paintImmediately(correctIncorrectLabel.getVisibleRect());
    		panel.repaint();
    		TimeUnit.SECONDS.sleep(1);
    		correctIncorrectLabel.setText("");
    		panel.repaint();
    	}
    }

    private void checkPuzzle() throws InterruptedException {
        int[] resultPositions = simulateDescent(puzzleNumbers, horizontalLines);
        boolean isCorrect = Arrays.equals(resultPositions, puzzleNumbers); 

        if (isCorrect) {
        	changeCorrectLabel(true);
            increaseScore();
            setupNewPuzzle();
        } else {
        	changeCorrectLabel(false);
        	resetScore();
        }

        updateScoreLabel();
    }

    private void setupNewPuzzle() {

        horizontalLines.clear();
        verticalLines.clear();

        puzzleNumbers = puzzle.generatePuzzle(puzzleDifficulty);

        panel.repaint();
    }

    private int[] simulateDescent(int[] puzzleNumbers, List<Line> horizontalLines) {
        int[] finalPositions = new int[puzzleNumbers.length];

        Arrays.fill(finalPositions, -1);

        horizontalLines.sort(Comparator.comparingInt(line -> line.y1));

        for (int numIndex = 0; numIndex < puzzleNumbers.length; numIndex++) {
            int currentPosition = numIndex; 
            int currentLine = verticalLines.get(numIndex);
            int lineVariance = 0;

            for (Line line : horizontalLines) {

                if (currentLine == line.x1) {
                    currentPosition++; 
                    lineVariance++;
                    currentLine = verticalLines.get(numIndex + lineVariance);
                } else if (currentLine == line.x2) {
                    currentPosition--; 
                    lineVariance--;
                    currentLine = verticalLines.get(numIndex + lineVariance);
                }
            }

            finalPositions[currentPosition] = numIndex + 1;
        }

        return finalPositions;
    }

    private void increaseScore() {
        score++;
        updateScoreLabel();
    }

    private void resetScore() {
        score = 0;
        updateScoreLabel();
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score); 
        scoreLabel.revalidate(); 
        scoreLabel.repaint(); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PuzzleGUI());
    }
}