package com.example.yuanyuanlai.legalapp.BlueTooth;

import com.example.yuanyuanlai.legalapp.Internet.NetworkType;

public interface BlueToothChangeObserver {

    void onNetDisconnected();

    void onNetConnected(NetworkType networkType);

}
