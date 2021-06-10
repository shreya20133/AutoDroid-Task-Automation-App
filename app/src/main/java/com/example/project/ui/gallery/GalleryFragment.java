package com.example.project.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.Objects;

public class GalleryFragment extends Fragment {

    RecyclerView recyclerViewMyMacros;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");

        recyclerViewMyMacros=root.findViewById(R.id.mymacrosListRV);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerViewMyMacros.setLayoutManager(linearLayoutManager);
        ListMyMacrosAdapter listMyMacrosAdapter=new ListMyMacrosAdapter(getActivity());
        recyclerViewMyMacros.setAdapter(listMyMacrosAdapter);
        listMyMacrosAdapter.notifyDataSetChanged();
        return root;
    }
}