/*
 * Decompiled with CFR 0.145.
 */
package me.rsk.hiroshima.gui.hiroshima.theme.hiroshima;

import me.rsk.hiroshima.gui.hiroshima.HiroshimaGUI;
import me.rsk.hiroshima.gui.hiroshima.theme.staticui.RadarUI;
import me.rsk.hiroshima.gui.hiroshima.theme.staticui.TabGuiUI;
import me.rsk.hiroshima.gui.rgui.render.AbstractComponentUI;
import me.rsk.hiroshima.gui.rgui.render.font.FontRenderer;
import me.rsk.hiroshima.gui.rgui.render.theme.AbstractTheme;

public class HiroshimaTheme
extends AbstractTheme {
    FontRenderer fontRenderer;

    public HiroshimaTheme() {
        this.installUI(new RootButtonUI());
        this.installUI(new GUIUI());
        this.installUI(new RootGroupboxUI());
        this.installUI(new HiroshimaFrameUI());
        this.installUI(new RootScrollpaneUI());
        this.installUI(new RootInputFieldUI());
        this.installUI(new RootLabelUI());
        this.installUI(new RootChatUI());
        this.installUI(new RootCheckButtonUI());
        this.installUI(new HiroshimaActiveModulesUI());
        this.installUI(new HiroshimaSettingsPanelUI());
        this.installUI(new RootSliderUI());
        this.installUI(new HiroshimaEnumbuttonUI());
        this.installUI(new RootColorizedCheckButtonUI());
        this.installUI(new HiroshimaUnboundSliderUI());
        this.installUI(new RadarUI());
        this.installUI(new TabGuiUI());
        this.fontRenderer = HiroshimaGUI.fontRenderer;
    }

    @Override
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }

    public class GUIUI
    extends AbstractComponentUI<HiroshimaGUI> {
    }

}

