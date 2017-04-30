package com.am.demo.taskapp.adapter;

/**
 * Created by malbor806 on 30.04.2017.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
