package com.example.android.vision.nutrition;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.vision.R;

import java.util.Collections;
import java.util.List;

public class NutriAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>


{
    private Context context;
    private LayoutInflater inflater;
    List<DataNutrition> data= Collections.emptyList();
    DataNutrition current;
    int currentPos=0;

    // create constructor to initialize context and data sent from MainActivity
    public NutriAdapter(Context context, List<DataNutrition> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }


    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.nutritions_list, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        DataNutrition current = data.get(position);
        myHolder.moviesDate.setText("Item name: " + current.itemName);
        myHolder.moviesTitle.setText("Calories: " + current.calories);
        myHolder.moviesOverview.setText("Fat: " + current.fat);


    }

    // return total item from List
    @Override
    public int getItemCount()
    {
        return data.size();
    }



    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView moviesTitle;
        TextView moviesOverview;
        TextView moviesDate;
        SharedPreferences profilePrefs;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            moviesTitle= (TextView) itemView.findViewById(R.id.movie_title);
            moviesOverview= (TextView) itemView.findViewById(R.id.movie_overview);
            moviesDate = (TextView) itemView.findViewById(R.id.movie_releaseDate);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Context context=view.getContext();
            profilePrefs = context.getSharedPreferences
                    (context.getResources().getString(R.string.profile_fileName), Context.MODE_PRIVATE);


            int mPosition = getLayoutPosition();
            DataNutrition element = data.get(mPosition);
            double calFood = element.calories;



        Intent intent = new Intent(context,NutritionAlert.class);
        intent.putExtra("ProductSelected", element.itemName);
        intent.putExtra("CurrentCalorie",calFood );
        context.startActivity(intent);
        }
    }

}
