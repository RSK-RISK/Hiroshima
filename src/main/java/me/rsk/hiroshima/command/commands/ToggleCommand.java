package me.rsk.hiroshima.command.commands;

import me.rsk.hiroshima.command.Command;
import me.rsk.hiroshima.command.syntax.ChunkBuilder;
import me.rsk.hiroshima.command.syntax.parsers.ModuleParser;
import me.rsk.hiroshima.module.Module;
import me.rsk.hiroshima.module.ModuleManager;

/**
 * Created by 086 on 17/11/2017.
 */
public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("toggle", new ChunkBuilder()
                .append("module", true, new ModuleParser())
                .build());
    }

    @Override
    public void call(String[] args) {
        if (args.length == 0) {
            Command.sendChatMessage("Please specify a module!");
            return;
        }
        Module m = ModuleManager.getModuleByName(args[0]);
        if (m == null) {
            Command.sendChatMessage("Unknown module '" + args[0] + "'");
            return;
        }
        m.toggle();
        Command.sendChatMessage(m.getName() + (m.isEnabled() ? " &aenabled" : " &cdisabled"));
    }
}
