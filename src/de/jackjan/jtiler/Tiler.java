/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jackjan.jtiler;

import de.janroslan.jputils.math.MathUtils;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javax.imageio.ImageIO;

/**
 *
 * @author jackjan
 */
public class Tiler extends Service<BufferedImage> {

    private List<File> files;

    public Tiler() {

    }

    /**
     * Creates a tile image of the given images. The output image is trying to
     * be as quadratic as possible. If the count of the input images is prime
     * (and so not dividble for a rectangular image) the ouput image will be in
     * only one line
     *
     * @return
     */
    @Override
    protected Task<BufferedImage> createTask() {
        return new Task<BufferedImage>() {
            @Override
            protected BufferedImage call() {
                // The amount of rows & columns
                int imgX = 0, imgY = 0;
                BufferedImage result;

                int[] square = MathUtils.equalSquare(files.size());
                imgX = square[0];
                imgY = square[1];

                // Read images
                BufferedImage[] images = new BufferedImage[files.size()];
                int bigX = 0;
                int bigY = 0;
                for (int i = 0; i < images.length; i++) {
                    updateProgress(i, images.length);
                    try {
                        images[i] = ImageIO.read(files.get(i));
                        if (images[i].getWidth() > bigX) {
                            bigX = images[i].getWidth();
                        }
                        if (images[i].getHeight() > bigY) {
                            bigY = images[i].getHeight();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        return null;
                    }
                }

                // Writing new image
                result = new BufferedImage(imgX * bigX, imgY * bigY, BufferedImage.TYPE_INT_ARGB);
                Graphics g = result.getGraphics();
                for (int i = 0; i < images.length; i++) {
                    updateProgress(i,images.length);
                    int x = i % imgX;
                    int y = i / imgX;
                    g.drawImage(images[i], x * bigX, y * bigY, null);
                }

                return result;
            }
        };
    }

    public void setImages(List<File> images) {
        this.files = images;
    }

}
