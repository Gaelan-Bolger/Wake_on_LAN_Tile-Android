package com.gaelanbolger.woltile;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.IconPopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaelanbolger.woltile.data.AppDatabase;
import com.gaelanbolger.woltile.data.Host;
import com.gaelanbolger.woltile.dialog.ConfirmActionDialog;
import com.gaelanbolger.woltile.qs.TileComponent;
import com.gaelanbolger.woltile.qs.WakeOnLanTask;
import com.gaelanbolger.woltile.settings.AppSettings;
import com.gaelanbolger.woltile.util.ResourceUtils;
import com.gaelanbolger.woltile.view.TileView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindViews;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static butterknife.ButterKnife.bind;
import static com.gaelanbolger.woltile.util.PackageUtils.isComponentEnabled;
import static com.gaelanbolger.woltile.util.PackageUtils.setComponentEnabled;

@SuppressWarnings("ConstantConditions")
public class MainFragment extends Fragment {

    public static final String TAG = MainFragment.class.getSimpleName();
    private static final int REQ_EDIT_TILE = 1014;

    private AppDatabase mDb;
    private Map<TileComponent, TileView> mTiles;

    @BindViews({R.id.tile_1, R.id.tile_2, R.id.tile_3, R.id.tile_4, R.id.tile_5,
            R.id.tile_6, R.id.tile_7, R.id.tile_8, R.id.tile_9})
    TileView[] mTileViews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        bind(this, view);
        TileComponent[] tileComponents = TileComponent.values();
        if (tileComponents.length != mTileViews.length)
            throw new IllegalArgumentException("A TileComponent must be provided for every TileView");
        mTiles = new HashMap<>(tileComponents.length);
        for (int i = 0; i < tileComponents.length; i++) {
            TileComponent tileComponent = tileComponents[i];
            TileView tileView = mTileViews[i];
            tileView.setTag(tileComponent);
            mTiles.put(tileComponent, tileView);
            refreshTileView(tileComponent);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
        onNewIntent(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_EDIT_TILE) {
            if (resultCode == Activity.RESULT_OK) {
                String tileComponentName = data.getStringExtra(EditTileActivity.EXTRA_TILE_COMPONENT);
                TileComponent tileComponent = TileComponent.valueOf(tileComponentName);
                ComponentName cn = new ComponentName(getActivity(), tileComponent.getServiceClass());
                if (!isComponentEnabled(getPackageManager(), cn))
                    setComponentEnabled(getPackageManager(), cn, true);
                refreshTileView(tileComponent);
                makeSnackbar(R.string.tile_enabled);
            }
        }
    }

    @OnClick({R.id.tile_1, R.id.tile_2, R.id.tile_3, R.id.tile_4, R.id.tile_5,
            R.id.tile_6, R.id.tile_7, R.id.tile_8, R.id.tile_9})
    public void onTileClick(TileView tileView) {
        TileComponent tileComponent = (TileComponent) tileView.getTag();
        onEditTile(tileComponent);
    }

    @OnLongClick({R.id.tile_1, R.id.tile_2, R.id.tile_3, R.id.tile_4, R.id.tile_5,
            R.id.tile_6, R.id.tile_7, R.id.tile_8, R.id.tile_9})
    public boolean onTileLongClick(TileView tileView) {
        TileComponent tileComponent = (TileComponent) tileView.getTag();
        Host host = mDb.hostDao().getById(tileComponent.ordinal());
        if (host != null) {
            IconPopupMenu popupMenu = new IconPopupMenu(getActivity(), tileView);
            popupMenu.getMenuInflater().inflate(R.menu.popup_tile, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.item_send:
                        int packetCount = AppSettings.getPacketCount(getActivity());
                        for (int i = 0; i < packetCount; i++) {
                            new WakeOnLanTask(host.getIp(), host.getMac(), host.getPort()).execute();
                        }
                        return true;
                    case R.id.item_delete:
                        ConfirmActionDialog.newInstance(getString(R.string.confirm_delete_tile), () -> {
                            mDb.hostDao().delete(host);
                            ComponentName cn = new ComponentName(getActivity(), tileComponent.getServiceClass());
                            if (isComponentEnabled(getPackageManager(), cn))
                                setComponentEnabled(getPackageManager(), cn, false);
                            refreshTileView(tileComponent);
                            makeSnackbar(R.string.tile_disabled);
                        }).show(getChildFragmentManager(), ConfirmActionDialog.TAG);
                        return true;
                }
                return false;
            });
            popupMenu.show();
            return true;
        }
        return false;
    }

    public void onNewIntent(Intent intent) {
        if (intent != null && intent.hasExtra(EditTileActivity.EXTRA_TILE_COMPONENT)) {
            String tileComponentName = intent.getStringExtra(EditTileActivity.EXTRA_TILE_COMPONENT);
            onEditTile(TileComponent.valueOf(tileComponentName));
        }
    }

    private void onEditTile(TileComponent tileComponent) {
        Intent intent = new Intent(getActivity(), EditTileActivity.class);
        intent.putExtra(EditTileActivity.EXTRA_TILE_COMPONENT, tileComponent.name());
        startActivityForResult(intent, REQ_EDIT_TILE);
    }

    private void refreshTileView(TileComponent tileComponent) {
        TileView tileView = mTiles.get(tileComponent);
        Host host = mDb.hostDao().getById(tileComponent.ordinal());
        if (host == null) {
            tileView.setIconResource(R.drawable.ic_block);
            tileView.setTitle(getString(tileComponent.getTitleResId()));
        } else {
            tileView.setIconResource(ResourceUtils.getDrawableForName(getActivity(), host.getIcon()));
            tileView.setTitle(host.getName());
        }
        ComponentName cn = new ComponentName(getActivity(), tileComponent.getServiceClass());
        tileView.setEnabled(isComponentEnabled(getPackageManager(), cn));
    }

    private void makeSnackbar(int resId) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), resId, Snackbar.LENGTH_SHORT).show();
    }

    private PackageManager getPackageManager() {
        return getActivity().getPackageManager();
    }
}
