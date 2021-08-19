package my.edu.utar.assignmentnightmare;
//Done by Low Wei Han
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

import java.util.Date;

public class DisplayOrderAdapter extends FirebaseRecyclerAdapter <OrderModel, DisplayOrderAdapter.myViewHolder> {

    public DisplayOrderAdapter(@NonNull FirebaseRecyclerOptions<OrderModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull OrderModel model) {

        holder.productName.setText(model.getProductName());
        holder.productPrice.setText(String.format("%.2f", model.getProductPrice()));
        holder.sellername.setText(model.getsellername());
        holder.productQuantity.setText(String.valueOf(model.getProductQuantity()));
        Picasso.get().load(model.getProductImgUri()).into(holder.orderimage);

        holder.scItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductDetail(v,position);
            }
        });



    }

    @NonNull
    @Override
    public DisplayOrderAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderitems, parent, false);
        return new myViewHolder(view);
    }

    private void openProductDetail(View v, int position) {
        String productKey = getRef(position).getKey();
        Intent intent = new Intent(v.getContext(), ProductActivity.class);
        intent.putExtra("productKey",productKey);
        v.getContext().startActivity(intent);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, sellername, productQuantity;
        private ImageView orderimage;
        CardView scItem;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            scItem = (CardView) itemView.findViewById(R.id.scitem);

            productName = (TextView) itemView.findViewById(R.id.ordername);
            productPrice = (TextView) itemView.findViewById(R.id.orderprice);
            sellername = (TextView) itemView.findViewById(R.id.sellername);
            productQuantity = (TextView) itemView.findViewById(R.id.orderquantity);
            orderimage = (ImageView) itemView.findViewById(R.id.orderimage);

        }
    }
}
