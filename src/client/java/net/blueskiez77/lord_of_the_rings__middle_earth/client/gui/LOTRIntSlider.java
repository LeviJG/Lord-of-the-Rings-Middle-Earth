package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.function.IntConsumer;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import org.jspecify.annotations.Nullable;

/**
 * LOTRGuiSlider: a whole number between two bounds, shown as "Name: n" (or
 * the number alone, zero-padded if asked, or a word in place of it), each new
 * value passed on as it moves.
 */
public class LOTRIntSlider extends AbstractSliderButton {

    private final Component baseDisplayString;
    private final int minValue;
    private final int maxValue;
    private final IntConsumer onChange;
    private boolean valueOnly;
    private int numberDigits;
    private @Nullable Component overrideStateString;

    public LOTRIntSlider(int x, int y, int width, int height, Component label, int min, int max, int value,
                         IntConsumer onChange) {
        super(x, y, width, height, CommonComponents.EMPTY, 0.0);
        this.baseDisplayString = label;
        this.minValue = min;
        this.maxValue = max;
        this.onChange = onChange;
        setSliderValue(value);
    }

    public LOTRIntSlider setValueOnly() {
        this.valueOnly = true;
        updateMessage();
        return this;
    }

    public LOTRIntSlider setNumberDigits(int digits) {
        this.numberDigits = digits;
        updateMessage();
        return this;
    }

    public void setOverrideStateString(@Nullable Component s) {
        this.overrideStateString = s;
        updateMessage();
    }

    public int getSliderValue() {
        return this.minValue + (int) Math.round(this.value * (this.maxValue - this.minValue));
    }

    public void setSliderValue(int v) {
        v = Mth.clamp(v, this.minValue, this.maxValue);
        this.value = (double) (v - this.minValue) / (this.maxValue - this.minValue);
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        Component state;
        if (this.overrideStateString != null) {
            state = this.overrideStateString;
        } else {
            int v = getSliderValue();
            state = Component.literal(this.numberDigits > 0 ? String.format("%0" + this.numberDigits + "d", v) : String.valueOf(v));
        }
        setMessage(this.valueOnly ? state : this.baseDisplayString.copy().append(": ").append(state));
    }

    @Override
    protected void applyValue() {
        this.onChange.accept(getSliderValue());
    }
}
