package com.svalero.toteco_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.svalero.toteco_app.database.AppDatabase;
import com.svalero.toteco_app.domain.Product;


public class DeleteProductDialog extends DialogFragment {

    private final Product product;
    private final AppDatabase db;

    public DeleteProductDialog(AppDatabase db, Product product) {
        this.db = db;
        this.product = product;
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.delete_product_message, product.getName()))
                .setPositiveButton(R.string.delete_submit, (dialog, id) -> {
                    db.productDao().delete(product);
                    listener.onDialogPositiveClick(DeleteProductDialog.this);
                })
                .setNegativeButton(R.string.delete_cancel, (dialog, id) ->
                    DeleteProductDialog.this.getDialog().cancel()
                );
        return builder.create();
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            System.out.println(e.getMessage());
        }
    }
}