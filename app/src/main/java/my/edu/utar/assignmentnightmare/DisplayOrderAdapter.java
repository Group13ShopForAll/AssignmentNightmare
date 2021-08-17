package my.edu.utar.assignmentnightmare;
//Done by Low Wei Han
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class DisplayOrderAdapter extends FirebaseRecyclerAdapter <OrderModel, DisplayOrderAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DisplayOrderAdapter(@NonNull FirebaseRecyclerOptions<OrderModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull OrderModel model) {

        holder.productName.setText(model.getProductName());
        holder.productPrice.setText(model.getProductPrice().toString());
        holder.sellername.setText(model.getsellername());
        holder.productQuantity.setText(String.valueOf(model.getProductQuantity()));
        Picasso.get().load(model.getProductImgUri()).into(holder.orderimage);

    }

    @NonNull
    @Override
    public DisplayOrderAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderitems, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, sellername, productQuantity;
        private ImageView orderimage;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = (TextView) itemView.findViewById(R.id.ordername);
            productPrice = (TextView) itemView.findViewById(R.id.orderprice);
            sellername = (TextView) itemView.findViewById(R.id.sellername);
            productQuantity = (TextView) itemView.findViewById(R.id.orderquantity);
            orderimage = (ImageView) itemView.findViewById(R.id.orderimage);

        }
    }
}
