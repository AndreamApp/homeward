package com.andreamapp.homeward.ui.widget;

import javax.swing.*;
import java.awt.*;

public class XPasswordField extends JPasswordField {
    @SuppressWarnings("WeakerAccess")
    public XPasswordField(String hint) {
        _hint = hint;
    }

    public XPasswordField() {
        this("");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        String password = String.valueOf(getPassword());
        if (password.length() == 0) {
            int h = getHeight();
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();
            int c0 = getBackground().getRGB();
            int c1 = getForeground().getRGB();
            int m = 0xfefefefe;
            int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
            g.setColor(new Color(c2, true));
            g.drawString(_hint, ins.left, h / 2 + fm.getAscent() / 2 - 2);
        }
    }

    public String getHint() {
        return _hint;
    }

    public void setHint(String hint) {
        this._hint = hint;
    }

    private String _hint;
}
