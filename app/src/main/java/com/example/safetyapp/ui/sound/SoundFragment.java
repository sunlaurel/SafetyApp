package com.example.safetyapp.ui.sound;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.safetyapp.databinding.FragmentSoundBinding;

public class SoundFragment extends Fragment {

    private FragmentSoundBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SoundViewModel soundViewModel =
                new ViewModelProvider(this).get(SoundViewModel.class);

        binding = FragmentSoundBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}