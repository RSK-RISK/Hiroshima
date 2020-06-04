package me.rsk.hiroshima.jews.gui.hiroshima.theme.hiroshima;

import me.rsk.hiroshima.HiroshimaClient;
import me.rsk.hiroshima.gasda.util.Bind;
import me.rsk.hiroshima.gasda.util.ColourHolder;
import me.rsk.hiroshima.gasda.util.TurokGL;
import me.rsk.hiroshima.gasda.util.Wrapper;
import me.rsk.hiroshima.jews.gui.font.CFontRenderer;
import me.rsk.hiroshima.jews.gui.hiroshima.*;
import me.rsk.hiroshima.jews.gui.rgui.component.AlignedComponent;
import me.rsk.hiroshima.jews.gui.rgui.component.Component;
import me.rsk.hiroshima.jews.gui.rgui.component.container.Container;
import me.rsk.hiroshima.jews.gui.rgui.component.container.use.Frame;
import me.rsk.hiroshima.jews.gui.rgui.component.listen.MouseListener;
import me.rsk.hiroshima.jews.gui.rgui.component.listen.UpdateListener;
import me.rsk.hiroshima.jews.gui.rgui.poof.use.FramePoof;
import me.rsk.hiroshima.jews.gui.rgui.render.AbstractComponentUI;
import me.rsk.hiroshima.jews.gui.rgui.render.font.FontRenderer;
import me.rsk.hiroshima.jews.gui.rgui.util.ContainerHelper;
import me.rsk.hiroshima.jews.gui.rgui.util.Docking;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class HiroshimaFrameUI<T extends Frame>
        extends AbstractComponentUI<Frame> {
    ColourHolder frameColour = HiroshimaGUI.primaryColour.setA(100);
    ColourHolder outlineColour = this.frameColour.darker();
    Component yLineComponent = null;
    Component xLineComponent = null;
    Component centerXComponent = null;
    Component centerYComponent = null;
    boolean centerX = false;
    boolean centerY = false;
    int xLineOffset = 0;

    private static final RootFontRenderer ff = new RootLargeFontRenderer();
    public float redForBG;
    public float greenForBG;
    public float blueForBG;
    CFontRenderer cFontRenderer = new CFontRenderer(new Font("Segoe UI", 0, 18), true, false);

    @Override
    public void renderComponent(Frame component, FontRenderer fontRenderer) {
        if (component.getOpacity() != 0.0F)


        TurokGL.turok_Disable(GL_TEXTURE_2D);

        TurokGL.turok_RGBA(0,0,0,150);
        RenderHelper.drawFilledRectangle(0, 0, component.getWidth(), component.getHeight());

        TurokGL.turok_RGBA(0,85,85,50);
        RenderHelper.drawFilledRectangle(2.5f,2.5f,component.getWidth() - 5f ,component.getHeight() - 5f);

        TurokGL.turok_RGBA(0,170,170,255);
        RenderHelper.drawFilledRectangle(0, 0, component.getWidth(), ff.getStringHeight(component.getTitle()) + 2);

        int top_y = 5;
        int bottom_y = component.getTheme().getFontRenderer().getFontHeight() - 9;

        if (component.isCloseable() && component.isMinimizeable()){
            top_y -= 4;
            bottom_y -= 4;
        }

        if (component.isCloseable() && component.isMinimizeable()){
            top_y += 12;
            bottom_y += 12;
        }

        if (component.isPinneable()){
            if (component.isPinned())
                TurokGL.turok_RGBA(0, 170, 170,  255);
            else
                TurokGL.turok_RGBA(0,75,75,255);
            RenderHelper.drawCircle(7,4,2f);
            glLineWidth(3f);
            glBegin(GL_LINES);
            {
                glVertex2d(7,4);
                glVertex2d(4,8);
            }
            glEnd();


            RenderHelper.drawFilledRectangle(0, 0, component.getWidth(), ff.getStringHeight(component.getTitle()) + 2);

            TurokGL.turok_RGBA(255, 255, 255, 255);
            ff.drawString(1, 1, component.getTitle());

        } else {
            TurokGL.turok_RGBA(255, 255, 255, 255);
            ff.drawString(1, 1, component.getTitle());
        }

        // I removed somethings. Dont worry.
        TurokGL.turok_FixGL("For fix OpenGL refresh Minecraft");
    }

    @Override
    public void handleMouseRelease(Frame component, int x, int y, int button) {
        yLineComponent = null;
        xLineComponent = null;
        centerXComponent = null;
        centerYComponent = null;
    }

    @Override
    public void handleMouseDrag(Frame component, int x, int y, int button) {
        super.handleMouseDrag(component, x, y, button);
    }

    @Override
    public void handleAddComponent(Frame component, Container container) {
        super.handleAddComponent(component, container);
        component.setOriginOffsetY(component.getTheme().getFontRenderer().getFontHeight() + 3);
        component.setOriginOffsetX(3);

        component.addMouseListener(new MouseListener() {
            @Override
            public void onMouseDown(MouseButtonEvent event) {
                int y = event.getY();
                int x = event.getX();

                if (y < 0){
                    if (x > component.getWidth() - 22){
                        if (component.isMinimizeable() && component.isCloseable()) {
                            if (y > -component.getOriginOffsetY()/2){
                                if (component.isMinimized()){
                                    component.callPoof(FramePoof.class, new FramePoof.FramePoofInfo(FramePoof.Action.MAXIMIZE));
                                }else {
                                    component.callPoof(FramePoof.class, new FramePoof.FramePoofInfo(FramePoof.Action.MINIMIZE));
                                }
                            }else{
                                component.callPoof(FramePoof.class, new FramePoof.FramePoofInfo(FramePoof.Action.CLOSE));
                            }
                        }else{
                            if (component.isMinimized() && component.isMinimizeable()){
                                component.callPoof(FramePoof.class, new FramePoof.FramePoofInfo(FramePoof.Action.MAXIMIZE));
                            }else if (component.isMinimizeable()){
                                component.callPoof(FramePoof.class, new FramePoof.FramePoofInfo(FramePoof.Action.MINIMIZE));
                            }else if (component.isCloseable()) {
                                component.callPoof(FramePoof.class, new FramePoof.FramePoofInfo(FramePoof.Action.CLOSE));
                            }
                        }
                    }
                    if (x < component.getWidth() - 22 && x > component.getWidth() - ff.getStringWidth(component.getTitle()) - 22) {
                        if (component.isPinneable()){
                            component.setPinned(!component.isPinned());
                        }
                    }
                }
            }

            @Override
            public void onMouseRelease(MouseButtonEvent event) {

            }

            @Override
            public void onMouseDrag(MouseButtonEvent event) {

            }

            @Override
            public void onMouseMove(MouseMoveEvent event) {
            }

            @Override
            public void onScroll(MouseScrollEvent event) {

            }
        });

        component.addUpdateListener(new UpdateListener() {
            @Override
            public void updateSize(Component component, int oldWidth, int oldHeight) {
                if (component instanceof Frame) {
                    HiroshimaGUI.dock((Frame) component);
                }
            }
            @Override
            public void updateLocation(Component component, int oldX, int oldY) { }
        });

        component.addPoof(new Frame.FrameDragPoof<Frame, Frame.FrameDragPoof.DragInfo>() {
            @Override
            public void execute(Frame component, DragInfo info) {
                if (Bind.isShiftDown() || Bind.isAltDown() || Bind.isCtrlDown()) return;
                int x = info.getX();
                int y = info.getY();
                yLineComponent = null;
                xLineComponent = null;

                component.setDocking(Docking.NONE);

                HiroshimaGUI rootGUI = HiroshimaClient.getInstance().getGuiManager();
                for (Component c : rootGUI.getChildren()){
                    if (c.equals(component)) continue;

                    int yDiff = Math.abs(y - c.getY());
                    if (yDiff < 4){
                        y = c.getY();
                        yLineComponent = component;
                    }

                    yDiff = Math.abs(y - (c.getY() + c.getHeight() + 3));
                    if (yDiff < 4) {
                        y = c.getY() + c.getHeight();
                        y += 3;
                        yLineComponent = component;
                    }

                    int xDiff = Math.abs((x + 130) - (c.getX() + c.getWidth()));
                    if (xDiff < 4){
                        x = c.getX() + c.getWidth();
                        x -= 130;
                        xLineComponent = component;
                        xLineOffset = 130;
                    }

                    xDiff = Math.abs(x - c.getX());
                    if (xDiff < 4){
                        x = c.getX();
                        xLineComponent = component;
                        xLineOffset = 0;
                    }

                    xDiff = Math.abs(x - (c.getX() + c.getWidth() + 3));
                    if (xDiff < 4){
                        x = c.getX() + c.getWidth() + 3;
                        xLineComponent = component;
                        xLineOffset = 0;
                    }

                }

                if (x < 5) {
                    x = 0;
                    ContainerHelper.setAlignment(component, AlignedComponent.Alignment.LEFT);
                    component.setDocking(Docking.LEFT);
                }
                int diff = (x+130) * DisplayGuiScreen.getScale() - Wrapper.getMinecraft().displayWidth;
                if (-diff < 5){
                    x = (Wrapper.getMinecraft().displayWidth / DisplayGuiScreen.getScale())-130;
                    ContainerHelper.setAlignment(component, AlignedComponent.Alignment.RIGHT);
                    component.setDocking(Docking.RIGHT);
                }

                if (y < 5) {
                    y = 0;
                    if (component.getDocking().equals(Docking.RIGHT))
                        component.setDocking(Docking.TOPRIGHT);
                    else if (component.getDocking().equals(Docking.LEFT))
                        component.setDocking(Docking.TOPLEFT);
                    else
                        component.setDocking(Docking.TOP);
                }

                diff = (y+component.getHeight()) * DisplayGuiScreen.getScale() - Wrapper.getMinecraft().displayHeight;
                if (-diff < 5) {
                    y = (Wrapper.getMinecraft().displayHeight / DisplayGuiScreen.getScale()) - component.getHeight();

                    if (component.getDocking().equals(Docking.RIGHT))
                        component.setDocking(Docking.BOTTOMRIGHT);
                    else if (component.getDocking().equals(Docking.LEFT))
                        component.setDocking(Docking.BOTTOMLEFT);
                    else
                        component.setDocking(Docking.BOTTOM);
                }

                if (Math.abs(((x + 130 / 2) * DisplayGuiScreen.getScale() * 2) - Wrapper.getMinecraft().displayWidth) < 5) { // Component is center-aligned on the x axis
                    xLineComponent = null;
                    centerXComponent = component;
                    centerX = true;
                    x = (Wrapper.getMinecraft().displayWidth / (DisplayGuiScreen.getScale() * 2)) - 130 / 2;
                    if (component.getDocking().isTop()) {
                        component.setDocking(Docking.CENTERTOP);
                    } else if (component.getDocking().isBottom()){
                        component.setDocking(Docking.CENTERBOTTOM);
                    } else {
                        component.setDocking(Docking.CENTERVERTICAL);
                    }
                    ContainerHelper.setAlignment(component, AlignedComponent.Alignment.CENTER);
                } else {
                    centerX = false;
                }

                if (Math.abs(((y + component.getHeight() / 2) * DisplayGuiScreen.getScale() * 2) - Wrapper.getMinecraft().displayHeight) < 5) { // Component is center-aligned on the y axis
                    yLineComponent = null;
                    centerYComponent = component;
                    centerY = true;
                    y = (Wrapper.getMinecraft().displayHeight / (DisplayGuiScreen.getScale() * 2)) - component.getHeight() / 2;
                    if (component.getDocking().isLeft()) {
                        component.setDocking(Docking.CENTERLEFT);
                    } else if (component.getDocking().isRight()) {
                        component.setDocking(Docking.CENTERRIGHT);
                    } else if (component.getDocking().isCenterHorizontal()) {
                        component.setDocking(Docking.CENTER);
                    } else {
                        component.setDocking(Docking.CENTERHOIZONTAL);
                    }
                } else {
                    centerY = false;
                }

                info.setX(x);
                info.setY(y);
            }
        });
    }
}