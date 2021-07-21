package com.klnvch.greenhousecommon.firestore;

import org.jetbrains.annotations.NotNull;

public class PhoneStateHelper extends StateHelper {
    private static final String COLLECTION_PATH = "phoneState";

    @Override
    public @NotNull String getCollectionPath() {
        return COLLECTION_PATH;
    }
}
