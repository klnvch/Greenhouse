package com.klnvch.greenhousecommon.ui.states;

public interface StateHolderInterface {
    void addInterface(PhoneStateInterface stateInterface);

    void removeInterface(PhoneStateInterface stateInterface);

    void addInterface(ModuleStateInterface stateInterface);

    void removeInterface(ModuleStateInterface stateInterface);
}
