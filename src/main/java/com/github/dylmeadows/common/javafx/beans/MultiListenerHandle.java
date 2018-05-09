package com.github.dylmeadows.common.javafx.beans;

class MultiListenerHandle implements ListenerHandle {

    private ListenerHandle[] handles;

    MultiListenerHandle(ListenerHandle... handles) {
        this.handles = handles;
    }

    @Override
    public void attach() {
        for (ListenerHandle handle : handles)
            handle.attach();
    }

    @Override
    public void detach() {
        for (ListenerHandle handle : handles)
            handle.detach();
    }
}
