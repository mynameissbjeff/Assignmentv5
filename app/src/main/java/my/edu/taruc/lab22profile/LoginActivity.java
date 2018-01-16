package my.edu.taruc.lab22profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userPassword;
    private Button btnLogin;
    private Button btnRegister;

    //private String masterName = "master";
    //private String masterPassword = "abcd1234";

    // for network
    private ProgressDialog pDialog;
    RequestQueue queue;
    // create array for user class
    public static List<User> userClassList = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_login);

        pDialog = new ProgressDialog(this);
        userName = (EditText) findViewById(R.id.editTextName);
        userPassword = (EditText) findViewById(R.id.editTextPassword);
        btnLogin = (Button) findViewById(R.id.buttonLogin);
        btnRegister = (Button) findViewById(R.id.buttonRegister);

        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }

        downloadUserInfo(getApplicationContext(),getString(R.string.user_url));
    }


    private boolean login()
    {
        String inputName = userName.getText().toString();
        String inputPassword =userPassword.getText().toString();

        for(int  i = 0; i < userClassList.size(); i++) {
            // Toast.makeText(getApplicationContext(), "U in here", Toast.LENGTH_LONG).show();
            if ( inputName.equals(userClassList.get(i).getName().toString())) {
                if(inputPassword.equals(userClassList.get(i).getPassword().toString())) {
                    SharedPreferences sharedPreferences = getSharedPreferences("CurrentUser",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Name",userClassList.get(i).getName());
                    editor.putString("Password", userClassList.get(i).getPassword());
                    editor.putInt("Score_addition", userClassList.get(i).getScore_addition());
                    editor.putInt("Score_subtraction", userClassList.get(i).getScore_subtraction());
                    editor.putInt("Score_multipication", userClassList.get(i).getScore_multipication());
                    editor.putInt("Score_division", userClassList.get(i).getScore_division());
                    editor.putInt("Score_mix",userClassList.get(i).getScore_mix());
                    editor.apply();
                    editor.commit();
                    return true;
                }
            }
            else if(inputName.equals(getString(R.string.masterName))){
                if(inputPassword.equals(getString(R.string.masterPassword))) {
                    return true;
                }
            }
        }
        //}
        return false;
    }

    public void startMain(View v)
    {
        boolean isLogin = login();
        if(isLogin)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Login failed"+getString(R.string.masterName)+userName.getText().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void downloadUserInfo(Context context, String url) {
        // Instantiate the RequestQueue
        queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            userClassList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject userResponse = (JSONObject) response.get(i);
                                String username, password;
                                int score_addition, score_subtraction,score_multipication, score_division,score_mix;

                                username = userResponse.getString("username");
                                password = userResponse.getString("password");
                                score_addition = userResponse.getInt("score_addition");
                                score_subtraction = userResponse.getInt("score_subtraction");
                                score_multipication = userResponse.getInt("score_multipication");
                                score_division = userResponse.getInt("score_division");
                                score_mix = userResponse.getInt("score_mix");
                                User userClass = new User(username, password, score_addition,score_subtraction
                                        ,score_multipication,score_division,score_mix);
                                userClassList.add(userClass);
                            }
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }


    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    public void RegisterMode(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }


}
