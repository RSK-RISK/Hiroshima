package me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima;

import me.rsk.hiroshima.jews.gui.font.CFontRenderer;
import me.rsk.hiroshima.jews.gui.hiroshima.RenderHelper;
import me.rsk.hiroshima.jews.gui.hiroshima.RootSmallFontRenderer;
import me.rsk.hiroshima.jews.gui.rgui.component.container.Container;
import me.rsk.hiroshima.jews.gui.rgui.component.use.Slider;
import me.rsk.hiroshima.jews.gui.rgui.render.AbstractComponentUI;
import me.rsk.hiroshima.jews.gui.rgui.render.font.FontRenderer;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RootSliderUI extends AbstractComponentUI<Slider> {

    RootSmallFontRenderer smallFontRenderer = new RootSmallFontRenderer();
    CFontRenderer cFontRenderer = new CFontRenderer(new Font("Segoe UI", 0, 24), true, false);

    @Override
    public void renderComponent(Slider component, FontRenderer aa) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, component.getOpacity());
        GL11.glLineWidth(1.0f);
        int height = component.getHeight();
        double value = component.getValue();
        double w = component.getWidth() * ((value - component.getMinimum()) / (component.getMaximum() - component.getMinimum()));
        float downscale = 1.1f;

        float w_ = (int) w;

        GL11.glColor3f(0.3f, 0.0f, 0.9f);
        RenderHelper.drawFilledRectangle(0, 0, w_, height/downscale);

        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        RenderHelper.drawRectangle(0, 0, component.getWidth(), height/downscale);

        String s = value + "";
        if (component.isPressed()){
            w_ -= smallFontRenderer.getStringWidth(s)/2;
            w_ = Math.max(0, Math.min(w_, component.getWidth() - smallFontRenderer.getStringWidth(s)));
            smallFontRenderer.drawString((int) w_, 2, s);
        }else{
            smallFontRenderer.drawString(2, 2, component.getText());
            smallFontRenderer.drawString(component.getWidth() - cFontRenderer.getStringWidth(s) - 2, 2, s);
        }
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void handleAddComponent(Slider component, Container container) {
        component.setHeight(component.getTheme().getFontRenderer().getFontHeight() + 2);
        component.setWidth(smallFontRenderer.getStringWidth(component.getText()) + smallFontRenderer.getStringWidth(component.getMaximum() + "") + 3);
    }
}