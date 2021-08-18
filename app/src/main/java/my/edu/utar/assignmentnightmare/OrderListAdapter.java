package my.edu.utar.assignmentnightmare;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ItemViewHolder>{


    private Context mContext;
    private List<OrderListModel> mListItem;
    private String voucherUsed, voucherDesc;

    public OrderListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<OrderListModel> list) {
        this.mListItem = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlistitem, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ItemViewHolder holder, int position) {
        OrderListModel item = mListItem.get(position);
        if(item == null){
            return;
        }
        //add items attributes here

        holder.itemDate.setText(item.getOrderdate());
        holder.itemTime.setText(item.getOrdertime());

        holder.cvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DisplayOrder.class);
                intent.putExtra("orderdate",item.getOrderdate());
                intent.putExtra("ordertime",item.getOrdertime());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mListItem != null){
            return mListItem.size();
        }
        return 0;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder{
        //add items attributes here
        private TextView itemDate, itemTime, itemPrice;
        private CardView cvOrder;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            //add items attributes here
            cvOrder = itemView.findViewById(R.id.cvOrder);
            itemDate = itemView.findViewById(R.id.listdate);
            itemTime = itemView.findViewById(R.id.listtime);

        }
    }
}
