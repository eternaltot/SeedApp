package com.example.user.seedapp;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
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
import com.example.user.seedapp.com.add.view.RoundedImageView;

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
            RoundedImageView imageDJ = (RoundedImageView) view.findViewById(R.id.imageDJ);
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

//            int width = 150;
//            int height = 150;
//
//            Bitmap circleBitmap = Bitmap.createBitmap(djInfo.getBitmap().getWidth(), djInfo.getBitmap().getHeight(), Bitmap.Config.ARGB_8888);
//
//            BitmapShader shader = new BitmapShader(djInfo.getBitmap(),  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//            Paint paint = new Paint();
//            paint.setShader(shader);
//
//            Canvas c = new Canvas(circleBitmap);
//            c.drawCircle(djInfo.getBitmap().getWidth()/2, djInfo.getBitmap().getHeight()/2, djInfo.getBitmap().getWidth()/2, paint);
//
//            imageDJ.setImageBitmap(circleBitmap);

            return view;
        }catch (Exception e){

        }
        return view;
    }
}
