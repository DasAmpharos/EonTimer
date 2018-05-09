package com.github.dylmeadows.common.javafx.paint;

import com.github.dylmeadows.common.lang.Maths;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class Colors {

    private static final String HEX_FORMAT = "#%02x%02x%02x";

    private static final String HEX_FORMAT_ALPHA = HEX_FORMAT + "%02x";

    public static final int H = 0;
    public static final int S = 1;
    public static final int L = 2;
    public static final int A = 3;

    private Colors() {
    }

    public static String toHex(Color color) {
        return toHex(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static String toHex(int r, int g, int b) {
        return String.format(HEX_FORMAT, r, g, b);
    }

    public static String toHex(double r, double g, double b) {
        return toHex((int) (r * 255), (int) (g * 255), (int) (b * 255));
    }

    public static String toHexAlpha(Color color) {
        return toHexAlpha(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getOpacity()
        );
    }

    public static String toHexAlpha(Color color, int alpha) {
        return toHexAlpha(
                toColorInt(color.getRed()),
                toColorInt(color.getGreen()),
                toColorInt(color.getBlue()),
                alpha
        );
    }

    public static String toHexAlpha(Color color, double alpha) {
        return toHexAlpha(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                alpha
        );
    }

    public static String toHexAlpha(int r, int g, int b, int a) {
        return String.format(HEX_FORMAT_ALPHA, r, g, b, a);
    }

    public static String toHexAlpha(double r, double g, double b, double a) {
        return toHexAlpha(toColorInt(r), toColorInt(g), toColorInt(b), toColorInt(a));
    }

    private static int toColorInt(double value) {
        return ((int) (value * 255)) & 0xFF;
    }

    public static double[] toHSL(Color color) {
        return Arrays.copyOf(toHSLA(color), 3);
    }

    public static double[] toHSLA(Color color) {
        double r = color.getRed();
        double g = color.getGreen();
        double b = color.getBlue();
        double a = color.getOpacity();

        double max = Maths.max(r, g, b);
        double min = Maths.min(r, g, b);

        double h, s, l = (max + min) / 2;

        if (max == min) {
            h = s = 0;
        } else {
            double d = max - min;
            s = l > 0.5 ? d / (2 - max - min) : d / (max + min);

            if (max == r) {
                h = (g - b) / d + (g < b ? 6 : 0);
            } else if (max == g) {
                h = (b - r) / d + 2;
            } else {
                h = (r - g) / d + 4;
            }

            h /= 6;
        }
        return new double[]{h, s, l, a};
    }

    public static Color toColor(double[] hsl) {
        if (hsl.length == 4) {
            return toColor(hsl[H], hsl[S], hsl[L], hsl[A]);
        }
        if (hsl.length == 3) {
            return toColor(hsl[H], hsl[S], hsl[L]);
        }
        throw new IllegalArgumentException("HSL(A) array must be of length 3 or 4.");
    }

    public static Color toColor(double h, double s, double l) {
        return toColor(h, s, l, 1);
    }

    public static Color toColor(double h, double s, double l, double a) {
        double r, g, b;
        if (s == 0) {
            r = g = b = 1;
        } else {
            double q = (l < 0.5) ? l * (1 + s) : l + s - l * s;
            double p = 2 * l - q;

            r = hue2rgb(p, q, h + 1.0 / 3);
            g = hue2rgb(p, q, h);
            b = hue2rgb(p, q, h - 1.0 / 3);
        }
        return new Color(r, g, b, a);
    }

    private static double hue2rgb(double p, double q, double t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1.0 / 6) return p + (q - p) * 6 * t;
        if (t < 1.0 / 2) return q;
        if (t < 2.0 / 3) return p + (q - p) * (2.0 / 3 - t) * 6;
        return p;
    }

    public static Color deriveHue(Color color, double percent) {
        double[] hsla = toHSLA(color);
        hsla = deriveHue(hsla, percent);
        return toColor(hsla);
    }

    public static double[] deriveHue(double[] hsl, double percent) {
        if (percent < 0) {
            hsl[H] = Maths.max(hsl[H] + percent, 0.0);
        }
        if (percent > 0) {
            hsl[H] = Maths.min(hsl[H] + percent, 1.0);
        }
        return hsl;
    }

    public static Color deriveSaturation(Color color, double percent) {
        double[] hsla = toHSLA(color);
        hsla = deriveSaturation(hsla, percent);
        return toColor(hsla);
    }

    public static double[] deriveSaturation(double[] hsl, double percent) {
        if (percent < 0) {
            hsl[S] = Maths.max(hsl[S] + percent, 0.0);
        }
        if (percent > 0) {
            hsl[S] = Maths.min(hsl[S] + percent, 1.0);
        }
        return hsl;
    }

    public static Color deriveLightness(Color color, double percent) {
        double[] hsla = toHSLA(color);
        hsla = deriveLightness(hsla, percent);
        return toColor(hsla);
    }

    public static double[] deriveLightness(double[] hsl, double percent) {
        if (percent < 0) {
            hsl[L] = Maths.max(hsl[L] + percent, 0.0);
        }
        if (percent > 0) {
            hsl[L] = Maths.min(hsl[L] + percent, 1.0);
        }
        return hsl;
    }
}
