package com.curtidosbadia.badiapp.viewmodels;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import com.curtidosbadia.badiapp.BR;
import com.curtidosbadia.badiapp.model.Post;

public class PostViewModel extends BaseObservable {
    private ArrayList<Post> post_list = new ArrayList<>();
    private Post post = new Post();
    public MutableLiveData<Post> didClickPost = new MutableLiveData<>();
    public MutableLiveData<Boolean> didClickPrevious = new MutableLiveData<>();

    public PostViewModel(){

    }

    @Bindable
    public ArrayList<Post> getPost_list() {
        return post_list;
    }

    public void setPost_list(ArrayList<Post> post_list) {
        this.post_list = post_list;
        notifyPropertyChanged(BR.post_list);
    }

    @Bindable
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
        notifyPropertyChanged(BR.post);
    }

    public void onClickPost(View view, Post data){ this.didClickPost.setValue(data); }
    public void onClickPrevious(){ this.didClickPrevious.setValue(true); }
}
