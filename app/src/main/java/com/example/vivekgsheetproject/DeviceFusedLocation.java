package com.example.vivekgsheetproject;

import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;

import androidx.annotation.NonNull;

public class DeviceFusedLocation extends FusedLocationProviderClient {
    public DeviceFusedLocation(@NonNull Context context) {
        super(context);
    }
}