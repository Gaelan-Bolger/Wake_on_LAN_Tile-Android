package com.gaelanbolger.woltile.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gaelanbolger.woltile.R;

public class TileIconAdapter extends RecyclerView.Adapter<TileIconAdapter.Holder> {

    private final LayoutInflater inflater;
    private final ColorStateList selectedColor;
    private final ColorStateList unselectedColor;
    private final OnItemClickListener clickListener;

    private String[] items;
    private String selectedItem;

    public TileIconAdapter(Context context, String[] items, String selectedItem) {
        this(context, items, selectedItem, null);
    }

    public TileIconAdapter(Context context, String[] items, String selectedItem, @Nullable OnItemClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.selectedColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorAccent));
        this.unselectedColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary));
        this.items = items;
        this.selectedItem = selectedItem;
        this.clickListener = clickListener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_tile_icon, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String item = getItem(position);
        ImageViewCompat.setImageTintList(holder.icon, TextUtils.equals(selectedItem, item) ? selectedColor : unselectedColor);

        Resources res = holder.itemView.getResources();
        int resId = res.getIdentifier(item, "drawable", holder.itemView.getContext().getPackageName());
        holder.icon.setImageResource(resId);
        holder.icon.setOnClickListener(view -> {
            int adapterPosition = holder.getAdapterPosition();
            selectedItem = getItem(adapterPosition);
            notifyDataSetChanged();
            if (clickListener != null)
                clickListener.onItemClick(view, adapterPosition);
        });
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public String getItem(int position) {
        return items[position];
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    class Holder extends RecyclerView.ViewHolder {

        private final ImageView icon;

        Holder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iv_tile_icon);
        }
    }
}
