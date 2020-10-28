package com.example.ea2soa.data;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryStatus {

    private IntentFilter intentFilter;
    private Intent batteryStatus;

    public BatteryStatus(Context context) {
        this.intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        this.batteryStatus = context.registerReceiver(null, this.intentFilter);
    }

    public int getBatteryPercentage() {

        int level = this.batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = this.batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float) scale;

        return (int) (batteryPct * 100);
    }
}
