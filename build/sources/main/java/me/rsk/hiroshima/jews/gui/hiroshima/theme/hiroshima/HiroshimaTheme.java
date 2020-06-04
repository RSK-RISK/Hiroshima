/*
 * Decompiled with CFR 0.145.
 */
package me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima;

import me.rsk.hiroshima.jews.gui.hiroshima.HiroshimaGUI;
import me.rsk.hiroshima.jews.gui.hiroshima.RootFontRenderer;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima.HiroshimaActiveModulesUI;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima.HiroshimaEnumbuttonUI;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima.HiroshimaFrameUI;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima.HiroshimaSettingsPanelUI;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima.HiroshimaUnboundSliderUI;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima.RootButtonUI;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima.RootChatUI;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima.RootCheckButtonUI;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima.RootColorizedCheckButtonUI;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima.RootGroupboxUI;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima.RootInputFieldUI;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima.RootLabelUI;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima.RootScrollpaneUI;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima.RootSliderUI;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.staticui.RadarUI;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.staticui.TabGuiUI;
import me.rsk.hiroshima.jews.gui.rgui.render.AbstractComponentUI;
import me.rsk.hiroshima.jews.gui.rgui.render.ComponentUI;
import me.rsk.hiroshima.jews.gui.rgui.render.font.FontRenderer;
import me.rsk.hiroshima.jews.gui.rgui.render.theme.AbstractTheme;

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

