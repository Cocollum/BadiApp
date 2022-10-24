package com.curtidosbadia.badiapp.viewmodels;

import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.curtidosbadia.badiapp.BR;
import com.curtidosbadia.badiapp.model.Post;

import java.util.ArrayList;

public class HomeViewModel extends BaseViewModel {

    private String username;
    private String password;
    private Post post;
    private Post event;
    private ArrayList<Fragment> related_products;

    private String app_splashscreen;

    public MutableLiveData<Boolean> didClickLogin = new MutableLiveData<>();
    public MutableLiveData<Boolean> didClickLogout = new MutableLiveData<>();
    public MutableLiveData<Post> didClickPost = new MutableLiveData<>();
    public MutableLiveData<String> didClickUrl = new MutableLiveData<>();
    public MutableLiveData<Boolean> didClickSeeNews = new MutableLiveData<>();
    public MutableLiveData<Boolean> didClickSeeEvents = new MutableLiveData<>();


    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
        notifyPropertyChanged(BR.post);
    }

    @Bindable
    public Post getEvent() {
        return event;
    }

    public void setEvent(Post event) {
        this.event = event;
        notifyPropertyChanged(BR.event);
    }

    @Bindable
    public ArrayList<Fragment> getRelated_products() {
        return related_products;
    }

    public void setRelated_products(ArrayList<Fragment> related_products) {
        this.related_products = related_products;
        notifyPropertyChanged(BR.related_products);
    }

    @Bindable
    public String getApp_splashscreen() {
        return app_splashscreen;
    }

    public void setApp_splashscreen(String app_splashscreen) {
        this.app_splashscreen = app_splashscreen;
        notifyPropertyChanged(BR.app_splashscreen);
    }

    public void onClickLogin(){
        didClickLogin.setValue(true);
    }
    public void onClickLogout(){
        didClickLogout.setValue(true);
    }
    public void onClickPost(Post url){
        didClickPost.setValue(url);
    }
    public void onClickPost(String url){
        didClickUrl.setValue(url);
    }
    public void onClickSeeNews(){
        didClickSeeNews.setValue(true);
    }
    public void onClickSeeEvents(){
        didClickSeeEvents.setValue(true);
    }
}
