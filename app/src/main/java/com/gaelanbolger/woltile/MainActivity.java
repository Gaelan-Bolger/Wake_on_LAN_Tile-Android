package com.gaelanbolger.woltile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gaelanbolger.woltile.adapter.OnItemClickListener;
import com.gaelanbolger.woltile.adapter.SpaceItemDecoration;
import com.gaelanbolger.woltile.data.Host;
import com.gaelanbolger.woltile.discover.DiscoverActivity;
import com.gaelanbolger.woltile.discover.DiscoverFragment;
import com.gaelanbolger.woltile.settings.SettingsActivity;
import com.gaelanbolger.woltile.util.DispUtils;

public class MainActivity extends AppCompatActivity implements DiscoverFragment.Listener {

    private SharedPreferences preferences;

    private TileIconOptionsAdapter tileIconAdapter;
    private RecyclerView tileIconRecycler;
    private EditText hostNameText;
    private EditText ipAddressText;
    private EditText macAddressText;
    private RadioGroup portGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.activity_main);

        int[] items = getTileIconOptions();
        int selectedItem = preferences.getInt(WolTileApplication.PREF_ICON, items[0]);
        tileIconAdapter = new TileIconOptionsAdapter(this, items, selectedItem, null);
        tileIconRecycler = findViewById(R.id.rv_tile_icon);
        tileIconRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tileIconRecycler.addItemDecoration(new SpaceItemDecoration(SpaceItemDecoration.HORIZONTAL, DispUtils.dp2px(16)));
        tileIconRecycler.setAdapter(tileIconAdapter);

        String hostName = preferences.getString(WolTileApplication.PREF_HOST_NAME, null);
        hostNameText = findViewById(R.id.et_host_name);
        hostNameText.setText(hostName);

        String ipAddress = preferences.getString(WolTileApplication.PREF_IP_ADDRESS, null);
        ipAddressText = findViewById(R.id.et_ip_address);
        ipAddressText.setText(ipAddress);

        String macAddress = preferences.getString(WolTileApplication.PREF_MAC_ADDRESS, null);
        macAddressText = findViewById(R.id.et_mac_address);
        macAddressText.setText(macAddress);

        int port = preferences.getInt(WolTileApplication.PREF_PORT, WolTileApplication.DEFAULT_PORT);
        portGroup = findViewById(R.id.rg_port);
        portGroup.check(port == WolTileApplication.DEFAULT_PORT ? R.id.rb_port_9 : R.id.rb_port_7);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_save:
                onSaveSettings();
                return true;
            case R.id.item_discover:
                startActivity(new Intent(this, DiscoverActivity.class));
                return true;
            case R.id.item_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHostSelected(Host host) {
        hostNameText.setText(host.getName());
        ipAddressText.setText(host.getIp());
        macAddressText.setText(host.getMac());
    }

    @NonNull
    private int[] getTileIconOptions() {
        TypedArray ta = getResources().obtainTypedArray(R.array.tile_icon_options);
        int length = ta.length();
        int[] items = new int[length];
        for (int i = 0; i < length; i++)
            items[i] = ta.getResourceId(i, -1);
        ta.recycle();
        return items;
    }

    private void onSaveSettings() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(WolTileApplication.PREF_HOST_NAME, hostNameText.getText().toString());
        editor.putString(WolTileApplication.PREF_IP_ADDRESS, ipAddressText.getText().toString());
        editor.putString(WolTileApplication.PREF_MAC_ADDRESS, macAddressText.getText().toString());
        editor.putInt(WolTileApplication.PREF_PORT, portGroup.getCheckedRadioButtonId() == R.id.rb_port_9 ? 9 : 7);
        editor.putInt(WolTileApplication.PREF_ICON, tileIconAdapter.getSelectedItem());
        editor.apply();
        Toast.makeText(MainActivity.this, "Settings saved", Toast.LENGTH_SHORT).show();
    }

    class TileIconOptionsAdapter extends RecyclerView.Adapter<TileIconOptionsAdapter.Holder> {

        private final LayoutInflater inflater;
        private final ColorStateList selectedColor;
        private final ColorStateList unselectedColor;
        private final OnItemClickListener clickListener;

        private int[] items;
        private int selectedItem;

        TileIconOptionsAdapter(Context context, int[] items, int selectedItem, @Nullable OnItemClickListener clickListener) {
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
            int item = getItem(position);
            ImageViewCompat.setImageTintList(holder.icon, selectedItem == item ? selectedColor : unselectedColor);
            holder.icon.setImageResource(item);
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

        int getItem(int position) {
            return items[position];
        }

        int getSelectedItem() {
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
}
