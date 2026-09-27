package com.w1z4r_d.noboringarrows.gui;

import com.w1z4r_d.noboringarrows.StuckConfig;
import com.w1z4r_d.noboringarrows.compat.Compat;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.function.IntConsumer;

public class StuckConfigScreen extends Screen {
    // Позиції повзунка: 0 (сховано), 1..MAX_SLIDER, і крайня права = без обмеження
    private static final int STOPS = StuckConfig.MAX_SLIDER + 1;

    private final Screen parent;

    public StuckConfigScreen(Screen parent) {
        super(Component.translatable("noboringarrows.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        StuckConfig config = StuckConfig.get();

        int w = 220;
        int x = this.width / 2 - w / 2;
        int y = this.height / 4;
        int gap = 24;

        this.addRenderableWidget(Button.builder(enabledLabel(config), button -> {
            config.enabled = !config.enabled;
            button.setMessage(enabledLabel(config));
        }).bounds(x, y, w, 20).build());

        this.addRenderableWidget(Button.builder(scopeLabel(config), button -> {
            config.scope = config.scope.next();
            button.setMessage(scopeLabel(config));
        }).bounds(x, y + gap, w, 20).build());

        this.addRenderableWidget(limitOption("noboringarrows.config.max_arrows", config.maxArrows,
                value -> config.maxArrows = value).createButton(this.minecraft.options, x, y + gap * 2, w));

        this.addRenderableWidget(limitOption("noboringarrows.config.max_stingers", config.maxStingers,
                value -> config.maxStingers = value).createButton(this.minecraft.options, x, y + gap * 3, w));

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose())
                .bounds(x, y + gap * 4 + 12, w, 20).build());
    }

    @Override
    public void onClose() {
        StuckConfig.get().save();
        Compat.setScreen(this.minecraft, this.parent);
    }

    private static Component enabledLabel(StuckConfig config) {
        return Component.translatable("noboringarrows.config.enabled",
                config.enabled ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF);
    }

    private static Component scopeLabel(StuckConfig config) {
        return Component.translatable("noboringarrows.config.scope",
                Component.translatable(config.scope.translationKey()));
    }

    private static OptionInstance<Double> limitOption(String key, int initial, IntConsumer setter) {
        return new OptionInstance<>(key,
                OptionInstance.noTooltip(),
                (caption, value) -> Component.translatable(key, limitText(toLimit(value))),
                OptionInstance.UnitDouble.INSTANCE,
                toSlider(initial),
                value -> setter.accept(toLimit(value)));
    }

    private static double toSlider(int limit) {
        if (limit < 0) {
            return 1.0;
        }
        return Math.min(limit, StuckConfig.MAX_SLIDER) / (double) STOPS;
    }

    private static int toLimit(double slider) {
        int index = (int) Math.round(slider * STOPS);
        return index >= STOPS ? StuckConfig.UNLIMITED : index;
    }

    private static Component limitText(int limit) {
        if (limit < 0) {
            return Component.translatable("noboringarrows.config.unlimited");
        }
        if (limit == 0) {
            return Component.translatable("noboringarrows.config.hidden");
        }
        return Component.literal(String.valueOf(limit));
    }
}
