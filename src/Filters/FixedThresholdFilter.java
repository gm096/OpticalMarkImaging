package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class FixedThresholdFilter implements PixelFilter {
    private int threshold;

    public FixedThresholdFilter() {
        threshold = 250;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if(grid[i][j] > threshold){
                    grid[i][j] = 255;
                }
                else{
                    grid[i][j] = 0;
                }
            }
        }
        img.setPixels(grid);
        return img;
    }
}

