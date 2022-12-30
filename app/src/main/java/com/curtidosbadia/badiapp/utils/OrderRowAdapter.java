package com.curtidosbadia.badiapp.utils;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class OrderRowAdapter {
    @BindingAdapter(value = {"swipeEnabled", "drawableSwipeLeft", "drawableSwipeRight", "bgColorSwipeLeft", "bgColorSwipeRight", "onItemSwipeLeft", "onItemSwipeRight"}, requireAll = false)
    public static void setItemSwipeToRecyclerView(RecyclerView recyclerView, boolean swipeEnabled, Drawable drawableSwipeLeft, Drawable drawableSwipeRight, int bgColorSwipeLeft, int bgColorSwipeRight,
                                                  SwipeItemTouchHelperCallback.onItemSwipeLeft onItemSwipeLeft) {

        ItemTouchHelper.Callback swipeCallback = new SwipeItemTouchHelperCallback
                .Builder(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
                .bgColorSwipeLeft(bgColorSwipeLeft)
                .bgColorSwipeRight(bgColorSwipeRight)
                .drawableSwipeLeft(drawableSwipeLeft)
                .drawableSwipeRight(drawableSwipeRight)
                .setSwipeEnabled(swipeEnabled)
                .onItemSwipeLeftListener(onItemSwipeLeft)
                .build();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    public static class SwipeItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

        private onItemSwipeLeft listener;

        public SwipeItemTouchHelperCallback(int dragDirs, int swipeDirs, onItemSwipeLeft listener) {
            super(dragDirs, swipeDirs);
            this.listener = listener;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.LEFT) {
                listener.onSwipeLeft(viewHolder.getAdapterPosition());
            } else {
            }
        }

        public static interface onItemSwipeLeft {
            void onSwipeLeft(int position);
        }
    }
}
