import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;

public class LinePuzzle {
    private int numOfVerticalLines;
    private int minimumLines;
    private int[] randomizedNumberOrder;
    private Random random = new Random();

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

    public int[] generatePuzzle(int difficulty) {
    	switch (difficulty) {
    	case 1:
    		numOfVerticalLines = 3 + random.nextInt(3);
    		break;
    	case 2:
    		numOfVerticalLines = 5 + random.nextInt(5);
    		break;
    	case 3:
    		numOfVerticalLines = 7 + random.nextInt(7);
    		break;
    	}

        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i <= numOfVerticalLines; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        randomizedNumberOrder = list.stream().mapToInt(i -> i).toArray();

        minimumLines = calculateMinimumLines(randomizedNumberOrder);
        return randomizedNumberOrder;
    }

    public int getNumOfVerticalLines() {
        return numOfVerticalLines;
    }

    public int getMinimumLines() {
        return minimumLines;
    }
}