package ibercivis.com.odourcollectapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


// Populate dynamically the list of comments
// Update the request
// Populate the text view with the report once the request has been received
// Add comment button (and check for logging) on the bar to add comments



public class DisplayReportActivity extends AppCompatActivity {

    private int report_id;

    private ListView commentsListView;

    private JSONArray data;

    // Using this increment value we can move the listview items
    private int increment = 0;

    private JSONArray sort;

    TextView report_content_textView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_report);

        report_content_textView = (TextView)findViewById(R.id.report_content);

        Intent myIntent = getIntent(); // gets the previously created intent
        report_id = myIntent.getIntExtra("report_id", 0); // will return "report_id"

        populateReportActivity();
    }

    protected void populateReportActivity () {

        // Url for the webservice
        String url = getString(R.string.base_url) + "/getreport.php?report_id=" + report_id;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response.toString());

                    JSONObject responseJSON = new JSONObject(response);

                    System.out.println("json response: "+responseJSON.toString());
                    System.out.println("json response annoyance: "+responseJSON.get("tasteintensity"));

                    if ((int) responseJSON.get("result") == 1) {

                        // Set the proper image for the report
                        ImageView imageView = (ImageView) findViewById(R.id.img_report);

                        Integer[] imageId = {
                                R.drawable.aqua1,
                                R.drawable.aqua1,
                                R.drawable.aqua1

                        };

                        try {
                            if (Integer.parseInt(responseJSON.get("tasteintensity").toString()) > 1) {
                                imageView.setImageResource(imageId[0]);
                            } else if (Integer.parseInt(responseJSON.get("tasteintensity").toString()) <= 1) {
                                imageView.setImageResource(imageId[1]);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Set the text with the report within the text view
                        try {
                                /* Create info to display in the pop up */
                            String odourRecordString = "User, date: "+responseJSON.get("username").toString()+" on "+responseJSON.get("report_date").toString()
                                    + "\npH: " + responseJSON.get("ph").toString()
                                    + "\nCloro: " + responseJSON.get("cl").toString()
                                    + "\nDescripción del olor: " + responseJSON.get("odourtype").toString()
                                    + "\nIntensidad del olor: " + responseJSON.get("odourintensity").toString()
                                   + "\nDescripción del sabor: " + responseJSON.get("tastetype").toString()
                                    + "\nIntensidad del sabor: " + responseJSON.get("tasteintensity").toString()
                                   // + "\nAlgún cambio reciente: " + responseJSON.get("anychange").toString()
                                    + "\nFecha: " + responseJSON.get("report_date").toString();
                                   // + "\nNúmero de comentarios: " + responseJSON.get("number_comments").toString();

                            report_content_textView.setText(odourRecordString);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Get the resources from the layout
                        commentsListView = (ListView)findViewById(R.id.report_comments_list);

                        String comments_string = responseJSON.getString("comments");
                        System.out.println("Comments: "+comments_string);

                        JSONArray comments_array = new JSONArray(comments_string);

                        ArrayList<String> commentsList= new ArrayList<String>();

                        for(int i=0; i<comments_array.length(); i++){
                            JSONObject aux = new JSONObject(comments_array.get(i).toString());
                            commentsList.add(aux.get("comment")+"\n"+aux.get("username")+", "+aux.get("comment_date"));
                        }

                        // Create The Adapter with passing ArrayList as 3rd parameter
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, commentsList);
                        // Set The Adapter
                        commentsListView.setAdapter(arrayAdapter);

                    } else {
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast;
                        CharSequence text;

                        text = "There was an error trying to retrieve report information.";
                        toast = Toast.makeText(getApplicationContext(), text, duration);
                        toast.show();

                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.getCache().clear();
        queue.add(sr);
    }

    /** Open Login Activity */
    public void openLoginDisplayReport(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /** Cancel Display Report Activity */
    public void cancelDisplayReport(View view) {
        finish();
    }

    /** Open Comment Activity */
    public void addCommentActivity(View view) {

        SessionManager session = new SessionManager(this);
        if(session.isLoggedIn())
        {
            // User logged in, launch add activity
            Intent intent = new Intent(this, AddCommentActivity.class);
            intent.putExtra("report_id", report_id);
            startActivity(intent);
        }
        else
        {
            int duration = Toast.LENGTH_SHORT;
            Toast toast;
            CharSequence text;

            // User not logged in, launch login activity
            text = "You have to be logged in to add or comment reports!";
            toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();

            openLoginDisplayReport(this.findViewById(android.R.id.content));
        }
    }

    /** Open Comment Activity */
    public void addCFAActivity(View view) {

        SessionManager session = new SessionManager(this);
        if(session.isLoggedIn())
        {
            // User logged in, launch add activity
            Intent intent = new Intent(this, AddCFAActivity.class);
            intent.putExtra("report_id", report_id);
            startActivity(intent);
        }
        else
        {
            int duration = Toast.LENGTH_SHORT;
            Toast toast;
            CharSequence text;

            // User not logged in, launch login activity
            text = "You have to be logged in to call for action!";
            toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();

            openLoginDisplayReport(this.findViewById(android.R.id.content));
        }
    }

}
