package ie.droidfactory.fibarodemoapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.FibaroType;
import ie.droidfactory.fibarodemoapp.model.Room;
import ie.droidfactory.fibarodemoapp.model.Section;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.viewmodel.RoomViewModel;
import ie.droidfactory.fibarodemoapp.viewmodel.SectionViewModel;

/**
 * Created by kudlaty on 2018-02-28.
 */

public class FragmentRoom extends Fragment implements FibaroAdapter.DeviceAdapterOnClickHandler{

    private final static String TAG = FragmentRoom.class.getSimpleName();

    private final static String KEY="object_index";

    private FibaroFragmentInterfaces mListener;
    private FibaroAdapter mFibaroAdapter;
    private RecyclerView mRecyclerView;
    private int index;

    public static FragmentRoom newInstance(int roomIndex){
        FragmentRoom fragment = new FragmentRoom();
        Bundle args = new Bundle();
        args.putInt(KEY, roomIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_room, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.room_recyclerview);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mFibaroAdapter = new FibaroAdapter(getContext(), this, FibaroType.ROOM);
        mRecyclerView.setAdapter(mFibaroAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            index = getArguments().getInt(KEY);
            if(index>=0){
                mListener.setActioneBar(getResources().getString(R.string.title_section_name)+" "+Section.getSectionsList().get(index).getName(), true);
                RoomViewModel viewModel = ViewModelProviders.of(this).get(RoomViewModel.class);
                viewModel.getRooms(getActivity(), FibaroService.getCredentials(), index).observe(this, new Observer<ArrayList<Room>>() {
                    @Override
                    public void onChanged(@Nullable ArrayList<Room> rooms) {
                        mFibaroAdapter.swapDevicesList(rooms);
                    }
                });
            }else mListener.loginResponse(false, "incorrect section index");

        }else mListener.loginResponse(false, "missed section index");

    }
    @Override
    public void onResume(){
        super.onResume();
        if(index>=0) mListener.setActioneBar(getResources().getString(R.string.title_section_name)+" "+Section.getSectionsList().get(index).getName(), true);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mListener = (FibaroFragmentInterfaces) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+ "loginCallback Listener is not " +
                    "implemented...");
        }

    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(int objectIndex) {
        mListener.onRoomSelected(objectIndex);
    }
}
