package fr.nathanael2611.commons.launcher.ui.swing.components.button;

import fr.nathanael2611.commons.launcher.ui.swing.ComponentListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractButton extends JComponent implements MouseListener
{

    private String text;

    private Color textColor;

    private boolean hover = false;

    public AbstractButton() {
        this.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(this.isEnabled())
        {
            this.listeners.forEach(ComponentListener::run);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        hover = true;

        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        hover = false;

        repaint();
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);

        repaint();
    }

    public void setText(String text)
    {
        if(text == null)
            throw new IllegalArgumentException("text == null");
        this.text = text;

        repaint();
    }

    public String getText() {
        return text;
    }


    public void setTextColor(Color textColor)
    {
        if(textColor == null)
            throw new IllegalArgumentException("textColor == null");
        this.textColor = textColor;

        repaint();
    }


    public Color getTextColor() {
        return textColor;
    }

    private List<ComponentListener> listeners = new ArrayList<>();

    public void addListener(ComponentListener listener)
    {
        this.listeners.add(listener);
    }

    public boolean isHover() {
        return this.hover;
    }

}
