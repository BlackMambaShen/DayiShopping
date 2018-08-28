package shoppingcart.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import shoppingcart.adapter.ShoppingCartAdapter;

public class MyReceiver extends BroadcastReceiver {
    private  ShoppingCartAdapter adapter;

    public MyReceiver(ShoppingCartAdapter adapter) {
        this.adapter=adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        adapter.notifyDataSetChanged();
    }
}
