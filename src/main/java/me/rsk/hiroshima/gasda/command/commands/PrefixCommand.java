package me.rsk.hiroshima.gasda.command.commands;

import me.rsk.hiroshima.gasda.command.Command;
import me.rsk.hiroshima.gasda.command.syntax.ChunkBuilder;

/**
 * Created by 086 on 10/10/2018.
 */
public class PrefixCommand extends Command {

    public PrefixCommand() {
        super("prefix", new ChunkBuilder().append("character").build());
    }

    @Override
    public void call(String[] args) {
        if (args.length == 0) {
            Command.sendChatMessage("Please specify a new prefix!");
            return;
        }

        Command.commandPrefix.setValue(args[0]);
        Command.sendChatMessage("Prefix set to &b" + Command.commandPrefix.getValue());
    }

}
