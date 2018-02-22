package ie.droidfactory.fibarodemoapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
     * interface receives fibaro object section/room/device ID .
     */
    public interface DeviceAdapterOnClickHandler {
        void onClick(int objectIndex);
//        void onClick(int index, int objectId);
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
    void updateDevice(int position){
        notifyItemChanged(position);
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
        int red=-1;// set RED backbround for ALL_ITEM
        String value;

        if(FibaroType.SECTION==fibaroType){
            Section section = (Section) mList.get(position);
            holder.objectName.setText(section.getName());
            if(section.getId()==-1) red=position;
        }

        if(FibaroType.ROOM==fibaroType){
            Room room = (Room) mList.get(position);
            holder.objectName.setText(room.getName());
            if(room.getSectionID()==-1) red=position;
        }

        if(FibaroType.DEVICE==fibaroType){
            Device dev = (Device)mList.get(position);
            holder.objectName.setText(dev.getName());
            holder.deviceType.setText(dev.getType());

            //TODO: neet to be fixed - colors and values....

            if(dev.getProperties().getValue().equals("0")){
                value = "OFF";
                setBackgroudDrawable(holder.imgValue, mContext, R.drawable.shape_rectangle_grey);
            }else {
                value = dev.getProperties().getValue();
            }
            if(dev.getType().equals(Device.KEY_BINARY) & !dev.getProperties().getValue().equals("0")){
                value = "ON";
            }
            holder.deviceValue.setText(value);

        }
        if(red!=-1){
            setBackgroudDrawable(holder.objectName, mContext, R.drawable.shape_rectangle_alert);
        }

    }
    private void setBackgroudDrawable(View view, Context context, int res){
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
            view.setBackground(context.getResources().getDrawable(res));
        else
            view.setBackground(ContextCompat.getDrawable(context, res));
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
        ImageView imgValue;

        FibaroAdapterViewHolder(View view) {
            super(view);

            objectName =  view.findViewById(R.id.tv_deviceName);
            if(FibaroType.DEVICE==fibaroType){
                imgValue = view.findViewById(R.id.imgDeviceValue);
                deviceType =  view.findViewById(R.id.tvDeviceType);
                deviceValue =  view.findViewById(R.id.tvDeviceValue);
            }

            view.setOnClickListener(this);
        }

        /**
         * fetch the ID of object that has been selected and call click handler
         * UPDATE: fetch index of selected item!!!!
         * @param v - clicked View
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

            int id = mList.get(adapterPosition).getId();
            //test..
            Log.d(TAG, "click item name: "+mList.get(adapterPosition).getName());
            mClickHandler.onClick(adapterPosition);

        }
    }
}
