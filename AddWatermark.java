package com.imagewatermark;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.FileUtils;

public class AddWatermark {
	
	public static OutputStream addImageWatermark(InputStream watermarkImageFile, InputStream sourceImageFile, OutputStream outputStream) {
	    try {
	    	System.out.println("555555555555");
	    	ImageInputStream isourceImageFile = ImageIO.createImageInputStream(sourceImageFile);
	    	ImageInputStream iwatermarkImageFile = ImageIO.createImageInputStream(watermarkImageFile);
	    	System.out.println(isourceImageFile);
	    	BufferedImage sourceImage = ImageIO.read(isourceImageFile);
	    	System.out.println(iwatermarkImageFile);
	    	BufferedImage watermarkImage = ImageIO.read(iwatermarkImageFile);
	        System.out.println("666666666666");
	        // initializes necessary graphic properties
	        Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();
	        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
	        g2d.setComposite(alphaChannel);
	 
	        // calculates the coordinate where the image is painted
	        int topLeftX = (sourceImage.getWidth() - watermarkImage.getWidth()) / 2;
	        int topLeftY = (sourceImage.getHeight() - watermarkImage.getHeight()) / 2;
	 
	        // paints the image watermark
	        g2d.drawImage(watermarkImage, topLeftX, topLeftY, null);
	        
	        ImageIO.write(sourceImage, "jpg", outputStream);
	        System.out.println("neel jain77777777");
	        g2d.dispose();
	 
	        System.out.println("The image watermark is added to the image.");
	    } catch (IOException ex) {
	        System.out.println(ex);
	    }
		return outputStream;
	}

}
