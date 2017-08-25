package com.example.administrator.mydiary;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;

public class SignActivity extends AppCompatActivity {
    final static int LOGIN_SIGN = 496431;
    final static int AFTER_SIGN = 846244;
    final static int LOGIN_CHECK = 416843;
    int id_checked = -1;
    private EditText edtID = null;
    private EditText edtPW = null;
    private EditText edtPWC = null;
    private TextView tv_idc = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        final TextView result = (TextView) findViewById(R.id.tv_error);
        tv_idc = (TextView)findViewById(R.id.tv_idc);
        edtID = (EditText) findViewById(R.id.et_id);
        edtID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                id_checked = 0;
                tv_idc.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtPW = (EditText) findViewById(R.id.et_pw);
        edtPWC = (EditText) findViewById(R.id.et_pwc);

        Button idc = (Button)findViewById(R.id.btn_idc);
        idc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new idCheck().execute(edtID.getText().toString());
            }
        });


        Button ok = (Button)findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_checked != 1){
                    Toast.makeText(getApplicationContext(),"아이디 중복확인을 해주세요.",Toast.LENGTH_SHORT).show();
                }else if (id_checked == 1){
                    if(edtPW.getText().toString().equals(edtPWC.getText().toString())){
                        result.setText("");
                        new HttpSign().execute(edtID.getText().toString(),edtPW.getText().toString());
                    }else{
                        result.setText("비밀번호가 일치하지 않습니다");
                    }

                }
            }
        });
    }

    public class HttpSign extends AsyncTask<String, Integer, String> {
        private ProgressDialog waitDlg = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //서버에 요청 동안 Wating dialog를 보여주도록 한다.
            waitDlg = new ProgressDialog( SignActivity.this );
            waitDlg.setMessage(" 확인 중");
            waitDlg.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String id = params[0];
            String pw = params[1];

            String result = sign( id, pw);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //서버 요청 완료 후 Waiting dialog를 제거한다.
            if( waitDlg != null ) {
                waitDlg.dismiss();
                waitDlg = null;
            }

            // 결과 처리
            if( s.equals("1") ) {
                // success
                setResult(AFTER_SIGN);
                finish();
            }
            else {
                // fail
                TextView result = (TextView) findViewById(R.id.tv_error);
                result.setText("sign fail");
            }
        }
    }


    public String sign(String id, String pw) {
        String weburl = "http://192.168.0.46:8080/rest/insertone";

        HttpRequest request = null;
        String response = "";

        try {
            request = new HttpRequest(weburl).addHeader("charset", "utf-8");
            request.addParameter("id", id );
            request.addParameter("pw", pw );
            int httpCode = request.post();

            if( httpCode == HttpURLConnection.HTTP_OK ){
                response = request.getStringResponse();
            }
            else {
                // error
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            request.close();
        }

        return response;
    }


    public class idCheck extends AsyncTask<String, Integer, String> {
        private ProgressDialog waitDlg = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //서버에 요청 동안 Wating dialog를 보여주도록 한다.
            waitDlg = new ProgressDialog( SignActivity.this );
            waitDlg.setMessage(" 확인 중");
            waitDlg.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String id = params[0];

            String result = check( id);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //서버 요청 완료 후 Waiting dialog를 제거한다.
            if( waitDlg != null ) {
                waitDlg.dismiss();
                waitDlg = null;
            }

            // 결과 처리
            if( s.equals("0") ) {
                Toast.makeText(getApplicationContext(),"사용가능한 아이디 입니다.",Toast.LENGTH_SHORT).show();
                tv_idc.setText("사용가능");
                id_checked = 1;

            }
            else {
                Toast.makeText(getApplicationContext(),"이미 존재하거나 사용할수 없는 아이디 입니다.",Toast.LENGTH_SHORT).show();
                tv_idc.setText("사용불가");
            }
        }
    }


    public String check(String id) {
        String weburl = "http://192.168.0.46:8080/rest/idcheck";

        HttpRequest request = null;
        String response = "";

        try {
            request = new HttpRequest(weburl).addHeader("charset", "utf-8");
            request.addParameter("id", id );
            int httpCode = request.post();
            if( httpCode == HttpURLConnection.HTTP_OK ){
                response = request.getStringResponse();
            }
            else {
                // error
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            request.close();
        }

        return response;
    }

}
