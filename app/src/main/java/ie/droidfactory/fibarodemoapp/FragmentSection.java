package ie.droidfactory.fibarodemoapp;

import android.app.Activity;
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
import ie.droidfactory.fibarodemoapp.model.Section;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.viewmodel.SectionViewModel;

/**
 * Created by kudlaty on 2018-02-28.
 */

public class FragmentSection extends Fragment implements FibaroAdapter.DeviceAdapterOnClickHandler{

    private final static String TAG = FragmentSection.class.getSimpleName();

    public static final String SECTION_INDEX ="section_index";
    private FibaroAdapter mFibaroAdapter;
    private RecyclerView mRecyclerView;

    private FibaroFragmentInterfaces mListener;


    public static Fragment newInstance(){
        return new FragmentSection();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_section, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.section_recyclerview);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mFibaroAdapter = new FibaroAdapter(getContext(), this, FibaroType.SECTION);
        mRecyclerView.setAdapter(mFibaroAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.setActioneBar(getResources().getString(R.string.title_sestions_all), false);
        SectionViewModel viewModel = ViewModelProviders.of(this).get(SectionViewModel.class);
        viewModel.getSections(getActivity(), FibaroService.getCredentials(), mListener).observe(this, new Observer<ArrayList<Section>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Section> sections) {
                mFibaroAdapter.swapDevicesList(sections);
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        mListener.setActioneBar(getResources().getString(R.string.title_sestions_all), false);
    }


    @Override
    public void onClick(int objectIndex) {
        Log.d(TAG, "onClick SECTION index: "+objectIndex);
        mListener.onSectionSelected(objectIndex);
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
}
