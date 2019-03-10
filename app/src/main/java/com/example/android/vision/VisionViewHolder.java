package com.example.android.vision;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class VisionViewHolder extends RecyclerView.ViewHolder {
    TextView textViewForItem;
    public VisionViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewForItem=(TextView)itemView.findViewById(R.id.T1);
    }
    void bind(int index){
        textViewForItem.setText(index+"");
    }
}
