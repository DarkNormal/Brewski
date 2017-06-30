package com.marklordan.brewski;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter for the Recyclerview to store the beers.
 * holder.bind() does most of the heavy lifting setting the text of the TextViews

 */

public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.BeerViewHolder> {

    private Context mContext;
    private ArrayList<Beer> mBeerList;

    public BeerAdapter(ArrayList<Beer> beerDataSource, Context context){
        mBeerList = beerDataSource;
        mContext = context;

    }

    @Override
    public BeerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.beer_list_item, parent, false);
        return new BeerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BeerViewHolder holder, int position) {
        holder.bind();

    }

    @Override
    public int getItemCount() {
        return mBeerList.size();
    }

    class BeerViewHolder extends RecyclerView.ViewHolder{
        private TextView beerTitle, beerBrewery;

        public BeerViewHolder(View itemView) {
            super(itemView);
            beerTitle = (TextView) itemView.findViewById(R.id.item_beer_title);
            beerBrewery = (TextView) itemView.findViewById(R.id.item_brewery_name);


        }

        public void bind(){
            beerTitle.setText(mBeerList.get(getAdapterPosition()).getBeerTitle());
            beerBrewery.setText(mBeerList.get(getAdapterPosition()).getBreweryName());
        }
    }
}
