
package me.rsk.hiroshima.gui.hiroshima.theme.hiroshima;

import me.rsk.hiroshima.gui.rgui.component.container.Container;
import me.rsk.hiroshima.gui.rgui.component.container.use.Groupbox;
import me.rsk.hiroshima.gui.rgui.render.AbstractComponentUI;
import me.rsk.hiroshima.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class RootGroupboxUI
extends AbstractComponentUI<Groupbox> {
    @Override
    public void renderComponent(Groupbox component, FontRenderer fontRenderer) {
        GL11.glLineWidth((float)1.0f);
        fontRenderer.drawString(1, 1, component.getName());
        GL11.glColor3f((float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glDisable((int)3553);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)component.getWidth(), (double)0.0);
        GL11.glVertex2d((double)component.getWidth(), (double)0.0);
        GL11.glVertex2d((double)component.getWidth(), (double)component.getHeight());
        GL11.glVertex2d((double)component.getWidth(), (double)component.getHeight());
        GL11.glVertex2d((double)0.0, (double)component.getHeight());
        GL11.glVertex2d((double)0.0, (double)component.getHeight());
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glEnd();
    }

    @Override
    public void handleMouseDown(Groupbox component, int x, int y, int button) {
    }

    @Override
    public void handleAddComponent(Groupbox component, Container container) {
        component.setWidth(100);
        component.setHeight(100);
        component.setOriginOffsetY(component.getTheme().getFontRenderer().getFontHeight() + 3);
    }
}

