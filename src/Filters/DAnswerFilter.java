package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

public class DAnswerFilter implements PixelFilter, Interactive {
    private final int cRadius = 9, dist = (2 * cRadius) + 20; // radius of bubbles and space between each bubble
    private final int xODist = 281; // dimensions of outer box
    private int startingX = 0, startingY = 0;


    public DImage processImage(DImage img) {
        short[][] pixels = img.getBWPixelGrid();
        short[][] outputPixels = new short[pixels.length / 2][pixels[0].length / 2];
        short[][][] cChannels = {new short[outputPixels.length][outputPixels[0].length], new short[outputPixels.length][outputPixels[0].length], new short[outputPixels.length][outputPixels[0].length]};


        downsampleImage(pixels, outputPixels);
        boxImage(outputPixels, cChannels);

        DImage nImg = new DImage(outputPixels[0].length, outputPixels.length);
        nImg.setPixels(outputPixels);
        nImg.setColorChannels(cChannels[0], cChannels[1], cChannels[2]);
        return nImg;
    }

    private void boxImage(short[][] outputPixels, short[][][] cChannels) {
        for (int c = startingX / 2; c < (4 * xODist / 2); c += xODist / 2) {
            for (int r = startingY / 2; r < startingY / 2 + 25 * (dist) / 2; r += dist / 2) {
                makeDBox(cChannels, r, c);
            }
        }

        for (int r = 0; r < outputPixels.length; r++) {
            for (int c = 0; c < outputPixels[0].length; c++) {
                if (cChannels[0][r][c] == 0) {
                    cChannels[0][r][c] = outputPixels[r][c];
                    cChannels[1][r][c] = outputPixels[r][c];
                    cChannels[2][r][c] = outputPixels[r][c];
                }
            }
        }
    }

    private void makeDBox(short[][][] iChannels, int row, int col) {
        int[] boxColor = {255, 0, 0};
        for (int c = 0; c < boxColor.length; c++) {
            for (int i = row; i <= row + dist / 2; i++) {
                if (inBound(iChannels[0], i, col)) iChannels[c][i][col] = (short) boxColor[c];
                if (inBound(iChannels[0], i, col + xODist / 2)) iChannels[c][i][col + xODist / 2] = (short) boxColor[c];
            }

            for (int i = col; i <= col + xODist / 2; i++) {
                if (inBound(iChannels[0], row, i)) iChannels[c][row][i] = (short) boxColor[c];
                if (inBound(iChannels[0], row + dist / 2, i)) iChannels[c][row + dist / 2][i] = (short) boxColor[c];
            }
        }
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

    public static boolean inBound(short[][] arr, int r, int c) {
        return (arr.length - 1 >= r && arr[0].length - 1 >= c && r >= 0 && c >= 0);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        startingX = mouseX * 2;
        startingY = mouseY * 2;
        processImage(img);
    }

    @Override
    public void keyPressed(char key) {

    }
}