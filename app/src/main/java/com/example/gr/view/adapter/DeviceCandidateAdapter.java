package com.example.gr.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gr.ControllerApplication;
import com.example.gr.R;
import com.example.gr.controller.device.DeviceCoordinator;
import com.example.gr.controller.device.model.DeviceType;
import com.example.gr.utils.GB;
import com.example.gr.controller.device.GBDevice;
import com.example.gr.controller.device.GBDeviceCandidate;
import com.example.gr.utils.DeviceHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Adapter for displaying GBDeviceCandate instances.
 */
public class DeviceCandidateAdapter extends ArrayAdapter<GBDeviceCandidate> {

    private final Context context;

    public DeviceCandidateAdapter(Context context, List<GBDeviceCandidate> deviceCandidates) {
        super(context, 0, deviceCandidates);

        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        GBDeviceCandidate device = getItem(position);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_with_details, parent, false);
        }
        ImageView deviceImageView = view.findViewById(R.id.item_image);
        TextView deviceNameLabel = view.findViewById(R.id.item_name);
        TextView deviceAddressLabel = view.findViewById(R.id.item_details);
        TextView deviceStatus = view.findViewById(R.id.item_status);

        DeviceType deviceType = DeviceHelper.getInstance().resolveDeviceType(device);

        DeviceCoordinator coordinator = deviceType.getDeviceCoordinator();

        String name = formatDeviceCandidate(device);
        deviceNameLabel.setText(name);
        deviceAddressLabel.setText(device.getMacAddress());
        deviceImageView.setImageResource(coordinator.getDefaultIconResource());

        final List<String> statusLines = new ArrayList<>();
        if (device.isBonded()) {
            statusLines.add(getContext().getString(R.string.device_is_currently_bonded));
            if (!ControllerApplication.getPrefs().getBoolean("ignore_bonded_devices", true)) { // This could be passed to the constructor instead
                deviceImageView.setImageResource(coordinator.getDisabledIconResource());
            }
        }

        if (!deviceType.isSupported()) {
            statusLines.add(getContext().getString(R.string.device_unsupported));
        }

        if (coordinator.isExperimental()) {
            statusLines.add(getContext().getString(R.string.device_experimental));
        }
        if (coordinator.getBondingStyle() == DeviceCoordinator.BONDING_STYLE_REQUIRE_KEY) {
            statusLines.add(getContext().getString(R.string.device_requires_key));
        }

        deviceStatus.setText(TextUtils.join("\n", statusLines));
        return view;
    }

    private String formatDeviceCandidate(GBDeviceCandidate device) {
        if (device.getRssi() > GBDevice.RSSI_UNKNOWN) {
            return context.getString(R.string.device_with_rssi, device.getName(), GB.formatRssi(device.getRssi()));
        }
        return device.getName();
    }
}
