
package me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima;

import java.awt.Color;
import me.rsk.hiroshima.jews.gui.hiroshima.HiroshimaGUI;
import me.rsk.hiroshima.jews.gui.hiroshima.RootFontRenderer;
import me.rsk.hiroshima.jews.gui.hiroshima.RootSmallFontRenderer;
import me.rsk.hiroshima.jews.gui.hiroshima.component.ColorizedCheckButton;
import me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima.RootCheckButtonUI;
import me.rsk.hiroshima.jews.gui.rgui.component.Component;
import me.rsk.hiroshima.jews.gui.rgui.component.use.CheckButton;
import me.rsk.hiroshima.jews.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class RootColorizedCheckButtonUI
extends RootCheckButtonUI<ColorizedCheckButton> {
    RootSmallFontRenderer ff = new RootSmallFontRenderer();

    public RootColorizedCheckButtonUI() {
        this.backgroundColour = new Color(0,0,170);
        this.backgroundColourHover = new Color(170,170,170);
        this.downColourNormal = new Color(70, 70, 70);
    }

    @Override
    public void renderComponent(CheckButton component, FontRenderer aa) {
        GL11.glColor4f((float)((float)this.backgroundColour.getRed() / 255.0f), (float)((float)this.backgroundColour.getGreen() / 255.0f), (float)((float)this.backgroundColour.getBlue() / 255.0f), (float)component.getOpacity());
        if (component.isHovered() || component.isPressed()) {
            GL11.glColor4f((float)((float)this.backgroundColourHover.getRed() / 255.0f), (float)((float)this.backgroundColourHover.getGreen() / 255.0f), (float)((float)this.backgroundColourHover.getBlue() / 255.0f), (float)component.getOpacity());
        }
        if (component.isToggled()) {
            GL11.glColor3f((float)((float)this.backgroundColour.getRed() / 255.0f), (float)((float)this.backgroundColour.getGreen() / 255.0f), (float)((float)this.backgroundColour.getBlue() / 255.0f));
        }
        GL11.glLineWidth((float)2.5f);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)0.0, (double)component.getHeight());
        GL11.glVertex2d((double)component.getWidth(), (double)component.getHeight());
        GL11.glEnd();
        Color idleColour = component.isToggled() ? this.idleColourToggle : this.idleColourNormal;
        Color downColour = component.isToggled() ? this.downColourToggle : this.downColourNormal;
        GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)3553);
        this.ff.drawString(component.getWidth() / 2 - HiroshimaGUI.fontRenderer.getStringWidth(component.getName()) / 2, 0, component.isPressed() ? downColour : idleColour, component.getName());
        GL11.glDisable((int)3553);
    }
}

