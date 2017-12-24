package com.gaelanbolger.woltile.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaelanbolger.woltile.data.Host;

import java.util.ArrayList;
import java.util.List;

public class HostAdapter extends RecyclerView.Adapter<HostAdapter.Holder> {

    protected LayoutInflater inflater;
    private List<Host> hosts;
    private OnItemClickListener clickListener;

    public HostAdapter(Context context, @Nullable OnItemClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(android.R.layout.simple_list_item_2, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Host host = getItem(position);
        holder.text1.setText(host.getName());
        holder.text2.setText(host.getAddress());
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

    public void addItem(Host host) {
        if (hosts == null) hosts = new ArrayList<>();
        hosts.add(host);
        notifyItemInserted(getItemCount() - 1);
    }

    public Host getItem(int position) {
        return hosts.get(position);
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView text1;
        private TextView text2;

        public Holder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
