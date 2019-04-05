package com.astghik.newsolearn.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.astghik.newsolearn.R;
import com.astghik.newsolearn.activities.BaseActivity;
import com.astghik.newsolearn.activities.DetailActivity;
import com.astghik.newsolearn.activities.MainActivity;
import com.astghik.newsolearn.models.ItemModel;
import com.astghik.newsolearn.models.NewsModel;
import com.astghik.newsolearn.utils.Constants;
import com.astghik.newsolearn.utils.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;


public class AllNewsAdapter extends RecyclerView.Adapter<AllNewsAdapter.ViewHolder> {

    private BaseActivity mBaseActivity;
    private List<NewsModel> newsList;
    private boolean isGrid;

    public AllNewsAdapter(BaseActivity activity, ArrayList<NewsModel> newsList, boolean isGrid) {
        this.mBaseActivity = activity;
        this.newsList = newsList;
        this.isGrid = isGrid;
        Util.initImageLoader(mBaseActivity);
    }

    @Override
    public AllNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        if (isGrid) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_news_grid, viewGroup, false);
        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_news, viewGroup, false);

        }
        AllNewsAdapter.ViewHolder vh = new AllNewsAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final AllNewsAdapter.ViewHolder viewHolder, final int i) {
        if (getItemCount() > 0) {
            DisplayImageOptions mOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .displayer(new RoundedBitmapDisplayer(10)) //rounded corner bitmap
                    .showImageForEmptyUri(R.drawable.placeholder_news)
                    .showImageOnLoading(R.drawable.placeholder_news)
                    .showImageOnFail(R.drawable.placeholder_news)
                    .imageScaleType(ImageScaleType.NONE)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            String title = newsList.get(i).getField() != null ?
                    newsList.get(i).getField().getHeadline() : newsList.get(i).getHeadline();
            viewHolder.txtTitle.setText(title);
            String thumbnail = newsList.get(i).getField() != null ?
                    newsList.get(i).getField().getThumbnail() : newsList.get(i).getThumbnail();
            viewHolder.txtTitle.setText(title);

            if (!TextUtils.isEmpty(thumbnail)) {
                com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                        .displayImage(thumbnail,
                                viewHolder.image, mOptions);
            }
            viewHolder.txtCategory.setText(newsList.get(i).getNeewsCategory());
            viewHolder.contentItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mBaseActivity, DetailActivity.class);
                    intent.putExtra("model", (Parcelable) createShareItem(newsList.get(i)));
                    ((MainActivity) mBaseActivity).setSelecedItem(newsList.get(i));
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(mBaseActivity,
                                    viewHolder.image,
                                    ViewCompat.getTransitionName(viewHolder.image));
                    mBaseActivity.startActivityForResult(intent, Constants.LIST_UPDATED, options.toBundle());
                }
            });
        }

    }

    private ItemModel createShareItem(NewsModel model) {
        ItemModel itemModel = new ItemModel();
        itemModel.setHeadline(model.getField().getHeadline());
        itemModel.setThumbnail(model.getField().getThumbnail());
        itemModel.setNeewsID(model.getNeewsID());
        itemModel.setNeewsCategory(model.getNeewsCategory());
        itemModel.setPined(model.isPined());
        itemModel.setSaved(model.isSaved());

        return itemModel;
    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView contentItem;
        private TextView txtTitle;
        private TextView txtCategory;
        private ImageView image;

        public ViewHolder(@NonNull View v) {
            super(v);
            contentItem = v.findViewById(R.id.root_layout);
            txtTitle = v.findViewById(R.id.txt_title);
            txtCategory = v.findViewById(R.id.txt_category);
            image = v.findViewById(R.id.image);
        }

    }
}

