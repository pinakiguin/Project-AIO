package com.github.abusalam.android.projectaio;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.abusalam.android.projectaio.ajax.VolleyAPI;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends ActionBarActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    protected EditText etMobileNo;
    protected ImageButton GetImgButton;
    protected TextView msgLoginText;
    protected ProgressBar pbLoginWait;
    protected Button btnLogin;

    protected RequestQueue rQueue;
    protected JSONObject apiRespUserStat;

    View.OnClickListener btnUpdateClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            etMobileNo.setVisibility(View.GONE);
            GetImgButton.setVisibility(View.GONE);
            msgLoginText.setText(getText(R.string.login_wait_message));
            pbLoginWait.setVisibility(View.VISIBLE);



            JSONObject jsonPost = new JSONObject();

            try {
                jsonPost.put("API", "RU");
                jsonPost.put("mdn", etMobileNo.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    DashAIO.API_URL, jsonPost,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            Toast.makeText(getApplicationContext(), response.optString("Status"), Toast.LENGTH_SHORT).show();
                            apiRespUserStat = response;
                            btnLogin.setVisibility(View.VISIBLE);
                            pbLoginWait.setVisibility(View.GONE);
                            msgLoginText.setText(response.optString("Status"));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String msgError = "Error: " + error.getMessage();
                    Log.e(TAG, msgError);
                    Toast.makeText(getApplicationContext(), msgError, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            );

            // Adding request to request queue
            jsonObjReq.setTag(TAG);
            rQueue.add(jsonObjReq);
        }
    };

    View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent data = new Intent();
            data.putExtra(DashAIO.PREF_KEY_UserID, apiRespUserStat.optString(DashAIO.PREF_KEY_UserID));
            data.putExtra(DashAIO.PREF_KEY_Secret, apiRespUserStat.optString(DashAIO.PREF_KEY_Secret));
            setResult(RESULT_OK, data);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        msgLoginText = (TextView) findViewById(R.id.tvLoginMessage);
        etMobileNo = (EditText) findViewById(R.id.etUserMobile);
        pbLoginWait = (ProgressBar) findViewById(R.id.pbLoginWait);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        GetImgButton = (ImageButton) findViewById(R.id.btnUpdateProfile);

        pbLoginWait.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);

        GetImgButton.setOnClickListener(btnUpdateClick);
        btnLogin.setOnClickListener(loginClick);

        rQueue = VolleyAPI.getInstance(this).getRequestQueue();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleyAPI.getInstance(LoginActivity.this).getRequestQueue().cancelAll(TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
