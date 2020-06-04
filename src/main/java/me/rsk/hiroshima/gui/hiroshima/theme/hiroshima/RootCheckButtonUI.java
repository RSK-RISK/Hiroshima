package me.rsk.hiroshima.gui.hiroshima.theme.hiroshima;

import me.rsk.hiroshima.gui.rgui.component.container.Container;
import me.rsk.hiroshima.gui.hiroshima.HiroshimaGUI;
import org.lwjgl.opengl.GL11;
import me.rsk.hiroshima.gui.rgui.render.font.FontRenderer;
import java.awt.Font;
import java.awt.Color;
import me.rsk.hiroshima.gui.font.CFontRenderer;
import me.rsk.hiroshima.gui.rgui.render.AbstractComponentUI;
import me.rsk.hiroshima.gui.rgui.component.use.CheckButton;

public class RootCheckButtonUI<T extends CheckButton> extends AbstractComponentUI<CheckButton>
{
    CFontRenderer cFontRenderer;
    protected Color backgroundColour = new Color(200, 56, 56);
    protected Color backgroundColourHover = new Color(255,66,66);

    protected Color idleColourNormal = new Color(200, 200, 200);
    protected Color downColourNormal = new Color(190, 190, 190);

    protected Color idleColourToggle = new Color(250, 120, 120);
    protected Color downColourToggle = idleColourToggle.brighter();

    public RootCheckButtonUI() {
        this.cFontRenderer = new CFontRenderer(new Font("Segoe UI", 0, 18), true, false);
    }

    @Override
    public void renderComponent(final CheckButton component, final FontRenderer ff) {
        if (component.isToggled()) {}
        if (component.isHovered() || component.isPressed()) {}
        final String text = component.getName();
        int c = component.isPressed() ? 0xffffff : component.isToggled() ? 0x0bc9e7 : 0xffffff;
        if (component.isHovered()) {
            c = (c & 0x7F7F7F) << 1;
        }
        GL11.glEnable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(2884);
        HiroshimaGUI.cFontRenderer.drawString(text, (float)(component.getWidth() / 2 - HiroshimaGUI.fontRenderer.getStringWidth(text) / 2), (float)(HiroshimaGUI.fontRenderer.getFontHeight() / 2 - 2), c);
        GL11.glDisable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2884);
    }

    @Override
    public void handleAddComponent(final CheckButton component, final Container container) {
        component.setWidth(HiroshimaGUI.fontRenderer.getStringWidth(component.getName()) + 28);
        component.setHeight(HiroshimaGUI.fontRenderer.getFontHeight() + 2);
    }
}