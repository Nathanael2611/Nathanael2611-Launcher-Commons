package fr.nathanael2611.commons.launcher.ui.swing;

import fr.nathanael2611.commons.launcher.update.UpdateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;

import static fr.nathanael2611.commons.launcher.update.minecraft.download.DownloadManager.crossMult;

public class GraphicsUtils
{

    public static BufferedImage copyImage(BufferedImage image)
    {
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }


    public static Image fillImage(Image image, Color color, ImageObserver imageObserver)
    {
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, image.getWidth(imageObserver), image.getHeight(imageObserver));

        return image;
    }

    public static Point getRecCenterPos(Rectangle parent, Rectangle rectangle)
    {
        double x = parent.getWidth() / 2 - rectangle.getWidth() / 2;
        double y = parent.getHeight() / 2 + rectangle.getHeight() / 2;

        return new Point((int) x, (int) y);
    }


    public static Point getStringCenterPos(Rectangle parent, String str, FontMetrics fontMetrics, Graphics g)
    {
        // Getting the string bounds
        Rectangle2D stringBounds = fontMetrics.getStringBounds(str, g);

        // Getting the center pos for this rectangle
        double x = ((parent.getWidth() - stringBounds.getWidth()) / 2);
        double y = ((parent.getHeight() - stringBounds.getHeight()) / 2 + fontMetrics.getAscent());
        return new Point((int) x, (int) y);
    }

    /**
     * Draw a centered string
     *
     * @param g      The graphics to use to draw
     * @param str    The string to draw
     * @param parent The parent space where the string will be drawn
     */
    public static void drawCenteredString(Graphics g, String str, Rectangle parent)
    {
        // Getting the Font Metrics
        FontMetrics fm = g.getFontMetrics();

        // Getting the center pos for this rectangle
        Point centerPos = getStringCenterPos(parent, str, fm, g);

        // Drawing the text, centered
        g.drawString(str, (int) centerPos.getX(), (int) centerPos.getY());
    }


    public static void activateAntialias(Graphics g)
    {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    public static BufferedImage colorImage(BufferedImage image, int red, int green, int blue)
    {
        // Creating a new translucent image with the same size as the given image, and creating its graphics
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics2D graphics = img.createGraphics();

        // Getting the given color with 0 alpha (its needed)
        Color newColor = new Color(red, green, blue, 0);

        // Drawing the given image, to the new image with the xor mode as the given color
        graphics.setXORMode(newColor);
        graphics.drawImage(image, null, 0, 0);
        graphics.dispose();

        // Returning the created image
        return img;
    }


    public static void drawFullsizedImage(Graphics g, JComponent component, Image image)
    {
        g.drawImage(image, 0, 0, component.getWidth(), component.getHeight(), component);
    }


    public static void fillFullsizedRect(Graphics g, JComponent component)
    {
        g.fillRect(0, 0, component.getWidth(), component.getHeight());
    }

    public static void fillFullsizedRect(Graphics g, JComponent component, Color color)
    {
        g.setColor(color);
        g.fillRect(0, 0, component.getWidth(), component.getHeight());
    }


    public static void drawColoredProgressBar(Graphics g, UpdateManager updateManager, Color emptyColor, Color fillColor, Rectangle bar)
    {
        g.setColor(emptyColor);
        g.fillRect(bar.x, bar.y, bar.width, bar.height);
        g.setColor(fillColor);
        g.fillRect(bar.x, bar.y, crossMult(updateManager.getDownloadedFiles(), updateManager.getTotalFiles(), bar.width), bar.height);
    }

    public static void drawCenteredString(Graphics g, String string, int posX, int posY)
    {
        FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
        Font font = g.getFont();
        Rectangle2D b = font.getStringBounds(string, frc);
        g.drawString(string, (int) (posX - b.getWidth() / 2), (int) (posY + b.getHeight() / 2));
    }

}
