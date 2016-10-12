package com.example.ilham.opangdrivermobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ilham.opangdrivermobile.Connection.IConnectionResponseHandler;
import com.example.ilham.opangdrivermobile.Connection.RequestRest;
import com.example.ilham.opangdrivermobile.Setup.ApplicationConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private EditText emaillogin,passwordlogin;
    private Button loginButton;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bindingXML();

    }

    private void  bindingXML(){
        emaillogin=(EditText)findViewById(R.id.email);
        passwordlogin=(EditText)findViewById(R.id.password);

        loginButton=(Button)findViewById(R.id.email_sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkLogin()){
                    attemptLogin();
                }
            }
        });
    }

    private boolean checkLogin(){
        emaillogin.setError(null);
        passwordlogin.setError(null);

        String email=emaillogin.getText().toString();
        String password=passwordlogin.getText().toString();
        View focusView = null;
        boolean cancel = false;
        if (TextUtils.isEmpty(password)){
            passwordlogin.setError("This Field is Required");
            focusView = passwordlogin;
            cancel =true;
        }
        if (TextUtils.isEmpty(email)){
            emaillogin.setError("This Field is Required");
            focusView = emaillogin;
            cancel =true;
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        }
            return true;


    }

    private void attemptLogin() {
        final ProgressDialog dialog = ProgressDialog.show(Login.this, "Connecting", "Send Data", true);
        RequestRest req = new RequestRest(Login.this, new IConnectionResponseHandler(){
            @Override
            public void OnSuccessArray(JSONArray result){
                Log.i("result", result.toString());
                dialog.dismiss();
            }

            @Override
            public void onSuccessJSONObject(String result){
                try {
                    JSONObject obj = new JSONObject(result);
                    Log.i("Test", result);
                    dialog.dismiss();

                    if (obj.getBoolean("success")){
                    JSONObject profile=obj.getJSONObject("Profile");
                        Log.i("profile",profile.toString());

                        saveToPreference(profile.getString("ID"),profile.getString("Name"),
                                profile.getString("Email"),profile.getString("ID_role"));

                        Intent i = new Intent(Login.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }else {

                    }



                } catch (JSONException e){

                }


            }

            @Override
            public void onFailure(String e){
                Log.i("Test", e);
                dialog.dismiss();
            }

            @Override
            public void onSuccessJSONArray(String result){
                Log.i("Test", result);
                dialog.dismiss();
            }
        });


        req.goLogin( emaillogin.getText().toString(), passwordlogin.getText().toString(),"2");
    }

    public void saveToPreference(String iduser,String name,String email,String idrole){
        prefs = getSharedPreferences(ApplicationConstants.USER_PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstants.ID_USER,iduser);
        editor.putString(ApplicationConstants.NAME,name);
        editor.putString(ApplicationConstants.EMAIL,email);
        editor.putString(ApplicationConstants.ID_ROLE,idrole);

    }
}
