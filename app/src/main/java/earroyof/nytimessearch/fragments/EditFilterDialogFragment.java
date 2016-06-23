package earroyof.nytimessearch.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import earroyof.nytimessearch.MultiSpinner;
import earroyof.nytimessearch.Query;
import earroyof.nytimessearch.R;

public class EditFilterDialogFragment extends DialogFragment implements TextView.OnEditorActionListener, MultiSpinner.MultiSpinnerListener {

    Query myQuery;

    AutoCompleteTextView atvNewsDesk;
    AutoCompleteTextView atvMaterial;
    AutoCompleteTextView atvName;

    MultiSpinner snNewsDesk;
    MultiSpinner snMaterial;
    MultiSpinner snSection;

    Button btnOk;
    Button btnClear;

    MultiSpinner spinner;

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

        if (savedInstanceState == null || !savedInstanceState.containsKey("section")) {
            // TODO: Nothing?

        } else {
            snMaterial.setSelected(savedInstanceState.getBooleanArray("material"));
            snNewsDesk.setSelected(savedInstanceState.getBooleanArray("newsDesk"));
            boolean[] test = savedInstanceState.getBooleanArray("selection");
            snSection.setSelected(savedInstanceState.getBooleanArray("section"));
        }



        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        super.onViewCreated(view, savedInstanceState);
    }

    public void finalizeQuery() {
        // TODO: Add data to myQuery
        /*String material = atvMaterial.getText().toString();
        String name = atvName.getText().toString();
        String newsDesk = atvNewsDesk.getText().toString();
        if (!material.isEmpty()) {
            if (Arrays.binarySearch(myQuery.getMatArray(), material) > 0) {
                myQuery.setMaterial(material);
            } else invalidOption();
        }
        if (!name.isEmpty()) {
            if (Arrays.binarySearch(myQuery.getSectionArray(), name) > 0) {
                myQuery.setSection(name);
            } else invalidOption();
        }
        if (!newsDesk.isEmpty()) {
            if (Arrays.binarySearch(myQuery.getNewsArray(), newsDesk) > 0) {
                myQuery.setNewsDesk(newsDesk);
            } else invalidOption();
        }
        */

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

    public void onItemsSelected(boolean[] selected) {

    }

}
