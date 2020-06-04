package me.rsk.hiroshima.jews.module.modules.hidden;


import me.rsk.hiroshima.gasda.event.events.RenderEvent;
import me.rsk.hiroshima.gasda.setting.Setting;
import me.rsk.hiroshima.gasda.setting.Settings;
import me.rsk.hiroshima.gasda.util.EntityUtil;
import me.rsk.hiroshima.gasda.util.Friends;
import me.rsk.hiroshima.gasda.util.OsirisTessellator;
import me.rsk.hiroshima.gasda.util.TurokGL;
import me.rsk.hiroshima.jews.module.Module;
import me.rsk.hiroshima.jews.module.modules.combat.Surround;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import java.nio.channels.SeekableByteChannel;

import static org.lwjgl.opengl.GL11.*;

@Module.Info(name = "CornerESP", category = Module.Category.RENDER)
public class CsgoESP extends Module {

    private Setting<Boolean> players = this.register(Settings.b("Players", true));
    private Setting<Boolean> passive = this.register(Settings.b("Passive", true));
    private Setting<Boolean> monsters = this.register(Settings.b("Monsters", true));
    private Setting<Boolean> items = this.register(Settings.b("Items", true));
    private Setting<Boolean> xpBottles = this.register(Settings.b("XP", true));
    private Setting<Boolean> crystals = this.register(Settings.b("Crystals", true));
    private Setting<Boolean> orbs = this.register(Settings.b("Orbs", true));



    public void onWorldRender(RenderEvent event) {
        if (mc.getRenderManager().options == null) return;
        boolean isThirdPersonFrontal = mc.getRenderManager().options.thirdPersonView == 2;
        float viewerYaw = mc.getRenderManager().playerViewY;

        mc.world.loadedEntityList.stream()
                .filter(entity -> mc.player != entity)
                .forEach(e -> {
                    OsirisTessellator.prepareGL();
                    GlStateManager.pushMatrix();
                    Vec3d pos = Surround.getInterpolatedPos(e, mc.getRenderPartialTicks());
                    GlStateManager.translate(pos.x-mc.getRenderManager().renderPosX, pos.y-mc.getRenderManager().renderPosY, pos.z-mc.getRenderManager().renderPosZ);
                    GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate((float)(isThirdPersonFrontal ? -1 : 1), 1.0F, 0.0F, 0.0F);
                    glColor4f(1, 1, 1, 0.5f);

                    glLineWidth(3f);
                    glEnable(GL_LINE_SMOOTH);

                    if(e instanceof EntityPlayer && players.getValBoolean()) {
                        if (Friends.isFriend(e.getName())) {
                            glColor4f(0, 1, 1, 0.5f);
                        } else {
                            glColor4f(1f, 0f, 0f, 0.5f);
                        }
                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(-e.width, 0);
                            glVertex2d(-e.width, e.height / 3);
                            glVertex2d(-e.width, 0);
                            glVertex2d((-e.width / 3) * 2, 0);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(-e.width, e.height);
                            glVertex2d((-e.width / 3) * 2, e.height);
                            glVertex2d(-e.width, e.height);
                            glVertex2d(-e.width, (e.height / 3) * 2);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(e.width, e.height);
                            glVertex2d((e.width / 3) * 2, e.height);
                            glVertex2d(e.width, e.height);
                            glVertex2d(e.width, (e.height / 3) * 2);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(e.width, 0);
                            glVertex2d((e.width / 3) * 2, 0);
                            glVertex2d(e.width, 0);
                            glVertex2d(e.width, e.height / 3);
                        }
                        glEnd();
                    }

                    glColor4f(1, 1, 1, 0.5f);

                    if(EntityUtil.isPassive(e) && passive.getValBoolean()) {
                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(-e.width, 0);
                            glVertex2d(-e.width, e.height / 3);
                            glVertex2d(-e.width, 0);
                            glVertex2d((-e.width / 3) * 2, 0);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(-e.width, e.height);
                            glVertex2d((-e.width / 3) * 2, e.height);
                            glVertex2d(-e.width, e.height);
                            glVertex2d(-e.width, (e.height / 3) * 2);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(e.width, e.height);
                            glVertex2d((e.width / 3) * 2, e.height);
                            glVertex2d(e.width, e.height);
                            glVertex2d(e.width, (e.height / 3) * 2);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(e.width, 0);
                            glVertex2d((e.width / 3) * 2, 0);
                            glVertex2d(e.width, 0);
                            glVertex2d(e.width, e.height / 3);
                        }
                        glEnd();
                    }

                    if(EntityUtil.isHostileMob(e) && monsters.getValBoolean()) {
                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(-e.width, 0);
                            glVertex2d(-e.width, e.height / 3);
                            glVertex2d(-e.width, 0);
                            glVertex2d((-e.width / 3) * 2, 0);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(-e.width, e.height);
                            glVertex2d((-e.width / 3) * 2, e.height);
                            glVertex2d(-e.width, e.height);
                            glVertex2d(-e.width, (e.height / 3) * 2);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(e.width, e.height);
                            glVertex2d((e.width / 3) * 2, e.height);
                            glVertex2d(e.width, e.height);
                            glVertex2d(e.width, (e.height / 3) * 2);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(e.width, 0);
                            glVertex2d((e.width / 3) * 2, 0);
                            glVertex2d(e.width, 0);
                            glVertex2d(e.width, e.height / 3);
                        }
                        glEnd();
                    }

