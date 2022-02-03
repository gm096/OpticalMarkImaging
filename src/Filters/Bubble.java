package Filters;

import processing.core.PApplet;

public class Bubble {
    public boolean filled_in;
    public int upX, upY, downX, downY, leftX, leftY, rightX, rightY;

    public Bubble(int upX, int upY, int downX, int downY, int leftX, int leftY, int rightX, int rightY) {
        this.upX = upX;
        this.upY = upY;
        this.downX = downX;
        this.downY = downY;
        this.leftX = leftX;
        this.leftY = leftY;
        this.rightX = rightX;
        this.rightY = rightY;
    }
    public void draw(PApplet window){
        window.ellipse(upX,upY,3,3);
        window.ellipse(downX,downY,3,3);
        window.ellipse(leftX,leftY,3,3);
        window.ellipse(rightX,rightY,3,3);
    }
}
