package my.edu.utar.assignmentnightmare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.List;


public class CartAdapter extends FirebaseRecyclerAdapter<CartProduct, CartAdapter.myViewHolder> {

    private String currentUserId;
    DatabaseReference reff;
    private static CartAdapter instance;

    public CartAdapter(String currentUserId, @NonNull FirebaseRecyclerOptions<CartProduct> options) {
        super(options);
        this.currentUserId = currentUserId;
        reff = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("cart");
        instance = this;
    }

    public static CartAdapter getInstance() {
        return instance;
    }

    @Override
    protected void onBindViewHolder(@NonNull CartAdapter.myViewHolder holder, int position, @NonNull CartProduct model) {
        holder.scName.setText(model.getProductName());
        holder.scSoldPrice.setText(String.format("%.2f", model.getProductPrice()));
        holder.scStock.setText(String.valueOf(model.getProductStock()));
        holder.scQuantity.setText(String.valueOf(model.getProductQuantity()));
        Picasso.get().load(model.getProductImgUri()).into(holder.scImage);
        instance = this;
        holder.scItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductDetail(v,position);
            }
        });

        holder.deleteitembutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reff.child(getRef(position).getKey()).removeValue();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getSnapshots().size());
                ShoppingCart.getInstance().refreshpage();
            }
        });


    }


    @NonNull
    @Override
    public CartAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scitems,parent, false);
        return new myViewHolder(view);
    }

    private void openProductDetail(View v, int position) {
        String productKey = getRef(position).getKey();
        Intent intent = new Intent(v.getContext(), ProductActivity.class);
        intent.putExtra("productKey",productKey);
        v.getContext().startActivity(intent);

    }

    class myViewHolder extends RecyclerView.ViewHolder{

        private CardView scItem;
        private ImageView scImage;
        private TextView scName, scSoldPrice, scQuantity, scStock, deleteitembutt;



        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            scItem = (CardView) itemView.findViewById(R.id.scitem);
            scImage = (ImageView) itemView.findViewById(R.id.scimage);
            scName = (TextView) itemView.findViewById(R.id.scname);
            scSoldPrice = (TextView) itemView.findViewById(R.id.scSoldPrice);
            scStock = (TextView) itemView.findViewById(R.id.scStock);
            scQuantity = (TextView) itemView.findViewById(R.id.scQuantity);
            deleteitembutt = (TextView) itemView.findViewById(R.id.deleteitembutt);

        }
    }

}
