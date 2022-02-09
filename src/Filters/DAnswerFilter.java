package Filters;

import FileIO.Location;
import Interfaces.Drawable;
import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PApplet;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DAnswerFilter implements PixelFilter, Interactive, Drawable {
    private int cRadius = 9; // radius of bubbles
    private int xODist, yDist, startingX, startingY;
    private int rows, columns, answers;
    private ArrayList<Location> corners;

    public DAnswerFilter() {
        corners = new ArrayList<>();

        Scanner kb = new Scanner(System.in);
        System.out.println("Supply the number of columns and rows and the number of bubbles per question, separated by a space:");
        String input = kb.nextLine();

        columns = Integer.parseInt(input.split(" ")[0]);
        rows = Integer.parseInt(input.split(" ")[1]);
        answers = Integer.parseInt(input.split(" ")[2]);
    }

    public DImage processImage(DImage img) {
        short[][] pixels = img.getBWPixelGrid();
        short[][] outputPixels = new short[pixels.length / 2][pixels[0].length / 2];
        short[][][] iChannels = {img.getRedChannel(), img.getGreenChannel(), img.getBlueChannel()};
        short[][][] oChannels = {new short[outputPixels.length][outputPixels[0].length], new short[outputPixels.length][outputPixels[0].length], new short[outputPixels.length][outputPixels[0].length]};


        downsampleImage(iChannels, oChannels);
        if (corners.size() >= 2) boxImage(oChannels);

        DImage nImg = new DImage(outputPixels[0].length, outputPixels.length);
        nImg.setPixels(outputPixels);
        nImg.setColorChannels(oChannels[0], oChannels[1], oChannels[2]);
        return nImg;
    }

    private void boxImage(short[][][] cChannels) {
        for (int c = startingX / 2; c < (columns * xODist / 2); c += xODist / 2) {
            for (int r = startingY / 2; r < startingY / 2 + rows * (yDist) / 2; r += yDist / 2) {
                makeDBox(cChannels, r, c);
            }
        }
    }

    private void makeDBox(short[][][] iChannels, int row, int col) {
        int[] boxColor = {255, 0, 0};
        for (int c = 0; c < boxColor.length; c++) {
            for (int i = row; i <= row + yDist / 2; i++) {
                if (inBound(iChannels[0], i, col))   iChannels[c][i][col] = (short) boxColor[c];
                if (inBound(iChannels[0], i, col + xODist / 2))  iChannels[c][i][col + xODist / 2] = (short) boxColor[c];
            }

            for (int i = col; i <= col + xODist / 2; i++) {
                if (inBound(iChannels[0], row, i)) iChannels[c][row][i] = (short) boxColor[c];
                if (inBound(iChannels[0], row + yDist / 2, i)) iChannels[c][row + yDist / 2][i] = (short) boxColor[c];
            }
        }
    }

    private void downsampleImage(short[][][] iChannels, short[][][] oChannels) {
        int rc = 0;
        int cc = 0;

        for (int r = 0; r < iChannels[0].length - 1; r += 2) {
            for (int c = 0; c < iChannels[0][r].length - 1; c += 2) {
                oChannels[0][rc][cc] = iChannels[0][r][c];
                oChannels[1][rc][cc] = iChannels[1][r][c];
                oChannels[2][rc][cc] = iChannels[2][r][c];
                cc++;
            }
            rc++;
            cc = 0;
        }
    }

    public static boolean inBound(short[][] arr, int r, int c) {
        return (arr.length - 1 >= r && arr[0].length - 1 >= c && r >= 0 && c >= 0);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        corners.add(new Location(mouseX, mouseY));
    }

    @Override
    public void keyPressed(char key) {
        if (key == 'r') corners.clear();
        if (key == 'c') saveToCSV();
    }

    private void saveToCSV() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter("format.csv"));

            out.println(columns + " " + rows + " " + startingX + " " + startingY + " " + xODist + " " + answers);

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        if (corners.size() >= 2) {
            Location l1 = corners.get(0), l2 = corners.get(1);

            startingX = l1.x * 2;
            startingY = l1.y * 2;
            xODist = (l2.x - l1.x) * 2;
            yDist = (l2.y - l1.y) * 2;
        }
    }
}
