package com.github.dylmeadows.eontimer.util;

import com.google.common.collect.Range;
import javafx.scene.paint.Color;
import lombok.experimental.UtilityClass;

import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;

@UtilityClass
public class Colors {

    private final Range<Double> DOUBLE_RANGE = Range.closed(0.0, 1.0);
    private final String INVALID_DOUBLE_MSG = "Color's %s value must be between 0.0 and 1.0";

    public String toHex(Color color) {
        return Stream.of(color.getRed(), color.getGreen(), color.getBlue())
                .map(Colors::toColorInt)
                .map(Colors::toHexString)
                .reduce("#", (s1, s2) -> s1 + s2);
    }

    public String toHexAlpha(Color color) {
        return Stream.of(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity())
                .map(Colors::toColorInt)
                .map(Colors::toHexString)
                .reduce("#", (s1, s2) -> s1 + s2);
    }

    public String toHexAlpha(Color color, double alpha) {
        checkArgument(DOUBLE_RANGE.contains(alpha), INVALID_DOUBLE_MSG, "alpha");
        return Stream.of(color.getRed(), color.getGreen(), color.getBlue(), alpha)
                .map(Colors::toColorInt)
                .map(Colors::toHexString)
                .reduce("#", (s1, s2) -> s1 + s2);
    }

    private String toHexString(int value) {
        return String.format("%02x", value);
    }

    private int toColorInt(double value) {
        return ((int) (value * 255)) & 0xFF;
    }

    public Color deriveHue(Color color, double percent) {
        double hue = derive(color.getHue(), percent);
        return Color.hsb(hue, color.getSaturation(), color.getBrightness());
    }

    public Color deriveSaturation(Color color, double percent) {
        double saturation = derive(color.getSaturation(), percent);
        return Color.hsb(color.getHue(), saturation, color.getBrightness(), color.getOpacity());
    }

    public Color deriveBrightness(Color color, double percent) {
        double brightness = derive(color.getBrightness(), percent);
        return Color.hsb(color.getHue(), color.getSaturation(), brightness, color.getOpacity());
    }

    private double derive(double value, double percent) {
        if (percent < 0) {
            value = Double.max(value + percent, 0.0);
        } else if (percent > 0) {
            value = Double.min(value + percent, 1.0);
        }
        return value;
    }
}
