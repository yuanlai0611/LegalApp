package com.example.yuanyuanlai.legalapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.yuanyuanlai.legalapp.Bean.ItemNotification;
import com.example.yuanyuanlai.legalapp.R;
import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ItemNotification> notificationBeanList;
    private Context mContext;

    public NotificationAdapter(Context context, List<ItemNotification> notificationBeanList) {
        this.notificationBeanList = notificationBeanList;
        mContext=context;
    }

    @Override
    public int getItemViewType(int position) {
        if (notificationBeanList.get(position).getViewType() == ItemNotification.SHOW_DATE){
            return ItemNotification.SHOW_DATE;
        }else {
            return ItemNotification.SHOW_MESSAGES;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ItemNotification.SHOW_MESSAGES){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_notification, parent, false);
            return new NotificationViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_date, parent, false);
            return new DateViewHolder(view);
        }
    }

     public class NotificationViewHolder extends RecyclerView.ViewHolder{
        View notificationView;
        TextView detailMessageTextView;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            notificationView = itemView;
            detailMessageTextView = (TextView)itemView.findViewById(R.id.notification_detail);
        }
    }

    public class DateViewHolder extends RecyclerView.ViewHolder{
        View dateView;
        TextView dateTextView;
        public DateViewHolder(View itemView){
            super(itemView);
            dateView = itemView;
            dateTextView = (TextView)itemView.findViewById(R.id.date);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof NotificationViewHolder){
            NotificationViewHolder notificationViewHolder = (NotificationViewHolder)holder;
            notificationViewHolder.detailMessageTextView.setText(notificationBeanList.get(position).getContent());
        }else {
            DateViewHolder dateViewHolder = (DateViewHolder)holder;
            dateViewHolder.dateTextView.setText(notificationBeanList.get(position).getCreateTime());
        }
    }

    @Override
    public int getItemCount() {
        return notificationBeanList.size();
    }
}
