package me.rsk.hiroshima.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rsk.hiroshima.command.Command;
import me.rsk.hiroshima.module.Module;
import me.rsk.hiroshima.module.ModuleManager;
import me.rsk.hiroshima.setting.Setting;
import me.rsk.hiroshima.setting.Settings;
import me.rsk.hiroshima.util.BlockInteractionHelper;
import me.rsk.hiroshima.util.EntityUtil;
import me.rsk.hiroshima.util.Friends;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static me.rsk.hiroshima.util.BlockInteractionHelper.canBeClicked;
import static me.rsk.hiroshima.util.BlockInteractionHelper.faceVectorPacketInstant;

@Module.Info(name = "SelfTrap", category = Module.Category.COMBAT)
public class SelfTrap extends Module {

    private Setting<Double> smartRange = register(Settings.doubleBuilder("Range").withMinimum(0.0).withValue(4.5).withMaximum(32.0).build());
    private Setting<Integer> blocksPerTick = register(Settings.integerBuilder("BlocksPerTick").withMinimum(1).withValue(2).withMaximum(23).build());
    private Setting<Integer> tickDelay = register(Settings.integerBuilder("TickDelay").withMinimum(0).withValue(2).withMaximum(10).build());
    private Setting<SelfTrap.Cage> cage = register(Settings.e("Cage", SelfTrap.Cage.BLOCKOVERHEAD));
    private Setting<Boolean> rotate = register(Settings.b("Rotate", true));
    private Setting<Boolean> announceUsage = register(Settings.b("AnnounceUsage", true));
    private Setting<Boolean> smart = register(Settings.b("Smart", false));
    private Setting<Boolean> disableOnPlace = register(Settings.b("Disable On Place", false));
    private Setting<Boolean> disableCAOnPlace = register(Settings.b("Disable CA On Place", false));
    private int playerHotbarSlot = -1;
    private int lastHotbarSlot = -1;
    private int delayStep = 0;
    private boolean isSneaking = false;
    private int offsetStep = 0;
    private boolean firstRun;
    private boolean caOn;
    private boolean isSpoofingAngles;
    private static double yaw;
    private EntityPlayer closestTarget;

    @Override
    protected void onEnable() {

        if (mc.player == null) {
            this.disable();
            return;
        }

        firstRun = true;

        // save initial player hand
        playerHotbarSlot = mc.player.inventory.currentItem;
        lastHotbarSlot = -1;

        if (announceUsage.getValue()) {
            Command.sendChatMessage("[SelfTrap] " + ChatFormatting.GREEN.toString() + "Enabled!");
        }

    }

    @Override
    protected void onDisable() {

        if (mc.player == null) {
            return;
        }

        // load initial player hand
        if (lastHotbarSlot != playerHotbarSlot && playerHotbarSlot != -1) {
            mc.player.inventory.currentItem = playerHotbarSlot;
        }

        if (isSneaking) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            isSneaking = false;
        }

        playerHotbarSlot = -1;
        lastHotbarSlot = -1;

