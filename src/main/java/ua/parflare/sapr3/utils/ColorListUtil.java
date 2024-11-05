package ua.parflare.sapr3.utils;

import java.awt.Color;
import java.util.ArrayList;

public class ColorListUtil {
    private static final ArrayList<Color> colors = new ArrayList<>() {{
        add(new Color(236, 132, 145)); // Light Pink
        add(new Color(137, 209, 234)); // Light Blue
        add(new Color(116, 220, 116)); // Pale Green
        add(new Color(211, 211, 109)); // Light Yellow
        add(new Color(236, 190, 93)); // Moccasin
        add(new Color(232, 126, 232)); // Plum
        add(new Color(69, 142, 142)); // Pale Turquoise
        add(new Color(246, 162, 45)); // Navajo White
        add(new Color(223, 91, 44)); // Light Salmon
        add(new Color(154, 53, 154)); // Thistle
        add(new Color(195, 185, 101)); // Khaki
        add(new Color(75, 170, 170)); // Light Cyan
        add(new Color(62, 159, 62)); // Honeydew
        add(new Color(135, 135, 53)); // Beige
        add(new Color(97, 77, 44)); // Papaya Whip
        add(new Color(211, 189, 52)); // Lemon Chiffon
        add(new Color(220, 90, 77)); // Misty Rose
        add(new Color(89, 89, 221)); // Lavender
    }};


    public static Color getColorByIndex(int index) {
        int effectiveIndex = index % colors.size();
        return colors.get(effectiveIndex);
    }
}

