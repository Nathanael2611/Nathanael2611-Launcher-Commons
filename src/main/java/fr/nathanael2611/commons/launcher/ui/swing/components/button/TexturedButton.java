package fr.nathanael2611.commons.launcher.ui.swing.components.button;

import java.awt.*;
import java.awt.image.BufferedImage;

import static fr.nathanael2611.commons.launcher.ui.swing.GraphicsUtils.*;

public class TexturedButton extends AbstractButton {

    public static final Color HOVER_COLOR = new Color(255, 255, 255, 100);


    public static final Color DISABLED_COLOR = HOVER_COLOR.darker().darker().darker();


    private Image texture;
    private Image textureHover;
    private Image textureDisabled;


    public TexturedButton(BufferedImage texture) {
        this(texture, null, null);
    }

    public TexturedButton(BufferedImage texture, BufferedImage textureHover) {
        this(texture, textureHover, null);
    }


    public TexturedButton(BufferedImage texture, BufferedImage textureHover, BufferedImage textureDisabled) {
        super();

        if(texture == null)
            throw new IllegalArgumentException("texture == null");
        this.texture = texture;

        if(textureHover == null)
            this.textureHover = fillImage(copyImage(texture), HOVER_COLOR, this.getParent());
        else
            this.textureHover = textureHover;
        if(textureDisabled == null)
            this.textureDisabled = fillImage(copyImage(texture), DISABLED_COLOR, this.getParent());
        else
            this.textureDisabled = textureDisabled;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image texture;
        if(!this.isEnabled())
            texture = textureDisabled;
        else if (super.isHover())
            texture = textureHover;
        else
            texture = this.texture;

        // Then drawing it
        drawFullsizedImage(g, this, texture);

        // If the text is not null
        if(getText() != null) {
            // Activating the anti alias
            activateAntialias(g);

            // Picking the string color
            if (getTextColor() != null)
                g.setColor(getTextColor());

            // Drawing the text, centered
            drawCenteredString(g, getText(), this.getBounds());
        }
    }

    public void setTexture(Image texture) {
        if(texture == null)
            throw new IllegalArgumentException("texture == null");
        this.texture = texture;

        repaint();
    }

    /**
     * Sets the texture of this button when the mouse is on
     *
     * @param textureHover
     *            The new hover texture
     */
    public void setTextureHover(Image textureHover) {
        // If the given hover texture is null, throwing an Illegal Argument Exception, else setting it
        if(textureHover == null)
            throw new IllegalArgumentException("textureHover == null");
        this.textureHover = textureHover;

        repaint();
    }

    /**
     * Sets the texture of this button when it is disabled
     *
     * @param textureDisabled
     *            The new disabled texture
     */
    public void setTextureDisabled(Image textureDisabled) {
        // If the given disabled texture is null, throwing an Illegal Argument Exception, else setting it
        if(textureDisabled == null)
            throw new IllegalArgumentException("textureDisabled == null");
        this.textureDisabled = textureDisabled;

        repaint();
    }

    /**
     * Return the texture of this button
     *
     * @return The button texture
     */
    public Image getTexture() {
        return this.texture;
    }

    /**
     * Return the texture of the button when the mouse is on
     *
     * @return The button hover texture
     */
    public Image getTextureHover() {
        return this.textureHover;
    }

    /**
     * Return the texture of the button when it is disabled
     *
     * @return The button disabled texture
     */
    public Image getTextureDisabled() {
        return this.textureDisabled;
    }

    public void setBounds(int x, int y)
    {
        this.setBounds(x, y, this.texture.getWidth(this.getParent()), this.texture.getHeight(this.getParent()));
    }

}
