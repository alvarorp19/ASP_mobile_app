// Parts of the code of this example app have ben taken from:
// https://enoent.fr/posts/recyclerview-basics/
// https://developer.android.com/guide/topics/ui/layout/recyclerview

package dte.masteriot.mdp.roverApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import dte.masteriot.mdp.roverApp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

public class MainActivity extends AppCompatActivity  {


    //MIME types
    static final String CONTENT_TYPE_JSON = "application/json";

    //constants for debugging puporses
    final static String LOADWEBTAG = "LOADWEB";
    final static String PARSINGJSONTAG = "PARSINGJSONTAG";
    final static String MAINACTIVITYTAG = "MAINACTIVITYTAG";

    //Handler Keys

    final static String HANDLER_KEY_JSON = "jsonInfo";

    //layout elements

    private EditText editTextPassword;
    private EditText editTextUser;
    private Button btSummitData;


    //API URL

    final static String APIURL = "https://srv-iot.diatel.upm.es";

    //retrofit

    private Retrofit retrofit;


    //parameters to be sent to the second activity

    final static String EXTRA_INFO_ACTIVITY_USER_TOKEN = "USERTOKEN";

    String userToken = "";
    String userRefreshToken = "";


    interface requestUser{

        @POST("/api/auth/login")
        Call<UserRequest> PostUser(@Body RequestPost RequestPost);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting recourses from the layout
        editTextUser = findViewById(R.id.editTextField1);
        editTextPassword = findViewById(R.id.editTextField2);
        btSummitData = findViewById(R.id.button1);

        //RetroFit initialization

        retrofit = new Retrofit.Builder()
                .baseUrl(APIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //setting callback for button

        btSummitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checking that login data is correct to launch the next activity
                //user and password will be used to perform HTTP requests

                if(editTextUser.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()){

                    Log.d(MAINACTIVITYTAG,"boton pressed -> incorrect login");
                    Toast.makeText(MainActivity.this, "INCORRECT CREDENTIALS", Toast.LENGTH_SHORT).show();

                }else{

                    Log.d(MAINACTIVITYTAG,"boton pressed -> correct login");

                    //checking whether the credentials are correct or not

                    userLoginRequest(editTextUser.getText().toString(), editTextPassword.getText().toString());

                }


            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void userLoginRequest(String username, String password){

        requestUser requestUser = retrofit.create(requestUser.class);
        requestUser.PostUser(new RequestPost(username,password)).enqueue(new Callback<UserRequest>() {
            @Override
            public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {

                try{

                    if (response.code() == 200){ //OK

                        userToken = response.body().getToken();
                        userRefreshToken = response.body().getRefreshToken();

                        Log.d(MAINACTIVITYTAG,"respuesta:");
                        Log.d(MAINACTIVITYTAG,"Response from API (token) -> " + userToken);
                        Log.d(MAINACTIVITYTAG,"Response from API (Refresh token) -> " + userRefreshToken);

                        launchSecondActivity();

                    }else{//ERROR
                        Log.d(MAINACTIVITYTAG,"Codigo de error:" + response.code());

                        Toast.makeText(MainActivity.this, "INCORRECT CREDENTIALS", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Log.d(MAINACTIVITYTAG,"excepcion en la respuesta : " + e);
                }
            }

            @Override
            public void onFailure(Call<UserRequest> call, Throwable throwable) {
                Log.d(MAINACTIVITYTAG,"Error response from API -> " + throwable.getMessage().toString());
            }
        });

    }


    private void launchSecondActivity(){

        //launching 3º activity
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra(EXTRA_INFO_ACTIVITY_USER_TOKEN,userToken);
        Log.d(MAINACTIVITYTAG,"Launching 2º activity");
        startActivity(intent);
    }

}