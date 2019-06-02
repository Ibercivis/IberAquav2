package ibercivis.com.odourcollectapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
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
import org.osmdroid.util.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class AddReportActivity extends AppCompatActivity {

    TextView addreport_origin_textview;
    Spinner type_spinner;
    Spinner duration_spinner;
    LinearLayout cargar;

    String error_check;

    // Intensity seek bar variables
    SeekBar sb_odourintensity;
    int sb_intensity_value = 0;
    float sb_intensity_start = 0;
    float sb_intensity_end = 2;
    int sb_intensity_start_position = 0;
    TextView intensity_result_value_textview;

    SeekBar sb_tasteannoyance;
    int sb_annoyance_value = 0;
    float sb_annoyance_start = 0;
    float sb_annoyance_end = 2;
    int sb_annoyance_start_position = 0;
    TextView annoyance_result_value_textview;


    EditText type_other_edit_text, cl_edit_text, ph_edit_text, odour_edit_text, taste_edit_text, change_edit_text;

    MyLocationListener locationListener;
    LocationManager lm;

// Center sliders and check fields to be sent in the POST request
// Move initializations to separate methods: initSpinners, initSliders
// Add hidden text field for option "Other"


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addreport);

        locationListener = new MyLocationListener();

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


        cargar = (LinearLayout) findViewById(R.id.cargando);


        // Set intensity slider
        intensity_result_value_textview = (TextView) findViewById(R.id.intensityResult);
        intensity_result_value_textview.setText(String.valueOf(sb_intensity_value));

        SeekBar sb_intensity = (SeekBar) findViewById(R.id.sbBar_intensity);
        sb_intensity.setProgress(sb_intensity_start_position);
        sb_intensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                float temp = seekBar.getProgress();
                float dis = sb_intensity_end - sb_intensity_start;
                sb_intensity_value = (int) (sb_intensity_start + ((temp / 100) * dis));

                intensity_result_value_textview.setText(String.valueOf(sb_intensity_value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                // To convert it as discrete value
                float temp = progress;
                float dis = sb_intensity_end - sb_intensity_start;
                sb_intensity_value = (int) (sb_intensity_start + ((temp / 100) * dis));

                intensity_result_value_textview.setText(String.valueOf(sb_intensity_value));
            }
        });

        // Set annoyance slider
        annoyance_result_value_textview = (TextView) findViewById(R.id.tasteResult);
        annoyance_result_value_textview.setText(String.valueOf(sb_annoyance_value));

        SeekBar sb_annoyance = (SeekBar) findViewById(R.id.sbBar_annoyance);
        sb_annoyance.setProgress(sb_annoyance_start_position);
        sb_annoyance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                float temp = seekBar.getProgress();
                float dis = sb_annoyance_end - sb_annoyance_start;
                sb_annoyance_value = (int) (sb_annoyance_start + ((temp / 100) * dis));

                annoyance_result_value_textview.setText(String.valueOf(sb_annoyance_value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                // To convert it as discrete value
                float temp = progress;
                float dis = sb_annoyance_end - sb_annoyance_start;
                sb_annoyance_value = (int) (sb_annoyance_start + ((temp / 100) * dis));

                annoyance_result_value_textview.setText(String.valueOf(sb_annoyance_value));
            }
        });

        // Set cloud slider
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    public void addReportRequest(View view) {


        final EditText cl_edit_text = (EditText) findViewById(R.id.addreport_cl);
        final EditText ph_edit_text = (EditText) findViewById(R.id.addreport_ph);
        final EditText odour_edit_text = (EditText) findViewById(R.id.addreport_odour);
        final EditText taste_edit_text = (EditText) findViewById(R.id.addreport_taste);
        final EditText change_edit_text = (EditText) findViewById(R.id.addreport_change);
        final SeekBar sb_intensity = (SeekBar) findViewById(R.id.sbBar_intensity);
        final SeekBar sb_annoyance = (SeekBar) findViewById(R.id.sbBar_annoyance);

        cargar.setVisibility(View.VISIBLE);


        if (1==1) {
            // Input data ok, so go with the request

            // Url for the webservice
            String url = getString(R.string.base_url) + "/addreport.php";

            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        System.out.println(response.toString());

                        JSONObject responseJSON = new JSONObject(response);

                        if ((int) responseJSON.get("result") == 1) {

                            int duration = Toast.LENGTH_LONG;
                            Toast toast;

                            toast = Toast.makeText(getApplicationContext(), "¡Gracias por contribuir con tu reporte!", duration);
                            toast.show();
                            Intent returnIntent = new Intent();
                            finish();
                            setResult(Activity.RESULT_OK, returnIntent);


                        } else {
                            showError("Error while adding report: " + responseJSON.get("message") + ".");
                            cargar.setVisibility(View.GONE);
                            // Clean the text fields for new entries
                            cl_edit_text.setText("");
                            ph_edit_text.setText("");
                            odour_edit_text.setText("");
                            taste_edit_text.setText("");
                            change_edit_text.setText("");




                            // Clean sliders (seek bars)
                            sb_intensity_value = 0;
                            sb_intensity.setProgress(0);
                            sb_annoyance_value = 0;
                            sb_annoyance.setProgress(0);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        cargar.setVisibility(View.GONE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    cargar.setVisibility(View.GONE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    SessionManager session = new SessionManager(getApplicationContext());

                    Map<String, String> addreport_params = new HashMap<String, String>();

                    addreport_params.put("odourintensity", String.valueOf(sb_intensity_value));
                    addreport_params.put("tasteintensity", String.valueOf(sb_annoyance_value));
                    addreport_params.put("odourtype", odour_edit_text.getText().toString());
                    addreport_params.put("ph", ph_edit_text.getText().toString());
                    addreport_params.put("cl", cl_edit_text.getText().toString());
                    addreport_params.put("anychange", change_edit_text.getText().toString());
                    addreport_params.put("user", session.getUsername());

                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    addreport_params.put("datetime", dateFormatter.format(new Date()));

                    // Get the location manager
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                        int duration = Toast.LENGTH_LONG;
                        Toast toast;

                        toast = Toast.makeText(getApplicationContext(), "Location permission must be granted to locate the report", duration);
                        toast.show();
                    }
                    Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if( location == null ) {
                        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if( location != null ) {
                            System.out.println(location);
                            addreport_params.put("latitude", String.valueOf(location.getLatitude()));
                            addreport_params.put("longitude", String.valueOf(location.getLongitude()));
                        }
                    }
                    else {
                        System.out.println(location);
                        addreport_params.put("latitude", String.valueOf(location.getLatitude()));
                        addreport_params.put("longitude", String.valueOf(location.getLongitude()));
                    }

/*                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if( location != null ) {
                        System.out.println(location);
                        addreport_params.put("latlng", "("+location.getLatitude()+", "+location.getLongitude()+")");
                    }
*/
                return addreport_params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.getCache().clear();
            queue.add(sr);
        }
        else {
            // Do nothing, error has been shown in a toast and views clean
        }
    }

    private void showError (CharSequence error) {
        int duration = Toast.LENGTH_LONG;
        Toast toast;

        toast = Toast.makeText(getApplicationContext(), error, duration);
        toast.show();
    }





    /** Finish Activity on Cancel Button Push */
    public void cancelAddReport(View view) {
        finish();
    }

}
