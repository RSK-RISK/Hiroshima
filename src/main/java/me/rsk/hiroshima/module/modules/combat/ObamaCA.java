package me.rsk.hiroshima.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.rsk.hiroshima.HiroshimaClient;
import me.rsk.hiroshima.command.Command;
import me.rsk.hiroshima.event.events.PacketEvent;
import me.rsk.hiroshima.event.events.RenderEvent;
import me.rsk.hiroshima.module.Module;
import me.rsk.hiroshima.setting.Setting;
import me.rsk.hiroshima.setting.Settings;
import me.rsk.hiroshima.util.EntityUtil;
import me.rsk.hiroshima.util.Friends;
import me.rsk.hiroshima.util.HiroshimaTessellator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Module.Info(name = "AAdevCA", description = "Crystal Aura", category = Module.Category.COMBAT)
public class ObamaCA extends Module {

    private Setting<Integer> tickPlaceDelay;
    private Setting<Integer> msPlaceDelay;
    private Setting<Integer> tickBreakDelay;
    private Setting<Integer> msBreakDelay;
    private Setting<Double> placeRange;
    private Setting<Double> breakRange;
    private Setting<Integer> enemyRange;
    private Setting<Integer> minDamage;
    private Setting<Double> placeThroughWallsRange;
    private Setting<Integer> ignoreMinDamageThreshold;
    private Setting<Integer> friendProtectThreshold;
    private Setting<Integer> selfProtectThreshold;
    private Setting<Double> breakThroughWallsRange;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Integer> alpha;
    private Setting<delayMode> delayMode;
    private Setting<Boolean> ignoreMinDamageOnBreak;
    private Setting<Boolean> antiSuicide;
    private Setting<Boolean> antiStuck;
    private Setting<Boolean> onlyBreakOwnCrystals;
    private Setting<Boolean> multiplace;
    private Setting<Boolean> friendProtect; // don't explode crystals that deal too much damage to friends
    private Setting<Boolean> selfProtect;
    private Setting<Boolean> lockOn; // only target one player
    private Setting<Boolean> enemyPriority; // prioritize targets on enemy list
    private Setting<Boolean> chatAlert;
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> autoOffhand; // enable offhand crystal with toggle
    private Setting<Boolean> antiWeakness;
    private Setting<Boolean> raytrace;
    private Setting<Boolean> place;
    private Setting<Boolean> explode;
    private Setting<Boolean> rainbow;
    private Setting<Boolean> antiWeaknessOffhand;
    private Setting<Double> breakYOffset;
    private Setting<Boolean> renderBreakTarget;

