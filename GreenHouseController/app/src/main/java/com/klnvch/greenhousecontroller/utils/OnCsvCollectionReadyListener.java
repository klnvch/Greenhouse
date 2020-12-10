package com.klnvch.greenhousecontroller.utils;

import java.util.Collection;

interface OnCsvCollectionReadyListener {
    void onCsvCollectionReady(Collection<String[]> collection);
}
