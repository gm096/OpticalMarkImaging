package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class ThresholdFilter implements PixelFilter {

    @Override
    public DImage processImage(DImage img) {
        short[][] output = img.getBWPixelGrid();
        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < output[0].length; j++) {
                if(output[i][j] < 100){
                    output[i][j] = 0;
                }
                else{
                    output[i][j] = 255;
                }
            }
        }
        return img;
    }
}

