package my.edu.utar.assignmentnightmare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

// Done by Jiun Lin
public class MyProductAdapter extends FirebaseRecyclerAdapter<Product, MyProductAdapter.myViewHolder> {

    private String currentUserId;
    private DatabaseReference myProductRef, homepageProductRef;

    public MyProductAdapter(String currentUserId, @NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
        this.currentUserId = currentUserId;
        // my product page reference
        myProductRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("products");
        // homepage product reference
        homepageProductRef = FirebaseDatabase.getInstance().getReference().child("homepage").child("products");
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

        // while click on the delete button prompt user the alert dialog to ensure deletion
        holder.btnMyProductDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.tvMyProductName.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleted product cannot be recovered");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete product from my product page
                        myProductRef.child(getRef(position).getKey()).removeValue();
                        // Delete product from home page
                        homepageProductRef.child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.tvMyProductName.getContext(),"Cancelled",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        // while click on the edit button prompt out the dialog for update product info
        holder.btnMyProductEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send to update product activity
                String productKey = getRef(position).getKey();
                Intent intent = new Intent(v.getContext(), UpdateProductActivity.class);
                intent.putExtra("productKey",productKey);
                intent.putExtra("currentUserId",currentUserId);
                v.getContext().startActivity(intent);
            }
        });
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
