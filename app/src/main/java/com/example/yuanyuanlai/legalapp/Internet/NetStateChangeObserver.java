package com.example.yuanyuanlai.legalapp.Internet;

public interface NetStateChangeObserver {

    void onNetDisconnected();

    void onNetConnected(NetworkType networkType);

}
