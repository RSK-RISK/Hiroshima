package me.rsk.hiroshima.gui.rgui.poof;

import me.rsk.hiroshima.gui.rgui.component.Component;

/**
 * Created by 086 on 21/07/2017.
 */
public interface IPoof<T extends Component, S extends PoofInfo> {
    public void execute(T component, S info);
    public Class getComponentClass();
    public Class getInfoClass();
}
