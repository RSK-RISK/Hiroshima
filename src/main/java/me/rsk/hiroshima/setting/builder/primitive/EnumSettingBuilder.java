package me.rsk.hiroshima.setting.builder.primitive;

import me.rsk.hiroshima.setting.Setting;
import me.rsk.hiroshima.setting.builder.SettingBuilder;
import me.rsk.hiroshima.setting.impl.EnumSetting;

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