    public static Logger logger;
    private long breakSystemTime;
    private long placeSystemTime;
    private long antiStuckSystemTime;
    private static double yaw;
    private static double pitch;
    private static boolean isSpoofingAngles;
    private boolean switchCooldown;
    private static boolean togglePitch;
    private int placements;
    private EntityPlayer playerTarget;
    private EntityPlayer closestTarget;
    private BlockPos breakTarget;
    private BlockPos render;
    private Entity renderEnt;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener;
    //re
    public ObamaCA() {
        this.place = this.register(Settings.b("Place", true));
        this.explode = this.register(Settings.b("Explode", true));
        this.autoOffhand = this.register(Settings.b("Auto Offhand Crystal", false));
        this.chatAlert = this.register(Settings.b("Chat Alert", false));
        this.antiSuicide = this.register(Settings.b("Anti Suicide", true));
        this.antiStuck = this.register(Settings.b("Anti Stuck", true));
        this.raytrace = this.register(Settings.b("Raytrace", false));
        this.autoSwitch = this.register(Settings.b("Auto Switch", true));
        this.selfProtect = this.register(Settings.b("Self Protect", false));
        this.rainbow = this.register(Settings.b("Rainbow", false));
        Setting<Boolean> rgb = register(Settings.b("RGB", true));
        this.red = this.register((Setting<Integer>) Settings.integerBuilder("Red").withValue(255).withMaximum(255).withVisibility(b -> rgb.getValue()).build());
        this.green = this.register((Setting<Integer>) Settings.integerBuilder("Green").withValue(255).withMaximum(255).withVisibility(b -> rgb.getValue()).build());
        this.blue = this.register((Setting<Integer>) Settings.integerBuilder("Blue").withValue(255).withMaximum(255).withVisibility(b -> rgb.getValue()).build());
        this.antiWeaknessOffhand = this.register(Settings.b("Anti Weakness Offhand", false));
        this.renderBreakTarget = this.register(Settings.b("Render Break Target", true));
        this.onlyBreakOwnCrystals = this.register(Settings.b("Only Break Own Crystals", false));

        this.msBreakDelay = this.register((Setting<Integer>) Settings.integerBuilder("MS Break Delay").withMinimum(0).withMaximum(300).withValue(10).build());
        this.msPlaceDelay = this.register((Setting<Integer>) Settings.integerBuilder("MS Place Delay").withMinimum(0).withMaximum(300).withValue(10).build());
        this.placeRange = this.register((Setting<Double>) Settings.doubleBuilder("Place Range").withMinimum(0.0).withMaximum(8.0).withValue(4.5).build());
        this.breakRange = this.register((Setting<Double>) Settings.doubleBuilder("Break Range").withMinimum(0.0).withMaximum(8.0).withValue(4.5).build());
        this.breakThroughWallsRange = this.register((Setting<Double>) Settings.doubleBuilder("Through Walls Break Range").withMinimum(0.0).withMaximum(8.0).withValue(4.5).build());
        this.enemyRange = this.register((Setting<Integer>) Settings.integerBuilder("Enemy Range").withMinimum(0).withMaximum(36).withValue(10).build());
        this.minDamage = this.register((Setting<Integer>) Settings.integerBuilder("Min Damage").withMinimum(0).withMaximum(36).withValue(4).build());
        this.ignoreMinDamageThreshold = this.register((Setting<Integer>) Settings.integerBuilder("Ignore Min Damage").withMinimum(0).withMaximum(36).withValue(8).build());
        this.selfProtectThreshold = this.register((Setting<Integer>) Settings.integerBuilder("Max Self Damage").withMinimum(0).withMaximum(16).withValue(8).build());
        this.breakYOffset = this.register((Setting<Double>) Settings.doubleBuilder("Break Y Offset").withMinimum(0.0).withMaximum(0.5).withValue(0.0).build());
        this.breakSystemTime = -1L;
        final Packet[] packet = new Packet[1];
        this.packetListener = new Listener<PacketEvent.Send>(event -> {
            packet[0] = event.getPacket();
            if (packet[0] instanceof CPacketPlayer && isSpoofingAngles) {
                ((CPacketPlayer) packet[0]).yaw = (float) yaw;
                ((CPacketPlayer) packet[0]).pitch = (float) pitch;
            }
        });

    }

    @Override
    public void onEnable() {

        if (mc.world == null)
            return;

        if (this.chatAlert.getValue()) {
            Command.sendChatMessage("[CrystalAura]" + (ChatFormatting.GREEN) + " Enabled!");
        }

    }

    public void onDisable() {

        if (chatAlert.getValue()) {
            Command.sendChatMessage("[CrystalAura]" + (ChatFormatting.RED) + " Disabled!");
        }

        resetRotation();

    }

