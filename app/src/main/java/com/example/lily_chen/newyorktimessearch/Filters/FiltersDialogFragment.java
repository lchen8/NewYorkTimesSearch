package com.example.lily_chen.newyorktimessearch.Filters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.lily_chen.newyorktimessearch.R;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by lily_chen on 10/27/16.
 */
public class FiltersDialogFragment extends DialogFragment{

    public interface FiltersDialogListener {
        void onFinishFiltersDialog(Filters filters);
    }

    @BindView(R.id.spSortOrder)
    Spinner spSortOrder;

    public FiltersDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static FiltersDialogFragment newInstance(Filters filters) {
        FiltersDialogFragment frag = new FiltersDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("filters", Parcels.wrap(filters));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filters, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        // Fetch arguments from bundle and set title
        Filters filters = Parcels.unwrap(getArguments().getParcelable("filters"));

        //set up the dialog to have previous filters shown
        if (filters.getSortOrder().equals("oldest")) {
            spSortOrder.setSelection(1);
        } else {
            spSortOrder.setSelection(0); // "newest" is the default choice
        }
    }

    @OnClick(R.id.btSave)
    public void onSave(){
        FiltersDialogListener listener = (FiltersDialogListener) getActivity();
        Filters filters = new Filters(null , null, spSortOrder.getSelectedItem().toString(), "");
        listener.onFinishFiltersDialog(filters);
        dismiss();
    }

}
