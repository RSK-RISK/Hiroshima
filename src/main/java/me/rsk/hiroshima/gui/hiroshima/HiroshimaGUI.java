
package me.rsk.hiroshima.gui.hiroshima;

import com.mojang.realmsclient.gui.ChatFormatting;

import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

import me.rsk.hiroshima.HiroshimaClient;
import me.rsk.hiroshima.command.Command;
import me.rsk.hiroshima.gui.font.CFontRenderer;
import me.rsk.hiroshima.gui.hiroshima.component.ActiveModules;
import me.rsk.hiroshima.gui.hiroshima.component.SettingsPanel;
import me.rsk.hiroshima.gui.hiroshima.theme.hiroshima.HiroshimaTheme;
import me.rsk.hiroshima.gui.rgui.GUI;
import me.rsk.hiroshima.gui.rgui.component.Component;
import me.rsk.hiroshima.gui.rgui.component.container.use.Frame;
import me.rsk.hiroshima.gui.rgui.component.container.use.Scrollpane;
import me.rsk.hiroshima.gui.rgui.component.listen.MouseListener;
import me.rsk.hiroshima.gui.rgui.component.listen.TickListener;
import me.rsk.hiroshima.gui.rgui.component.use.CheckButton;
import me.rsk.hiroshima.gui.rgui.component.use.Label;
import me.rsk.hiroshima.gui.rgui.render.theme.Theme;
import me.rsk.hiroshima.gui.rgui.util.ContainerHelper;
import me.rsk.hiroshima.gui.rgui.util.Docking;
import me.rsk.hiroshima.module.Module;
import me.rsk.hiroshima.module.ModuleManager;
import me.rsk.hiroshima.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.biome.Biome;

