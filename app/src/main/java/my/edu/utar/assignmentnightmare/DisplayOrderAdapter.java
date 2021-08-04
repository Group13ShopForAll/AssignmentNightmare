package my.edu.utar.assignmentnightmare;
//Done by Low Wei Han
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

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
        holder.buyerName.setText(model.getBuyerName());
        holder.buyerPhoneNumber.setText(model.getBuyerPhoneNumber());
        holder.buyerAddress.setText(model.getBuyerAddress());
        holder.sellerName.setText(model.getSellerName());
        holder.purchaseDate.setText(model.getPurchaseDate().toString());

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_display_order, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, buyerName, buyerPhoneNumber, buyerAddress, sellerName, purchaseDate;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = (TextView) itemView.findViewById(R.id.productName);
            productPrice = (TextView) itemView.findViewById(R.id.productPrice);
            buyerName = (TextView) itemView.findViewById(R.id.buyerName);
            buyerPhoneNumber = (TextView) itemView.findViewById(R.id.buyerPhoneNumber);
            buyerAddress = (TextView) itemView.findViewById(R.id.buyerAddress);
            sellerName = (TextView) itemView.findViewById(R.id.sellerName);
            purchaseDate = (TextView) itemView.findViewById(R.id.purchaseDate);

        }
    }
}
