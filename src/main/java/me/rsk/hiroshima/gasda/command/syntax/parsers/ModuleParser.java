package me.rsk.hiroshima.gasda.command.syntax.parsers;


import me.rsk.hiroshima.gasda.command.syntax.SyntaxChunk;
import me.rsk.hiroshima.jews.module.Module;
import me.rsk.hiroshima.jews.module.ModuleManager;

public class ModuleParser extends AbstractParser {

    @Override
    public String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] values, String chunkValue) {
        if (chunkValue == null)
            return getDefaultChunk(thisChunk);

        Module chosen = ModuleManager.getModules().stream()
                .filter(module -> module.getName().toLowerCase().startsWith(chunkValue.toLowerCase()))
                .findFirst()
                .orElse(null);
        if (chosen == null) return null;
        return chosen.getName().substring(chunkValue.length());
    }

}
