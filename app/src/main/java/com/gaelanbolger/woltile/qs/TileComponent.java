package com.gaelanbolger.woltile.qs;

import com.gaelanbolger.woltile.R;

public enum TileComponent {

    TILE_1(R.string.tile_1, WolTile1Service.class),
    TILE_2(R.string.tile_2, WolTile2Service.class),
    TILE_3(R.string.tile_3, WolTile3Service.class),
    TILE_4(R.string.tile_4, WolTile4Service.class),
    TILE_5(R.string.tile_5, WolTile5Service.class),
    TILE_6(R.string.tile_6, WolTile6Service.class),
    TILE_7(R.string.tile_7, WolTile7Service.class),
    TILE_8(R.string.tile_8, WolTile8Service.class),
    TILE_9(R.string.tile_9, WolTile9Service.class);

    private final int titleResId;
    private final Class<? extends AbsWolTileService> serviceClass;

    TileComponent(int titleResId, Class<? extends AbsWolTileService> serviceClass) {
        this.titleResId = titleResId;
        this.serviceClass = serviceClass;
    }

    public String getKey() {
        return name().toLowerCase();
    }

    public int getTitleResId() {
        return titleResId;
    }

    public Class<? extends AbsWolTileService> getServiceClass() {
        return serviceClass;
    }
}
