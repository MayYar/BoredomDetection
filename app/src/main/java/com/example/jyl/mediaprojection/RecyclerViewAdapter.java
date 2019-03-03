package com.example.jyl.mediaprojection;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context Context, ArrayList<String> mNames, ArrayList<String> mImages) {
        this.mNames = mNames;
        this.mImages = mImages;
        this.mContext = Context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder called");

        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);

        holder.name.setText(mNames.get(position));
//        String fileName = "1.jpg";
//        String completePath = Environment.getExternalStorageDirectory() + "/" + fileName;
//
//        File file = new File(completePath);
//        Uri imageUri = Uri.fromFile(file);
//
//        Glide.with(this)
//                .load(imageUri)
//                .into(imgView);
        holder.image.setOnClickListener(new View.OnClickListener() {
            int check = 1; //When check = 1 ,you have your FIRST background set to the button
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClick: clicked on an image: " + mImages.get(position));
                Toast.makeText(mContext, Integer.toString(position), Toast.LENGTH_SHORT).show();
//                holder.image.setBackgroundResource(R.drawable.border_selected);
                if(check == 1){
                    holder.image.setImageResource(R.drawable.test_pic);
                    check = 0;
                }else{
                    holder.image.setBackgroundColor(Color.WHITE);
                    check = 1;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
        }
    }
}
