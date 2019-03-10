package com.example.android.vision;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
//import com.bumptech.glide.Glide;

import com.example.android.vision.nutrition.NutritionActivity;

import java.util.Collections;
import java.util.List;

public class VisionAdapter extends RecyclerView.Adapter<VisionAdapter.NumberViewHolder>  {

    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView iView, probView;
        final VisionAdapter mAdapter;


        public NumberViewHolder(View itemView, VisionAdapter visionAdapter) {
            super(itemView);
            iView = (TextView) itemView.findViewById(R.id.T1);
            probView = (TextView) itemView.findViewById(R.id.T2);
            this.mAdapter = visionAdapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            context = itemView.getContext();

            int mPosition = getLayoutPosition();

            // Use that to access the affected item in mWordList.
            Collection element = data.get(mPosition);
            // Change the word in the mWordList.

           String foodName = element.itemName;
            // Notify the adapter, that the data has changed so it can
            // update the RecyclerView to display the data.
            Toast.makeText(context, foodName, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, NutritionActivity.class);
            intent.putExtra("Foodname", foodName);
            context.startActivity(intent);
            mAdapter.notifyDataSetChanged();
        }
    }

    private static final String TAG = VisionAdapter.class.getSimpleName();
    private Context c;
    private int mNumberItems;

    private Context context;
    private LayoutInflater inflater;
    List<Collection> data = Collections.emptyList();
    Collection current;
    int currentPos = 0;


    public VisionAdapter(Context context, List<Collection> data) {
        this.c = c;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recyclerlist;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view, this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {

        NumberViewHolder myHolder = (NumberViewHolder) holder;
        Collection current = data.get(position);
        myHolder.iView.setText("" + current.itemName);
        myHolder.probView.setText("" + current.probability);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }




}
