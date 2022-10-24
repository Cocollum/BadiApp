package com.curtidosbadia.badiapp.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.adapters.ListenerUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.transition.TransitionManager;

import com.curtidosbadia.badiapp.R;

public class ListBindingAdapters <T>{
    public static final String TAG = "ListBindingAdapters";

    private List<T> entries;

    private ListBindingAdapters() {}

    @BindingAdapter({"entries", "layout", "viewModel"})
    public static <T> void setEntries(ViewGroup viewGroup,
                                      List<T> oldEntries, int oldLayoutId, BaseObservable oldViewModel,
                                      List<T> newEntries, int newLayoutId, BaseObservable newViewModel) {
        /*if (oldEntries == newEntries && oldLayoutId == newLayoutId) {
            return; // nothing has changed
        }*/
        EntryChangeListener listener =
                ListenerUtil.getListener(viewGroup, R.id.entryListener);
        if (oldEntries != newEntries && listener != null && oldEntries instanceof ObservableList) {
            ((ObservableList)oldEntries).removeOnListChangedCallback(listener);
        }

        if (newEntries == null) {
            viewGroup.removeAllViews();
        } else {
            if (newEntries instanceof ObservableList) {
                if (listener == null) {
                    listener =
                            new EntryChangeListener(viewGroup, newLayoutId, newViewModel);
                    ListenerUtil.trackListener(viewGroup, listener,
                            R.id.entryListener);
                } else {
                    listener.setLayoutId(newLayoutId);
                }
                if (newEntries != oldEntries) {
                    ((ObservableList)newEntries).addOnListChangedCallback(listener);
                }
            }
            resetViews(viewGroup, newLayoutId, newEntries, newViewModel);
        }
    }

    private static ViewDataBinding bindLayout(LayoutInflater inflater,
                                              ViewGroup parent, int layoutId, Object entry, BaseObservable viewModel, Integer index) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater,
                layoutId, parent, false);
        if (!binding.setVariable(BR.viewModel, viewModel)) {
            String layoutName = parent.getResources().getResourceEntryName(layoutId);
            Log.w(TAG, "There is no variable 'viewModel' in layout " + layoutName);
        }
        if (!binding.setVariable(BR.data, entry)) {
            String layoutName = parent.getResources().getResourceEntryName(layoutId);
            Log.w(TAG, "There is no variable 'data' in layout " + layoutName);
        }
        if (!binding.setVariable(BR.index, index)) {
            String layoutName = parent.getResources().getResourceEntryName(layoutId);
            Log.w(TAG, "There is no variable 'index' in layout " + layoutName);
        }

        return binding;
    }

    private static void resetViews(ViewGroup parent, int layoutId,
                                   List entries, BaseObservable viewModel) {
        parent.removeAllViews();
        if (layoutId == 0) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < entries.size(); i++) {
            Object entry = entries.get(i);
            ViewDataBinding binding = bindLayout(inflater, parent,
                    layoutId, entry, viewModel, i);
            parent.addView(binding.getRoot());
        }
    }

    private static void startTransition(ViewGroup root) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(root);
        }
    }

    private static class EntryChangeListener
            extends ObservableList.OnListChangedCallback {
        private final ViewGroup mTarget;
        private int mLayoutId;
        private BaseObservable mViewModel;

        public EntryChangeListener(ViewGroup target, int layoutId, BaseObservable viewModel) {
            mTarget = target;
            mLayoutId = layoutId;
            mViewModel = viewModel;
        }

        public void setLayoutId(int layoutId) {
            mLayoutId = layoutId;
        }

        @Override
        public void onChanged(ObservableList observableList) {
            resetViews(mTarget, mLayoutId, observableList, mViewModel);
        }

        @Override
        public void onItemRangeChanged(ObservableList observableList,
                                       int start, int count) {
            if (mLayoutId == 0) {
                return;
            }
            LayoutInflater inflater = (LayoutInflater) mTarget.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            startTransition(mTarget);
            final int end = start + count;
            for (int i = start; i < end; i++) {
                Object data = observableList.get(i);
                ViewDataBinding binding = bindLayout(inflater,
                        mTarget, mLayoutId, data, mViewModel, i);
                binding.setVariable(BR.data, observableList.get(i));
                mTarget.removeViewAt(i);
                mTarget.addView(binding.getRoot(), i);
            }
        }

        @Override
        public void onItemRangeInserted(ObservableList observableList,
                                        int start, int count) {
            if (mLayoutId == 0) {
                return;
            }
            startTransition(mTarget);
            final int end = start + count;
            LayoutInflater inflater = (LayoutInflater) mTarget.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = end - 1; i >= start; i--) {
                Object entry = observableList.get(i);
                ViewDataBinding binding =
                        bindLayout(inflater, mTarget, mLayoutId, entry, mViewModel, i);
                mTarget.addView(binding.getRoot(), start);
            }
        }

        @Override
        public void onItemRangeMoved(ObservableList observableList,
                                     int from, int to, int count) {
            if (mLayoutId == 0) {
                return;
            }
            startTransition(mTarget);
            for (int i = 0; i < count; i++) {
                View view = mTarget.getChildAt(from);
                mTarget.removeViewAt(from);
                int destination = (from > to) ? to + i : to;
                mTarget.addView(view, destination);
            }
        }

        @Override
        public void onItemRangeRemoved(ObservableList observableList,
                                       int start, int count) {
            if (mLayoutId == 0) {
                return;
            }
            startTransition(mTarget);
            for (int i = 0; i < count; i++) {
                mTarget.removeViewAt(start);
            }
        }
    }
}
