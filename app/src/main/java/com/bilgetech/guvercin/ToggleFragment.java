package com.bilgetech.guvercin;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ToggleFragment extends Fragment {
    private static final int REQUEST_CODE_FOR_PERMISSION = 1001;

    public static final int INITIAL_ANIMATION_START_DELAY = 200;
    private PigeonButton pigeonButton;
    private PulseAnimationView pulseAnimationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toggle, container, false);
        pigeonButton = view.findViewById(R.id.pigeonButton);
        pulseAnimationView = view.findViewById(R.id.pulseAnimationView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pigeonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pigeonButton.isActive()) {
                    deactivatePigeon();
                } else if (Prefs.get().isReadyToConnect()){
                    activatePigeon();
                } else {
                    Toast.makeText(getContext(), R.string.you_must_configure_pigeon, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @AfterPermissionGranted(REQUEST_CODE_FOR_PERMISSION)
    private void activatePigeon() {
        if (hasSmsPermission()) {
            SmsService.start();
            Prefs.get().setActive(true).save();
            pigeonButton.activate(new Runnable() {
                @Override
                public void run() {
                    pulseAnimationView.show();
                }
            });
        } else {
            requestSmsPermission();
        }
    }

    private void deactivatePigeon() {
        SmsService.stop();
        Prefs.get().setActive(false).save();
        pigeonButton.deactivate(new Runnable() {
            @Override
            public void run() {
                pulseAnimationView.hide();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        pigeonButton.setActive(Prefs.get().isActive());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pigeonButton.showUp();
            }
        }, INITIAL_ANIMATION_START_DELAY);

        if (pigeonButton.isActive()) {
            pulseAnimationView.show();
        }

        pigeonButton.onResume();
    }

    @Override
    public void onPause() {
        pigeonButton.onPause();
        pulseAnimationView.stopAnimation();
        super.onPause();
    }

    public void hideButton(Runnable endAction) {
        pigeonButton.hide(endAction);
    }

    private boolean hasSmsPermission() {
        return EasyPermissions.hasPermissions(getContext(), Manifest.permission.RECEIVE_SMS);
    }

    void requestSmsPermission() {
        if (EasyPermissions.somePermissionDenied(this, Manifest.permission.RECEIVE_SMS)) {
            new AppSettingsDialog.Builder(this)
                    .setRationale(R.string.rationale_sms)
                    .setRequestCode(REQUEST_CODE_FOR_PERMISSION)
                    .build().show();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_sms), REQUEST_CODE_FOR_PERMISSION, Manifest.permission.RECEIVE_SMS);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FOR_PERMISSION) {
            if (hasSmsPermission()) {
                activatePigeon();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
