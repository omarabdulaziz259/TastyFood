package com.example.tastyfood.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import io.reactivex.rxjava3.core.Observable;

public class InternetConnectivity {
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            Network activeNetwork = cm.getActiveNetwork();
            if (activeNetwork != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);
                return capabilities != null &&
                        (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            }
        }
        return false;
    }

    public static Observable<Boolean> observeInternetConnectivity(Context context) {
        return Observable.create(emitter -> {
                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (cm == null) {
                        emitter.onNext(false);
                        return;
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        cm.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                            @Override
                            public void onAvailable(Network network) {
                                emitter.onNext(true);
                            }

                            @Override
                            public void onLost(Network network) {
                                emitter.onNext(false);
                            }
                        });
                    } else {
                        BroadcastReceiver networkReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                emitter.onNext(isInternetAvailable(context));
                            }
                        };
                        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                        context.registerReceiver(networkReceiver, filter);
                    }
                });
    }
}
