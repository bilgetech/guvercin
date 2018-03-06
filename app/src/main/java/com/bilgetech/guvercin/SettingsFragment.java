package com.bilgetech.guvercin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SettingsFragment extends Fragment {

    EditText urlEdit, phoneEdit;
    Button saveButton;
    ImageView pigeonImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        urlEdit = view.findViewById(R.id.urlEdit);
        phoneEdit = view.findViewById(R.id.phoneEdit);
        saveButton = view.findViewById(R.id.saveButton);
        pigeonImage = view.findViewById(R.id.pigeonImage);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.get()
                        .setUrl(urlEdit.getText().toString())
                        .setPhone(phoneEdit.getText().toString())
                        .save();

                SmsService.stop();

                if(Prefs.get().isReadyToConnect()) {
                    SmsService.start();
                }
            }
        });

        phoneEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        String url = Prefs.get().getUrl();
        String phone = Prefs.get().getPhone();

        if(!TextUtils.isEmpty(url)) {
            urlEdit.setText(url);
        }

        if(!TextUtils.isEmpty(phone)) {
            phoneEdit.setText(phone);
        }
    }
}
