package at.dalex.grape.sdk.window.event;

import at.dalex.grape.sdk.window.viewport.ViewportPanel;

//TODO: Unused
public class ViewportRefreshEvent extends EventBase {

    private ViewportPanel viewportInstance;

    public ViewportRefreshEvent(ViewportPanel viewportInstance) {
        this.viewportInstance = viewportInstance;
    }

    public ViewportPanel getViewportInstance() {
        return this.viewportInstance;
    }
}
