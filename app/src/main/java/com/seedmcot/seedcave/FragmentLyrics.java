package com.seedmcot.seedcave;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seedmcot.seedcave.add.model.Music;


/**
 * Created by LacNoito on 2/15/2015.
 */
public class FragmentLyrics extends Fragment {
    private static View view;
    private Music music;
    private MainActivity mainActivity;
    private ImageView back_bt;


    public void setMusic(Music music) {
        this.music = music;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view!=null){
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup!=null){
                viewGroup.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.fragment_lyrics, container, false);

            TextView name = (TextView) view.findViewById(R.id.name);
            TextView nowPlaying = (TextView) view.findViewById(R.id.now_playing);

            TextView author = (TextView) view.findViewById(R.id.txtAuthor);
            TextView author2 = (TextView) view.findViewById(R.id.txtAuthor2);
            TextView author3 = (TextView) view.findViewById(R.id.txtAuthor3);
            TextView lyrics = (TextView) view.findViewById(R.id.lyrics);
            TextView tv_lyrics = (TextView) view.findViewById(R.id.tv_lyrics);
            back_bt = (ImageView) view.findViewById(R.id.back_bt);
            name.setSelected(true);

            back_bt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mainActivity.setFragmentNoBack(mainActivity.getFragmentMain());
                }
            });


            mainActivity = (MainActivity) getActivity();
            mainActivity.setTypeFace(nowPlaying);
            mainActivity.setTypeFace(tv_lyrics);
            mainActivity.setTypeFaceOther(name);
            mainActivity.setTypeFaceOther(author);
            mainActivity.setTypeFaceOther(author2);
            mainActivity.setTypeFaceOther(author3);
            mainActivity.setTypeFaceOther(lyrics);

            if(music != null){
                if(music.getName() != null)
                    name.setText(music.getName());
                else
                    name.setText(name.getText());
//                if(music.getAuthor() != null)
//                    author.setText(author.getText() + music.getAuthor());
//                else
//                    author.setText(author.getText() + "-");
//                if(music.getTempo() != null)
//                    tempo.setText(tempo.getText() + music.getTempo());
//                else
//                    tempo.setText(tempo.getText() + "-");
//                if(music.getCompose() != null)
//                    compose.setText(compose.getText() + music.getCompose());
//                else
//                    compose.setText(compose.getText() + "-");
                if(music.getLyrics() != null)
                    lyrics.setText(lyrics.getText() + music.getLyrics());
                else
                    lyrics.setText(lyrics.getText());
                if(music.getAuthor() != null && !music.getAuthor().equals(""))
                    author.setText("คำร้อง : " + music.getAuthor());
                else
                    author.setText("คำร้อง : -");
                if(music.getAuthor2() != null && !music.getAuthor2().equals(""))
                    author2.setText("ทำนอง : " + music. getAuthor2());
                else
                    author2.setText("ทำนอง : -");
                if(music.getAuthor3() != null && !music.getAuthor3().equals(""))
                    author3.setText("เรียบเรียง : " + music.getAuthor3());
                else
                    author3.setText("เรียบเรียง : -");
            }

        }catch (Exception e){
            Log.e("Lyrics",e.getMessage());
        }

        setBackEvent();

        return view;
    }

    public void setBackEvent(){
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.i("system", "keyCode: " + keyCode);
//                if( keyCode == KeyEvent.KEYCODE_BACK ) {
//                    mainActivity.setFragmentNoBack(mainActivity.getFragmentMain());
//                    return true;
//                } else {
//                    return false;
//                }
                return view.onKeyDown(keyCode, event);
            }
        });
    }

}
