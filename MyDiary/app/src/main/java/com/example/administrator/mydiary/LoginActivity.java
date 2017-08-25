package com.example.administrator.mydiary;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import java.io.IOException;
import java.net.HttpURLConnection;


public class LoginActivity extends AppCompatActivity {
    final static int LOGIN_SIGN = 496431;
    final static int AFTER_SIGN = 846244;
    final static int LOGIN_CHECK = 416843;
    private EditText edtID = null;
    private EditText edtPW = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtID = (EditText) findViewById(R.id.et_id);
        edtPW = (EditText) findViewById(R.id.et_pw);

        Button btnSign = (Button)findViewById(R.id.btn_sign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(LOGIN_SIGN);
                finish();
            }
        });
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpLogin().execute(edtID.getText().toString(),edtPW.getText().toString());
            }
        });
    }

    public class HttpLogin extends AsyncTask<String, Integer, String> {
        private ProgressDialog waitDlg = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //서버에 요청 동안 Wating dialog를 보여주도록 한다.
            waitDlg = new ProgressDialog( LoginActivity.this );
            waitDlg.setMessage(" 버전 확인 중");
            waitDlg.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String id = params[0];
            String pw = params[1];

            String result = login( id, pw);

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
                Intent data = new Intent();
                data.putExtra("id",edtID.getText().toString());
                setResult(LOGIN_CHECK,data);
                finish();
            }
            else {
                // fail
                TextView result = (TextView) findViewById(R.id.tv_error);
                result.setText("login fail");
            }
        }
    }


    public String login(String id, String pw) {
        String weburl = "http://192.168.0.46:8080/rest/login";

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


}
