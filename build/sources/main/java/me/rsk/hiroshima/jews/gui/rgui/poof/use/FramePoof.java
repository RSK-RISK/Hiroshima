package me.rsk.hiroshima.jews.gui.rgui.poof.use;

import me.rsk.hiroshima.jews.gui.rgui.component.Component;
import me.rsk.hiroshima.jews.gui.rgui.poof.PoofInfo;

/**
 * Created by 086 on 29/07/2017.
 */
public abstract class FramePoof<T extends Component, S extends PoofInfo> extends Poof<T, S> {
    public static class FramePoofInfo extends PoofInfo {
        private Action action;

        public FramePoofInfo(Action action) {
            this.action = action;
        }

        public Action getAction() {
            return action;
        }
    }

    public enum Action {
        MINIMIZE,MAXIMIZE,CLOSE
    }
}
