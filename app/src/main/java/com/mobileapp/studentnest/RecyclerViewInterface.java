package com.mobileapp.studentnest;

import android.view.View;

import java.util.HashMap;
// interface to implement onItemClick to keep track of what view
// was clicked and what action should be performed.
public interface RecyclerViewInterface {
    void onItemClick(int position, HashMap<Object, Object> specificLoan, View view);
}
