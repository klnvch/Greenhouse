package com.klnvch.greenhousecommon.firestore;

public class ModuleStateHelper extends StateHelper {
    private static final String COLLECTION_PATH = "moduleState";

    @Override
    public String getCollectionPath() {
        return COLLECTION_PATH;
    }
}
