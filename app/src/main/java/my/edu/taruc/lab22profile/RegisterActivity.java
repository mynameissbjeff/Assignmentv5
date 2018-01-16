package my.edu.taruc.lab22profile;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputUsername;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private EditText inputStandard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_register);

        inputUsername = (EditText)findViewById(R.id.editTextName);
        inputPassword = (EditText)findViewById(R.id.editTextPassword);
        inputConfirmPassword = (EditText)findViewById(R.id.editTextConfirmPassword);
        inputStandard = (EditText)findViewById(R.id.editTextStandard);
    }

    public void regsiterUser(View view)
    {
        if(checkPassword()) {
            User user = new User();
            user.setName(inputUsername.getText().toString());
            user.setPassword(inputPassword.getText().toString());
            user.setStandard(inputStandard.getText().toString());

            if(!isConnected())
            {
                Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
            }

            try {
                makeServiceCall(this, getString(R.string.insert_newuser_url), user);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }

    // this is for read data to database
    public void makeServiceCall(Context context, String url, final User user) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if (success==0) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", user.getName());
                    params.put("password", user.getPassword());
                    params.put("standard", user.getStandard());
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // check internet connection
    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    private boolean checkPassword()
    {
        if(inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString()))
        {
            return true;
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please ensure password is correct.", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}
