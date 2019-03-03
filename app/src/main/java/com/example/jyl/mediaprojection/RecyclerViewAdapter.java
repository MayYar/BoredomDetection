package com.example.jyl.mediaprojection;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Boolean> mCheck = new ArrayList<>();

    private Context mContext;
    int selectedPosition=-1;

    public RecyclerViewAdapter(Context Context, ArrayList<String> mNames, ArrayList<String> mImages, ArrayList<Boolean> mCheck) {
        this.mNames = mNames;
        this.mImages = mImages;
        this.mCheck = mCheck;
        this.mContext = Context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.image = (ImageView)view.findViewById(R.id.image);
        holder.name = (TextView)view.findViewById(R.id.name);
        holder.check = (ImageView)view.findViewById(R.id.check);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder called");

        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);

        holder.name.setText(mNames.get(position));

        if(mCheck.get(position))
            holder.check.setVisibility(View.VISIBLE);
        else
            holder.check.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClick: clicked on an image: " + mImages.get(position));
                Toast.makeText(mContext, Integer.toString(position), Toast.LENGTH_SHORT).show();
                if(!mCheck.get(position))
                    mCheck.set(position, true);
                else
                    mCheck.set(position, false);
//                selectedPosition=position;
                notifyDataSetChanged();

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
        ImageView check;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
