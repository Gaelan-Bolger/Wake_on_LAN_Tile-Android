package com.gaelanbolger.woltile.discover;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.gaelanbolger.woltile.R;
import com.gaelanbolger.woltile.adapter.OnItemClickListener;
import com.gaelanbolger.woltile.data.Host;

public class DiscoverFragment extends Fragment implements OnItemClickListener {

    public interface Listener {
        void onHostSelected(Host host);
    }

    public static final String TAG = DiscoverFragment.class.getSimpleName();

    private DiscoverViewModel mModel;
    private HostAdapter mHostsAdapter;
    private MenuItem mLoadingItem;
    private Listener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mModel = ViewModelProviders.of(this).get(DiscoverViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        RecyclerView rv = view.findViewById(R.id.rv_host);
        rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(mHostsAdapter = new HostAdapter(getActivity(), this));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mModel.getData().observe(this, data -> {
            if (data != null) {
                mHostsAdapter.setHosts(data.getHosts());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_discover, menu);
        mLoadingItem = menu.findItem(R.id.item_loading);
        mModel.getData().observe(this, data -> {
            if (data != null) {
                mLoadingItem.setVisible(!data.isLoaded());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener)
            mListener = (Listener) context;
        else
            throw new IllegalArgumentException("Activity must implement DiscoverFragment.Listener");
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onItemClick(View view, int position) {
        Host host = mHostsAdapter.getItem(position);
        mListener.onHostSelected(host);
    }
}
