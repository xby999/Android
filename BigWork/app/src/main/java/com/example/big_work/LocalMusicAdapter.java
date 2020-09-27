package com.example.big_work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.LocalMusicHolder> {
    Context context;
    List<LocalMusicBean> mDatas;

    OnItemClickListener onItemClickListener;   //传入接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void OnItemClick(View view,int position);          //传递被点击的view，和被点击的位置
    }

    public LocalMusicAdapter(Context context, List<LocalMusicBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public LocalMusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_l_m,parent,false);
        LocalMusicHolder holder = new LocalMusicHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocalMusicHolder holder, final int position) {

        LocalMusicBean musicBean = mDatas.get(position);
        holder.idTv.setText(musicBean.getId());
        holder.songTv.setText(musicBean.getSong());
        holder.singerTv.setText(musicBean.getSinger());
        holder.ablumTv.setText(musicBean.getAlbum());
        holder.timeTv.setText(musicBean.getDuration());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(v,position);     //当每一项被点击的时候，传入itemClick方法，这是接口对调。
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class LocalMusicHolder extends RecyclerView.ViewHolder{
        TextView idTv,songTv,singerTv,timeTv,ablumTv;

        public LocalMusicHolder(@NonNull View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.item_l_m_num);
            songTv = itemView.findViewById(R.id.item_l_m_song);
            singerTv = itemView.findViewById(R.id.item_l_m_singer);
            timeTv = itemView.findViewById(R.id.item_l_m_duration);
            ablumTv = itemView.findViewById(R.id.item_l_m_album);
        }
    }
}
