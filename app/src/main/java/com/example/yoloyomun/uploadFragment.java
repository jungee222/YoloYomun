package com.example.yoloyomun;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;

/**
 * Lets the user capture a photo with the device camera. The capture is written
 * straight into the shared archive directory so it shows up in the Archive tab,
 * and the most recent shot is previewed here.
 */
public class uploadFragment extends Fragment {

    private static final String ARG_PENDING_PATH = "pending_photo_path";

    private ImageView preview;
    private File pendingPhotoFile;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> onCaptureResult(result.getResultCode()));

    public uploadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preview = view.findViewById(R.id.image_preview);

        view.findViewById(R.id.btn_language)
                .setOnClickListener(v -> LocaleHelper.toggleLanguage());
        view.findViewById(R.id.btn_take_photo)
                .setOnClickListener(v -> launchCamera());

        if (savedInstanceState != null) {
            String path = savedInstanceState.getString(ARG_PENDING_PATH);
            if (path != null) {
                pendingPhotoFile = new File(path);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (pendingPhotoFile != null) {
            outState.putString(ARG_PENDING_PATH, pendingPhotoFile.getAbsolutePath());
        }
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireContext().getPackageManager()) == null) {
            Toast.makeText(requireContext(), R.string.upload_camera_unavailable,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        pendingPhotoFile = ArchiveStorage.createPhotoFile(requireContext());
        Uri photoUri = FileProvider.getUriForFile(requireContext(),
                requireContext().getPackageName() + ".fileprovider", pendingPhotoFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cameraLauncher.launch(intent);
    }

    private void onCaptureResult(int resultCode) {
        if (pendingPhotoFile == null) {
            return;
        }
        if (resultCode == Activity.RESULT_OK && pendingPhotoFile.length() > 0) {
            showPreview(pendingPhotoFile);
            Toast.makeText(requireContext(), R.string.upload_saved,
                    Toast.LENGTH_SHORT).show();
        } else {
            // Cancelled or failed: don't leave a 0-byte file in the archive.
            if (pendingPhotoFile.exists() && pendingPhotoFile.length() == 0) {
                pendingPhotoFile.delete();
            }
            if (resultCode != Activity.RESULT_CANCELED) {
                Toast.makeText(requireContext(), R.string.upload_capture_failed,
                        Toast.LENGTH_SHORT).show();
            }
        }
        pendingPhotoFile = null;
    }

    private void showPreview(File file) {
        Bitmap bitmap = ImageUtils.decodeScaled(file.getAbsolutePath(), 1080, 1080);
        if (bitmap != null) {
            preview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            preview.setImageBitmap(bitmap);
        }
    }
}
