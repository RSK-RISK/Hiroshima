package me.rsk.hiroshima.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.rsk.hiroshima.command.Command;
import me.rsk.hiroshima.event.events.PacketEvent;
import me.rsk.hiroshima.event.events.RenderEvent;
import me.rsk.hiroshima.module.Module;
import me.rsk.hiroshima.module.ModuleManager;
import me.rsk.hiroshima.setting.Setting;
import me.rsk.hiroshima.setting.Settings;
import me.rsk.hiroshima.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static me.rsk.hiroshima.util.EntityUtil.calculateLookAt;

@Module.Info(name = "HoleFiller", category = Module.Category.COMBAT)
public class HoleFiller extends Module {

    private static BlockPos PlayerPos;
    private Setting<Double> range = register(Settings.d("Range", 4.5));
    private Setting<Boolean> smart =  register(Settings.b("Smart", false));
    private Setting<Integer> smartRange = register(Settings.i("Smart Range", 4));
    private Setting<Boolean> announceUsage = register(Settings.b("Announce Usage", false));
    private BlockPos render;
    private Entity renderEnt;
    private EntityPlayer closestTarget;
    private long systemTime = -1;
    private static boolean togglePitch = false;
    // we need this cooldown to not place from old hotbar slot, before we have
    // switched to crystals
    private boolean switchCooldown = false;
    private boolean isAttacking = false;
    private boolean caOn;
    private int newSlot;
    double d;

    @Override
    public void onEnable() {

        if (ModuleManager.getModuleByName("HiroshimaCA").isEnabled())
            caOn = true;

        if (announceUsage.getValue()) {
            Command.sendChatMessage("[HoleFiller] " + ChatFormatting.GREEN.toString() + "Enabled" + ChatFormatting.RESET.toString() + "!");
        }
    }

    @Override
    public void onUpdate() {
        if (mc.world == null) {
            return;
        }

        if (smart.getValue())
            findClosestTarget();
        List<BlockPos> blocks = findCrystalBlocks();
        BlockPos q = null;
        double dist = 0;
        double prevDist = 0;
        int obsidianSlot = mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN)
                ? mc.player.inventory.currentItem
                : -1;
        if (obsidianSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN)) {
                    obsidianSlot = l;
                    break;
                }
            }
        }
        if (obsidianSlot == -1) {
            return;
        }
        for (BlockPos blockPos : blocks) {
            if (mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos)).isEmpty())
                if (smart.getValue() && isInRange(blockPos))
                    q = blockPos;
                else
                    q = blockPos;
        }
        render = q;
        if (q != null && mc.player.onGround) {
            if (caOn)
                ModuleManager.getModuleByName("HiroshimaCA").disable();
            int oldSlot = mc.player.inventory.currentItem;
            if (mc.player.inventory.currentItem != obsidianSlot)
                mc.player.inventory.currentItem = obsidianSlot;
            lookAtPacket(q.x + .5, q.y - .5, q.z + .5, mc.player);
            BlockInteractionHelper.placeBlockScaffold(render);
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.inventory.currentItem = oldSlot;
            resetRotation();
            if (caOn)
                ModuleManager.getModuleByName("HiroshimaCA").enable();
        }
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        if (render != null) {
            HiroshimaTessellator.prepare(GL11.GL_QUADS);
            HiroshimaTessellator.drawBox(render, 0x30FF0000, GeometryMasks.Quad.ALL);
            HiroshimaTessellator.release();
            HiroshimaTessellator.prepare(GL11.GL_QUADS);
            HiroshimaTessellator.drawBoundingBoxBlockPos(render, 1f, 0, 255, 0, 170);
            HiroshimaTessellator.release();
        }
    }

    private double getDistanceToBlockPos(BlockPos pos1, BlockPos pos2) {
        double x = pos1.getX() - pos2.getX();
        double y = pos1.getY() - pos2.getY();
        double z = pos1.getZ() - pos2.getZ();

        return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = calculateLookAt(px, py, pz, me);
        setYawAndPitch((float) v[0], (float) v[1]);
    }

    private boolean IsHole(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 0, 0);
        BlockPos boost3 = blockPos.add(0, 0, -1);
        BlockPos boost4 = blockPos.add(1, 0, 0);
        BlockPos boost5 = blockPos.add(-1, 0, 0);
        BlockPos boost6 = blockPos.add(0, 0, 1);
        BlockPos boost7 = blockPos.add(0, 2, 0);
        BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
        BlockPos boost9 = blockPos.add(0, -1, 0);
        if ((mc.world.getBlockState(boost).getBlock() == Blocks.AIR
                && (mc.world.getBlockState(boost2).getBlock() == Blocks.AIR)
                && (mc.world.getBlockState(boost7).getBlock() == Blocks.AIR)
                && ((mc.world.getBlockState(boost3).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(boost3).getBlock() == Blocks.BEDROCK))
                && ((mc.world.getBlockState(boost4).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(boost4).getBlock() == Blocks.BEDROCK))
                && ((mc.world.getBlockState(boost5).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(boost5).getBlock() == Blocks.BEDROCK))
                && ((mc.world.getBlockState(boost6).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(boost6).getBlock() == Blocks.BEDROCK))
                && (mc.world.getBlockState(boost8).getBlock() == Blocks.AIR)
                && ((mc.world.getBlockState(boost9).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(boost9).getBlock() == Blocks.BEDROCK)))) {
            return true;
        } else {
            return false;
        }
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public BlockPos getClosestTargetPos() {
        if (closestTarget != null) {
            return new BlockPos(Math.floor(closestTarget.posX), Math.floor(closestTarget.posY), Math.floor(closestTarget.posZ));
        } else {
            return null;
        }
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

    private boolean isInRange(BlockPos blockPos) {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(
                getSphere(getPlayerPos(), range.getValue().floatValue(), range.getValue().intValue(), false, true, 0)
                        .stream().filter(this::IsHole).collect(Collectors.toList()));
        if (positions.contains(blockPos))
            return true;
        else
            return false;
    }

    private List<BlockPos> findCrystalBlocks() {
        NonNullList<BlockPos> positions = NonNullList.create();
        if (smart.getValue() && closestTarget != null)
            positions.addAll(
                    getSphere(getClosestTargetPos(), smartRange.getValue().floatValue(), range.getValue().intValue(), false, true, 0)
                            .stream().filter(this::IsHole).filter(this::isInRange).collect(Collectors.toList()));
        else if(!smart.getValue())
            positions.addAll(
                    getSphere(getPlayerPos(), range.getValue().floatValue(), range.getValue().intValue(), false, true, 0)
                            .stream().filter(this::IsHole).collect(Collectors.toList()));
        return positions;
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    // Better Rotation Spoofing System:

    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;

    // this modifies packets being sent so no extra ones are made. NCP used to flag
    // with "too many packets"
    private static void setYawAndPitch(float yaw1, float pitch1) {
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

    @EventHandler
    private Listener<PacketEvent.Send> packetListener = new Listener<>(event -> {
        Packet packet = event.getPacket();
        if (packet instanceof CPacketPlayer) {
            if (isSpoofingAngles) {
                ((CPacketPlayer) packet).yaw = (float) yaw;
                ((CPacketPlayer) packet).pitch = (float) pitch;
            }
        }
    });

    @Override
    public void onDisable() {
        closestTarget = null;
        render = null;
        resetRotation();

        if (announceUsage.getValue()) {
            Command.sendChatMessage("[HoleFiller] " + ChatFormatting.RED.toString() + "Disabled" + ChatFormatting.RESET.toString() + "!");
        }
    }
}