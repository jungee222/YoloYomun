package com.example.yoloyomun;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;

/**
 * Full photo viewer shown when a gallery item is tapped. Lets the user act on
 * the photo: share it, send it to analysis, or delete it from the archive.
 * Results are reported back to the host fragment via the Fragment Result API.
 */
public class PhotoDetailDialog extends DialogFragment {

    public static final String REQUEST_KEY = "photo_detail_request";
    public static final String RESULT_ACTION = "action";
    public static final String ACTION_DELETED = "deleted";
    public static final String ACTION_ANALYZE = "analyze";

    private static final String ARG_PATH = "photo_path";

    public static PhotoDetailDialog newInstance(@NonNull File photo) {
        PhotoDetailDialog dialog = new PhotoDetailDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PATH, photo.getAbsolutePath());
        dialog.setArguments(args);
        return dialog;
    }

    private File photo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String path = getArguments() != null ? getArguments().getString(ARG_PATH) : null;
        if (path != null) {
            photo = new File(path);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_photo_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (photo == null || !photo.exists()) {
            dismiss();
            return;
        }

        ImageView image = view.findViewById(R.id.image_full);
        Bitmap bitmap = ImageUtils.decodeScaled(photo.getAbsolutePath(), 1080, 1080);
        if (bitmap != null) {
            image.setImageBitmap(bitmap);
        }

        view.findViewById(R.id.btn_share).setOnClickListener(v -> sharePhoto());
        view.findViewById(R.id.btn_analyze).setOnClickListener(v -> finishWith(ACTION_ANALYZE));
        view.findViewById(R.id.btn_delete).setOnClickListener(v -> confirmDelete());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.92f),
                    WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    private void sharePhoto() {
        Uri uri = FileProvider.getUriForFile(requireContext(),
                requireContext().getPackageName() + ".fileprovider", photo);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, getString(R.string.share_chooser)));
    }

    private void confirmDelete() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_confirm_msg)
                .setNegativeButton(R.string.action_cancel, null)
                .setPositiveButton(R.string.action_delete, (d, which) -> {
                    if (photo.exists()) {
                        photo.delete();
                    }
                    finishWith(ACTION_DELETED);
                })
                .show();
    }

    private void finishWith(String action) {
        Bundle result = new Bundle();
        result.putString(RESULT_ACTION, action);
        getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
        dismiss();
    }
}
