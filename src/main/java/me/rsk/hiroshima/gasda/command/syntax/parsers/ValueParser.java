package me.rsk.hiroshima.gasda.command.syntax.parsers;

import me.rsk.hiroshima.gasda.command.syntax.SyntaxChunk;
import me.rsk.hiroshima.jews.module.Module;
import me.rsk.hiroshima.jews.module.ModuleManager;
import me.rsk.hiroshima.gasda.setting.Setting;

import java.util.HashMap;
import java.util.TreeMap;

public class ValueParser extends AbstractParser {

    int moduleIndex;

    public ValueParser(int moduleIndex) {
        this.moduleIndex = moduleIndex;
    }

    public String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] values, String chunkValue) {
        if (moduleIndex>values.length-1 || chunkValue == null) return getDefaultChunk(thisChunk);
        String module = values[moduleIndex];
        Module m = ModuleManager.getModuleByName(module);
        if (m == null) return "";

        HashMap<String, Setting> possibilities = new HashMap<>();

        for (Setting v : m.settingList){
            if (v.getName().toLowerCase().startsWith(chunkValue.toLowerCase()))
                possibilities.put(v.getName(), v);
        }

        if (possibilities.isEmpty()) return "";

        TreeMap<String, Setting> p = new TreeMap<>(possibilities);
        Setting aV = p.firstEntry().getValue();
        return aV.getName().substring(chunkValue.length());
    }
}
