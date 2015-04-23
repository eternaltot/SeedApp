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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.seedapp.com.add.model.DJInfo;
import com.example.user.seedapp.com.add.view.RoundedImageView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LacNoito on 2/22/2015.
 */
public class FragmentDJIndoPage extends Fragment {

    private DJInfo djInfo;
    private static View view;
    private MainActivity mainActivity;
    private Button btn_to_dj;

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
            mainActivity = (MainActivity) getActivity();

            TextView nameDJ = (TextView) view.findViewById(R.id.name_dj);
            TextView online_time = (TextView) view.findViewById(R.id.online_time);
            CircleImageView imageDJ = (CircleImageView) view.findViewById(R.id.imageDJ);
            btn_to_dj = (Button) view.findViewById(R.id.text_dj);
            mainActivity.setTypeFace(nameDJ);

            btn_to_dj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.customdialog);
                    dialog.setCancelable(true);
                    Button btnSend = (Button) dialog.findViewById(R.id.btnSend);
                    Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                    final EditText editText = (EditText) dialog.findViewById(R.id.editText);
                    final EditText editFrom = (EditText) dialog.findViewById(R.id.editFrom);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    btnSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!editText.getText().toString().isEmpty() && !editFrom.getText().toString().isEmpty()){
                                if(sendText(editText,editFrom) == 201){
                                    dialog.dismiss();
                                }

                            }else{
                                Toast.makeText(getActivity(),"กรุณาใส่ข้อความและผู้ส่ง",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.show();
                }
            });
            nameDJ.setText(djInfo.getName());

            online_time.setText(djInfo.getStartTime() + " - " + djInfo.getStopTime());

            imageDJ.setImageBitmap(djInfo.getBitmap());

            DisplayMetrics displaymetrics = new DisplayMetrics();
            mainActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            android.view.ViewGroup.LayoutParams layoutimageDJ = imageDJ.getLayoutParams();
            ((ViewGroup.MarginLayoutParams) imageDJ.getLayoutParams()).leftMargin = (width/4) - (layoutimageDJ.width/2)-5;

            Log.d("system", "(width/4) - (layoutimageDJ.width/2) " + ((width/4) - (layoutimageDJ.width/2)));


            return view;
        }catch (Exception e){

        }
        return view;
    }

    public int sendText(EditText editText,EditText editFrom){
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(MainActivity.url_TEXT_TO_DJ);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        try{
            JSONObject object = new JSONObject();
            object.put("text",editText.getText().toString());
            object.put("from",editFrom.getText().toString());
            StringEntity se = new StringEntity(object.toString(), HTTP.UTF_8);
            httpPost.setEntity(se);

            HttpResponse response = client.execute(httpPost);

            if(response.getStatusLine().getStatusCode() == 201){
                Toast.makeText(getActivity(),"Send Message Complete",Toast.LENGTH_LONG).show();
                return response.getStatusLine().getStatusCode();
            }
        }catch (Exception e){
            Log.e("Send Text",e.getMessage());
        }
        return 0;
    }
}
