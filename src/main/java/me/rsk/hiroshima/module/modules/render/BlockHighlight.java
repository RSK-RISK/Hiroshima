package me.rsk.hiroshima.module.modules.render;

import me.rsk.hiroshima.module.Module;
import me.rsk.hiroshima.setting.*;
import net.minecraftforge.common.*;
import me.rsk.hiroshima.event.events.*;
import net.minecraft.client.*;
import net.minecraft.block.material.*;
import net.minecraft.entity.*;
import me.rsk.hiroshima.util.*;
import net.minecraft.block.state.*;
import net.minecraft.util.math.*;
import net.minecraftforge.client.event.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.eventhandler.*;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

    // Created by RSK
    // 16/05/2020


@Module.Info(name = "BlockHighlight", description = "RSK Block Highlight", category = Module.Category.RENDER)
public class BlockHighlight extends Module {
    private Setting<Boolean> boundingbox = register(Settings.b("Bouding Box", true));
    private Setting<Boolean> box = register(Settings.b("Full Block Highlight", true));
    private Setting<Double> width = register(Settings.d("Width", 1.0));
    private Setting<Integer> Red = register(
            Settings.integerBuilder("BlockRed").withMinimum(1).withMaximum(255).withValue(0));
    private Setting<Integer> Green = register(
            Settings.integerBuilder("BlockGreen").withMinimum(1).withMaximum(255).withValue(170));
    private Setting<Integer> Blue = register(
            Settings.integerBuilder("BlockBlue").withMinimum(1).withMaximum(255).withValue(170));
    private Setting<Integer> alpha = register(
            Settings.integerBuilder("Block Alpha").withMinimum(1).withMaximum(255).withValue(0));
    private Setting<Integer> Red2 = register(
            Settings.integerBuilder("OutlineRed").withMinimum(1).withMaximum(255).withValue(0));
    private Setting<Integer> Green2 = register(
            Settings.integerBuilder("OutlineGreen").withMinimum(1).withMaximum(255).withValue(170));
    private Setting<Integer> Blue2 = register(
            Settings.integerBuilder("OutlineBlue").withMinimum(1).withMaximum(255).withValue(170));
    private Setting<Integer> alpha2 = register(
            Settings.integerBuilder("Outline Alpha").withMinimum(1).withMaximum(255).withValue(255));
    private Setting<Boolean> rainbow = register(Settings.b("Rainbow", false));

    public void onWorldRender(RenderEvent event) {
        final float[] hue = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32)};
        int rgb = Color.HSBtoRGB(hue[0], 1, 1);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        final Minecraft mc = Minecraft.getMinecraft();
        final RayTraceResult ray = mc.objectMouseOver;
        if (ray.typeOfHit == RayTraceResult.Type.BLOCK) {

            final BlockPos blockpos = ray.getBlockPos();
            final IBlockState iblockstate = mc.world.getBlockState(blockpos);

            if (iblockstate.getMaterial() != Material.AIR && mc.world.getWorldBorder().contains(blockpos)) {
                if (box.getValue()) {
                    HiroshimaTessellator.prepare(GL11.GL_QUADS);
                    if (rainbow.getValue()) {
                        HiroshimaTessellator.drawBox(blockpos, r, g, b, alpha.getValue(), GeometryMasks.Quad.ALL);
                    } else {
                        HiroshimaTessellator.drawBox(blockpos, this.Red.getValue(), this.Green.getValue(), this.Blue.getValue(), alpha.getValue(), GeometryMasks.Quad.ALL);
                    }
                    HiroshimaTessellator.release();
                }
                if (boundingbox.getValue()) {
                    HiroshimaTessellator.prepare(GL11.GL_QUADS);
                    if (rainbow.getValue()) {
                        HiroshimaTessellator.drawBoundingBoxBlockPos(blockpos, width.getValue().floatValue(), r, g, b, alpha2.getValue());
                    } else {
                        HiroshimaTessellator.drawBoundingBoxBlockPos(blockpos, width.getValue().floatValue(), this.Red2.getValue(), this.Green2.getValue(), this.Blue2.getValue(), alpha2.getValue());
                    }
                    HiroshimaTessellator.release();
                }
            }
        }
    }
}