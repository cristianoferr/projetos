package com.cristiano.galactic.view.demos;

import java.awt.Color;
import java.awt.Graphics2D;

public class PaintedGauge extends PaintableImage {
    private static final int MAXIMUM = 100;
    private int value = 0;

    public PaintedGauge() {
        super(64, 64, true);
        refreshImage();
    }

    public void setValue(int newValue) {
        newValue %= MAXIMUM;
        if (newValue != value) {
            value = newValue;
            refreshImage();
        }
    }

    public void paint(Graphics2D g) {
        g.setBackground(new Color(0f, 0f, 0f, 0f));
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(1f, 0f, 1f, 1f));
        g.fillRect(0, 0, value * getWidth() / MAXIMUM, getHeight());
    }
}