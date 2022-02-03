package Filters;

import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PApplet;

import java.util.ArrayList;

public class AnswerFilterBubbleSearch{
    private static final int MIN_DIAMETER = 14;
    private static final int MAX_DIAMETER = 28;
    private static final int START_ROW = 70;
    private static final int START_COL = 30;
    public DImage processImage(DImage img) {
        FixedThresholdFilter f = new FixedThresholdFilter();
        DImage input = f.processImage(img);
        short[][] image = input.getBWPixelGrid();
        ArrayList<Bubble> bubbles = findBubbles(image);
        drawOverlay(bubbles, input);
        return input;
    }
    public ArrayList<Bubble> findBubbles(short[][] image){
        int upX,upY, downX, downY, leftX,leftY,rightX,rightY;
        ArrayList<Bubble> bubbles = new ArrayList<>();
        for (int i = START_ROW; i < image.length-MAX_DIAMETER; i++) {
            for (int j = START_COL; j < image[0].length - MAX_DIAMETER; j++) {
                if(image[i][j] == 0){
                    upY = i;
                    upX = j;
                    for (int k = MIN_DIAMETER; k <= MAX_DIAMETER; k++) {
                        if(image[i + k][j] == 0){
                            downY = i + k;
                            downX = j;
                        }
                        if(image[i + k/2][j + k/2] == 0){
                            leftY = i + k/2;
                            leftX = j + k/2;
                        }
                        if(image[i + k/2][j - k/2] == 0){
                            rightY = i + k/2;
                            rightX = j + k/2;
                        }
                    }
                }
            }
        }
        return bubbles;
    }
    public void drawOverlay(ArrayList<Bubble> bubbles, DImage input) {
        short[][] red = input.getRedChannel();
        short[][] green = input.getGreenChannel();
        short[][] blue = input.getBlueChannel();
        for (Bubble b :
                bubbles) {
            red[b.upX][b.upY] = 255;
            red[b.downX][b.downY] = 255;
            red[b.leftX][b.leftY] = 255;
            red[b.rightX][b.rightY] = 255;
        }
        input.setColorChannels(red,blue,green);
    }
}
