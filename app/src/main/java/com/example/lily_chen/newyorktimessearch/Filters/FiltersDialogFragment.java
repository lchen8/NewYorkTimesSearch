package com.example.lily_chen.newyorktimessearch.Filters;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lily_chen.newyorktimessearch.R;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by lily_chen on 10/27/16.
 */
public class FiltersDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Date beginDate;
    String sortOrder;
    String newsDesk;

    @BindView(R.id.spSortOrder)
    Spinner spSortOrder;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.rbArts) RadioButton rbArts;
    @BindView(R.id.rbFashion) RadioButton rbFashion;
    @BindView(R.id.rbSports) RadioButton rbSports;
    @BindView(R.id.rbAny) RadioButton rbAny;

    public interface FiltersDialogListener {
        void onFinishFiltersDialog(Filters filters);
    }


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

        beginDate = filters.getBeginDate();
        if (beginDate != null) {
            tvDate.setText(String.format("%d / %d / %d", beginDate.getMonth()+1, beginDate.getDay()+1, beginDate.getYear()));
        }

        String newsDeskTest = filters.getNewsDesk();
        if (newsDeskTest.equals("Arts")) {
            radioGroup.check(R.id.rbArts);
        } else if (newsDeskTest.equals("\"Fashion & Style\"")) {
            radioGroup.check(R.id.rbFashion);
        } else if (newsDeskTest.equals("sports")) {
            radioGroup.check(R.id.rbFashion);
        } else {
            radioGroup.check(R.id.rbAny);
        }
    }

    // attach to an onclick handler to show the date picker
    @OnClick(R.id.tvDate)
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        FragmentManager fm = getFragmentManager();
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance();
        // SETS the target fragment for use later when sending results
        datePickerFragment.setTargetFragment(FiltersDialogFragment.this, 300);
        datePickerFragment.show(fm, "fragment_date_picker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        beginDate = new Date(year, monthOfYear, dayOfMonth);
        tvDate.setText(String.format("%d / %d / %d", monthOfYear+1, dayOfMonth, year));
    }

    @OnClick(R.id.btSave)
    public void onSave(){
        FiltersDialogListener listener = (FiltersDialogListener) getActivity();

        int rbId = radioGroup.getCheckedRadioButtonId();
        String newsDeskText;

        switch (rbId) {
            case R.id.rbArts:
                newsDeskText = "Arts";
                break;
            case R.id.rbFashion:
                newsDeskText = "\"Fashion & Style\"";
                break;
            case R.id.rbSports:
                newsDeskText = "Sports";
                break;
            default:
                newsDeskText = "";
        }

        Filters filters = new Filters(beginDate, spSortOrder.getSelectedItem().toString(), newsDeskText);
        listener.onFinishFiltersDialog(filters);
        dismiss();
    }

}
