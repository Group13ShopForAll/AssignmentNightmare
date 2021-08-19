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
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;
// Done by Felix
public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ItemViewHolder>{


    private Context mContext;
    private List<Vou> mListItem;
    private String voucherUsed, voucherDesc;

    public VoucherAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Vou> list) {
        this.mListItem = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucheritem, parent, false);
        return new ItemViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull VoucherAdapter.ItemViewHolder holder, int position) {
        Vou item = mListItem.get(position);
        if(item == null){
            return;
        }
        //add items attributes here

        holder.itemName.setText(item.getVouName());
        holder.desc.setText(item.getVouDesc());

        Intent intent = new Intent(mContext, Checkout.class);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = prefs.edit();

                if(item.vouName == "❌ Shipping"){
                    editor.putString("ShippingVoucherUsed", item.vouName);
                    editor.commit();
                }
                else if(item.vouName == "Free Shipping"){
                    editor.putString("ShippingVoucherUsed", item.vouName);
                    editor.commit();
                }

                if(item.vouName == "❌ Coupon"){
                    editor.putString("VoucherUsed", item.vouName);
                    editor.commit();
                }
                else if(item.vouName == "RM 5 OFF Code SHOP5OFF"){
                    editor.putString("VoucherUsed", item.vouName);
                    editor.commit();
                }
                else if(item.vouName == "10 % OFF Sitewide"){
                    editor.putString("VoucherUsed", item.vouName);
                    editor.commit();
                }


                ((Voucher)mContext).finish();

                mContext.startActivity(intent);
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
        private TextView itemName;
        private TextView desc;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            //add items attributes here
            itemName = itemView.findViewById(R.id.vouname);
            desc = itemView.findViewById(R.id.voudesc);

        }
    }
}
