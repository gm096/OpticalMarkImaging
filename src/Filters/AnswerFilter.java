package Filters;

import core.DImage;

import java.util.ArrayList;

public class AnswerFilter {
    private final int cRadius = 9; // radius of bubbles
    private final int xODist, dist; // dimensions of outer box
    private final int startingX, startingY, endingX, endingY; // starting and ending locations of the pdf
    private final int numAnswers; // answers per question


    public AnswerFilter(String[] values) {
        int rows = Integer.parseInt(values[0].trim());
        int columns = Integer.parseInt(values[1].trim());

        startingX = Integer.parseInt(values[2].trim());
        startingY = Integer.parseInt(values[3].trim());

        xODist = Integer.parseInt(values[4].trim());
        dist = Integer.parseInt(values[5].trim());

        numAnswers = Integer.parseInt(values[6].trim());

        endingX = rows * xODist + startingX;
        endingY = columns * dist + startingY;
    }

    public DImage processImage(DImage img, ArrayList<String> answers, ArrayList<String> IDs) {
        short[][] pixels = img.getBWPixelGrid();

        generateAnswers(pixels, answers);
        generateIDs(pixels, IDs);

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

            teacherID += (maxSum + 1);
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

            studentId += (maxSum + 1);
        }
        return studentId;
    }

    private void generateAnswers(short[][] pixels, ArrayList<String> answers) {
        int debug = 1;

        for (int c = startingX; c < endingX; c += xODist) {
            for (int r = startingY; r < endingY; r += dist) {
                String ans = getAnswer(pixels, r - calcMoE(debug), c, debug);
                answers.add(ans);
                debug++;
            }
        }
    }

    private String getAnswer(short[][] pixels, int row, int col, int debug) {
        String[] answers = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        ArrayList<Integer> sums = new ArrayList<>();
        for (int c = col + 55; c < col + 55 + (dist * numAnswers); c += dist) {
            int sum = getSumOfNWPixels(pixels, row + cRadius, c);
            // if (debug == 74) System.out.println((row + cRadius) + " " + (c) + " ");
            sums.add(sum);
        }

        // if (debug == 74) System.out.println(sums);
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
        for (int i = row; i < row + (cRadius * 2) + 5; i++) {
            for (int j = col; j < col + (cRadius * 2) + 5; j++) {
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
}
