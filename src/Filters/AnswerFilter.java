package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import java.util.ArrayList;

public class AnswerFilter {
    private final int cRadius = 9, dist = (2 * cRadius) + 20; // radius of bubbles and space between each bubble
    private final int xODist = 281; // dimensions of outer box
    private final int startingX = 83, startingY = 455;
    private final int endingX = 4 * xODist + startingX, endingY = 25 * dist + startingY;
    private final int numAnswers = 5;


    public DImage processImage(DImage img, ArrayList<String> answers, ArrayList<String> IDs) {
        short[][] pixels = img.getBWPixelGrid();
        short[][] outputPixels = new short[pixels.length / 2][pixels[0].length / 2];
        short[][][] cChannels = {img.getRedChannel(), img.getGreenChannel(), img.getBlueChannel()};

        generateAnswers(pixels, answers, cChannels);
        generateIDs(pixels, IDs);

        downsampleImage(pixels, outputPixels);
        img.setPixels(outputPixels);
        return img;
    }

    private void generateIDs(short[][] pixels, ArrayList<String> IDs) {
        // Student ID
        IDs.add(getStudentID(pixels));

        // Teacher ID
        IDs.add(getTeacherID(pixels));
    }
    

    private String getTeacherID(short[][] pixels) {
        String teacherID = "";
        for (int r = 330; r < 330 + (cRadius * 2 * 4) + (3 * 5); r += (cRadius * 2) + 5) {
            ArrayList<Integer> sums = new ArrayList<>();
            for (int c = 640; c < 640 + (cRadius * 2 * 10) + (35 * 9); c += 54) {
                int sum = getSumOfNWPixels(pixels, r, c);
                sums.add(sum);
            }

            int maxSum = 0;

            for (int i = 0; i < sums.size(); i++) {
                if (sums.get(i) > sums.get(maxSum)) maxSum = i;
            }

            teacherID += (maxSum+1);
        }
        return teacherID;
    }

    private String getStudentID(short[][] pixels) {
        String studentId = "";
        for (int r = 330; r < 330 + (cRadius * 2 * 4) + (3 * 5); r += (cRadius * 2) + 5) {
            ArrayList<Integer> sums = new ArrayList<>();
            for (int c = 80; c < 80 + (cRadius * 2 * 10) + (35 * 9); c += 54) {
                int sum = getSumOfNWPixels(pixels, r, c);
                sums.add(sum);
            }

            int maxSum = 0;

            for (int i = 0; i < sums.size(); i++) {
                if (sums.get(i) > sums.get(maxSum)) maxSum = i;
            }

            studentId += (maxSum+1);
        }
        return studentId;
    }

    private void generateAnswers(short[][] pixels, ArrayList<String> answers, short[][][] cChannels) {
        int count = 1;

        for (int c = startingX; c < endingX; c += xODist) {
            for (int r = startingY; r < endingY; r += dist) {
                String ans = getAnswer(pixels, r - calcMoE(count), c, count);
                answers.add(ans);
                makeBox(cChannels, r, c);
                count++;
            }
        }
    }

    private String getAnswer(short[][] pixels, int row, int col, int debug) {
        String[] answers = {"A", "B", "C", "D", "E"};
        ArrayList<Integer> sums = new ArrayList<>();
        for (int c = col + 60; c < col + 60 + (dist * numAnswers); c += dist) {
            int sum = getSumOfNWPixels(pixels, row + cRadius, c);
            // if (debug == 74) System.out.println((row + cRadius) + " " + (c) + " ");
            sums.add(sum);
        }

        // if (debug == 50) System.out.println(sums);
        int maxSum = 0;

        for (int i = 0; i < sums.size(); i++) {
            if (sums.get(i) > sums.get(maxSum)) maxSum = i;
        }
        return answers[maxSum];
    }

    private int calcMoE(int num) {
        if (num > 25 && num < 50) num -= 25;
        else if (num < 75) num -= 50;
        else if (num < 100) num -= 75;

        double expo = (-(0.20802) * num) + 6.41132;
        double numerator = 253.996, denominator = 22.1472 + Math.pow(Math.E, expo);
        return (int) Math.round(numerator / denominator);
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

    private void downsampleImage(short[][] pixels, short[][] outputPixels) {
        int rc = 0;
        int cc = 0;

        for (int r = 0; r < pixels.length - 1; r += 2) {
            for (int c = 0; c < pixels[r].length - 1; c += 2) {
                outputPixels[rc][cc] = pixels[r][c];
                cc++;
            }
            rc++;
            cc = 0;
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
