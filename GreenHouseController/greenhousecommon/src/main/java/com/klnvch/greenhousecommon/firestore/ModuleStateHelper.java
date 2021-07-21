package com.klnvch.greenhousecommon.firestore;

import org.jetbrains.annotations.NotNull;

public class ModuleStateHelper extends StateHelper {
    private static final String COLLECTION_PATH = "moduleState";

    @Override
    public @NotNull String getCollectionPath() {
        return COLLECTION_PATH;
    }
}
