package me.rsk.hiroshima.gasda.mixin.client;

import me.rsk.hiroshima.gasda.command.Command;
import me.rsk.hiroshima.jews.gui.mc.HiroshimaGuiChat;
import me.rsk.hiroshima.gasda.util.Wrapper;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by 086 on 11/11/2017.
 */
@Mixin(GuiChat.class)
public abstract class MixinGuiChat {

    @Shadow protected GuiTextField inputField;

    @Shadow public String historyBuffer;

    @Shadow public int sentHistoryCursor;

    @Shadow public abstract void initGui();

    @Inject(method = "Lnet/minecraft/client/gui/GuiChat;keyTyped(CI)V", at = @At("RETURN"))
    public void returnKeyTyped(char typedChar, int keyCode, CallbackInfo info) {
        if (!(Wrapper.getMinecraft().currentScreen instanceof GuiChat) || Wrapper.getMinecraft().currentScreen instanceof HiroshimaGuiChat) return;
        if (inputField.getText().startsWith(Command.getCommandPrefix())) {
            Wrapper.getMinecraft().displayGuiScreen(new HiroshimaGuiChat(inputField.getText(), historyBuffer, sentHistoryCursor));
        }
    }

}
