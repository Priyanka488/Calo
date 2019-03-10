package com.example.android.vision;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class VAdapter extends RecyclerView.Adapter {
    private int mNumberItems;
    private List<String> mList;
    public VAdapter(List<String> mList) {
        this.mList = mList;}

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context=viewGroup.getContext();
        int LayoutIDforListItem=R.layout.recyclerlist;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately=false;
        View view=inflater.inflate(LayoutIDforListItem, viewGroup,shouldAttachToParentImmediately);
        VisionViewHolder viewHolder=new VisionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
    //viewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }
}
