package com.mittal.nitin.jsonparsingdemo.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mittal.nitin.jsonparsingdemo.Helper.ItemTouchHelperAdapter;
import com.mittal.nitin.jsonparsingdemo.Helper.ItemTouchHelperViewHolder;
import com.mittal.nitin.jsonparsingdemo.Helper.OnStartDragListener;
import com.mittal.nitin.jsonparsingdemo.R;
import com.mittal.nitin.jsonparsingdemo.models.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by HP on 18-04-2016.
 */
public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.CustomViewHolder>
        implements ItemTouchHelperAdapter
{



    public class CustomViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{
        protected ImageView movieImage;
        protected TextView txtMovieName;
        protected TextView txtYear,txtStory;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.movieImage=(ImageView)itemView.findViewById(R.id.movieImage);
            this.txtMovieName=(TextView)itemView.findViewById(R.id.txtMovieName);
            this.txtStory=(TextView)itemView.findViewById(R.id.txtStory);
            this.txtYear=(TextView)itemView.findViewById(R.id.txtYear);
        }

        @Override
        public void onItemSelected()
        {
            YoYo.with(Techniques.Pulse).duration(700).playOn(itemView);
            itemView.setBackgroundColor(Color.argb(255,219,219,219));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.argb(255,255,255,255));
        }
    }

    private List<MovieModel> movieModelList;
    private Context context;
    private final OnStartDragListener mDragStartListener;

    public MovieRecyclerAdapter(Context context,List<MovieModel> movieModelList,OnStartDragListener mDragStartListener)
    {
        this.movieModelList=movieModelList;
        this.context=context;
        this.mDragStartListener=mDragStartListener;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

        movieModelList.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public MovieRecyclerAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        CustomViewHolder customViewHolder=new CustomViewHolder(view);
        return  customViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieRecyclerAdapter.CustomViewHolder holder, int position) {

        MovieModel movieModel=movieModelList.get(position);
        Picasso.with(context).load(movieModel.getImage()).
                error(R.drawable.starwars).fit().into(holder.movieImage);
        holder.txtMovieName.setText(movieModel.getMovie().toString());
        holder.txtYear.setText(new Integer(movieModel.getYear()).toString());
        String story=movieModel.getStory().toString();
        int numberchar= holder.txtStory.getWidth();
        if(story.length()>=numberchar)
        {
            Log.d("Adapter numberchar= ",new Integer(numberchar).toString());
            //story=story.substring(0,numberchar-10);
            story=story+ "......" ;
        }
        holder.txtStory.setText(story);



    }

    @Override
    public int getItemCount() {
        return (null!=movieModelList ?movieModelList.size():0);
    }


}
