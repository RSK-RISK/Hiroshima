package me.rsk.hiroshima.gasda.setting.builder.primitive;

import me.rsk.hiroshima.gasda.setting.builder.SettingBuilder;
import me.rsk.hiroshima.gasda.setting.impl.StringSetting;

/**
 * Created by 086 on 13/10/2018.
 */
public class StringSettingBuilder extends SettingBuilder<String> {
    @Override
    public StringSetting build() {
        return new StringSetting(initialValue, predicate(), consumer(), name, visibilityPredicate());
    }
}
