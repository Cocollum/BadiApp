package com.curtidosbadia.badiapp.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.curtidosbadia.badiapp.R;
import com.curtidosbadia.badiapp.databinding.HomeFragmentBinding;
import com.curtidosbadia.badiapp.model.Post;
import com.curtidosbadia.badiapp.model.User;
import com.curtidosbadia.badiapp.utils.BadiaApi;
import com.curtidosbadia.badiapp.utils.BadiaWebApi;
import com.curtidosbadia.badiapp.utils.Session;
import com.curtidosbadia.badiapp.viewmodels.HomeViewModel;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HomeContentFragment extends Fragment {
    private static final String TEXT = "text";
    private Session session;
    User user;

    public static HomeContentFragment newInstance(String text){
        HomeContentFragment frag = new HomeContentFragment();

        Bundle args = new Bundle();
        args.putString(TEXT, text);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        HomeViewModel viewModel = new HomeViewModel();
        session = new Session(getContext());

        try {
            user = session.getUser();
            if(user != null) {
                viewModel.setUsername(user.getUser_name());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HomeFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        View layout = binding.getRoot();

        binding.setViewModel(viewModel);

        RequestParams paramspost = new RequestParams();

        BadiaWebApi.get("?action=getLastPost", paramspost, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d("getLastPost", response);
                try {
                    if(!response.equals("")){
                        JSONObject obj = new JSONObject(response);
                        Post post = new Post(obj);

                        viewModel.setPost(post);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = new String(responseBody);
                Log.d("getLastPost", response);
            }
        });

        BadiaApi.get("getAppSplashscreen", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d("getAppSplashscreen",response);
                viewModel.setApp_splashscreen(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        viewModel.didClickUrl.observe(getActivity(), (url) -> {
            if(!url.equals("")){
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        viewModel.didClickPost.observe(getActivity(), value -> {
            if(value != null){
                Intent intent = new Intent(getContext(), PostActivity.class);
                intent.putExtra("post", value.toJSON());
                viewModel.didClickPost.setValue(null);
                startActivity(intent);
            }
        });

        viewModel.didClickSeeNews.observe(getActivity(), value -> {
            if(value == true){
                Fragment fragment = PostContentFragment.newInstance("");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.home_content, fragment).commit();
            }
        });

        viewModel.didClickSeeEvents.observe(getActivity(), value -> {
            if(value == true){
                Fragment fragment = EventContentFragment.newInstance("");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.home_content, fragment).commit();
            }
        });

        return layout;
    }
}
