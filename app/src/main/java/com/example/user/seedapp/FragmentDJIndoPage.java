package com.example.user.seedapp;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.seedapp.com.add.model.DJInfo;

import org.json.JSONObject;

import java.net.URL;

/**
 * Created by LacNoito on 2/22/2015.
 */
public class FragmentDJIndoPage extends Fragment {

    private DJInfo djInfo;
    private static View view;

    public void setObject(DJInfo djInfo){
        this.djInfo = djInfo;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view!=null){
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup!=null){
                viewGroup.removeView(view);
            }
        }

        try {
            View view = inflater.inflate(R.layout.dj_infomation_page, container, false);

            TextView nameDJ = (TextView) view.findViewById(R.id.name_dj);
            TextView online_time = (TextView) view.findViewById(R.id.online_time);
            ImageView imageDJ = (ImageView) view.findViewById(R.id.imageDJ);
            Button btnTextToDJ = (Button) view.findViewById(R.id.text_dj);
            btnTextToDJ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.customdialog);
                    dialog.setCancelable(true);
                    Button btnSend = (Button) dialog.findViewById(R.id.btnSend);
                    Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
            nameDJ.setText(nameDJ.getText().toString() + " " + djInfo.getJSONObject().getString("name"));
            online_time.setText(djInfo.getJSONObject().getString("time_online"));

//            URL newurl = new URL(jsonObject.getString("image"));
//            Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            imageDJ.setImageBitmap(djInfo.getBitmap());


            return view;
        }catch (Exception e){

        }
        return view;
    }
}
