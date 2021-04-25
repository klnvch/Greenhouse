package com.klnvch.greenhousecommon.firestore;

public class PhoneStateHelper extends StateHelper {
    private static final String COLLECTION_PATH = "phoneState";

    @Override
    public String getCollectionPath() {
        return COLLECTION_PATH;
    }
}
