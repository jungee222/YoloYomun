package com.example.yoloyomun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

/**
 * Modern, organized landing screen. Shows a hero banner and quick-access cards
 * that jump straight to the other destinations, plus the shared globe button.
 */
public class homeFragment extends Fragment {

    public homeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_language)
                .setOnClickListener(v -> LocaleHelper.toggleLanguage());

        bindCard(view, R.id.card_upload, R.drawable.ic_upload,
                R.string.home_card_upload_title, R.string.home_card_upload_desc,
                R.id.uploadFragment);
        bindCard(view, R.id.card_analysis, R.drawable.ic_analysis,
                R.string.home_card_analysis_title, R.string.home_card_analysis_desc,
                R.id.analysisFragment);
        bindCard(view, R.id.card_archive, R.drawable.ic_archive,
                R.string.home_card_archive_title, R.string.home_card_archive_desc,
                R.id.archiveFragment);
    }

    private void bindCard(View root, @IdRes int cardId, @DrawableRes int icon,
                          @StringRes int title, @StringRes int desc,
                          @IdRes int destination) {
        View card = root.findViewById(cardId);
        ((ImageView) card.findViewById(R.id.qa_icon)).setImageResource(icon);
        ((TextView) card.findViewById(R.id.qa_title)).setText(title);
        ((TextView) card.findViewById(R.id.qa_desc)).setText(desc);
        card.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(destination));
    }
}
