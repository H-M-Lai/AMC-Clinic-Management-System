/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.GUI.Common;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 *
 * @author NICK
 */
public class UploadProfilePic {
        // Resize method
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }
    public static void upload(String id) {
        try {
            // Open file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select an Image");
            
            // Filter only image files
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(null);
            if (result != JFileChooser.APPROVE_OPTION) {
                System.out.println("No file selected.");
                return;
            }

            File inputFile = fileChooser.getSelectedFile();
            BufferedImage originalImage = ImageIO.read(inputFile);
            // Resize
            BufferedImage resizedImage = resizeImage(originalImage, 150, 150);

            // Create output folder if not exists
            File outputDir = new File("profilePic");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            String newFileName = id + ".jpg";

            // Save into folder
            File outputFile = new File(outputDir, newFileName);
            ImageIO.write(resizedImage, "jpg", outputFile);

            System.out.println("Image saved to: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
