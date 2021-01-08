package stsutil.cardimagescreator;

import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CardImagesCreator {

    public static final int PORTRAIT_IMAGE_WIDTH = 500;
    public static final int PORTRAIT_IMAGE_HEIGHT = 380;
    public static final int IMAGE_WIDTH = 250;
    public static final int IMAGE_HEIGHT = 190;

    private final BufferedImage attackPortraitImageMask = ImageIO.read(new File("masks/AttackMask_p.png"));
    private final BufferedImage skillPortraitImageMask = ImageIO.read(new File("masks/SkillMask_p.png"));
    private final BufferedImage powerPortraitImageMask = ImageIO.read(new File("masks/PowerMask_p.png"));
    private final BufferedImage attackImageMask = ImageIO.read(new File("masks/AttackMask.png"));
    private final BufferedImage skillImageMask = ImageIO.read(new File("masks/SkillMask.png"));
    private final BufferedImage powerImageMask = ImageIO.read(new File("masks/PowerMask.png"));
    private static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);

    public CardImagesCreator() throws IOException {
    }

    public void createCardImages(File file) throws IOException {

        BufferedImage originalImage = ImageIO.read(file);
        BufferedImage portraitImage = null;
        BufferedImage cardImage = null;

        if (getImageRatio(originalImage) != getImageRatio(attackImageMask)) {
            throw new WrongRatioException("Image has wrong ratio, should be a multiple of 250x190");
        }

        if (originalImage.getWidth() == PORTRAIT_IMAGE_WIDTH) {
            portraitImage = originalImage;
        }
        if (originalImage.getWidth() == IMAGE_WIDTH) {
            cardImage = originalImage;
        }
        if (originalImage.getWidth() > PORTRAIT_IMAGE_WIDTH) {
            portraitImage = scaleImageDownToDimensions(originalImage, PORTRAIT_IMAGE_WIDTH, PORTRAIT_IMAGE_HEIGHT);
        }
        if (originalImage.getWidth() > IMAGE_WIDTH) {
            cardImage = scaleImageDownToDimensions(originalImage, IMAGE_WIDTH, IMAGE_HEIGHT);
        }

        if(portraitImage != null) {
            BufferedImage attackPortraitImage = maskImage(portraitImage, attackPortraitImageMask);
            BufferedImage skillPortraitImage = maskImage(portraitImage, skillPortraitImageMask);
            BufferedImage powerPortraitImage = maskImage(portraitImage, powerPortraitImageMask);
            saveBufferedImageInFile(attackPortraitImage, "images/Attacks/" + FilenameUtils.getBaseName(file.getName()) + "_p." + FilenameUtils.getExtension(file.getName()));
            saveBufferedImageInFile(skillPortraitImage, "images/Skills/" + FilenameUtils.getBaseName(file.getName()) + "_p." + FilenameUtils.getExtension(file.getName()));
            saveBufferedImageInFile(powerPortraitImage, "images/Powers/" + FilenameUtils.getBaseName(file.getName()) + "_p." + FilenameUtils.getExtension(file.getName()));
        }

        BufferedImage attackImage = maskImage(cardImage, attackImageMask);
        BufferedImage skillImage = maskImage(cardImage, skillImageMask);
        BufferedImage powerImage = maskImage(cardImage, powerImageMask);

        saveBufferedImageInFile(attackImage, "images/Attacks/" + file.getName());
        saveBufferedImageInFile(skillImage, "images/Skills/" + file.getName());
        saveBufferedImageInFile(powerImage, "images/Powers/" + file.getName());

    }

    private float getImageRatio(BufferedImage originalImage) {
        return (float) originalImage.getWidth() / (float) originalImage.getHeight();
    }

    private BufferedImage scaleImageDownToDimensions(BufferedImage originalImage, int newWidth, int newHeight) {
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        AffineTransform transform = new AffineTransform();
        transform.scale((float) newWidth / (float) originalImage.getWidth(),
            (float) newHeight / (float) originalImage.getHeight());
        AffineTransformOp scaleOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        return scaleOp.filter(originalImage, scaledImage);
    }

    private BufferedImage maskImage(BufferedImage imageToMask, BufferedImage mask) {
        BufferedImage outputImage = new BufferedImage(mask.getWidth(), mask.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < mask.getWidth(); i++) {
            for (int j = 0; j < mask.getHeight(); j++) {
                setPixelAsTransparentOrCopyFromOriginal(imageToMask, outputImage, mask, i, j);
            }
        }
        return outputImage;
    }

    private void saveBufferedImageInFile(BufferedImage bufferedImage, String filePath) throws IOException {
        File outputFile = new File(filePath);
        Files.createDirectories(outputFile.toPath().getParent());
        outputFile.createNewFile();
        ImageIO.write(bufferedImage, "png", outputFile);
    }

    private void setPixelAsTransparentOrCopyFromOriginal(BufferedImage originalImage, BufferedImage image, BufferedImage imageMask, int i, int j) {
        if (imageMask.getRGB(i, j) == Color.black.getRGB()) {
            image.setRGB(i, j, TRANSPARENT_COLOR.getRGB());
        } else {
            image.setRGB(i, j, originalImage.getRGB(i, j));
        }
    }

    private static class WrongRatioException extends RuntimeException {
        public WrongRatioException(String message) {
            super(message);
        }
    }
}
