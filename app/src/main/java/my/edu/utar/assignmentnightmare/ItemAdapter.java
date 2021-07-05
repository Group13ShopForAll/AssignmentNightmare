package my.edu.utar.assignmentnightmare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.Formatter;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{

    private Context mContext;
    private List<Item> mListItem;

    public ItemAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Item> list) {
        this.mListItem = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {
        Item item = mListItem.get(position);
        if(item == null){
            return;
        }
        //add items attributes here
        holder.imgItem.setImageResource(item.getResourceImage());
        holder.itemName.setText(item.getName());
        holder.itemprice.setText("RM " + String.format("%.2f",item.getPrice()));
        holder.soldnum.setText(item.getSoldnum() + " Sold");
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
        private ImageView imgItem;
        private TextView itemName;
        private TextView itemprice;
        private TextView soldnum;
        
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            //add items attributes here
            imgItem = itemView.findViewById(R.id.itemimg);
            itemName = itemView.findViewById(R.id.itemname);
            itemprice = itemView.findViewById(R.id.price);
            soldnum = itemView.findViewById(R.id.sold);
            
        }
    }

}
