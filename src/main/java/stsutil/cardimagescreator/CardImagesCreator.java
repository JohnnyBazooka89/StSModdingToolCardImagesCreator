package stsutil.cardimagescreator;

import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CardImagesCreator {

    public static final int DOUBLE_PORTRAIT_IMAGE_WIDTH = 1000;
    public static final int DOUBLE_PORTRAIT_IMAGE_HEIGHT = 760;
    public static final int PORTRAIT_IMAGE_WIDTH = 500;
    public static final int PORTRAIT_IMAGE_HEIGHT = 380;
    public static final int IMAGE_WIDTH = 250;
    public static final int IMAGE_HEIGHT = 190;

    private BufferedImage attackPortraitImageMask = ImageIO.read(new File("masks/AttackMask_p.png"));
    private BufferedImage skillPortraitImageMask = ImageIO.read(new File("masks/SkillMask_p.png"));
    private BufferedImage powerPortraitImageMask = ImageIO.read(new File("masks/PowerMask_p.png"));
    private BufferedImage attackImageMask = ImageIO.read(new File("masks/AttackMask.png"));
    private BufferedImage skillImageMask = ImageIO.read(new File("masks/SkillMask.png"));
    private BufferedImage powerImageMask = ImageIO.read(new File("masks/PowerMask.png"));
    private Color transparentColor = new Color(0, 0, 0, 0);

    public CardImagesCreator() throws IOException {
    }

    public void createCardImages(File file) throws IOException {

        BufferedImage originalImage = ImageIO.read(file);

        boolean doublePortraitSize = false;
        if (originalImage.getWidth() == DOUBLE_PORTRAIT_IMAGE_WIDTH && originalImage.getHeight() == DOUBLE_PORTRAIT_IMAGE_HEIGHT) {
            doublePortraitSize = true;
        }
        
        boolean portraitSize = false;
        if ((originalImage.getWidth() == PORTRAIT_IMAGE_WIDTH && originalImage.getHeight() == PORTRAIT_IMAGE_HEIGHT)){
            portraitSize = true;
        }
        boolean cardSize = false;
        if ((originalImage.getWidth() == IMAGE_WIDTH && originalImage.getHeight() == IMAGE_HEIGHT)){
            cardSize = true;
        }
        if(!doublePortraitSize && !portraitSize && !cardSize) {
            throw new IllegalArgumentException("Image has incorrect size.");
        }

        BufferedImage portraitImage;
        BufferedImage cardImage;
        if(doublePortraitSize) {
            BufferedImage scaledImage = new BufferedImage(PORTRAIT_IMAGE_WIDTH, PORTRAIT_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics g = scaledImage.createGraphics();
            g.drawImage(originalImage, 0, 0, PORTRAIT_IMAGE_WIDTH, PORTRAIT_IMAGE_HEIGHT, null);
            g.dispose();
            portraitImage = scaledImage;
            
            scaledImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            g = scaledImage.createGraphics();
            g.drawImage(originalImage, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, null);
            g.dispose();
            cardImage = scaledImage;
        } else if(portraitSize) {
            BufferedImage scaledImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics g = scaledImage.createGraphics();
            g.drawImage(originalImage, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, null);
            g.dispose();
            portraitImage = originalImage;
            cardImage = scaledImage;
        } else{
            portraitImage = null;
            cardImage = originalImage;
        }

        if(portraitImage != null) {
            BufferedImage attackPortraitImage = new BufferedImage(PORTRAIT_IMAGE_WIDTH, PORTRAIT_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            BufferedImage skillPortraitImage = new BufferedImage(PORTRAIT_IMAGE_WIDTH, PORTRAIT_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            BufferedImage powerPortraitImage = new BufferedImage(PORTRAIT_IMAGE_WIDTH, PORTRAIT_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < PORTRAIT_IMAGE_WIDTH; i++) {
                for (int j = 0; j < PORTRAIT_IMAGE_HEIGHT; j++) {
                    setPixelAsTransparentOrCopyFromOriginal(originalImage, attackPortraitImage, attackPortraitImageMask, i, j);
                    setPixelAsTransparentOrCopyFromOriginal(originalImage, skillPortraitImage, skillPortraitImageMask, i, j);
                    setPixelAsTransparentOrCopyFromOriginal(originalImage, powerPortraitImage, powerPortraitImageMask, i, j);
                }
            }
            saveBufferedImageInFile(attackPortraitImage, "images/Attacks/" + FilenameUtils.getBaseName(file.getName()) + "_p." + FilenameUtils.getExtension(file.getName()));
            saveBufferedImageInFile(skillPortraitImage, "images/Skills/" + FilenameUtils.getBaseName(file.getName()) + "_p." + FilenameUtils.getExtension(file.getName()));
            saveBufferedImageInFile(powerPortraitImage, "images/Powers/" + FilenameUtils.getBaseName(file.getName()) + "_p." + FilenameUtils.getExtension(file.getName()));
        }

        BufferedImage attackImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        BufferedImage skillImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        BufferedImage powerImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < IMAGE_WIDTH; i++) {
            for (int j = 0; j < IMAGE_HEIGHT; j++) {
                setPixelAsTransparentOrCopyFromOriginal(cardImage, attackImage, attackImageMask, i, j);
                setPixelAsTransparentOrCopyFromOriginal(cardImage, skillImage, skillImageMask, i, j);
                setPixelAsTransparentOrCopyFromOriginal(cardImage, powerImage, powerImageMask, i, j);
            }
        }

        saveBufferedImageInFile(attackImage, "images/Attacks/" + file.getName());
        saveBufferedImageInFile(skillImage, "images/Skills/" + file.getName());
        saveBufferedImageInFile(powerImage, "images/Powers/" + file.getName());

    }

    private void saveBufferedImageInFile(BufferedImage bufferedImage, String filePath) throws IOException {
        File outputFile = new File(filePath);
        Files.createDirectories(outputFile.toPath().getParent());
        outputFile.createNewFile();
        ImageIO.write(bufferedImage, "png", outputFile);
    }

    private void setPixelAsTransparentOrCopyFromOriginal(BufferedImage originalImage, BufferedImage image, BufferedImage imageMask, int i, int j) {
        if (imageMask.getRGB(i, j) == Color.black.getRGB()) {
            image.setRGB(i, j, transparentColor.getRGB());
        } else {
            image.setRGB(i, j, originalImage.getRGB(i, j));
        }
    }
}