public class HiroshimaGUI
        extends GUI {
    public static final RootFontRenderer fontRenderer = new RootFontRenderer(1.0f);
    public static CFontRenderer cFontRenderer = new CFontRenderer(new Font("Segoe UI", 0, 18), true, false);
    public Theme theme = this.getTheme();
    public static ColourHolder primaryColour = new ColourHolder(29, 29, 29);
    private static final int DOCK_OFFSET = 0;

    public HiroshimaGUI() {
        super(new HiroshimaTheme());
    }

    @Override
    public void drawGUI() {
        super.drawGUI();
    }

    @Override
    public void initializeGUI() {
        int y;
        HashMap<Module.Category, Pair<Scrollpane, SettingsPanel>> categoryScrollpaneHashMap = new HashMap<Module.Category, Pair<Scrollpane, SettingsPanel>>();
        for (final Module module : ModuleManager.getModules()) {
            Scrollpane scrollpane;
            if (module.getCategory().isHidden()) continue;
            Module.Category moduleCategory = module.getCategory();
            if (!categoryScrollpaneHashMap.containsKey((Object) moduleCategory)) {
                Stretcherlayout stretcherlayout = new Stretcherlayout(1);
                stretcherlayout.setComponentOffsetWidth(0);
                scrollpane = new Scrollpane(this.getTheme(), stretcherlayout, 300, 260);
                scrollpane.setMaximumHeight(180);
                categoryScrollpaneHashMap.put(moduleCategory, new Pair<Scrollpane, SettingsPanel>(scrollpane, new SettingsPanel(this.getTheme(), null)));
            }
            final Pair pair = (Pair) categoryScrollpaneHashMap.get((Object) moduleCategory);
            scrollpane = (Scrollpane) pair.getKey();
            final CheckButton checkButton = new CheckButton(module.getName());
            checkButton.setToggled(module.isEnabled());
            checkButton.addTickListener(() -> {
                checkButton.setToggled(module.isEnabled());
                checkButton.setName(module.getName());
            });
            checkButton.addMouseListener(new MouseListener() {

                @Override
                public void onMouseDown(MouseListener.MouseButtonEvent event) {
                    if (event.getButton() == 1) {
                        ((SettingsPanel) pair.getValue()).setModule(module);
                        ((SettingsPanel) pair.getValue()).setX(event.getX() + checkButton.getX());
                        ((SettingsPanel) pair.getValue()).setY(event.getY() + checkButton.getY());
                    }
                }

                @Override
                public void onMouseRelease(MouseListener.MouseButtonEvent event) {
                }

                @Override
                public void onMouseDrag(MouseListener.MouseButtonEvent event) {
                }

                @Override
                public void onMouseMove(MouseListener.MouseMoveEvent event) {
                }

                @Override
                public void onScroll(MouseListener.MouseScrollEvent event) {
                }
            });
            checkButton.addPoof(new CheckButton.CheckButtonPoof<CheckButton, CheckButton.CheckButtonPoof.CheckButtonPoofInfo>() {

                @Override
                public void execute(CheckButton component, CheckButton.CheckButtonPoof.CheckButtonPoofInfo info) {
                    if (info.getAction().equals((Object) CheckButton.CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction.TOGGLE)) {
                        module.setEnabled(checkButton.isToggled());
                    }
                }
            });
            scrollpane.addChild(checkButton);
        }
        int x = 10;
        int nexty = y = 10;
        for (Map.Entry entry : categoryScrollpaneHashMap.entrySet()) {
            Stretcherlayout stretcherlayout = new Stretcherlayout(1);
            stretcherlayout.COMPONENT_OFFSET_Y = 1;
            Frame frame = new Frame(this.getTheme(), stretcherlayout, ((Module.Category) ((Object) entry.getKey())).getName());
            Scrollpane scrollpane = (Scrollpane) ((Pair) entry.getValue()).getKey();
            frame.addChild(scrollpane);
            frame.addChild((Component) ((Pair) entry.getValue()).getValue());
            scrollpane.setOriginOffsetY(0);
            scrollpane.setOriginOffsetX(0);
            frame.setCloseable(false);
            frame.setX(x);
            frame.setY(y);
            this.addChild(frame);
            nexty = Math.max(y + frame.getHeight() + 10, nexty);
            if (!((float) (x += frame.getWidth() + 10) > (float) Wrapper.getMinecraft().displayWidth / 1.2f)) continue;
            nexty = y = nexty;
        }
        this.addMouseListener(new MouseListener() {

            private boolean isBetween(int min, int val, int max) {
                return val <= max && val >= min;
            }

            @Override
            public void onMouseDown(MouseListener.MouseButtonEvent event) {
                List<SettingsPanel> panels = ContainerHelper.getAllChildren(SettingsPanel.class, HiroshimaGUI.this);
                for (SettingsPanel settingsPanel : panels) {
                    if (!settingsPanel.isVisible()) continue;
                    int[] real = GUI.calculateRealPosition(settingsPanel);
                    int pX = event.getX() - real[0];
                    int pY = event.getY() - real[1];
                    if (this.isBetween(0, pX, settingsPanel.getWidth()) && this.isBetween(0, pY, settingsPanel.getHeight()))
                        continue;
                    settingsPanel.setVisible(false);
                }
            }

            @Override
            public void onMouseRelease(MouseListener.MouseButtonEvent event) {
            }

            @Override
            public void onMouseDrag(MouseListener.MouseButtonEvent event) {
            }

            @Override
            public void onMouseMove(MouseListener.MouseMoveEvent event) {
            }

            @Override
            public void onScroll(MouseListener.MouseScrollEvent event) {
            }
        });
        ArrayList<Frame> frames = new ArrayList<Frame>();
        Frame frame = new Frame(this.getTheme(), new Stretcherlayout(1), "Active modules");
        frame.setCloseable(false);
        frame.addChild(new ActiveModules());
        frame.setPinneable(true);
        frames.add(frame);
        frame = new Frame(this.getTheme(), new Stretcherlayout(1), "Info");
        frame.setCloseable(false);
        frame.setPinneable(true);
        Label information = new Label("");
        information.setShadow(true);
        information.addTickListener(() -> {
            information.setText("");
            information.addLine((ChatFormatting.GRAY) + HiroshimaClient.MODNAME + (ChatFormatting.DARK_AQUA) + HiroshimaClient.MODVER);
            information.addLine("\u00a77Welcome " + (ChatFormatting.DARK_AQUA) + Wrapper.getMinecraft().getSession().getUsername());
            information.addLine("\u00a77" + Math.round(LagCompensator.INSTANCE.getTickRate()) + Command.SECTIONSIGN() + "3 tps");
            Wrapper.getMinecraft();
            information.addLine("\u00a77" + Minecraft.debugFPS + Command.SECTIONSIGN() + "3 fps");
        });
        frame.addChild(information);
        information.setFontRenderer(fontRenderer);
        frames.add(frame);
        frame = new Frame(getTheme(), new Stretcherlayout(1), "Japanese");
        frame.setCloseable(false);
        frame.setPinneable(true);
        frame.setMinimumWidth(75);
        frame.setHeight(20);
        frames.add(frame);
        Label watermark = new Label((ChatFormatting.GRAY) + "Hiroshima" + (ChatFormatting.DARK_AQUA) + "\u5e83\u5cf6");
        watermark.setX((frame.getWidth() / 2));
        watermark.setShadow(true);
        frame.addChild(watermark);
        frames.add(frame);
        frame = new Frame(getTheme(), new Stretcherlayout(1), "Coordinates");
        frame.setCloseable(false);
        frame.setPinneable(true);
        Label coordsLabel = new Label("");
        coordsLabel.addTickListener(new TickListener() {
            Minecraft mc = Minecraft.getMinecraft();

            @Override
            public void onTick() {
                boolean inHell = (mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell"));

                int posX = (int) mc.player.posX;
                int posY = (int) mc.player.posY;
                int posZ = (int) mc.player.posZ;

                float f = !inHell ? 0.125f : 8;
                int hposX = (int) (mc.player.posX * f);
                int hposZ = (int) (mc.player.posZ * f);

                coordsLabel.setText(String.format(" %sf%,d%s7, %sf%,d%s7, %sf%,d %s7(%sf%,d%s7, %sf%,d%s7, %sf%,d%s7)",
                        Command.SECTIONSIGN(),
                        posX,
                        Command.SECTIONSIGN(),
                        Command.SECTIONSIGN(),
                        posY,
                        Command.SECTIONSIGN(),
                        Command.SECTIONSIGN(),
                        posZ,
                        Command.SECTIONSIGN(),
                        Command.SECTIONSIGN(),
                        hposX,
                        Command.SECTIONSIGN(),
                        Command.SECTIONSIGN(),
                        posY,
                        Command.SECTIONSIGN(),
                        Command.SECTIONSIGN(),
                        hposZ,
                        Command.SECTIONSIGN()
                ));
            }
        });
        frame.addChild(coordsLabel);
        coordsLabel.setFontRenderer(fontRenderer);
        coordsLabel.setShadow(true);
        frame.setHeight(20);
        frames.add(frame);
        frame = new Frame(getTheme(), new Stretcherlayout(1), "Text Radar");
        Label list = new Label("");
        DecimalFormat dfHealth = new DecimalFormat("#.#");
        dfHealth.setRoundingMode(RoundingMode.CEILING);
        StringBuilder healthSB = new StringBuilder();
        StringBuilder potsSB = new StringBuilder();
        list.addTickListener(() -> {
            if (!list.isVisible()) {
                return;
            }
            list.setText("");

            Minecraft mc = Wrapper.getMinecraft();

            if (mc.player == null) {
                return;
            }

            List<EntityPlayer> entityList = mc.world.playerEntities;

            Map<String, Integer> players = new HashMap<>();
            int playerStep = 0;

            for (Entity entity : entityList) {

                {
                }

                if (entity.getName().equals(mc.player.getName())) {
                    continue;
                }

                EntityPlayer entityPlayer = (EntityPlayer) entity;

                String posString = (entityPlayer.posY > mc.player.posY ? ChatFormatting.DARK_GREEN.toString() + "+" : (entityPlayer.posY == mc.player.posY ? " " : ChatFormatting.DARK_RED.toString() + "-"));
                float hpRaw = entityPlayer.getHealth() + ((EntityLivingBase) entityPlayer).getAbsorptionAmount();
                String hp = dfHealth.format(hpRaw);

                if (hpRaw >= 20) {
                    healthSB.append(ChatFormatting.GREEN.toString());
                } else if (hpRaw >= 10) {
                    healthSB.append(ChatFormatting.YELLOW.toString());
                } else if (hpRaw >= 5) {
                    healthSB.append(ChatFormatting.GOLD.toString());
                } else {
                    healthSB.append(ChatFormatting.RED.toString());
                }
                healthSB.append(hp);
                healthSB.append(" ");

{
                    PotionEffect effectStrength = entityPlayer.getActivePotionEffect(MobEffects.STRENGTH);
                    if (effectStrength != null && entityPlayer.isPotionActive(MobEffects.STRENGTH)) {
                        int duration = effectStrength.getDuration();
                        if (duration > 0) {
                            potsSB.append(ChatFormatting.DARK_RED);
                            potsSB.append(" S ");
                            potsSB.append(ChatFormatting.GRAY);
                            potsSB.append(Potion.getPotionDurationString(effectStrength, 1.0f));
                        }
                    }

                    PotionEffect effectweakness = entityPlayer.getActivePotionEffect(MobEffects.WEAKNESS);
                    if (effectweakness != null && entityPlayer.isPotionActive(MobEffects.WEAKNESS)) {
                        int duration = effectweakness.getDuration();
                        if (duration > 0) {
                            potsSB.append(ChatFormatting.GOLD);
                            potsSB.append(" W ");
                            potsSB.append(ChatFormatting.GRAY);
                            potsSB.append(Potion.getPotionDurationString(effectweakness, 1.0f));
                        }
                    }
                }

                String nameColor;
                if (Friends.isFriend(entity.getName())) {
                    nameColor = ChatFormatting.GREEN.toString();
                } else {
                    nameColor = ChatFormatting.RED.toString();
                }

                players.put(ChatFormatting.GRAY.toString() + posString + " " + healthSB.toString() + nameColor + entityPlayer.getName() + potsSB.toString(), (int) mc.player.getDistance(entityPlayer));

                healthSB.setLength(0);
                potsSB.setLength(0);

                playerStep++;

            }

            if (players.isEmpty()) {
                list.setText("");
                return;
            }

            players = sortByValue(players);

            for (Map.Entry<String, Integer> player : players.entrySet()) {
                list.addLine(player.getKey() + " " + ChatFormatting.DARK_GRAY.toString() + player.getValue());
            }

        });
        frame.setCloseable(false);
        frame.setPinneable(true);
        frame.setMinimumWidth(75);
        list.setShadow(true);
        frame.addChild(list);
        list.setFontRenderer(fontRenderer);
        frames.add(frame);
        frame = new Frame(getTheme(), new Stretcherlayout(1), "PVPGear");
        frame.setCloseable(false);
        frame.setPinneable(true);
        Label PVPGear = new Label("");
        PVPGear.setShadow(true);
        PVPGear.addTickListener(() -> {
            PVPGear.setText("");
            int totemCount = 0;
            int crystalCount = 0;
            int gappleCount = 0;
            int xpCount = 0;
            for (int i=0; i < 45; i++) {
                ItemStack itemStack = Wrapper.getMinecraft().player.inventory.getStackInSlot(i);
                if (itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
                    totemCount += itemStack.stackSize;
                }
                if (itemStack.getItem() == Items.END_CRYSTAL) {
                    crystalCount += itemStack.stackSize;
                }
                if (itemStack.getItem() == Items.GOLDEN_APPLE && itemStack.getItemDamage() == 1) {
                    gappleCount += itemStack.stackSize;
                }
                if (itemStack.getItem() == Items.EXPERIENCE_BOTTLE) {
                    xpCount += itemStack.stackSize;
                }
            }
            PVPGear.addLine((ChatFormatting.GRAY) + "Totems: " + (ChatFormatting.DARK_AQUA) + String.valueOf(totemCount));
            PVPGear.addLine((ChatFormatting.GRAY) + "Crystals: " + (ChatFormatting.DARK_AQUA) + String.valueOf(crystalCount));
            PVPGear.addLine((ChatFormatting.GRAY) + "Gapples: " + (ChatFormatting.DARK_AQUA) + String.valueOf(gappleCount));
            PVPGear.addLine((ChatFormatting.GRAY) + "XP: " + (ChatFormatting.DARK_AQUA) + String.valueOf(xpCount));
        });
 frame.addChild(PVPGear);
 PVPGear.setFontRenderer(fontRenderer);
 frames.add(frame);
 frame = new Frame(this.getTheme(), new Stretcherlayout(1), "Radar");
// frame.setCloseable(false);
// frame.setMinimizeable(true);
// frame.setPinneable(true);
// frame.addChild(new Radar());
// frame.setWidth(100);
// frame.setHeight(100);
        //frames.add(frame);
        for (Frame frame1 : frames) {
            frame1.setX(x);
            frame1.setY(y);
            nexty = Math.max(y + frame1.getHeight() + 10, nexty);
            x += frame1.getWidth() + 10;
            if ((float)(x * DisplayGuiScreen.getScale()) > (float)Wrapper.getMinecraft().displayWidth / 1.2f) {
                nexty = y = nexty;
                x = 10;
            }
            this.addChild(frame1);
        }
    }

    private static String getEntityName(@Nonnull Entity entity) {
        if (entity instanceof EntityItem) {
            return (Object)TextFormatting.DARK_AQUA + ((EntityItem)entity).getItem().getItem().getItemStackDisplayName(((EntityItem)entity).getItem());
        }
        if (entity instanceof EntityWitherSkull) {
            return (Object)TextFormatting.DARK_GRAY + "Wither skull";
        }
        if (entity instanceof EntityEnderCrystal) {
            return (Object)TextFormatting.GRAY + "End crystal";
        }
        if (entity instanceof EntityEnderPearl) {
            return "Thrown ender pearl";
        }
        if (entity instanceof EntityMinecart) {
            return "Minecart";
        }
        if (entity instanceof EntityItemFrame) {
            return "Item frame";
        }
        if (entity instanceof EntityEgg) {
            return "Thrown egg";
        }
        if (entity instanceof EntitySnowball) {
            return "Thrown snowball";
        }
        return entity.getName();
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        LinkedList<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, Comparator.comparing(o -> (Comparable)o.getValue()));
        LinkedHashMap result = new LinkedHashMap();
        for (Map.Entry entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @Override
    public void destroyGUI() {
        this.kill();
    }

    public static void dock(Frame component) {
        Docking docking = component.getDocking();
        if (docking.isTop()) {
            component.setY(0);
        }
        if (docking.isBottom()) {
            component.setY(Wrapper.getMinecraft().displayHeight / DisplayGuiScreen.getScale() - component.getHeight() - 0);
        }
        if (docking.isLeft()) {
            component.setX(0);
        }
        if (docking.isRight()) {
            component.setX(Wrapper.getMinecraft().displayWidth / DisplayGuiScreen.getScale() - component.getWidth() - 0);
        }
        if (docking.isCenterHorizontal()) {
            component.setX(Wrapper.getMinecraft().displayWidth / (DisplayGuiScreen.getScale() * 2) - component.getWidth() / 2);
        }
        if (docking.isCenterVertical()) {
            component.setY(Wrapper.getMinecraft().displayHeight / (DisplayGuiScreen.getScale() * 2) - component.getHeight() / 2);
        }
    }

}