    @Override
    public void onUpdate() {
        final EntityEnderCrystal crystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> mc.player.getDistance(c))).orElse(null);
        if (crystal != null && this.explode.getValue()) {
            BlockPos breakTarget = new BlockPos(crystal.posX, crystal.posY, crystal.posZ);
            if (!canBlockBeSeen(breakTarget)) {
                if (mc.player.getDistance((Entity) crystal) <= this.breakThroughWallsRange.getValue()) {
                    if (!this.selfProtect.getValue()) {
                        if (System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                            this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer) mc.player);
                            mc.playerController.attackEntity((EntityPlayer) mc.player, (Entity) crystal);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                            this.breakSystemTime = System.nanoTime() / 1000000L;
                            HiroshimaClient.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                        }
                    } else if (calculateDamage(crystal, mc.player) <= selfProtectThreshold.getValue()) {
                        if (System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                            this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer) mc.player);
                            mc.playerController.attackEntity((EntityPlayer) mc.player, (Entity) crystal);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                            this.breakSystemTime = System.nanoTime() / 1000000L;
                            HiroshimaClient.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                        }
                    }
                }
            } else {
                if (mc.player.getDistance((Entity) crystal) <= this.breakRange.getValue()) {
                    if (this.selfProtect.getValue() && calculateDamage(crystal, mc.player) <= selfProtectThreshold.getValue()) {
                        if (System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                            this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer) mc.player);
                            mc.playerController.attackEntity((EntityPlayer) mc.player, (Entity) crystal);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                            this.breakSystemTime = System.nanoTime() / 1000000L;
                            HiroshimaClient.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                        }
                    } else if (!this.selfProtect.getValue()) {
                        if (System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                            this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer) mc.player);
                            mc.playerController.attackEntity((EntityPlayer) mc.player, (Entity) crystal);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                            this.breakSystemTime = System.nanoTime() / 1000000L;
                            HiroshimaClient.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                        }
                    }
                }
            }

        } else if (crystal == null){
            resetRotation();
        }
        int crystalSlot = (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        } else if (crystalSlot == -1) {
            return;
        }
        Entity ent = null;
        Entity lastTarget = null;

        BlockPos finalPos = null;
        final List<BlockPos> blocks = this.findCrystalBlocks();
        final List<Entity> entities = new ArrayList<Entity>();
        entities.addAll((Collection<? extends Entity>) mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        double damage = 0.5;
        for (final Entity entity2 : entities) {
            if (entity2 != mc.player) {
                if (((EntityLivingBase) entity2).getHealth() <= 0.0f) {
                    continue;

                }
                if (mc.player.getDistanceSq(entity2) > this.enemyRange.getValue() * this.enemyRange.getValue()) {
                    continue;
                }
                for (final BlockPos blockPos : blocks) {
                    if (!canBlockBeSeen(blockPos) && mc.player.getDistanceSq(blockPos) > 25.0 && this.raytrace.getValue()) {
                        continue;
                    }
                    final double b = entity2.getDistanceSq(blockPos);
                    if (b > 56.2) {
                        continue;
                    }
                    final double d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2);
                    if (d < this.minDamage.getValue() && ((EntityLivingBase) entity2).getHealth() + ((EntityLivingBase) entity2).getAbsorptionAmount() > this.ignoreMinDamageThreshold.getValue()) {
                        continue;
                    }
                    if (d <= damage) {
                        continue;
                    }
                    final double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity) mc.player);
                    if (this.antiSuicide.getValue()) {
                        if (mc.player.getHealth() + mc.player.getAbsorptionAmount() - self <= 7.0) {
                            continue;
                        }
                        if (self > d) {
                            continue;
                        }
                    }
                    damage = d;
                    finalPos = blockPos;
                    ent = entity2;
                    lastTarget = entity2;

                }
            }
        }
        if (damage == 0.5) {
            this.render = null;
            this.renderEnt = null;
            resetRotation();
            return;
        }
        this.render = finalPos;
        this.renderEnt = ent;
        if (this.place.getValue()) {
            if (!offhand && mc.player.inventory.currentItem != crystalSlot) {
                if (this.autoSwitch.getValue()) {
                    mc.player.inventory.currentItem = crystalSlot;
                    resetRotation();
                    this.switchCooldown = true;
                }
                return;
            }
            this.lookAtPacket(finalPos.x + 0.5, finalPos.y - 0.5, finalPos.z + 0.5, (EntityPlayer) mc.player);
            final RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(finalPos.x + 0.5, finalPos.y - 0.5, finalPos.z + 0.5));
            EnumFacing f;
            if (result == null || result.sideHit == null) {
                f = EnumFacing.UP;
            } else {
                f = result.sideHit;
            }
            if (this.switchCooldown) {
                this.switchCooldown = false;
                return;
            }
            if (System.nanoTime() / 1000000L - this.placeSystemTime >= this.msPlaceDelay.getValue()) {
                mc.player.connection.sendPacket((Packet) new CPacketPlayerTryUseItemOnBlock(finalPos, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                ++this.placements;
                this.antiStuckSystemTime = System.nanoTime() / 1000000L;
                this.placeSystemTime = System.nanoTime() / 1000000L;
                HiroshimaClient.log.info("Crystal Placed!");
            }
        }
        if (isSpoofingAngles) {
            if (togglePitch) {
                final EntityPlayerSP player = mc.player;
                player.rotationPitch += (float) 4.0E-4;
                togglePitch = false;
            } else {
                final EntityPlayerSP player2 = mc.player;
                player2.rotationPitch -= (float) 4.0E-4;
                togglePitch = true;
            }
        }

    }

    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null) {
            final float[] hue = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32)};
            int rgb = Color.HSBtoRGB(hue[0], 1, 1);
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;
            if (rainbow.getValue()) {
                HiroshimaTessellator.prepare(7);
                HiroshimaTessellator.drawBox(this.render, r, g, b, 77, 63);
                HiroshimaTessellator.release();
                HiroshimaTessellator.prepare(7);
                HiroshimaTessellator.drawBoundingBoxBlockPos(this.render, 1.00f,  r, g, b, 255);
            } else {
                HiroshimaTessellator.prepare(7);
                HiroshimaTessellator.drawBox(this.render, this.red.getValue(), this.green.getValue(), this.blue.getValue(), 77, 63);
                HiroshimaTessellator.release();
                HiroshimaTessellator.prepare(7);
                HiroshimaTessellator.drawBoundingBoxBlockPos(this.render, 1.00f, this.red.getValue(), this.green.getValue(), this.blue.getValue(), 244);
            }
            HiroshimaTessellator.release();
        }

    }

    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }

    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && mc.world.getBlockState(boost).getBlock() == Blocks.AIR && mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    private List<BlockPos> findCrystalBlocks() {
        NonNullList positions = NonNullList.create();
        positions.addAll(this.getSphere(getPlayerPos(), this.placeRange.getValue().floatValue(), this.placeRange.getValue().intValue(), false, true, 0).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
        return (List<BlockPos>)positions;
    }

    public List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 6.0F * 2.0F;
        double distancedsize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = (double) entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        double v = (1.0D - distancedsize) * blockDensity;
        float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * (double) doubleExplosionSize + 1.0D));
        double finald = 1;
        /*if (entity instanceof EntityLivingBase)
            finald = getBlastReduction((EntityLivingBase) entity,getDamageMultiplied(damage));*/
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6F, false, true));
        }
        return (float) finald;
    }

    public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

            int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            float f = MathHelper.clamp(k, 0.0F, 20.0F);
            damage = damage * (1.0F - f / 25.0F);

            if (entity.isPotionActive(Potion.getPotionById(11))) {
                damage = damage - (damage / 4);
            }

            damage = Math.max(damage, 0.0F);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private static float getDamageMultiplied(float damage) {
        int diff = mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0 : (diff == 2 ? 1 : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static float calculateDamage(EntityEnderCrystal crystal, Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }

    public static boolean canBlockBeSeen(final BlockPos blockPos) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ()), false, true, false) == null;
    }

    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
            isSpoofingAngles = false;
        }
    }

    public boolean isObbyOrBedrock(BlockPos blockPos) {
        if (mc.world.getBlockState(blockPos) == Blocks.OBSIDIAN || mc.world.getBlockState(blockPos) == Blocks.BEDROCK) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getObbyAndBedrockInRange () {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(
                getSphere(getPlayerPos(), placeRange.getValue().floatValue() + 2, placeRange.getValue().intValue() + 2, false, true, 0)
                        .stream().filter(this::isObbyOrBedrock).collect(Collectors.toList())
        );
        if (!positions.isEmpty()) {
            HiroshimaClient.log.info("Obisidan or Bedrock Found!");
            return true;
        } else {
            return false;
        }
    }

    public void breakCrystal(EntityEnderCrystal crystal) {
        if (crystal != null && this.explode.getValue()) {
            BlockPos breakTarget = new BlockPos(crystal.posX, crystal.posY + 1, crystal.posZ);
            if (!canBlockBeSeen(breakTarget)) {
                if (mc.player.getDistance((Entity) crystal) <= this.breakThroughWallsRange.getValue()) {
                    if (!this.selfProtect.getValue()) {
                        if (System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                            this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer) mc.player);
                            mc.playerController.attackEntity((EntityPlayer) mc.player, (Entity) crystal);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                            this.breakSystemTime = System.nanoTime() / 1000000L;
                            HiroshimaClient.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                        }
                    } else if (calculateDamage(crystal, mc.player) <= selfProtectThreshold.getValue()) {
                        if (System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                            this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer) mc.player);
                            mc.playerController.attackEntity((EntityPlayer) mc.player, (Entity) crystal);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                            this.breakSystemTime = System.nanoTime() / 1000000L;
                            HiroshimaClient.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                        }
                    }
                }
            } else {
                if (mc.player.getDistance((Entity) crystal) <= this.breakRange.getValue()) {
                    if (this.selfProtect.getValue() && calculateDamage(crystal, mc.player) <= selfProtectThreshold.getValue()) {
                        if (System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                            this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer) mc.player);
                            mc.playerController.attackEntity((EntityPlayer) mc.player, (Entity) crystal);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                            this.breakSystemTime = System.nanoTime() / 1000000L;
                            HiroshimaClient.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                        }
                    } else if (!this.selfProtect.getValue()) {
                        if (System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                            this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer) mc.player);
                            mc.playerController.attackEntity((EntityPlayer) mc.player, (Entity) crystal);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                            this.breakSystemTime = System.nanoTime() / 1000000L;
                            HiroshimaClient.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                        }
                    }
                }
            }

        } else if (crystal == null){
            resetRotation();
        }
    }

    public void placeCrystal(BlockPos blockPos) {
        int crystalSlot = (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        } else if (crystalSlot == -1) {
            return;
        }
        if (this.place.getValue()) {
            if (!offhand && mc.player.inventory.currentItem != crystalSlot) {
                if (this.autoSwitch.getValue()) {
                    mc.player.inventory.currentItem = crystalSlot;
                    resetRotation();
                    this.switchCooldown = true;
                }
                return;
            }
            this.lookAtPacket(blockPos.x + 0.5, blockPos.y - 0.5, blockPos.z + 0.5, (EntityPlayer) mc.player);
            final RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(blockPos.x + 0.5, blockPos.y - 0.5, blockPos.z + 0.5));
            EnumFacing f;
            if (result == null || result.sideHit == null) {
                f = EnumFacing.UP;
            } else {
                f = result.sideHit;
            }
            if (this.switchCooldown) {
                this.switchCooldown = false;
                return;
            }
            if (System.nanoTime() / 1000000L - this.placeSystemTime >= this.msPlaceDelay.getValue()) {
                mc.player.connection.sendPacket((Packet) new CPacketPlayerTryUseItemOnBlock(blockPos, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                ++this.placements;
                this.antiStuckSystemTime = System.nanoTime() / 1000000L;
                this.placeSystemTime = System.nanoTime() / 1000000L;
                HiroshimaClient.log.info("Crystal Placed!");
            }
        }
        if (isSpoofingAngles) {
            if (togglePitch) {
                final EntityPlayerSP player = mc.player;
                player.rotationPitch += (float) 4.0E-4;
                togglePitch = false;
            } else {
                final EntityPlayerSP player2 = mc.player;
                player2.rotationPitch -= (float) 4.0E-4;
                togglePitch = true;
            }
        }
    }

    public BlockPos getPlaceTarget(List<Entity> entities, List<BlockPos> blocks) {
        Entity ent = null;
        BlockPos finalPos = null;
        double damage = 0.5;
        for (final Entity entity2 : entities) {
            if (entity2 != mc.player) {
                if (((EntityLivingBase) entity2).getHealth() <= 0.0f) {
                    HiroshimaClient.log.info("Target Alive!");
                    continue;
                }
                if (mc.player.getDistanceSq(entity2) > this.enemyRange.getValue() * this.enemyRange.getValue()) {
                    HiroshimaClient.log.info("Target In Range!");
                    continue;
                }
                for (final BlockPos blockPos : blocks) {
                    if (!canBlockBeSeen(blockPos) && mc.player.getDistanceSq(blockPos) > 25.0 && this.raytrace.getValue()) {
                        HiroshimaClient.log.info("Raytrace Successful!");
                        continue;
                    }
                    final double b = entity2.getDistanceSq(blockPos);
                    if (b > 56.2) {
                        HiroshimaClient.log.info("Step 4 Complete!");
                        continue;
                    }
                    final double d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2);
                    if (d < this.minDamage.getValue() && ((EntityLivingBase) entity2).getHealth() > this.ignoreMinDamageThreshold.getValue()) {
                        HiroshimaClient.log.info("Crystal Will Deal More Than Min Damage!");
                        continue;
                    }
                    if (d <= damage) {
                        HiroshimaClient.log.info("Crystal Will Deal More Than 0.5 Damage!");
                        continue;
                    }
                    final double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity) mc.player);
                    if (this.antiSuicide.getValue()) {
                        if (mc.player.getHealth() - self <= 7.0) {
                            HiroshimaClient.log.info("Crystal Will Not Kill The Player!");
                            continue;
                        }
                        if (self > d) {
                            HiroshimaClient.log.info("Self Damage Exceeds 0.5!");
                            continue;
                        }
                    }
                    damage = d;
                    finalPos = blockPos;
                    ent = entity2;
                }
            }
        }
        if (finalPos == null) {
            HiroshimaClient.log.info("Place Target null! Will not place");
            return finalPos;
        }
        HiroshimaClient.log.info("Place Target Gotten!");
        return finalPos;
    }

    private void findClosestTarget() {

        List<EntityPlayer> playerList = mc.world.playerEntities;

        closestTarget = null;

        for (EntityPlayer target : playerList) {

            if (target == mc.player) {
                continue;
            }

            if (Friends.isFriend(target.getName())) {
                continue;
            }

            if (!EntityUtil.isLiving(target)) {
                continue;
            }

            if ((target).getHealth() <= 0) {
                continue;
            }

            if (closestTarget == null) {
                closestTarget = target;
                continue;
            }

            if (mc.player.getDistance(target) < mc.player.getDistance(closestTarget)) {
                closestTarget = target;
            }

        }

    }

    private enum delayMode {
        TICKS, MILLISECONDS
    }
}