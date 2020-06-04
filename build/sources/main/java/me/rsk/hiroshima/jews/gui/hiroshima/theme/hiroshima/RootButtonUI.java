/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima;

import java.awt.Color;
import java.awt.Font;

import me.rsk.hiroshima.jews.gui.font.CFontRenderer;
import me.rsk.hiroshima.jews.gui.hiroshima.HiroshimaGUI;
import me.rsk.hiroshima.jews.gui.hiroshima.RenderHelper;
import me.rsk.hiroshima.jews.gui.hiroshima.RootFontRenderer;
import me.rsk.hiroshima.jews.gui.rgui.component.Component;
import me.rsk.hiroshima.jews.gui.rgui.component.container.Container;
import me.rsk.hiroshima.jews.gui.rgui.component.use.Button;
import me.rsk.hiroshima.jews.gui.rgui.render.AbstractComponentUI;
import me.rsk.hiroshima.jews.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class RootButtonUI<T extends Button> extends AbstractComponentUI<Button> {
    CFontRenderer cFontRenderer = new CFontRenderer(new Font("Segoe UI", 0, 24), true, false);

    protected Color idleColour = new Color(163, 163, 163);
    protected Color downColour = new Color(255, 255, 255);

    @Override
    public void renderComponent(Button component, FontRenderer ff) {
        GL11.glColor3f((float)0.22f, (float)0.22f, (float)0.22f);
        if (component.isHovered() || component.isPressed()) {
            GL11.glColor3f((float)0.26f, (float)0.26f, (float)0.26f);
        }
        RenderHelper.drawRoundedRectangle(0.0f, 0.0f, component.getWidth(), component.getHeight(), 3.0f);
        GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)3553);
        //dont cfont this
        HiroshimaGUI.fontRenderer.drawString(component.getWidth() / 2 - cFontRenderer.getStringWidth(component.getName()) / 2, 0, component.isPressed() ? this.downColour : this.idleColour, component.getName());
        GL11.glDisable((int)3553);
        GL11.glDisable((int)3042);
    }

    @Override
    public void handleAddComponent(Button component, Container container) {
        component.setWidth(cFontRenderer.getStringWidth(component.getName()) + 28);
        component.setHeight(HiroshimaGUI.fontRenderer.getFontHeight() + 2);
    }
}