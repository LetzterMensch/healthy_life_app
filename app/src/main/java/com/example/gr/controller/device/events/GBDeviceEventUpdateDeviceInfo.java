package com.example.gr.controller.device.events;

import com.example.gr.controller.device.GenericItem;
import com.example.gr.controller.device.ItemWithDetails;

public class GBDeviceEventUpdateDeviceInfo extends GBDeviceEvent {
    public ItemWithDetails item;

    public GBDeviceEventUpdateDeviceInfo(final ItemWithDetails item) {
        this.item = item;
    }

    public GBDeviceEventUpdateDeviceInfo(final String name, final String details) {
        this(new GenericItem(name, details));
    }
}
