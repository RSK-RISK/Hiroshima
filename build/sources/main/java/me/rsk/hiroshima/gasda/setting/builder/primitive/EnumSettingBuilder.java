package me.rsk.hiroshima.gasda.setting.builder.primitive;

import me.rsk.hiroshima.gasda.setting.Setting;
import me.rsk.hiroshima.gasda.setting.builder.SettingBuilder;
import me.rsk.hiroshima.gasda.setting.impl.EnumSetting;

/**
 * Created by 086 on 14/10/2018.
 */
public class EnumSettingBuilder<T extends Enum> extends SettingBuilder<T> {
    Class<? extends Enum> clazz;

    public EnumSettingBuilder(Class<? extends Enum> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Setting<T> build() {
        return new EnumSetting<>(initialValue, predicate(), consumer(), name, visibilityPredicate(), clazz);
    }
}
