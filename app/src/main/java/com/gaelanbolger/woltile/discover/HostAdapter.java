package com.gaelanbolger.woltile.discover;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaelanbolger.woltile.R;
import com.gaelanbolger.woltile.adapter.OnItemClickListener;
import com.gaelanbolger.woltile.data.Host;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class HostAdapter extends RecyclerView.Adapter<HostAdapter.Holder> {

    private LayoutInflater inflater;
    private OnItemClickListener clickListener;
    private List<Host> hosts;

    public HostAdapter(Context context, @Nullable OnItemClickListener clickListener) {
        this(context, null, clickListener);
    }

    public HostAdapter(Context context, List<Host> hosts, @Nullable OnItemClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.hosts = hosts;
        this.clickListener = clickListener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_host, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Host host = getItem(position);
        holder.text1.setText(host.getName());
        holder.text2.setText(host.getIp());
        holder.itemView.setOnClickListener(view -> {
            if (clickListener != null) clickListener.onItemClick(view, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return hosts != null ? hosts.size() : 0;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
        notifyDataSetChanged();
    }

    public Host getItem(int position) {
        if (getItemCount() > position) {
            return hosts.get(position);
        }
        return null;
    }

    public Host removeItem(int position) {
        if (getItemCount() > position) {
            Host host = hosts.remove(position);
            notifyItemRemoved(position);
            return host;
        }
        return null;
    }

    public void insertItem(Host host) {
        if (hosts == null)
            hosts = new ArrayList<>();
        hosts.add(host);
        notifyItemInserted(getItemCount() - 1);
    }

    public void moveItem(int from, int to) {
        if (getItemCount() > from && getItemCount() > to) {
            Host host = hosts.remove(from);
            hosts.add(to, host);
            notifyItemMoved(from, to);
        }
    }

    protected class Holder extends RecyclerView.ViewHolder {

        private TextView text1;
        private TextView text2;

        Holder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
