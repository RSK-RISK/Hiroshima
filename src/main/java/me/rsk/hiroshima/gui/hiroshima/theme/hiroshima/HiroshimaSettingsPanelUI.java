package me.rsk.hiroshima.gui.hiroshima.theme.hiroshima;

import me.rsk.hiroshima.gui.hiroshima.RenderHelper;
import me.rsk.hiroshima.gui.hiroshima.component.SettingsPanel;
import me.rsk.hiroshima.gui.rgui.render.AbstractComponentUI;
import me.rsk.hiroshima.gui.rgui.render.font.FontRenderer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by 086 on 16/12/2017.
 */
public class HiroshimaSettingsPanelUI extends AbstractComponentUI<SettingsPanel> {

    @Override
    public void renderComponent(SettingsPanel component, FontRenderer fontRenderer) {
        super.renderComponent(component, fontRenderer);
//        glLineWidth(2);
//        glColor3f(.59f,.05f,.11f);
//        glBegin(GL_LINES);
//        {
//            glVertex2d(0,component.getHeight());
//            glVertex2d(component.getWidth(),component.getHeight());
//        }
//        glEnd();

        glLineWidth(2f);
        glColor4f(0,0,0,.6f);
        RenderHelper.drawFilledRectangle(0,0,component.getWidth(),component.getHeight());
        glColor3f(0,170,170);
        glLineWidth(1.5f);
        RenderHelper.drawRectangle(0,0,component.getWidth(),component.getHeight());
    }
}
