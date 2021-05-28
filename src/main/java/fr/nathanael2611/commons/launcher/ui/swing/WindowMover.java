package fr.nathanael2611.commons.launcher.ui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowMover extends MouseAdapter
{

    private Point click;
    private JFrame window;

    public WindowMover(JFrame window)
    {
        this.window = window;
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (click != null)
        {
            Point draggedPoint = MouseInfo.getPointerInfo().getLocation();
            window.setLocation(new Point((int) draggedPoint.getX() - (int) click.getX(), (int) draggedPoint.getY() - (int) click.getY()));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        click = e.getPoint();
    }

}