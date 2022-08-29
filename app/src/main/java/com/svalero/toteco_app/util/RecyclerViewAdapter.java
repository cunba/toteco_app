package com.svalero.toteco_app.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.toteco_app.R;
import com.svalero.toteco_app.domain.util.PublicationToRecyclerView;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final List<PublicationToRecyclerView> publications;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCardTitle;
        private final ImageView ivCardImage;
        private final TextView tvCardProducts;
        private final TextView tvCardPrice;
        private final TextView tvCardPunctuation;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvCardTitle = (TextView) view.findViewById(R.id.card_title);
            ivCardImage = (ImageView) view.findViewById(R.id.card_image);
            tvCardProducts = (TextView) view.findViewById(R.id.card_products_list);
            tvCardPrice = (TextView) view.findViewById(R.id.card_price);
            tvCardPunctuation = (TextView) view.findViewById(R.id.card_punctuation);
        }

        public TextView getTvCardTitle() {
            return tvCardTitle;
        }

        public ImageView getIvCardImage() {
            return ivCardImage;
        }

        public TextView getTvCardProducts() {
            return tvCardProducts;
        }

        public TextView getTvCardPrice() {
            return tvCardPrice;
        }

        public TextView getTvCardPunctuation() {
            return tvCardPunctuation;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public RecyclerViewAdapter(List<PublicationToRecyclerView> dataSet) {
        publications = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);

        return new ViewHolder(view);
    }

        // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.getTvCardTitle().setText(publications.get(position).getEstablishmentName());

        Bitmap image = BitmapFactory.decodeByteArray(publications.get(position).getImage(), 0,
                publications.get(position).getImage().length);
        viewHolder.getIvCardImage().setImageBitmap(image);

        AtomicReference<String> products = new AtomicReference<>("");
        publications.get(position).getProducts().stream().forEach(p ->
                products.set("" + products.get() + '\n' + p.toString() + '\n')
        );
        viewHolder.getTvCardProducts().setText(products.get());

        viewHolder.getTvCardPrice().setText(publications.get(position).getTotalPrice());
        viewHolder.getTvCardPunctuation().setText(publications.get(position).getTotalPunctuation());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return publications.size();
    }
}

