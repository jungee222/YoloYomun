package com.example.yoloyomun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

/**
 * Gallery of saved photos. Photos are laid out in a 3-column grid; tapping one
 * opens {@link PhotoDetailDialog} where it can be shared, analyzed, or deleted.
 * The grid refreshes on resume so freshly captured photos appear automatically.
 */
public class archiveFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private PhotoAdapter adapter;

    public archiveFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_archive, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_language)
                .setOnClickListener(v -> LocaleHelper.toggleLanguage());

        recyclerView = view.findViewById(R.id.recycler_photos);
        emptyView = view.findViewById(R.id.empty_view);

        adapter = new PhotoAdapter(this::openDetail);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        recyclerView.setAdapter(adapter);

        getParentFragmentManager().setFragmentResultListener(
                PhotoDetailDialog.REQUEST_KEY, getViewLifecycleOwner(),
                (requestKey, result) -> {
                    String action = result.getString(PhotoDetailDialog.RESULT_ACTION);
                    if (PhotoDetailDialog.ACTION_ANALYZE.equals(action)) {
                        Navigation.findNavController(view).navigate(R.id.analysisFragment);
                    } else {
                        // Photo deleted (or otherwise changed): rebuild the grid.
                        refresh();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        List<File> photos = ArchiveStorage.listPhotos(requireContext());
        adapter.submit(photos);
        boolean empty = photos.isEmpty();
        emptyView.setVisibility(empty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    private void openDetail(File photo) {
        PhotoDetailDialog.newInstance(photo).show(getParentFragmentManager(), "photo_detail");
    }
}
