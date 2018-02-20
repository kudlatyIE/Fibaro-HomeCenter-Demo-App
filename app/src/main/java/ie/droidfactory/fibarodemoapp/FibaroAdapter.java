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
import ie.droidfactory.fibarodemoapp.model.FibaroObject;
import ie.droidfactory.fibarodemoapp.model.FibaroType;
import ie.droidfactory.fibarodemoapp.model.Room;
import ie.droidfactory.fibarodemoapp.model.Section;

/**
 * Created by kudlaty on 2018-02-19.
 */

public class FibaroAdapter extends RecyclerView.Adapter<FibaroAdapter.FibaroAdapterViewHolder>{

    private static final String TAG = FibaroAdapter.class.getSimpleName();

    private final Context mContext;
    private ArrayList<FibaroObject> mList;
    private FibaroType fibaroType;


    final private DeviceAdapterOnClickHandler mClickHandler;

    /**
     * interface that receives fibaro object section/room/device ID .
     */
    public interface DeviceAdapterOnClickHandler {
        void onClick(int objectId);
    }
    public FibaroAdapter(@NonNull Context context, DeviceAdapterOnClickHandler clickHandler, FibaroType fibaroType) {
        mContext = context;
        mClickHandler = clickHandler;
        this.fibaroType=fibaroType;
        Log.d(TAG, "ADAPTER TYPE: "+fibaroType.name());
    }

    void swapDevicesList(Object devicesList) {
        mList = (ArrayList<FibaroObject>) devicesList;
        notifyDataSetChanged();
    }


    @Override
    public FibaroAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resource = -1;
        switch (fibaroType){
            case SECTION: resource = R.layout.section_item; break;
            case ROOM: resource = R.layout.section_item; break;// use the same layout
            case DEVICE: resource = R.layout.device_item; break;

        }
        View view = LayoutInflater.from(mContext).inflate(resource, parent, false);
        view.setFocusable(true);
        return new FibaroAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FibaroAdapterViewHolder holder, int position) {
//        String id = String.valueOf(mList.get(position).getId());
        if(FibaroType.SECTION==fibaroType){
            Section section = (Section) mList.get(position);
            holder.objectName.setText(section.getName());
        }

        if(FibaroType.ROOM==fibaroType){
            Room room = (Room) mList.get(position);
            holder.objectName.setText(room.getName());
        }

        if(FibaroType.DEVICE==fibaroType){
            Device dev = (Device)mList.get(position);
            holder.deviceType.setText(dev.getType());
            holder.deviceValue.setText(dev.getProperties().getValue());
        }

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

    class FibaroAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView objectName;
        TextView deviceType;
        TextView deviceValue;

        FibaroAdapterViewHolder(View view) {
            super(view);

            objectName =  view.findViewById(R.id.tv_deviceName);
            if(FibaroType.DEVICE==fibaroType){
                deviceType =  view.findViewById(R.id.tvDeviceType);
                deviceValue =  view.findViewById(R.id.tvDeviceValue);
            }

            view.setOnClickListener(this);
        }

        /**
         * fetch the ID of object that has been selected and call click handler
         * @param v - clicked View
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

            int id = mList.get(adapterPosition).getId();
            Log.d(TAG, "click item name: "+mList.get(adapterPosition).getName());
            mClickHandler.onClick(id);

        }
    }
}
