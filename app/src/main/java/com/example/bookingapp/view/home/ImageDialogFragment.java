package com.example.bookingapp.view.home;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bookingapp.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ImageDialogFragment extends DialogFragment {

    private static final String ARG_IMAGE_URL = "image_url";

    public static ImageDialogFragment newInstance(String imageUrl) {
        ImageDialogFragment fragment = new ImageDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image, container, false);
        ImageView ivFullScreen = view.findViewById(R.id.ivFullScreen);

        if (getArguments() != null) {
            String imageUrl = getArguments().getString(ARG_IMAGE_URL);
            Picasso.get().load(imageUrl).into(ivFullScreen);
        }

        ivFullScreen.setOnClickListener(v -> dismiss());
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent); // Để dialog trong suốt
        return dialog;
    }
}