                    if(e instanceof EntityItem && items.getValBoolean()) {
                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(-e.width, 0);
                            glVertex2d(-e.width, e.height / 3);
                            glVertex2d(-e.width, 0);
                            glVertex2d((-e.width / 3) * 2, 0);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(-e.width, e.height);
                            glVertex2d((-e.width / 3) * 2, e.height);
                            glVertex2d(-e.width, e.height);
                            glVertex2d(-e.width, (e.height / 3) * 2);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(e.width, e.height);
                            glVertex2d((e.width / 3) * 2, e.height);
                            glVertex2d(e.width, e.height);
                            glVertex2d(e.width, (e.height / 3) * 2);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(e.width, 0);
                            glVertex2d((e.width / 3) * 2, 0);
                            glVertex2d(e.width, 0);
                            glVertex2d(e.width, e.height / 3);
                        }
                        glEnd();
                    }

                    if(e instanceof EntityExpBottle && xpBottles.getValBoolean()) {
                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(-e.width, 0);
                            glVertex2d(-e.width, e.height / 3);
                            glVertex2d(-e.width, 0);
                            glVertex2d((-e.width / 3) * 2, 0);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(-e.width, e.height);
                            glVertex2d((-e.width / 3) * 2, e.height);
                            glVertex2d(-e.width, e.height);
                            glVertex2d(-e.width, (e.height / 3) * 2);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(e.width, e.height);
                            glVertex2d((e.width / 3) * 2, e.height);
                            glVertex2d(e.width, e.height);
                            glVertex2d(e.width, (e.height / 3) * 2);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(e.width, 0);
                            glVertex2d((e.width / 3) * 2, 0);
                            glVertex2d(e.width, 0);
                            glVertex2d(e.width, e.height / 3);
                        }
                        glEnd();
                    }


                    if(e instanceof EntityEnderCrystal && crystals.getValBoolean()) {
                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(-e.width / 2, 0);
                            glVertex2d(-e.width / 2, e.height / 3);
                            glVertex2d(-e.width / 2, 0);
                            glVertex2d(((-e.width / 3) * 2) / 2, 0);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(-e.width / 2, e.height);
                            glVertex2d(((-e.width / 3) * 2) / 2, e.height);
                            glVertex2d(-e.width / 2, e.height);
                            glVertex2d(-e.width / 2, (e.height / 3) * 2);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(e.width / 2, e.height);
                            glVertex2d(((e.width / 3) * 2) / 2, e.height);
                            glVertex2d(e.width / 2, e.height);
                            glVertex2d(e.width / 2, (e.height / 3) * 2);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(e.width / 2, 0);
                            glVertex2d(((e.width / 3) * 2) / 2, 0);
                            glVertex2d(e.width / 2, 0);
                            glVertex2d(e.width / 2, e.height / 3);
                        }
                        glEnd();
                    }

                    if(e instanceof EntityXPOrb && orbs.getValBoolean()) {
                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(-e.width / 2, 0);
                            glVertex2d(-e.width / 2, e.height / 3);
                            glVertex2d(-e.width / 2, 0);
                            glVertex2d(((-e.width / 3) * 2) / 2, 0);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(-e.width / 2, e.height);
                            glVertex2d(((-e.width / 3) * 2) / 2, e.height);
                            glVertex2d(-e.width / 2, e.height);
                            glVertex2d(-e.width / 2, (e.height / 3) * 2);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(e.width / 2, e.height);
                            glVertex2d(((e.width / 3) * 2) / 2, e.height);
                            glVertex2d(e.width / 2, e.height);
                            glVertex2d(e.width / 2, (e.height / 3) * 2);
                        }
                        glEnd();

                        glBegin(GL_LINE_LOOP);
                        {
                            glVertex2d(e.width / 2, 0);
                            glVertex2d(((e.width / 3) * 2) / 2, 0);
                            glVertex2d(e.width / 2, 0);
                            glVertex2d(e.width / 2, e.height / 3);
                        }
                        glEnd();
                    }

                    OsirisTessellator.releaseGL();
                    GlStateManager.popMatrix();
                });
        glColor4f(1,1,1, 1);
    }
}
