package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;


public class ImageToText implements TextGraphicsConverter {
    private TextColorSchema schema = new ConvertImage();
    private int maxWidth;
    private int maxHeight;
    private double maxRatio;
// yyfjhvjkfjykulgft
    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        int newWidth = img.getWidth();
        int newHeight = img.getHeight();

        if (this.maxRatio != 0) {
            double width = newWidth;
            double height = newHeight;
            double imgRatio = width / height;
            if (imgRatio > this.maxRatio) {
                throw new BadImageSizeException(imgRatio, this.maxRatio);
            }
        }

        if (newWidth > this.maxWidth) {
            double width = newWidth;
            double ratio = width / this.maxWidth;
            newWidth = (int) (newWidth / ratio);
            newHeight = (int) (newHeight / ratio);
        }

        if (newHeight > this.maxHeight) {
            double height = newHeight;
            double ratio = height / this.maxHeight;
            newWidth = (int) (newWidth / ratio);
            newHeight = (int) (newHeight / ratio);
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();

        StringBuilder stringBuilder = new StringBuilder();

        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                stringBuilder.append(c);
                stringBuilder.append(c);
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
