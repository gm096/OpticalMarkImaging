package Filters;

import core.DImage;

import java.util.ArrayList;

public class AnswerFilter {
    private final int cRadius = 9, yDist = (2 * cRadius) + 20; // radius of bubbles and space between
    private final int xODist = 285; // dimensions of outer box
    private final int startingX = 75, startingY = 455;
    private final int numAnswers = 5;


    public AnswerFilter() {
        System.out.println("Filter running...");
    }

    public DImage processImage(DImage img, ArrayList<String> answers) {
        short[][] pixels = img.getBWPixelGrid();

        applyThreshold(pixels, 240);

        for (int c = startingX; c < pixels[0].length; c += xODist) {
            for (int r = startingY; r < pixels.length; r += yDist) {
                String ans = getAnswer(pixels, r, c);
                answers.add(ans);
            }
        }
        System.out.println(answers);
        return img;
    }

    private String getAnswer(short[][] pixels, int row, int col) {
        String[] answers = {"A", "B", "C", "D", "E"};
        ArrayList<Integer> sums = new ArrayList<>();
        for (int c = col + 60; c < col + 60 + (cRadius * 4 * numAnswers); c += cRadius * 4) {
            int sum = getSumOfNWPixels(pixels, row + cRadius, c);
            System.out.println((row + cRadius) + " " + (c) + " ");
            sums.add(sum);
        }
        int maxSum = 0;
        // System.out.println(sums);
        for (int i = 0; i < sums.size(); i++) {
            if (sums.get(i) > sums.get(maxSum)) maxSum = i;
        }
        return answers[maxSum];
    }

    private int getSumOfNWPixels(short[][] pixels, int row, int col) {
        int sum = 0;
        for (int i = row; i < row + (cRadius * 2); i++) {
            for (int j = col; j < col + (cRadius * 2); j++) {
                if (inBound(pixels, i, j)) {
                    if (pixels[i][j] != 255) sum++;
                }
            }
        }

        return sum;
    }

    public static boolean inBound(short[][] arr, int r, int c) {
        return (arr.length - 1 >= r && arr[0].length - 1 >= c && r >= 0 && c >= 0);
    }

    private void applyThreshold(short[][] pixels, int t) {
        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[0].length; c++) {
                if (pixels[r][c] > t) pixels[r][c] = 255;
                else pixels[r][c] = 0;
            }
        }
    }
}
