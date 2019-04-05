package com.astghik.newsolearn.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.astghik.newsolearn.R;
import com.astghik.newsolearn.adapters.AllNewsAdapter;
import com.astghik.newsolearn.adapters.OfflineNewsAdapter;
import com.astghik.newsolearn.listeners.NetworkRequestListener;
import com.astghik.newsolearn.models.FieldModel;
import com.astghik.newsolearn.models.ItemModel;
import com.astghik.newsolearn.models.NewsModel;
import com.astghik.newsolearn.utils.Constants;
import com.astghik.newsolearn.utils.SharedPreferencesData;
import com.astghik.newsolearn.utils.Util;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends BaseActivity implements NetworkRequestListener, View.OnClickListener {

    private AllNewsAdapter allNewsAdapter;
    private AllNewsAdapter gridAdapter;
    private AllNewsAdapter pinNewsAdapter;

    private ArrayList<NewsModel> allNewsArrayList;
    private ArrayList<NewsModel> pinNewsArrayList;

    private RecyclerView allNewsRecylerView;
    private RecyclerView pinNewsRecylerView;

    private LinearLayoutManager allNewsLayoutManager;
    private GridLayoutManager gridLayoutManager;

    private NewsModel selecedItem;

    private boolean loading = true;
    private boolean isFirst = true;
    private boolean isGrid = false;
    private int pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;
    private int count = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (Util.isNetworkAvailable(this, findViewById(R.id.root_layout))) {
            getNetworkServiceManager().getNews(this, String.valueOf(count));
            addItem();
            checkNewItem();
        } else if (SharedPreferencesData.getInstance().getOfflineNews(this) != null) {
            setOfflineNewsListUi();
        }
        updatePinedItemsList();
    }

    private void initView() {
        ImageButton imgList = findViewById(R.id.img_lits);
        ImageButton imgGrid = findViewById(R.id.img_grid);
        ImageView imgNewest = findViewById(R.id.img_newst);

        allNewsRecylerView = findViewById(R.id.list);
        pinNewsRecylerView = findViewById(R.id.list_pined);

        allNewsArrayList = new ArrayList<>();
        pinNewsArrayList = new ArrayList<>();
        selecedItem = new NewsModel();

        imgGrid.setOnClickListener(this);
        imgList.setOnClickListener(this);
        imgNewest.setOnClickListener(this);
        imgList.setBackgroundColor(getResources().getColor(R.color.colorCategory));
        imgGrid.setBackgroundColor(android.R.drawable.btn_default);


        ArrayList<ItemModel> offlineItemsList = SharedPreferencesData.getInstance().getOfflineNews(this);

        if (offlineItemsList != null) {
            for (int i = 0; i < offlineItemsList.size(); i++) {
                if (offlineItemsList.get(i).isPined()) {
                    NewsModel model = new NewsModel();
                    model.setNeewsID(offlineItemsList.get(i).getNeewsID());
                    model.setThumbnail(offlineItemsList.get(i).getThumbnail());
                    model.setHeadline(offlineItemsList.get(i).getHeadline());
                    model.setNeewsCategory(offlineItemsList.get(i).getNeewsCategory());
                    model.setPined(true);
                    model.setSaved(offlineItemsList.get(i).isSaved());
                    model.setField(new FieldModel(offlineItemsList.get(i).getHeadline(), offlineItemsList.get(i).getThumbnail()));
                    pinNewsArrayList.add(model);
                }
            }
        }

    }

    private void checkNewItem() {
        if (Util.isNetworkAvailable(this, findViewById(R.id.root_layout))) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getNetworkServiceManager().getNews(new NetworkRequestListener() {
                        @Override
                        public void onResponseReceive(Object obj) {
                            ArrayList<NewsModel> newestList = (ArrayList<NewsModel>) obj;
                            ArrayList<NewsModel> newesNewstList = new ArrayList<>();

                            for (int i = 0; i < newestList.size(); i++) {
                                if (!newestList.get(i).getNeewsID().equals(allNewsArrayList.get(0).getNeewsID())) {
                                    newesNewstList.add(newestList.get(i));
                                } else {
                                    break;
                                }
                            }
                            if (newesNewstList.size() > 0) {
                                Log.d("new :", newesNewstList.get(0).getNeewsID());
                                allNewsArrayList.addAll(0, newesNewstList);
                                findViewById(R.id.img_newst).setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onError(String message) {

                        }
                    }, "1");
                    handler.postDelayed(this, Constants.RESQUEST_PERIOD);
                }
            }, Constants.RESQUEST_PERIOD);
        }
    }

    private void addItem() {
        allNewsRecylerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {//check for scroll down
                    visibleItemCount = allNewsLayoutManager.getChildCount();
                    totalItemCount = allNewsLayoutManager.getItemCount();
                    if (isGrid) {
                        pastVisiblesItems = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                    } else {
                        pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    }
                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            getNetworkServiceManager().getNews(MainActivity.this, String.valueOf(++count));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResponseReceive(Object obj) {
        ArrayList<NewsModel> newList = (ArrayList<NewsModel>) obj;
        if (((ArrayList<NewsModel>) obj).size() > 0) {
            for (int i = 0; i < pinNewsArrayList.size(); i++) {
                for (int j = 0; j < newList.size(); j++) {
                    if (pinNewsArrayList.get(i).getNeewsID().equals(newList.get(j).getNeewsID())) {
                        newList.get(j).setPined(true);
                    }
                }
            }
            loading = true;
            allNewsArrayList.addAll((Collection<? extends NewsModel>) obj);
            setAllNewsListUi();
        } else {
            loading = false;
        }
    }

    private void setAllNewsListUi() {

        if (isFirst) {
            isFirst = false;
            allNewsAdapter = new AllNewsAdapter(this, allNewsArrayList, false);
            allNewsRecylerView.setAdapter(allNewsAdapter);
            allNewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            allNewsRecylerView.setLayoutManager(allNewsLayoutManager);
        } else {
            allNewsAdapter.notifyDataSetChanged();
        }
    }

    private void setOfflineNewsListUi() {
        ArrayList<ItemModel> offlineItemsList = SharedPreferencesData.getInstance().getOfflineNews(this);
        if (isGrid) {
            OfflineNewsAdapter gridAdapter = new OfflineNewsAdapter(this, offlineItemsList, true);
            allNewsRecylerView.setAdapter(gridAdapter);
            gridLayoutManager = new GridLayoutManager(this, 2);
            allNewsRecylerView.setLayoutManager(gridLayoutManager);
        } else {
            OfflineNewsAdapter allNewsAdapter = new OfflineNewsAdapter(this, offlineItemsList, isGrid);
            allNewsRecylerView.setAdapter(allNewsAdapter);
            allNewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            allNewsRecylerView.setLayoutManager(allNewsLayoutManager);
        }
    }

    public void setSelecedItem(NewsModel selecedItem) {
        this.selecedItem = selecedItem;
    }

    private void setListUi(boolean b) {
        isGrid = b;
        if (Util.isNetworkAvailable(this, findViewById(R.id.root_layout))) {
            if (b) {
                gridAdapter = new AllNewsAdapter(this, allNewsArrayList, true);
                allNewsRecylerView.setAdapter(gridAdapter);
                gridLayoutManager = new GridLayoutManager(this, 2);
                allNewsRecylerView.setLayoutManager(gridLayoutManager);
            } else {
                allNewsAdapter = new AllNewsAdapter(this, allNewsArrayList, false);
                allNewsRecylerView.setAdapter(allNewsAdapter);
                allNewsRecylerView.setLayoutManager(allNewsLayoutManager);
            }
        } else {
            setOfflineNewsListUi();
        }
    }

    private void updatePinedItemsList() {

        pinNewsAdapter = new AllNewsAdapter(this, pinNewsArrayList, false);
        pinNewsRecylerView.setAdapter(pinNewsAdapter);
        LinearLayoutManager pinNewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        pinNewsRecylerView.setLayoutManager(pinNewsLayoutManager);
        if (pinNewsArrayList.size() > 0) {
            findViewById(R.id.txt_pined).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.txt_pined).setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_grid:
                if (!isGrid) {
                    view.setBackgroundColor(getResources().getColor(R.color.colorCategory));
                    findViewById(R.id.img_lits).setBackgroundColor(android.R.drawable.btn_default);
                    setListUi(true);
                }
                break;
            case R.id.img_lits:
                if (isGrid) {
                    findViewById(R.id.img_grid).setBackgroundColor(android.R.drawable.btn_default);
                    view.setBackgroundColor(getResources().getColor(R.color.colorCategory));
                    setListUi(false);

                }
                break;
            case R.id.img_newst:
                allNewsLayoutManager.scrollToPosition(0);
                view.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.LIST_UPDATED) {
            if (resultCode == Activity.RESULT_OK) {
                boolean result = data.getBooleanExtra(Constants.UPDATE_PIN_LIST, false);
                if (result) {
                    selecedItem.setPined(true);
                    pinNewsArrayList.add(0, selecedItem);
                } else {
                    selecedItem.setPined(false);
                    for (int i = 0; i < pinNewsArrayList.size(); i++) {
                        if (pinNewsArrayList.get(i).getNeewsID().equals(selecedItem.getNeewsID())) {
                            pinNewsArrayList.remove(i);
                        }
                    }
                }
                updatePinedItemsList();
            }
        }
    }

}
