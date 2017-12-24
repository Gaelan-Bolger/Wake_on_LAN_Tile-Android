package com.gaelanbolger.woltile.discover;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.gaelanbolger.woltile.R;
import com.gaelanbolger.woltile.adapter.HostAdapter;
import com.gaelanbolger.woltile.adapter.OnItemClickListener;
import com.gaelanbolger.woltile.data.Host;


public class DiscoverDialog extends DialogFragment implements DiscoverTask.Callback, OnItemClickListener {

    private DiscoverTask mDiscoverTask;

    public interface Listener {

        void onHostSelected(Host host);
    }

    public static final String TAG = DiscoverDialog.class.getSimpleName();

    private AlertDialog.Builder mBuilder;
    private HostLoadingAdapter mHostsAdapter;
    private Listener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(getActivity(), R.layout.dialog_discover, null);

        mHostsAdapter = new HostLoadingAdapter(getActivity(), this);
        RecyclerView recyclerView = view.findViewById(R.id.rv_host);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mHostsAdapter);

        mBuilder = new AlertDialog.Builder(getActivity()).setTitle(R.string.discover).setView(view)
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = mBuilder.create();
        dialog.setOnShowListener(dialogInterface -> {
            mDiscoverTask = new DiscoverTask(getActivity(), DiscoverDialog.this);
            mDiscoverTask.execute();
        });
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener)
            mListener = (Listener) context;
        else
            throw new IllegalArgumentException("Caller must implement Listener");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mDiscoverTask != null) {
            mDiscoverTask.cancel(true);
        }
        mDiscoverTask = null;
        super.onDismiss(dialog);
    }

    @Override
    public void onNewHostDiscovered(Host host) {
        mHostsAdapter.addItem(host);
    }

    @Override
    public void onAllHostsDiscovered() {
        mHostsAdapter.setLoading(false);
    }

    @Override
    public void onItemClick(View view, int position) {
        Host host = mHostsAdapter.getItem(position);
        mListener.onHostSelected(host);
        dismiss();
    }

    class HostLoadingAdapter extends HostAdapter {

        private boolean loading = true;

        HostLoadingAdapter(Context context, @Nullable OnItemClickListener clickListener) {
            super(context, clickListener);
        }

        @Override
        public HostAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return viewType == 0 ? super.onCreateViewHolder(parent, viewType) :
                    new LoadingHolder(inflater.inflate(R.layout.item_loading, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            if (getItemViewType(position) == 0)
                super.onBindViewHolder(holder, position);
        }

        @Override
        public int getItemViewType(int position) {
            return loading && position == getItemCount() - 1 ? 1 : 0;
        }

        @Override
        public int getItemCount() {
            return (loading ? 1 : 0) + super.getItemCount();
        }

        void setLoading(boolean loading) {
            this.loading = loading;
            notifyDataSetChanged();
        }

        class LoadingHolder extends Holder {

            LoadingHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
