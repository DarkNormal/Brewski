package com.marklordan.brewski;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BeerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BeerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BeerFragment extends Fragment implements BeerAdapter.BeerItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private ArrayList<Beer> beerList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private LinearLayout mNetworkErrorView;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private Toolbar toolbar;

    private BeerAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isListSelected = true;

    private OnFragmentInteractionListener mListener;
    BreweryService service;
    private Retrofit retrofit;

    public BeerFragment() {
        // Required empty public constructor
    }

    public static BeerFragment newInstance() {
        BeerFragment fragment = new BeerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(BreweryService.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_beer, container, false);



        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        gridLayoutManager = new GridLayoutManager(getContext(), calculateNoOfColumns(getContext()));
        //Recyclerview Setup
        recyclerView = (RecyclerView) v.findViewById(R.id.beer_recyclerview);
        if(isListSelected){

            adapter = new BeerAdapter(beerList, getContext(), this, true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);

            //Recyclerview divivders between items
            dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
        }else{

            adapter = new BeerAdapter(beerList, getContext(), this, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter);
        }

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshBeers();
            }
        });

        //These are for displaying the progressbar on load, or the error message on fail
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressbar_indicator);
        mNetworkErrorView = (LinearLayout) v.findViewById(R.id.network_error_view);

        //if there are no beers from savedInstanceState, get them from the API
        if(beerList.size() == 0){
            refreshBeers();
        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(isListSelected){
            item.setIcon(R.drawable.ic_grid_on);
            item.setTitle(getString(R.string.grid_options));
            isListSelected = false;
            adapter = new BeerAdapter(beerList, getContext(), this, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.removeItemDecoration(dividerItemDecoration);
            recyclerView.setAdapter(adapter);
        } else{
            item.setIcon(R.drawable.ic_format_list_bulleted);
            item.setTitle(getString(R.string.list_options));
            adapter = new BeerAdapter(beerList, getContext(), this, true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
            isListSelected = true;
        }

        return super.onOptionsItemSelected(item);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //Callback interface for use with Retrofit
    public interface BeerCallback{
        void onSuccess();
        void onError();
    }
    private void refreshBeers(){
        getBeers(new BeerCallback() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();
                //switch out the progressbar for the recyclerview
                mProgressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError() {
                //switch out the progressbar for the error view
                mProgressBar.setVisibility(View.INVISIBLE);
                mNetworkErrorView.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    public void getBeers(final BeerCallback callback){
        Call<JsonObject> beers = service.getBeers(BuildConfig.API_KEY);
        if(!mSwipeRefreshLayout.isRefreshing()) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mNetworkErrorView.setVisibility(View.INVISIBLE);

        beers.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray data = response.body().getAsJsonArray("data");
                beerList.clear();
                //Parse the JSON to POJOs and store in a list
                for (JsonElement element : data ) {
                    Beer beer = new Gson().fromJson(element, Beer.class);
                    beerList.add(beer);
                }
                callback.onSuccess();
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("MainActivity", "Error in getBeers", t);
                callback.onError();
            }
        });
    }

    //Callback for launching DetailsActivity when an item is clicked
    @Override
    public void onItemClick(int itemClicked, ImageView beerImage, TextView beerTitle) {
        Intent intent = DetailActivity.newInstance(getContext(), beerList.get(itemClicked));

        //Shared Transition pairs between both activities
        Pair<View, String> p1 = Pair.create((View) toolbar, ViewCompat.getTransitionName(toolbar));
        Pair<View, String> p2 = Pair.create((View) beerImage, ViewCompat.getTransitionName(beerImage));
        Pair<View, String> p3 = Pair.create((View) beerTitle, ViewCompat.getTransitionName(beerTitle));


        //Shared Element Transition
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2, p3);
        startActivity(intent, options.toBundle());
    }


    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}
