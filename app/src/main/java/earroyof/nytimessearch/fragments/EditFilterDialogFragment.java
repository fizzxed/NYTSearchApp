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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import earroyof.nytimessearch.Query;
import earroyof.nytimessearch.R;

public class EditFilterDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    Query myQuery;

    AutoCompleteTextView atvNewsDesk;
    AutoCompleteTextView atvMaterial;
    AutoCompleteTextView atvName;

    Button btnOk;
    Button btnClear;

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
        atvNewsDesk = (AutoCompleteTextView) view.findViewById(R.id.atvNewsDesk);
        atvMaterial = (AutoCompleteTextView) view.findViewById(R.id.atvMaterial);
        atvName = (AutoCompleteTextView) view.findViewById(R.id.atvName);
        btnOk = (Button) view.findViewById(R.id.btnOk);
        btnClear = (Button) view.findViewById(R.id.btnClear);

        // Create/Set Adapters
        ArrayAdapter<String> newsDeskAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, myQuery.getNewsArray());
        ArrayAdapter<String> secNameAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, myQuery.getSectionArray());
        ArrayAdapter<String> matTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, myQuery.getMatArray());

        atvNewsDesk.setAdapter(newsDeskAdapter);
        atvMaterial.setAdapter(matTypeAdapter);
        atvName.setAdapter(secNameAdapter);

        // Setup work

        atvName.setOnEditorActionListener(this);
        atvMaterial.setOnEditorActionListener(this);
        atvNewsDesk.setOnEditorActionListener(this);

        atvName.setSingleLine();
        atvMaterial.setSingleLine();
        atvNewsDesk.setSingleLine();
        atvName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        atvMaterial.setImeOptions(EditorInfo.IME_ACTION_DONE);
        atvNewsDesk.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // Clear Button functionality
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atvName.setText("");
                atvMaterial.setText("");
                atvNewsDesk.setText("");
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
            atvName.setText(myQuery.getSection());
            atvMaterial.setText(myQuery.getMaterial());
            atvNewsDesk.setText(myQuery.getNewsDesk());
        } else {
            atvName.setText(savedInstanceState.getString("section"));
            atvNewsDesk.setText(savedInstanceState.getString("newsDesk"));
            atvMaterial.setText(savedInstanceState.getString("material"));
        }



        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        super.onViewCreated(view, savedInstanceState);
    }

    public void finalizeQuery() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("queryStart", myQuery);
        outState.putString("material", atvMaterial.getText().toString());
        outState.putString("newsDesk", atvNewsDesk.getText().toString());
        outState.putString("section", atvName.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            EditFilterDialogListener listener = (EditFilterDialogListener) getActivity();
            listener.onFinishFilterDialog(myQuery);
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }

}
