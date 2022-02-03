package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import java.util.ArrayList;

public class AnswerFilter implements PixelFilter {
    private final int cRadius = 9, dist = (2 * cRadius) + 20; // radius of bubbles and space between each bubble
    private final int xODist = 285; // dimensions of outer box
    private final int startingX = 75, startingY = 455;
    private final int endingX = 4 * xODist + startingX, endingY = 25 * dist + startingY;
    private final int numAnswers = 5;


    public AnswerFilter() {
        // System.out.println("Filter running...");
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] pixels = img.getBWPixelGrid();
        short[][][] cChannels = {img.getRedChannel(), img.getGreenChannel(), img.getBlueChannel()};
        ArrayList<String> answers = new ArrayList<>();

        int count = 0;

        for (int c = startingX; c < endingX; c += xODist) {
            for (int r = startingY; r < endingY; r += dist) {
                String ans = getAnswer(pixels, r, c);
                answers.add(ans);
                makeBox(cChannels, r, c);
                // if (count == 47) System.out.println(c + " " + r);
                // count++;
            }
        }
        System.out.println(answers.get(47));
        img.setColorChannels(cChannels[0], cChannels[1], cChannels[2]);
        return img;
    }

    private String getAnswer(short[][] pixels, int row, int col) {
        String[] answers = {"A", "B", "C", "D", "E"};
        ArrayList<Integer> sums = new ArrayList<>();
        for (int c = col + 60; c < col + 60 + (dist * numAnswers); c += dist) {
            int sum = getSumOfNWPixels(pixels, row + cRadius, c);
            // System.out.println((row + cRadius) + " " + (c) + " ");
            sums.add(sum);
        }
        // if (row == 1291 && col == 360) System.out.println(sums);
        int maxSum = 0;

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

    private void makeBox(short[][][] iChannels, int row, int col) {
        int[] boxColor = {255, 0, 0};
        for (int c = 0; c < boxColor.length; c++) {
            for (int i = row; i <= row + dist; i++) {
                iChannels[c][i][col] = (short) boxColor[c];
                iChannels[c][i][col + xODist] = (short) boxColor[c];
            }

            for (int i = col; i <= col + xODist; i++) {
                iChannels[c][row][i] = (short) boxColor[c];
                iChannels[c][row + dist][i] = (short) boxColor[c];
            }

        }
    }
}
