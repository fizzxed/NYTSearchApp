package earroyof.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import earroyof.nytimessearch.MultiSpinner;
import earroyof.nytimessearch.R;
import earroyof.nytimessearch.dataModels.Query;

public class EditFilterDialogFragment extends DialogFragment implements TextView.OnEditorActionListener, MultiSpinner.MultiSpinnerListener,
        DatePickerDialog.OnDateSetListener {

    Query myQuery;

    MultiSpinner snNewsDesk;
    MultiSpinner snMaterial;
    MultiSpinner snSection;

    Button btnOk;
    Button btnClear;

    TextView tvSelectDate;
    Spinner snSort;

    int year;
    int month;
    int day;

    // Defines the listener interface with a method passing back data result.
    public interface EditFilterDialogListener {
        void onFinishFilterDialog(Query query);
    }



    public EditFilterDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditFilterDialogFragment newInstance(String title) {
        EditFilterDialogFragment frag = new EditFilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState == null || !savedInstanceState.containsKey("queryStart")) {
            myQuery = getArguments().getParcelable("query");
        } else {
            myQuery = savedInstanceState.getParcelable("queryStart");
        }


        return inflater.inflate(R.layout.fragment_edit_filter, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Reference views
        snNewsDesk = (MultiSpinner) view.findViewById(R.id.snNews);
        snNewsDesk.setItems(Arrays.asList(myQuery.getNewsArray()), "News Desk", this);
        snMaterial = (MultiSpinner) view.findViewById(R.id.snMaterial);
        snMaterial.setItems(Arrays.asList(myQuery.getMatArray()), "Material", this);
        snSection = (MultiSpinner) view.findViewById(R.id.snSection);
        snSection.setItems(Arrays.asList(myQuery.getSectionArray()), "Section", this);

        btnOk = (Button) view.findViewById(R.id.btnOk);
        btnClear = (Button) view.findViewById(R.id.btnClear);

        tvSelectDate = (TextView) view.findViewById(R.id.tvSelectDate);
        snSort = (Spinner) view.findViewById(R.id.snSort);



        // Clear Button functionality
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clear", Toast.LENGTH_SHORT).show();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizeQuery();
                EditFilterDialogListener listener = (EditFilterDialogListener) getActivity();
                listener.onFinishFilterDialog(myQuery);
                // Close the dialog and return back to the parent activity
                dismiss();
            }
        });

        String title = getArguments().getString("title", "Search Settings");
        getDialog().setTitle(title);

        if (savedInstanceState != null && savedInstanceState.containsKey("section")) {
            snMaterial.setSelected(savedInstanceState.getBooleanArray("material"));
            snNewsDesk.setSelected(savedInstanceState.getBooleanArray("newsDesk"));
            snSection.setSelected(savedInstanceState.getBooleanArray("section"));
        } else {
            snMaterial.setSelected(myQuery.getMatSelect());
            snNewsDesk.setSelected(myQuery.getNewsSelect());
            snSection.setSelected(myQuery.getSectionSelect());
        }

        tvSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey("date")) {
            day = savedInstanceState.getInt("day");
            month = savedInstanceState.getInt("month");
            year = savedInstanceState.getInt("year");
            tvSelectDate.setText(savedInstanceState.getString("date"));
        } else if (myQuery.getDay() != 0) {
            day = myQuery.getDay();
            month = myQuery.getMonth();
            year = myQuery.getYear();
            String dateform = String.format("%02d", month) + "." + String.format("%02d", day) + "." + year;
            tvSelectDate.setText(dateform);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey("order")) {
            snSort.setSelection(savedInstanceState.getInt("order"));
        } else {
            snSort.setSelection(myQuery.getOrder());
        }



        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onViewCreated(view, savedInstanceState);
    }

    public void finalizeQuery() {
        myQuery.setMatSelect(snMaterial.getSelected());
        myQuery.setNewsSelect(snNewsDesk.getSelected());
        myQuery.setSectionSelect(snSection.getSelected());
        myQuery.setYear(year);
        myQuery.setMonth(month);
        myQuery.setDay(day);
        myQuery.setOrder(snSort.getSelectedItemPosition());
    }

    public void invalidOption() {
        Toast.makeText(getActivity(), "Invalid Parameter(s)", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("queryStart", myQuery);
        outState.putBooleanArray("material", snMaterial.getSelected());
        outState.putBooleanArray("newsDesk", snNewsDesk.getSelected());
        outState.putBooleanArray("section", snSection.getSelected());
        outState.putInt("year", year);
        outState.putInt("month", month);
        outState.putInt("day", day);
        outState.putString("date", tvSelectDate.getText().toString());
        outState.putInt("order", snSection.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            finalizeQuery();
            // Return input text back to activity through the implemented listener
            EditFilterDialogListener listener = (EditFilterDialogListener) getActivity();
            listener.onFinishFilterDialog(myQuery);
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onPause() {
        snMaterial.onPause();
        snSection.onPause();
        snNewsDesk.onPause();
        super.onPause();
    }

    public void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance("Title");

        datePickerFragment.setTargetFragment(EditFilterDialogFragment.this, 300);

        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
        String dateform = String.format("%02d", month) + "." + String.format("%02d", day) + "." + year;
        tvSelectDate.setText(dateform);
    }

    public void onItemsSelected(boolean[] selected) {

    }

}
