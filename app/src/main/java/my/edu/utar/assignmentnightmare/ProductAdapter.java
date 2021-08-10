package my.edu.utar.assignmentnightmare;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

//Done by Jiun Lin

public class ProductAdapter extends FirebaseRecyclerAdapter<Product, ProductAdapter.myViewHolder> {

    public ProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductAdapter.myViewHolder holder, int position, @NonNull Product model) {
        // display product info on the card view
        holder.tvProductSoldName.setText(model.getProductName());
        holder.tvProductSoldPrice.setText(String.valueOf(model.getProductPrice()));
        holder.tvProductSoldStock.setText(String.valueOf(model.getProductStock()));
        Picasso.get().load(model.getProductImgUri()).into(holder.ivProductSoldImg);
        
        holder.cvProductSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductDetail(v,position);
            }
        });
        
    }

    private void openProductDetail(View v, int position) {
        String productKey = getRef(position).getKey();
        Intent intent = new Intent(v.getContext(), ProductActivity.class);
        intent.putExtra("productKey",productKey);
        v.getContext().startActivity(intent);
    }

    @NonNull
    @Override
    public ProductAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        private CardView cvProductSold;
        private ImageView ivProductSoldImg;
        private TextView tvProductSoldName, tvProductSoldPrice, tvProductSoldStock;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            cvProductSold = (CardView) itemView.findViewById(R.id.cvProductSold);
            ivProductSoldImg = (ImageView) itemView.findViewById(R.id.ivProductSoldImage);
            tvProductSoldName = (TextView) itemView.findViewById(R.id.tvProductSoldName);
            tvProductSoldPrice = (TextView) itemView.findViewById(R.id.tvProductSoldPrice);
            tvProductSoldStock = (TextView) itemView.findViewById(R.id.tvProductSoldStock);
        }
    }

}
