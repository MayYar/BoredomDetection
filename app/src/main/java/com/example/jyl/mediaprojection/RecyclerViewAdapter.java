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
//    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Boolean> mCheck = new ArrayList<>();
    private ArrayList<String> mStart = new ArrayList<>();
    private ArrayList<String> mEnd = new ArrayList<>();
    private ArrayList<String> mLabel = new ArrayList<>();

    private Context mContext;
    public static int selectedPosition1 = -1;
    public static int selectedPosition2 = -1;
    private int checkCount = 0;

    public RecyclerViewAdapter(Context Context, ArrayList<String> mImages, ArrayList<Boolean> mCheck, ArrayList<String> mLabel, ArrayList<String> mStart, ArrayList<String> mEnd) {
//        this.mNames = mNames;
        this.mImages = mImages;
        this.mCheck = mCheck;
        this.mContext = Context;
        this.mLabel = mLabel;
        this.mStart = mStart;
        this.mEnd = mEnd;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.image = (ImageView)view.findViewById(R.id.image);
//        holder.name = (TextView)view.findViewById(R.id.name);
        holder.check = (ImageView)view.findViewById(R.id.check);
        holder.label = (View)view.findViewById(R.id.v_label);
        holder.start = (ImageView)view.findViewById(R.id.iv_start);
        holder.end = (ImageView)view.findViewById(R.id.iv_end);

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
//        holder.name.setText("");

        if(mCheck.get(position))
            holder.check.setVisibility(View.VISIBLE);
        else
            holder.check.setVisibility(View.INVISIBLE);

        if(mLabel.get(position).equals("NA")){
            holder.label.setVisibility(View.INVISIBLE);
            holder.start.setVisibility(View.INVISIBLE);
            holder.end.setVisibility(View.INVISIBLE);

        }else if(mLabel.get(position).equals("bored")){
            holder.label.setVisibility(View.VISIBLE);
            if(mStart.get(position).equals("1")){
                holder.start.setVisibility(View.VISIBLE);
                holder.end.setVisibility(View.INVISIBLE);
            }
            else if(mEnd.get(position).equals("1")){
                holder.start.setVisibility(View.INVISIBLE);
                holder.end.setVisibility(View.VISIBLE);
            }

            else{
                holder.start.setVisibility(View.INVISIBLE);
                holder.end.setVisibility(View.INVISIBLE);
            }

        }else if(mLabel.get(position).equals("not_bored")){
            holder.label.setVisibility(View.VISIBLE);
            if(mStart.get(position).equals("1"))
                holder.start.setVisibility(View.VISIBLE);
            else if(mEnd.get(position).equals("1"))
                holder.end.setVisibility(View.VISIBLE);
            else{
                holder.start.setVisibility(View.INVISIBLE);
                holder.end.setVisibility(View.INVISIBLE);
            }
        }

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

                //未選擇 > 選擇
                if(!mCheck.get(position)){

                    if(checkCount == 2){
                        int temp = -1;
                    new AlertDialog.Builder(mContext)
                            .setTitle("Warning")
                            .setMessage("You have already marked the interval between position " + (temp = (selectedPosition1<selectedPosition2)?selectedPosition1:selectedPosition2) + " and " + (temp = (selectedPosition1>selectedPosition2)?selectedPosition1:selectedPosition2))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();

                    }else{
                            mCheck.set(position, true);
                            checkCount++;
                            if(selectedPosition1 == -1)
                                selectedPosition1 = position;
                            else
                                selectedPosition2 = position;
                        }
                    }
                //選擇 > 未選擇
                else {
                    mCheck.set(position, false);
                    checkCount--;

                    if(selectedPosition1 == position)
                        selectedPosition1 = -1;
                    else
                        selectedPosition2 = -1;
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
//        TextView name;
        ImageView check;
        ImageView start;
        ImageView end;
        View label;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }


}
