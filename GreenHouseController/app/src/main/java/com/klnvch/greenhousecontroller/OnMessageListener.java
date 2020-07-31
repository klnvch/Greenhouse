package com.klnvch.greenhousecontroller;

interface OnMessageListener {
    void onMessage(String msg);

    void onError(Throwable throwable);
}
