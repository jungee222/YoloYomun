package com.example.yoloyomun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Placeholder "분석" (Analysis) tab. Content is intentionally left blank for now;
 * only the shared globe language toggle is wired up.
 */
public class analysisFragment extends Fragment {

    public analysisFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analysis, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_language)
                .setOnClickListener(v -> LocaleHelper.toggleLanguage());
    }
}
