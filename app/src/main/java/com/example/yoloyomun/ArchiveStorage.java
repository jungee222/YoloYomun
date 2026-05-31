package com.example.yoloyomun;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Central place for where captured photos live. Photos are stored in the app's
 * external files directory under /archive, which the upload flow writes to and
 * the archive gallery reads from.
 */
public final class ArchiveStorage {

    private ArchiveStorage() {
    }

    public static File getArchiveDir(Context context) {
        File dir = new File(context.getExternalFilesDir(null), "archive");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /** A fresh, unique target file for a new capture. */
    public static File createPhotoFile(Context context) {
        String name = "IMG_" + System.currentTimeMillis() + ".jpg";
        return new File(getArchiveDir(context), name);
    }

    /** All saved photos, newest first. */
    public static List<File> listPhotos(Context context) {
        File[] files = getArchiveDir(context).listFiles((dir, name) -> {
            String lower = name.toLowerCase(Locale.US);
            return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png");
        });
        List<File> list = new ArrayList<>();
        if (files != null) {
            list.addAll(Arrays.asList(files));
            list.sort((a, b) -> Long.compare(b.lastModified(), a.lastModified()));
        }
        return list;
    }
}
