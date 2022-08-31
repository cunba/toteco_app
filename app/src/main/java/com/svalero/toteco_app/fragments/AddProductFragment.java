package com.svalero.toteco_app.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.svalero.toteco_app.R;
import com.svalero.toteco_app.database.AppDatabase;
import com.svalero.toteco_app.domain.Product;

public class AddProductFragment extends DialogFragment {

    private final AppDatabase db;
    private String error;

    public AddProductFragment(AppDatabase db) {
        this.db = db;
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, String error);
    }

    // Use this instance of the interface to deliver action events
    AddProductFragment.NoticeDialogListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        error = "";
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (AddProductFragment.NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            System.out.println(e.getMessage());
        }
    }

    @SuppressLint({"InflateParams", "ResourceType"})
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_product, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.add_product_submit, (dialog, id) -> {
                    onPressSubmit(view);
                    listener.onDialogPositiveClick(AddProductFragment.this, error);
                })
                .setNegativeButton(R.string.add_product_cancel, (dialog, id) ->
                        AddProductFragment.this.getDialog().cancel()
                );

        return builder.create();
    }

    private void onPressSubmit(View view) {
        EditText etName = view.findViewById(R.id.add_product_name);
        EditText etPrice = view.findViewById(R.id.add_product_price);
        EditText etPunctuation = view.findViewById(R.id.add_product_punctuation);

        String name = etName.getText().toString();
        String priceString = etPrice.getText().toString();
        String punctuationString = etPunctuation.getText().toString();

        if (name.equals("") || priceString.equals("") || punctuationString.equals("")) {
            error = getString(R.string.error_field_empty);
        } else {
            float price = Float.parseFloat(priceString);
            float punctuation = Float.parseFloat(punctuationString);

            if (price < 0.0 || punctuation < 0.0) {
                error = getString(R.string.add_product_error_price_punctuation);
            } else if (punctuation > 5.0) {
                error = getString(R.string.add_product_error_punctuation);
            } else {
                error = "";
                Product newProduct = new Product(name, price, punctuation, 1);
                db.productDao().insert(newProduct);
            }
        }
    }
}