package ie.droidfactory.fibarodemoapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Device;

/**
 * Created by kudlaty on 2018-02-19.
 */

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceAdapterViewHolder>{

    private static final String TAG = DevicesAdapter.class.getSimpleName();

    private final Context mContext;
    //    private Cursor mCursor;
    private ArrayList<Device> mList;


    final private DeviceAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives device ID (or better position?).
     */
    public interface DeviceAdapterOnClickHandler {
        void onClick(int deviceId);
    }
    public DevicesAdapter(@NonNull Context context, DeviceAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    void swapDevicesList(ArrayList<Device> devicesList) {
        mList = devicesList;
        notifyDataSetChanged();
    }


    @Override
    public DeviceAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.device_item, parent, false);
        view.setFocusable(true);
        return new DeviceAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceAdapterViewHolder holder, int position) {
        String id = String.valueOf(mList.get(position).getId());
        holder.deviceName.setText(mList.get(position).getName());
        holder.deviceType.setText(mList.get(position).getType());
        holder.deviceValue.setText(mList.get(position).getProperties().getValue());

    }

    @Override
    public int getItemCount() {
        if(null == mList)return 0;
        else return mList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return 0;
    }




    class DeviceAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView deviceName;
        TextView deviceType;
        TextView deviceValue;

        DeviceAdapterViewHolder(View view) {
            super(view);

            deviceName =  view.findViewById(R.id.tv_deviceName);
            deviceType =  view.findViewById(R.id.tvDeviceType);
            deviceValue =  view.findViewById(R.id.tvDeviceValue);

            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * date.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

            int deviceId = mList.get(adapterPosition).getId();
            Log.d(TAG, "click item name: "+mList.get(adapterPosition).getName());
            Log.d(TAG, "click item value: "+mList.get(adapterPosition).getProperties().getValue());
//            int deviceId = deviceList.get(v.getId()).getId();
            mClickHandler.onClick(deviceId);

        }
    }
}
