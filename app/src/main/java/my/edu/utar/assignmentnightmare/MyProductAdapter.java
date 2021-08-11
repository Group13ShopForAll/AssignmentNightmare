package my.edu.utar.assignmentnightmare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

// Done by Jiun Lin
public class MyProductAdapter extends FirebaseRecyclerAdapter<Product, MyProductAdapter.myViewHolder> {

    public MyProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyProductAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myproducts,parent, false);
        return new MyProductAdapter.myViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyProductAdapter.myViewHolder holder, int position, @NonNull Product model) {
        // display retrieve data at the respective field
        holder.tvMyProductName.setText(model.getProductName());
        holder.tvMyProductPrice.setText(String.valueOf(model.getProductPrice()));
        holder.tvMyProductStock.setText(String.valueOf(model.getProductStock()));
        Picasso.get().load(model.getProductImgUri()).into(holder.civMyProductImg);


    }

    class myViewHolder extends RecyclerView.ViewHolder{

        private CardView cvMyProduct;
        private CircleImageView civMyProductImg;
        private TextView tvMyProductName, tvMyProductPrice, tvMyProductStock;
        private Button btnMyProductEdit, btnMyProductDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            cvMyProduct = (CardView) itemView.findViewById(R.id.cvMyProduct);
            civMyProductImg = (CircleImageView) itemView.findViewById(R.id.civMyProductImg);
            tvMyProductName = (TextView) itemView.findViewById(R.id.tvMyProductName);
            tvMyProductPrice = (TextView) itemView.findViewById(R.id.tvMyProductPrice);
            tvMyProductStock = (TextView) itemView.findViewById(R.id.tvMyProductStock);

            btnMyProductEdit = (Button) itemView.findViewById(R.id.btnMyProductEdit);
            btnMyProductDelete = (Button) itemView.findViewById(R.id.btnMyProductDelete);
        }
    }
}
