package com.curtidosbadia.badiapp.viewmodels;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.curtidosbadia.badiapp.utils.DownloadImageTask;
import com.curtidosbadia.badiapp.utils.PageViewerAdapter;

import java.util.ArrayList;

public class BaseViewModel extends BaseObservable {
    private static FragmentManager fm;

    public BaseViewModel(){}

    public void setFragmentAdapter(FragmentManager fm){ this.fm = fm; }

    @BindingAdapter("fragment_adapter")
    public static void setAdapter(ViewPager view, ArrayList<Fragment> fragments){
        PageViewerAdapter adapter = new PageViewerAdapter(fm);
        adapter.setItems(fragments);
        view.setId(View.generateViewId());
        view.setAdapter(adapter);
    }

    @BindingAdapter("pager")
    public static void bindViewPagerTabs(final com.google.android.material.tabs.TabLayout view, androidx.viewpager.widget.ViewPager pagerView){
        view.setupWithViewPager(pagerView, true);
    }

    @BindingAdapter("url")
    public static void setImageToImageView(ImageView view, String url){
        new DownloadImageTask(view).execute(url);
    }

    @BindingAdapter("html")
    public static void setHtml(TextView view, String html){
        view.setText(Html.fromHtml(html));
    }
}
