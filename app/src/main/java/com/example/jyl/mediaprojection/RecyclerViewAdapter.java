package com.example.jyl.mediaprojection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import static com.example.jyl.mediaprojection.MainActivity.imageShow;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Boolean> mCheck = new ArrayList<>();

    private Context mContext;
    int selectedPosition1 = -1;
    int selectedPosition2 = -1;
    private int checkCount = 0;

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

//        holder.name.setText(mNames.get(position));
        holder.name.setText("");

        if(mCheck.get(position))
            holder.check.setVisibility(View.VISIBLE);
        else
            holder.check.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClick: clicked on an image: " + mImages.get(position));
                Toast.makeText(mContext, Integer.toString(position), Toast.LENGTH_SHORT).show();

                Glide.with(mContext)
                        .asBitmap()
                        .load(mImages.get(position))
                        .into(imageShow);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


//                if(checkCount == 2){
//                    new AlertDialog.Builder(mContext)
//                            .setTitle("Warning")
//                            .setMessage("You have already marked the interval between position " + (selectedPosition1 = (selectedPosition1<selectedPosition2)?selectedPosition1:selectedPosition2) + " and " + (selectedPosition2 = (selectedPosition1<selectedPosition2)?selectedPosition2:selectedPosition1))
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            })
//                            .show();

//                }
                if(!mCheck.get(position)){
                    mCheck.set(position, true);
                    checkCount++;

//                        if(selectedPosition1 == -1)
//                            selectedPosition1 = position;
//                        else
//                            selectedPosition2 = position;
                }
                else {
                    mCheck.set(position, false);
                    checkCount--;
                }




                notifyDataSetChanged();

                return true;
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
