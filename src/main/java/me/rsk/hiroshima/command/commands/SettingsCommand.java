package me.rsk.hiroshima.command.commands;

import me.rsk.hiroshima.command.Command;
import me.rsk.hiroshima.command.syntax.ChunkBuilder;
import me.rsk.hiroshima.command.syntax.parsers.ModuleParser;
import me.rsk.hiroshima.module.Module;
import me.rsk.hiroshima.module.ModuleManager;
import me.rsk.hiroshima.setting.Setting;
import me.rsk.hiroshima.setting.impl.EnumSetting;

import java.util.List;

/**
 * Created by 086 on 11/12/2017.
 */
public class SettingsCommand extends Command {
    public SettingsCommand() {
        super("settings", new ChunkBuilder()
            .append("module", true, new ModuleParser())
            .build());
    }

    @Override
    public void call(String[] args) {
        if (args[0]==null) {
            Command.sendChatMessage("Please specify a module to display the settings of.");
            return;
        }

        Module m = ModuleManager.getModuleByName(args[0]);
        if (m == null) {
            Command.sendChatMessage("Couldn't find a module &b" + args[0] + "!");
            return;
        }

        List<Setting> settings = m.settingList;
        String[] result = new String[settings.size()];
        for (int i = 0; i < settings.size(); i++) {
            Setting setting = settings.get(i);
            result[i] = "&b" + setting.getName() + "&3(=" + setting.getValue() + ")  &ftype: &3" + setting.getValue().getClass().getSimpleName();

            if (setting instanceof EnumSetting){
                result[i] += "  (";
                Enum[] enums = (Enum[]) ((EnumSetting) setting).clazz.getEnumConstants();
                for (Enum e : enums)
                    result[i] += e.name() + ", ";
                result[i] = result[i].substring(0, result[i].length()-2) + ")";
            }
        }
        Command.sendStringChatMessage(result);
    }
}
