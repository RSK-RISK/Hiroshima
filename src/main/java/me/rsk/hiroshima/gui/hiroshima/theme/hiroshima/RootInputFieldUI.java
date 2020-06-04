
package me.rsk.hiroshima.gui.hiroshima.theme.hiroshima;

import me.rsk.hiroshima.gui.hiroshima.RenderHelper;
import me.rsk.hiroshima.gui.rgui.component.container.Container;
import me.rsk.hiroshima.gui.rgui.component.use.InputField;
import me.rsk.hiroshima.gui.rgui.render.AbstractComponentUI;
import me.rsk.hiroshima.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class RootInputFieldUI<T extends InputField>
extends AbstractComponentUI<InputField> {
    @Override
    public void renderComponent(InputField component, FontRenderer fontRenderer) {
        GL11.glColor3f((float)0.33f, (float)0.22f, (float)0.22f);
        RenderHelper.drawFilledRectangle(0.0f, 0.0f, component.getWidth(), component.getHeight());
        GL11.glLineWidth((float)1.5f);
        GL11.glColor4f((float)1.0f, (float)0.33f, (float)0.33f, (float)0.6f);
        RenderHelper.drawRectangle(0.0f, 0.0f, component.getWidth(), component.getHeight());
    }

    @Override
    public void handleAddComponent(InputField component, Container container) {
        component.setWidth(200);
        component.setHeight(component.getTheme().getFontRenderer().getFontHeight());
    }
}

