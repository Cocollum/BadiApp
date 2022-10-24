package com.curtidosbadia.badiapp.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.curtidosbadia.badiapp.R;
import com.curtidosbadia.badiapp.databinding.RelatedProductFragmentBinding;
import com.curtidosbadia.badiapp.model.RelatedProduct;
import com.curtidosbadia.badiapp.viewmodels.HomeViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class RelatedProductFragment extends Fragment {
    public static RelatedProductFragment newInstance(String product){
        RelatedProductFragment frag = new RelatedProductFragment();

        Bundle args = new Bundle();

        args.putString("relatedProduct", product);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        Bundle args = getArguments();

        HomeViewModel viewModel = new HomeViewModel();
        RelatedProduct product = new RelatedProduct();

        try {
            product = new RelatedProduct(new JSONObject(args.getString("relatedProduct")));
        }catch(JSONException e){
            e.printStackTrace();
        }

        RelatedProductFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.related_product_fragment, container, false);
        View root = binding.getRoot();

        binding.setViewModel(viewModel);
        binding.setData(product);

        viewModel.didClickUrl.observe(getActivity(), (url) -> {
            if(!url.equals("")){
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        return root;
    }
}
