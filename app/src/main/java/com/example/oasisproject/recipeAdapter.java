package com.example.oasisproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class recipeAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<RecipeData> Recipedata;
    ArrayList<RecipeData> retRecipedata;
    String split[];

    public recipeAdapter(Context context, ArrayList<RecipeData> Recipedata){
        mContext = context;
        this.Recipedata = Recipedata;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return Recipedata.size();
    }

    @Override
    public Object getItem(int position) {
        return Recipedata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.recipelist, null);
        TextView textviewRecipeName = (TextView) view.findViewById(R.id.textviewRecipeName);
        ImageView imageviewRecipe = (ImageView) view.findViewById(R.id.imageviewRecipe);
        Button buttonurl = (Button)view.findViewById(R.id.buttonurl);

        textviewRecipeName.setText(Recipedata.get(position).getName());
        imageviewRecipe.setImageResource(Recipedata.get(position).getImage());
        buttonurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(Recipedata.get(position).getUrl()));
                browse.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(browse);
            }
        });

        return view;
    }
}
