package com.example.gr.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.gr.ControllerApplication;
import com.example.gr.R;
import com.example.gr.activity.MainActivity;
import com.example.gr.databinding.FragmentProfileBinding;
import com.example.gr.device.DeviceCoordinator;
import com.example.gr.device.DeviceManager;
import com.example.gr.device.GBDevice;
import com.example.gr.utils.DeviceHelper;
import com.example.gr.utils.GB;
import com.example.gr.utils.GBPrefs;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ProfileFragment extends BaseFragment {
    private FragmentProfileBinding mFragmentProfileBinding;
    private DeviceManager deviceManager;
    private GBDevice device;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false);
        deviceManager = ((ControllerApplication) getActivity().getApplication()).getDeviceManager();
        context = getContext();
        initUi();

        return mFragmentProfileBinding.getRoot();
    }
    private void initUi(){
        if(deviceManager.getDevices().size() < 1){
            mFragmentProfileBinding.disconnectButton.setEnabled(false);
            mFragmentProfileBinding.disconnectButton.setBackgroundResource(R.drawable.bg_gray_shape_corner_6);
            mFragmentProfileBinding.removeButton.setEnabled(false);
            mFragmentProfileBinding.removeButton.setBackgroundResource(R.drawable.bg_gray_shape_corner_6);
        }else{
            mFragmentProfileBinding.disconnectButton.setEnabled(true);
            mFragmentProfileBinding.disconnectButton.setBackgroundResource(R.drawable.bg_red_shape_corner_6);
            mFragmentProfileBinding.removeButton.setEnabled(true);
            mFragmentProfileBinding.removeButton.setBackgroundResource(R.drawable.bg_green_shape_corner_6);
            device = deviceManager.getDevices().get(0);
            mFragmentProfileBinding.disconnectButton.setOnClickListener(v->{
                if (device.getState() != GBDevice.State.NOT_CONNECTED) {
                    showTransientSnackbar(R.string.controlcenter_snackbar_disconnecting);
                    ControllerApplication.deviceService(device).disconnect();
                }
                removeFromLastDeviceAddressesPref(device);
            });
            mFragmentProfileBinding.removeButton.setOnClickListener(v->{
                showRemoveDeviceDialog(device);
            });
        }
    }
    private void removeFromLastDeviceAddressesPref(GBDevice device) {
        Set<String> lastDeviceAddresses = ControllerApplication.getPrefs().getStringSet(GBPrefs.LAST_DEVICE_ADDRESSES, Collections.emptySet());
        if (lastDeviceAddresses.contains(device.getAddress())) {
            lastDeviceAddresses = new HashSet<String>(lastDeviceAddresses);
            lastDeviceAddresses.remove(device.getAddress());
            ControllerApplication.getPrefs().getPreferences().edit().putStringSet(GBPrefs.LAST_DEVICE_ADDRESSES, lastDeviceAddresses).apply();
            Toast.makeText(getContext(),"removed successfully",Toast.LENGTH_SHORT).show();
        }
    }
    private void showRemoveDeviceDialog(final GBDevice device) {
        new MaterialAlertDialogBuilder(context)
                .setCancelable(true)
                .setTitle(context.getString(R.string.controlcenter_delete_device_name, device.getName()))
                .setMessage(R.string.controlcenter_delete_device_dialogmessage)
                .setPositiveButton(R.string.Delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            DeviceCoordinator coordinator = device.getDeviceCoordinator();
                            if (coordinator != null) {
                                coordinator.deleteDevice(device);
                            }
                            DeviceHelper.getInstance().removeBond(device);
                        } catch (Exception ex) {
                            GB.toast(context, context.getString(R.string.error_deleting_device, ex.getMessage()), Toast.LENGTH_LONG, GB.ERROR, ex);
                        } finally {
                            Intent refreshIntent = new Intent(DeviceManager.ACTION_REFRESH_DEVICELIST);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(refreshIntent);
                        }
                    }
                })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    @Override
    protected void updateUIAfterShowSnackBar() {

    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, "Cá nhân");
        }
    }
}
