package com.klnvch.greenhousecommon.ui.states;

import com.klnvch.greenhousecommon.models.ModuleState;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.List;

public class ViewState {
    private final List<PhoneState> phoneStates;
    private final List<ModuleState> moduleStates;

    public ViewState(List<PhoneState> phoneStates, List<ModuleState> moduleStates) {
        this.phoneStates = phoneStates;
        this.moduleStates = moduleStates;
    }

    public PhoneState getNewestPhoneState() {
        if (phoneStates != null && phoneStates.size() > 0) {
            return phoneStates.get(0);
        }
        return null;
    }

    public ModuleState getNewestModuleState() {
        if (moduleStates != null && moduleStates.size() > 0) {
            return moduleStates.get(0);
        }
        return null;
    }

    public List<ModuleState> getModuleStates() {
        return moduleStates;
    }

    public List<PhoneState> getPhoneStates() {
        return phoneStates;
    }
}
