package me.rsk.hiroshima.module.modules.hidden;

import me.rsk.hiroshima.command.Command;
import me.rsk.hiroshima.module.Module;
import me.rsk.hiroshima.setting.Setting;
import me.rsk.hiroshima.setting.Settings;
import me.rsk.hiroshima.util.Friends;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;

/**
 * Created by 086 on 8/04/2018.
 */
@Module.Info(name = "ExtraTab", description = "Expands the player tab menu", category = Module.Category.HIDDEN)
public class ExtraTab extends Module {

    public Setting<Integer> tabSize = register(Settings.integerBuilder("Players").withMinimum(1).withValue(80).build());

    public static ExtraTab INSTANCE;

    public ExtraTab() {
        ExtraTab.INSTANCE = this;
    }

    public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
        String dname = networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        if (Friends.isFriend(dname)) return String.format("%sa%s", Command.SECTIONSIGN(), dname);
        return dname;
    }
}
