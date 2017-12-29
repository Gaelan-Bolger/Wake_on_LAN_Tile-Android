package com.gaelanbolger.woltile.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaelanbolger.woltile.R;
import com.gaelanbolger.woltile.adapter.OnItemClickListener;
import com.gaelanbolger.woltile.data.Host;
import com.gaelanbolger.woltile.qs.TileComponent;
import com.gaelanbolger.woltile.widget.TileView;

import java.util.List;

import static com.gaelanbolger.woltile.util.ResourceUtils.getDrawable;
import static com.gaelanbolger.woltile.util.ResourceUtils.getDrawableForName;

class TilesAdapter extends RecyclerView.Adapter<TilesAdapter.Holder> {

    private int itemHeight = 0;
    private Context context;
    private TileComponent[] tileComponents;
    private OnItemClickListener clickListener;
    private List<Host> hosts;

    TilesAdapter(Context context, TileComponent[] tileComponents, OnItemClickListener clickListener) {
        this.context = context;
        this.tileComponents = tileComponents;
        this.clickListener = clickListener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_tile, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        TileComponent tileComponent = getItem(position);
        Host host = getHost(tileComponent.ordinal());
        if (host == null) {
            holder.tileView.setTitle(tileComponent.getTitleResId());
            holder.tileView.setIcon(getDrawable(context, R.drawable.ic_block));
            holder.tileView.setEnabled(false);
        } else {
            holder.tileView.setTitle(host.getName());
            holder.tileView.setIcon(getDrawableForName(context, host.getIcon()));
            holder.tileView.setEnabled(true);
        }
        holder.itemView.getLayoutParams().height = itemHeight;
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null)
                clickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
        });
        holder.itemView.setOnLongClickListener(v -> clickListener != null
                && clickListener.onItemLongClick(holder.itemView, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return tileComponents.length;
    }

    TileComponent getItem(int position) {
        return tileComponents[position];
    }

    void setHosts(List<Host> hosts) {
        this.hosts = hosts;
        notifyDataSetChanged();
    }

    Host getHost(int hostId) {
        if (hosts != null)
            for (Host host : hosts) {
                if (host.getId() == hostId) {
                    return host;
                }
            }
        return null;
    }

    void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {

        private final TileView tileView;

        Holder(View itemView) {
            super(itemView);
            tileView = (TileView) itemView;
        }
    }
}
