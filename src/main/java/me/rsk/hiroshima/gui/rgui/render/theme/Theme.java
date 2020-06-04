package me.rsk.hiroshima.gui.rgui.render.theme;

import me.rsk.hiroshima.gui.rgui.component.Component;
import me.rsk.hiroshima.gui.rgui.render.ComponentUI;
import me.rsk.hiroshima.gui.rgui.render.font.FontRenderer;

/**
 * Created by 086 on 25/06/2017.
 */
public interface Theme {
    public ComponentUI getUIForComponent(Component component);
    public FontRenderer getFontRenderer();
}