        if (announceUsage.getValue()) {
            Command.sendChatMessage("[SelfTrap] " + ChatFormatting.RED.toString() + "Disabled!");
        }

    }

    @Override
    public void onUpdate() {

        if (smart.getValue())
            findClosestTarget();

        if (mc.player == null) {
            return;
        }

        {
        }

        if (!firstRun) {
            if (delayStep < tickDelay.getValue()) {
                delayStep++;
                return;
            } else {
                delayStep = 0;
            }
        }

        List<Vec3d> placeTargets = new ArrayList<>();

        if (cage.getValue().equals(SelfTrap.Cage.TRAP)) {
            Collections.addAll(placeTargets, SelfTrap.Offsets.TRAP);
        }

        if (cage.getValue().equals(SelfTrap.Cage.BLOCKOVERHEAD)) {
            if (getViewYaw() <= 315 && getViewYaw() >= 225)
                Collections.addAll(placeTargets, SelfTrap.Offsets.BLOCKOVERHEADFACINGNEGX);
            else if (getViewYaw() < 45 && getViewYaw() > 0 || getViewYaw() > 315 && getViewYaw() < 360)
                Collections.addAll(placeTargets, SelfTrap.Offsets.BLOCKOVERHEADFACINGPOSZ);
            else if (getViewYaw() <= 135 && getViewYaw() >= 45)
                Collections.addAll(placeTargets, SelfTrap.Offsets.BLOCKOVERHEADFACINGPOSX);
            else if (getViewYaw() < 225 && getViewYaw() > 135)
                Collections.addAll(placeTargets, SelfTrap.Offsets.BLOCKOVERHEADFACINGNEGZ);
        }

        // TODO: dont use static bridging in offset but calculate them on the fly
        //  based on view direction (or relative direction of target to player)
        //  (add full/half n/e/s/w patterns to append dynamically)

        // TODO: sort offsetList by optimal caging success factor ->
        // sort them by pos y up AND start building behind target

        int blocksPlaced = 0;

        while (blocksPlaced < blocksPerTick.getValue()) {

            if (offsetStep >= placeTargets.size()) {
                offsetStep = 0;
                break;
            }

            BlockPos offsetPos = new BlockPos(placeTargets.get(offsetStep));
            BlockPos targetPos = new BlockPos(mc.player.getPositionVector()).down().add(offsetPos.x, offsetPos.y, offsetPos.z);

            if (closestTarget != null && smart.getValue()) {
                if (isInRange(getClosestTargetPos())) {
                    if (placeBlockInRange(targetPos)) {
                        blocksPlaced++;
                    }
                }
            } else if (!smart.getValue()) {
                if (placeBlockInRange(targetPos)) {
                    blocksPlaced++;
                }
            }


            offsetStep++;

        }

        if (blocksPlaced > 0) {

            if (lastHotbarSlot != playerHotbarSlot && playerHotbarSlot != -1) {
                mc.player.inventory.currentItem = playerHotbarSlot;
                lastHotbarSlot = playerHotbarSlot;
            }

            if (isSneaking) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                isSneaking = false;
            }

        }

        Vec3d overHead = new Vec3d(0, 3, 0);
        BlockPos blockOverHead = new BlockPos(mc.player.getPositionVector()).down().add(overHead.x, overHead.y, overHead.z);
        Block block2 = mc.world.getBlockState(blockOverHead).getBlock();

        if (!(block2 instanceof BlockAir) && !(block2 instanceof BlockLiquid) && disableCAOnPlace.getValue() && caOn) {
            ModuleManager.getModuleByName("HiroshimaCA").enable();
        }

        if (!(block2 instanceof BlockAir) && !(block2 instanceof BlockLiquid) && disableOnPlace.getValue()) {
            this.disable();
        }

    }

    private boolean placeBlockInRange(BlockPos pos) {

        // check if crystalaura is on, if so disable it
        if (caOn && disableCAOnPlace.getValue()) {
            ModuleManager.getModuleByName("HiroshimaCA").disable();
        }

        // check if block is already placed
        Block block = mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }

        // check if entity blocks placing
        for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                return false;
            }
        }

        EnumFacing side = BlockInteractionHelper.getPlaceableSide(pos);

        // check if we have a block adjacent to blockpos to click at
        if (side == null) {
            return false;
        }

        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();

        // check if neighbor can be right clicked
        if (!canBeClicked(neighbour)) {
            return false;
        }

        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();

        int obiSlot = findObiInHotbar();

        if (obiSlot == -1) {
            this.disable();
        }

        if (lastHotbarSlot != obiSlot) {
            mc.player.inventory.currentItem = obiSlot;
            lastHotbarSlot = obiSlot;
        }

        if (!isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }

        if (rotate.getValue()) {
            faceVectorPacketInstant(hitVec);
        }

        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.rightClickDelayTimer = 0;

        return true;

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

    public int getViewYaw() {
        return (int)Math.abs(Math.floor(Minecraft.getMinecraft().player.rotationYaw * 8.0F / 360.0F));
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
                getSphere(getPlayerPos(), smartRange.getValue().floatValue(), smartRange.getValue().intValue(), false, true, 0)
                        .stream().collect(Collectors.toList()));
        if (positions.contains(blockPos))
            return true;
        else
            return false;
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

    private int findObiInHotbar() {

        // search blocks in hotbar
        int slot = -1;
        for (int i = 0; i < 9; i++) {

            // filter out non-block items
            ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) {
                continue;
            }

            Block block = ((ItemBlock) stack.getItem()).getBlock();
            if (block instanceof BlockObsidian) {
                slot = i;
                break;
            }

        }

        return slot;

    }

    private enum Cage {
        TRAP, BLOCKOVERHEAD
    }

    private static class Offsets {

        private static final Vec3d[] TRAP = {
                new Vec3d(0, 0, -1),
                new Vec3d(1, 0, 0),
                new Vec3d(0, 0, 1),
                new Vec3d(-1, 0, 0),
                new Vec3d(0, 1, -1),
                new Vec3d(1, 1, 0),
                new Vec3d(0, 1, 1),
                new Vec3d(-1, 1, 0),
                new Vec3d(0, 2, -1),
                new Vec3d(1, 2, 0),
                new Vec3d(0, 2, 1),
                new Vec3d(-1, 2, 0),
                new Vec3d(0, 3, -1),
                new Vec3d(0, 3, 0)
        };

        private static final Vec3d[] BLOCKOVERHEADFACINGPOSX = {
                new Vec3d(1, 0, 0),
                new Vec3d(1, 1, 0),
                new Vec3d(1, 2, 0),
                new Vec3d(1, 3, 0),
                new Vec3d(0, 3, 0)
        };

        private static final Vec3d[] BLOCKOVERHEADFACINGPOSZ = {
                new Vec3d(0, 0, 1),
                new Vec3d(0, 1, 1),
                new Vec3d(0, 2, 1),
                new Vec3d(0, 3, 1),
                new Vec3d(0, 3, 0)
        };

        private static final Vec3d[] BLOCKOVERHEADFACINGNEGX = {
                new Vec3d(-1, 0, 0),
                new Vec3d(-1, 1, 0),
                new Vec3d(-1, 2, 0),
                new Vec3d(-1, 3, 0),
                new Vec3d(0, 3, 0)
        };

        private static final Vec3d[] BLOCKOVERHEADFACINGNEGZ = {
                new Vec3d(0, 0, -1),
                new Vec3d(0, 1, -1),
                new Vec3d(0, 2, -1),
                new Vec3d(0, 3, -1),
                new Vec3d(0, 3, 0)
        };

    }

}