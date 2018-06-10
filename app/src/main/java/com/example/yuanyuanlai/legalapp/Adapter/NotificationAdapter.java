package com.example.yuanyuanlai.legalapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.yuanyuanlai.legalapp.Bean.ItemNotification;
import com.example.yuanyuanlai.legalapp.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Creste by GongYunHao on 2018/5/31
 */
public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<ItemNotification> notificationBeanList=new ArrayList<>(  );
    private Context mContext;

    public NotificationAdapter(Context context, List<ItemNotification> notificationBeanList) {
        this.notificationBeanList = notificationBeanList;
        mContext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_notification,parent,false);
        return new MyNotifyViewHolder(view);
    }

    class MyNotifyViewHolder extends RecyclerView.ViewHolder{
        View notifyview;
        TextView detail_message,detail_time,mdate;
        RelativeLayout rela_date,rela_mess;

        public MyNotifyViewHolder(View itemView) {
            super( itemView );
            notifyview=itemView;
            detail_message=itemView.findViewById( R.id.notif_detail );
            detail_time=itemView.findViewById( R.id.notif_detail_time );
            mdate=itemView.findViewById( R.id.recycler_date_tv );
            rela_date=itemView.findViewById( R.id.relative_date );
            rela_mess=itemView.findViewById( R.id.relative_message );
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyNotifyViewHolder notiViewHolder=(MyNotifyViewHolder)holder;
        ItemNotification itemNotification=notificationBeanList.get( position );
        if (position==0){
            //显示日期item
            notiViewHolder.rela_date.setVisibility( View.VISIBLE );
            notiViewHolder.rela_mess.setVisibility( View.GONE );
            notiViewHolder.mdate.setText( itemNotification.getDate());
            notiViewHolder.detail_time.setText( "" );
            notiViewHolder.detail_message.setText( "" );
        }else {
            if ( !itemNotification.getDate().equals( notificationBeanList.get( position-1 ))){
                //显示日期item
                notiViewHolder.rela_date.setVisibility( View.VISIBLE );
                notiViewHolder.rela_mess.setVisibility( View.GONE );
                notiViewHolder.mdate.setText( itemNotification.getDate());
                notiViewHolder.detail_time.setText( "" );
                notiViewHolder.detail_message.setText( "" );
            }else {
                //显示messages
                notiViewHolder.rela_date.setVisibility( View.GONE );
                notiViewHolder.rela_mess.setVisibility( View.VISIBLE );
                notiViewHolder.mdate.setText( "" );
                notiViewHolder.detail_time.setText( itemNotification.getDate());
                notiViewHolder.detail_message.setText( itemNotification.getDetail_notification() );
                }
        }
    }

    @Override
    public int getItemCount() {
        return notificationBeanList.size();
    }
}
