package com.marklordan.brewski;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adapter for the Recyclerview to store the beers.
 * holder.bind() does most of the heavy lifting setting the text of the TextViews

 */

public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.BeerViewHolder> {

    private Context mContext;
    private ArrayList<Beer> mBeerList;
    private BeerItemClickListener mListener;

    public BeerAdapter(ArrayList<Beer> beerDataSource, Context context, BeerItemClickListener clickListener){
        mBeerList = beerDataSource;
        mContext = context;
        mListener = clickListener;

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

    public interface BeerItemClickListener{
        void onItemClick(int itemClicked, ImageView beerImage, TextView beerTitle);
    }

    class BeerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView beerTitle, beerBrewery;
        private ImageView beerLabel;

        public BeerViewHolder(View itemView) {
            super(itemView);
            beerTitle = (TextView) itemView.findViewById(R.id.item_beer_title);
            beerBrewery = (TextView) itemView.findViewById(R.id.item_brewery_name);
            beerLabel = (ImageView) itemView.findViewById(R.id.item_beer_image);
            itemView.setOnClickListener(this);


        }

        public void bind(){
            Beer beerToBind = mBeerList.get(getAdapterPosition());
            beerTitle.setText(beerToBind.getBeerTitle());
            beerBrewery.setText(beerToBind.getBrewery().getBreweryName());
            Picasso.with(mContext).load(beerToBind.getBeerLabels().getmMediumLabel()).fit().into(beerLabel);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(getAdapterPosition(),
                    (ImageView) v.findViewById(R.id.item_beer_image),
                    (TextView) v.findViewById(R.id.item_beer_title));
        }
    }
}
