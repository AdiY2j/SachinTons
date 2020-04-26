package com.adityay.sachintons.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adityay.sachintons.R;
import com.adityay.sachintons.activities.MainActivity;
import com.adityay.sachintons.adapters.CenturyAdapter;
import com.adityay.sachintons.customviews.CustomImageView;
import com.adityay.sachintons.interfaces.ApiEndPoints;
import com.adityay.sachintons.models.Century;
import com.adityay.sachintons.utils.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CenturyFragment extends Fragment implements CenturyAdapter.MatchSelectListener {

    @BindView(R.id.rv_century)
    RecyclerView rvCentury;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.error_view)
    ViewGroup errorView;
    @BindView(R.id.iv_error)
    CustomImageView ivError;

    private List<Century> centuryList = new ArrayList<>();
    private CenturyAdapter centuryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_century, container, false);
        ButterKnife.bind(this, view);
        setupAdapter();
        return view;
    }

    private void setupAdapter() {
        centuryAdapter = new CenturyAdapter(getActivity(), centuryList, this);
        rvCentury.setAdapter(centuryAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(centuryList.isEmpty()) {
            hideErrorLayout();
            progressBar.setVisibility(View.VISIBLE);
            getCenturyList();
        }
    }


    private void getCenturyList() {
        ApiEndPoints api = RetrofitInstance.getRetrofitInstance(getString(R.string.century_api)).create(ApiEndPoints.class);
        Call<List<Century>> callGetCentury = api.getCenturyList();
        callGetCentury.enqueue(new Callback<List<Century>>() {
            @Override
            public void onResponse(Call<List<Century>> call, Response<List<Century>> response) {
                if(response != null && response.isSuccessful() && response.body() != null && !response.body().isEmpty()){
                    centuryList.clear();
                    centuryList.addAll(response.body());
                    centuryAdapter.notifyDataSetChanged();
                }else{
                    showErrorLayout();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Century>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showErrorLayout();
            }
        });
    }

    private void showErrorLayout() {
        errorView.setVisibility(View.VISIBLE);
        ivError.setGifImageWithGlide(getActivity(), R.raw.something_went_wrong);
        rvCentury.setVisibility(View.GONE);
    }

    private void hideErrorLayout(){
        errorView.setVisibility(View.GONE);
        rvCentury.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMatchSelect(Century century) {
        MatchFragment matchFragment = new MatchFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", century);
        matchFragment.setArguments(bundle);

        if (getParentFragment() != null) {
            getParentFragment().getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, matchFragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    @OnClick(R.id.btn_retry)
    void retryApi(){
        hideErrorLayout();
        progressBar.setVisibility(View.VISIBLE);
        getCenturyList();
    }
}
