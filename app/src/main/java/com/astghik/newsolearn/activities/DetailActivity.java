package com.astghik.newsolearn.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.astghik.newsolearn.R;
import com.astghik.newsolearn.models.ItemModel;
import com.astghik.newsolearn.utils.Constants;
import com.astghik.newsolearn.utils.SharedPreferencesData;
import com.astghik.newsolearn.utils.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class DetailActivity extends BaseActivity implements View.OnClickListener {

    private ItemModel model;

    private ImageButton btnSave;
    private ImageButton btnPin;

    private boolean stateChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Util.initImageLoader(this);
        initView();
    }

    private void initView() {
        model = getIntent().getParcelableExtra("model");
        assert model != null;
        ((TextView) findViewById(R.id.txt_title)).setText(model.getHeadline());
        ((TextView) findViewById(R.id.txt_category)).setText(model.getNeewsCategory());

        ImageView image = findViewById(R.id.image_detail);
        btnSave = findViewById(R.id.btn_save);
        btnPin = findViewById(R.id.btn_pin);
        btnSave.setOnClickListener(this);
        btnPin.setOnClickListener(this);
        if (!Util.isNetworkAvailable(this, findViewById(R.id.root_layout))) {
            btnSave.setVisibility(View.GONE);
        }
        btnPin.setBackground(getDrawable(model.isPined() ? R.drawable.btn_pin : R.drawable.btn_inactive_pin));
        btnSave.setBackground(getDrawable(model.isSaved() ? R.drawable.btn_save : R.drawable.btn_inactive_save));

        DisplayImageOptions mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(10)) //rounded corner bitmap
                .showImageForEmptyUri(R.drawable.placeholder_news)
                .showImageOnLoading(R.drawable.placeholder_news)
                .showImageOnFail(R.drawable.placeholder_news)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        if (!TextUtils.isEmpty(model.getThumbnail())) {
            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(model.getThumbnail(),
                            image, mOptions);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pin:
                stateChange = true;
                model.setPined(!model.isPined());
                btnPin.setBackground(getDrawable(model.isPined() ? R.drawable.btn_pin : R.drawable.btn_inactive_pin));
                if (model.isPined()) {
                    SharedPreferencesData.getInstance().addForOfflieUse(this, model);
                } else if (!model.isSaved()) {
                    SharedPreferencesData.getInstance().removeFromOfflineUse(this, model);
                }
                break;

            case R.id.btn_save:
                model.setSaved(!model.isSaved());
                btnSave.setBackground(getDrawable(model.isSaved() ? R.drawable.btn_save : R.drawable.btn_inactive_save));
                if (model.isSaved()) {
                    SharedPreferencesData.getInstance().addForOfflieUse(this, model);
                } else {
                    SharedPreferencesData.getInstance().removeFromOfflineUse(this, model);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        if (stateChange) {
            returnIntent.putExtra(Constants.UPDATE_PIN_LIST, model.isPined());
            setResult(Activity.RESULT_OK, returnIntent);
        }
        finish();
    }

}
