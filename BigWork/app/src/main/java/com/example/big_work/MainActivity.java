package com.example.big_work;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView nextIv,lastIv,playIv;
    TextView singerTv,songTv;
    RecyclerView musicRv;

    //数据源
    List<LocalMusicBean>mDatas;
    private LocalMusicAdapter adapter;
    private int position;

    //记录当前播放音乐的位置
    int currentPlayPosition = -1;

    //记录暂停音乐时的进度条位置
    int currentPausePositionInSong = 0;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mDatas = new ArrayList<>();
        mediaPlayer = new MediaPlayer();

        //创建适配器
        adapter = new LocalMusicAdapter(this, mDatas);
        musicRv.setAdapter(adapter);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        musicRv.setLayoutManager(layoutManager);

        //加载本地数据源
        loadLocalMusicData();

        //设置没一项的监听事件
        setEventListener();
    }

    private void setEventListener() {
        /*设置每一项的点击事件*/
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                currentPlayPosition = position;
                LocalMusicBean musicBean = mDatas.get(position);
                playMusicInMusicBean(musicBean);

            }
        });
    }

    public void playMusicInMusicBean(LocalMusicBean musicBean) {
        /*根据传入对象播放音乐*/
        //设置底部显示的歌手和歌名
        singerTv.setText(musicBean.getSinger());
        songTv.setText(musicBean.getSong());
        stopMusic();

        //重置多媒体播放器
        mediaPlayer.reset();

        //设置新的路径
        try {
            mediaPlayer.setDataSource(musicBean.getPath());
            playMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playMusic(){
        /*播放音乐*/

        if (mediaPlayer!=null && !mediaPlayer.isPlaying()){  //mediaPlayer不为空，同时不是播放状态
            if (currentPausePositionInSong==0) {    //重头开始播放
                try{
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                //从暂停处开始播放
                mediaPlayer.seekTo(currentPausePositionInSong);
                mediaPlayer.start();
            }
            playIv.setImageResource(R.mipmap.icon_pause);
        }
    }

    private void stopMusic() {
        /*停止音乐*/
       if (mediaPlayer!=null){    //如果mediaPlayer不为空那就可以操作
           currentPausePositionInSong = 0;
           mediaPlayer.pause();   //先暂停
           mediaPlayer.seekTo(0);   //正在播放的进度条回到最初
           mediaPlayer.stop();    //停止
           playIv.setImageResource(R.mipmap.icon_play);    //改为播放图标
       }
    }

    private void pauseMusic() {
        /*暂停音乐*/
        if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
            currentPausePositionInSong =  mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            playIv.setImageResource(R.mipmap.icon_play);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    private void loadLocalMusicData() {
        /*加载本地存储当中的音乐文件到集合中*/
        // 1.获取ContentResolver对象(这是接受者，用来接收contentProvider内容提供者提供的数据，实现跨进程通信的操作)
        ContentResolver resolver = getContentResolver();

        // 2.获取本地音乐存储的uri地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        // 3.查询地址
        Cursor cursor = resolver.query(uri,null,null,null,null);

        // 4.遍历Cursor对象 (通过它是否能移动到下一个来遍历)
        int id = 0; //我自己给歌曲命名id
        while(cursor.moveToNext()){
            String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            id++;
            String sid = String.valueOf(id);
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            String time = sdf.format(new Date(duration));

            //将一行当中的数据封装到对象当中
            LocalMusicBean bean = new LocalMusicBean(sid, song, singer, album, time, path);
            mDatas.add(bean);
        }
        adapter.notifyDataSetChanged();
    }

    private void initView(){
        //初始化控件的函数

        nextIv = findViewById(R.id.l_m_bottom_next);
        lastIv = findViewById(R.id.l_m_bottom_last);
        playIv = findViewById(R.id.l_m_bottom_play);
        singerTv = findViewById(R.id.l_m_singer);
        songTv = findViewById(R.id.l_m_song);
        musicRv = findViewById(R.id.l_m_music);

        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.l_m_bottom_last:
                if (currentPlayPosition==0) {
                    Toast.makeText(this,"这是第一首",Toast.LENGTH_SHORT);
                    return;
                }
                currentPlayPosition = currentPlayPosition - 1;
                LocalMusicBean lastBean = mDatas.get(currentPlayPosition);
                playMusicInMusicBean(lastBean);
                break;

            case R.id.l_m_bottom_next:
                if (currentPlayPosition==mDatas.size()-1) {
                    Toast.makeText(this,"这是最后一首",Toast.LENGTH_SHORT);
                    return;
                }
                currentPlayPosition = currentPlayPosition + 1;
                LocalMusicBean nextBean = mDatas.get(currentPlayPosition);
                playMusicInMusicBean(nextBean);
                break;

            case R.id.l_m_bottom_play:
                if (currentPlayPosition == -1){  //并没有选中要播放的音乐

                    Toast.makeText(this,"选择想要播放的音乐",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mediaPlayer.isPlaying()) {
                    //此时处于播放状态，需要暂停
                    pauseMusic();
                }else {
                    //此时没有播放音乐，点击开始播放音乐
                    playMusic();
                }

                break;
        }
    }


}
