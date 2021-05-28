package fr.nathanael2611.commons.launcher.ui.swing.components.button;

import java.awt.*;

import static fr.nathanael2611.commons.launcher.ui.swing.GraphicsUtils.*;

public class ColoredButton extends AbstractButton {


    private Color color;

    private Color colorHover;


    private Color colorDisabled;


    public ColoredButton(Color color) {
        this(color, null, null);
    }


    public ColoredButton(Color color, Color colorHover) {
        this(color, colorHover, null);
    }

    public ColoredButton(Color color, Color colorHover, Color colorDisabled) {
        if(color == null)
            throw new IllegalArgumentException("Color == null");
        this.color = color;

        if(colorHover == null)
            this.colorHover = color.brighter();
        else
            this.colorHover = colorHover;

        if(colorDisabled == null)
            this.colorDisabled = color.darker();
        else
            this.colorDisabled = colorDisabled;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color color;
        if(!this.isEnabled())
            color = colorDisabled;
        else if (super.isHover())
            color = colorHover;
        else
            color = this.color;

        fillFullsizedRect(g, this, color);

        // If the text is not null
        if(getText() != null) {
            // Activating the anti alias
            activateAntialias(g);

            if (getTextColor() != null)
                g.setColor(getTextColor());

            drawCenteredString(g, getText(), this.getBounds());
        }
    }


    public void setColor(Color color) {
        if(color == null)
            throw new IllegalArgumentException("Color == null");
        this.color = color;

        repaint();
    }

    public Color getColor() {
        return color;
    }

    public void setColorHover(Color colorHover) {
        // If the given hover color is null, throwing an Illegal Argument Exception, else setting it
        if(colorHover == null)
            throw new IllegalArgumentException("colorHover == null");
        this.colorHover = colorHover;

        repaint();
    }


    public Color getColorHover()
    {
        return colorHover;
    }


    public void setColorDisabled(Color colorDisabled)
    {
        if(colorDisabled == null)
            throw new IllegalArgumentException("colorDisabled == null");
        this.colorDisabled = colorDisabled;

        repaint();
    }
    public Color getColorDisabled() {
        return colorDisabled;
    }
}
