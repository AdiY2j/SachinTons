package com.adityay.sachintons.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adityay.sachintons.R;
import com.adityay.sachintons.customviews.CustomImageView;
import com.adityay.sachintons.models.Century;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CenturyAdapter extends RecyclerView.Adapter<CenturyAdapter.CenturyViewHolder> {

    private Context context;
    private List<Century> centuryList;
    private MatchSelectListener listener;

    public CenturyAdapter(Context context, List<Century> centuryList, MatchSelectListener listener) {
        this.context = context;
        this.centuryList = centuryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CenturyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_century, parent, false);
        return new CenturyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CenturyViewHolder holder, int position) {
        Century century = centuryList.get(position);
        if(!TextUtils.isEmpty(century.getDescription())) {
            holder.tvDetail.setText(century.getDescription());
        }
        if(!TextUtils.isEmpty(century.getImageUrl())){
            holder.ivPic.setImageWithGlide(context, century.getImageUrl(), R.drawable.placeholder);
        }

        String sign = century.getNotOut() == Boolean.TRUE ? "*" : "";
        String title = "Century No. "+ (position+1) + " : " + century.getScore() + sign + " vs " + century.getCountry();
        holder.tvTitle.setText(title);
        century.setTitle(title);

        holder.itemView.setOnClickListener(view -> listener.onMatchSelect(century));
    }

    @Override
    public int getItemCount() {
        if(centuryList == null || centuryList.isEmpty()){
            return 0;
        }
        return centuryList.size();
    }

    public class CenturyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_pic)
        CustomImageView ivPic;
        @BindView(R.id.tv_detail)
        TextView tvDetail;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public CenturyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface MatchSelectListener{
        void onMatchSelect(Century century);
    }
}
