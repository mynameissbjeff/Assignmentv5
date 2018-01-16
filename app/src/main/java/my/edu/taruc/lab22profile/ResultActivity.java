package my.edu.taruc.lab22profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class ResultActivity extends AppCompatActivity {

    public TextView textViewResult;
    public TextView textViewPlayer1Result;
    public TextView textViewPlayer2Result;
    public TextView textViewComment;
    public Button buttonOk;
    String result,player1,player2, comment;
    String operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Bundle bundle = getIntent().getExtras();
        result= bundle.getString("layout");
        if(result.equals("practice")){
            setContentView(R.layout.activity_result);
            textViewResult = (TextView)findViewById(R.id.textViewResult);
            textViewComment = (TextView) findViewById(R.id.textViewComment);
            getResult1();
            saveHighscore();
           // saveRecord();
        }
        else if(result.equals("challenge")){
            setContentView(R.layout.activity_result2);
            textViewPlayer1Result = (TextView)findViewById(R.id.textViewPlayer1Result);
            textViewPlayer2Result = (TextView)findViewById(R.id.textViewPlayer2Result);
            textViewComment = (TextView) findViewById(R.id.textViewComment2);
            getResult2();
        }
        buttonOk = (Button)findViewById(R.id.buttonOk);
    }

   // public void saveRecord() {
   //     Score score = new Score();
//
   //     score.setScore(textViewResult.getText().toString());
//
   //     if (!isConnected()) {
   //         Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_SHORT).show();
   //     }
//
   //     try {
   //         makeServiceCall(this, getString(R.string.insert_score_url), score);
   //     } catch (Exception e) {
   //         e.printStackTrace();
   //         Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
   //     }
   // }

    public void saveHighscore()
    {
        int UserScore_addition=0;
        int UserScore_subtraction=0;
        int UserScore_multipication=0;
        int UserScore_division=0;
        int UserScore_mix=0;

        // assign value to original user data
        SharedPreferences sharedPreferences = getSharedPreferences("CurrentUser",Context.MODE_PRIVATE);
        String UserName = sharedPreferences.getString("Name","");
        String UserPassword = sharedPreferences.getString("Password","");
        UserScore_addition=sharedPreferences.getInt("Score_addition", 0);
        UserScore_subtraction=sharedPreferences.getInt("Score_subtraction", 0);
        UserScore_multipication=sharedPreferences.getInt("Score_multipication", 0);
        UserScore_division=sharedPreferences.getInt("Score_division", 0);
        UserScore_mix = sharedPreferences.getInt("Score_mix", 0);

        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }

        // get the previous information from other activity
        Bundle bundle = getIntent().getExtras();
        // get the operator user select
        operator = bundle.getString("operator");
        // get the current score of the user
        result= bundle.getString("result");
        int currentHighScore = Integer.parseInt(result);


        // assign score to specify operator
        if(operator.equals("ADD")){
            // compare if the current score is higher than user original score
            if(UserScore_addition < currentHighScore)
            {
                UserScore_addition = currentHighScore;
            }
        }
        else if(operator.equals("SUB")){
            // compare if the current score is higher than user original score
            if(UserScore_subtraction < currentHighScore)
            {
                UserScore_subtraction = currentHighScore;
            }
        }
        else if(operator.equals("MUL")){
            // compare if the current score is higher than user original score
            if(UserScore_multipication < currentHighScore)
            {
                UserScore_multipication = currentHighScore;
            }
        }
        else if(operator.equals("DIV")){
            // compare if the current score is higher than user original score
            if(UserScore_division < currentHighScore)
            {
                UserScore_division = currentHighScore;
            }
        }
        else if(operator.equals("MIX"))
        {
            // compare if the current score is higher than user original score
            if(UserScore_mix < currentHighScore)
            {
                UserScore_mix = currentHighScore;
            }
        }

        User currentUser = new User(UserName,UserPassword, UserScore_addition,UserScore_subtraction,UserScore_multipication,
                UserScore_division,UserScore_mix);
        try {
            makeServiceCall(getApplicationContext(),getString(R.string.update_score_url),currentUser);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    void getResult1(){
        Bundle bundle = getIntent().getExtras();
        result= bundle.getString("result");
        comment = bundle.getString("comment");
        textViewResult.setText(result + " / 12");
        textViewComment.setText(comment);
    }

    void getResult2(){
        Bundle bundle = getIntent().getExtras();
        player1= bundle.getString("result1");
        player2= bundle.getString("result2");
        comment = bundle.getString("comment2");
        textViewPlayer1Result.setText(player1 + " / 12");
        textViewPlayer2Result.setText(player2 + " / 12");
        textViewComment.setText(comment);
    }

    // upload the data to database
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
                                    //finish();
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
                    params.put("score_addition", Integer.toString(user.getScore_addition()));
                    params.put("score_subtraction", Integer.toString(user.getScore_subtraction()));
                    params.put("score_multipication", Integer.toString(user.getScore_multipication()));
                    params.put("score_division", Integer.toString(user.getScore_division()));
                    params.put("score_mix", Integer.toString(user.getScore_mix()));
                    //params.put("score",score.getScore());
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

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    public void OK(View view){
        finish();
    }
}
