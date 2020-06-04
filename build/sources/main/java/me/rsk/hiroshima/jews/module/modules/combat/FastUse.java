
package me.rsk.hiroshima.jews.module.modules.combat;

import me.rsk.hiroshima.jews.module.Module;
import me.rsk.hiroshima.gasda.setting.Setting;
import me.rsk.hiroshima.gasda.setting.Settings;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;
//d
/**
 * Krts
 */
@Module.Info(name="FastUse", category=Module.Category.COMBAT)
public class FastUse extends Module {

    private Setting<Boolean> blocks = this.register(Settings.b("Blocks", false));
    private Setting<Boolean> exp = this.register(Settings.b("Exp Bottles", true));
    private Setting<Boolean> crystal = this.register(Settings.b("Crystals", true));
    private Setting<Boolean> bow = this.register(Settings.b("BowFagSpam", true));
    private Setting<Boolean> other = this.register(Settings.b("Other", false));

    @Override
    public void onUpdate() {
        if (FastUse.mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle) {
            if (this.exp.getValue().booleanValue()) {
                FastUse.mc.rightClickDelayTimer = 0;
            }
        } else if (FastUse.mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal) {
            if (this.crystal.getValue().booleanValue()) {
                FastUse.mc.rightClickDelayTimer = 0;
            }
        } else if (Block.getBlockFromItem((Item) FastUse.mc.player.getHeldItemMainhand().getItem()).getDefaultState().isFullBlock()) {
            if (this.blocks.getValue().booleanValue()) {
                FastUse.mc.rightClickDelayTimer = 0;
            }

        } else if (FastUse.mc.player.getHeldItemMainhand().getItem() instanceof ItemBow) {
            if (this.bow.getValue().booleanValue()) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= 3) {
                    mc.player.connection.sendPacket((Packet) new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                    mc.player.connection.sendPacket((Packet) new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
                    mc.player.stopActiveHand();
                }


            } else if (this.other.getValue().booleanValue() && !(FastUse.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
                FastUse.mc.rightClickDelayTimer = 0;
            }
        }
    }
}