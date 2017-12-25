package com.gaelanbolger.woltile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaelanbolger.woltile.data.AppDatabase;
import com.gaelanbolger.woltile.data.Host;
import com.gaelanbolger.woltile.util.ResUtils;
import com.gaelanbolger.woltile.view.TileView;


public class MainFragment extends Fragment {

    private static final int[] TILE_IDS = {R.id.tile_1, R.id.tile_2, R.id.tile_3, R.id.tile_4,
            R.id.tile_5, R.id.tile_6, R.id.tile_7, R.id.tile_8, R.id.tile_9};

    private AppDatabase mDb;
    private TileView[] mTileViews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mTileViews = new TileView[TILE_IDS.length];
        for (int i = 0; i < TILE_IDS.length; i++) {
            TileView tile = view.findViewById(TILE_IDS[i]);
            Host host = mDb.hostDao().getById(i);
            if (host == null) host = new Host(i);
            tile.setTitle(host.getName());
            tile.setIconResource(ResUtils.getResourceId(getActivity(), host.getIcon(), "drawable"));
            mTileViews[i] = tile;
        }
        return view;
    }
}
