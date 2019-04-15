package com.example.my.labelmanagement.listener;

/**
 * activity 和 fragment通信接口，即fragment通过该接口通知activity干活
 */

public interface OnSwipeRefreshListener {

    void onRefreshing();

    void onRefreshFinish();

}
