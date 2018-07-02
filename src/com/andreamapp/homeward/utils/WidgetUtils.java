package com.andreamapp.homeward.utils;

import com.andreamapp.homeward.ui.widget.Measurable;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;

public class WidgetUtils {

    public static void format(JLabel btn, Font font, int horizontalAlign, int verticalAlign) {
        btn.setFont(font);
        btn.setHorizontalAlignment(horizontalAlign);
        btn.setVerticalAlignment(verticalAlign);
    }

    public static void format(JButton btn, Font font, int horizontalAlign, int verticalAlign) {
        btn.setFont(font);
        btn.setHorizontalAlignment(horizontalAlign);
        btn.setVerticalAlignment(verticalAlign);
    }

    public static void popup(Class<? extends JFrame> cls) {
        popup(cls, null, WindowConstants.DISPOSE_ON_CLOSE);
    }

    public static void popup(Class<? extends JFrame> cls, int closeOperation) {
        popup(cls, null, closeOperation);
    }

    public static void popup(Class<? extends JFrame> cls, Dimension size, int closeOperation) {
        try {
            Constructor<? extends JFrame> constructor = cls.getConstructor();
            JFrame frame = constructor.newInstance();
            popup(frame, size, closeOperation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void popup(JFrame frame, Dimension size, int closeOperation) {
        EventQueue.invokeLater(() -> {
            Dimension dimen = size;
            if (size == null) {
                if (frame instanceof Measurable) {
                    dimen = new Dimension(((Measurable) frame).width(), ((Measurable) frame).height());
                } else {
                    dimen = frame.getPreferredSize();
                }
            }
            frame.setSize(dimen);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(closeOperation);
            frame.setVisible(true);
        });
    }
}
