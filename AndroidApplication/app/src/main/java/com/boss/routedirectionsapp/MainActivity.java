package com.boss.routedirectionsapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String IP = "https://morning-shelf-67965.herokuapp.com";

    //    if you want to test your app locally then please host the backend
    //    using nodejs as told in the installation guide and then copy and paste the link to localhost
    //    here in the commented IP part and uncomment it

//    private static final String IP = "http://localhost:7777";
    private static final String Safest = "/map";
    private static final String SOS = "/sos";
    private FusedLocationProviderClient client;
    public static final int REQUEST_CODE = 101;
    EditText source, destination;
    Button button;
    ProgressDialog progressDialog;
    String sourceLat = "NaN";
    String sourceLong = "NaN";
    String destinationLat = "NaN";
    String destinationLong = "NaN";
    String sosLat = "NaN";
    String sosLong = "NaN";
    String phoneNo;
    String message;
    ArrayList<Loc> arrayList;
    LocationAdapter locationAdapter;
    Button generate, convert;
    FloatingActionButton sos;
    String myResponse = "";
    String myResponse1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<>();

        source = findViewById(R.id.source);
        destination = findViewById(R.id.destination);
        button = findViewById(R.id.get);
        generate = findViewById(R.id.generate);
        convert = findViewById(R.id.convert);
        sos = findViewById(R.id.sos);
        client = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }


        sos.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>()
                   {
                       @Override
                       public void onSuccess(final Location location) {
                           if (location != null)
                           {
                               runOnUiThread(new Runnable() {
                                   public void run() {
                                   sosLat = Double.toString(location.getLatitude());
                                   sosLong = Double.toString(location.getLongitude());
                                   Toast.makeText(getApplicationContext(), "GPS Working " + location.getLatitude() + " , "  + location.getLongitude(), Toast.LENGTH_LONG).show();
                                   }
                               });
                               OkHttpClient.Builder okhttpBuilder1 = new OkHttpClient.Builder();//
                               okhttpBuilder1.connectTimeout(10, TimeUnit.MINUTES) // connect timeout
                                       .writeTimeout(10, TimeUnit.MINUTES) // write timeout
                                       .readTimeout(10, TimeUnit.MINUTES); // read timeout

                               OkHttpClient client = okhttpBuilder1.build();

                               // Initialize Builder (not RequestBody)
                               FormBody.Builder builder1 = new FormBody.Builder();

                               builder1.add("sos_lat", sosLat);//"22.5809404"
                               builder1.add("sos_long",sosLong);//"88.3750068"

                               // Create RequestBody
                               RequestBody requestBody1 = builder1.build();

                               Request request1 = new Request.Builder()
                                       .url(IP + SOS)
                                       .post(requestBody1)
                                       .build();
                               client.newCall(request1).enqueue(new Callback() {
                                   @Override
                                   public void onFailure(Call call, IOException e) {
                                       Log.e("ERROR", e.getMessage());
                                       call.cancel();
                                   }
                                   @Override
                                   public void onResponse(Call call, Response response) throws IOException {
                                       myResponse1 = response.body().string();
                                       Log.e("RESPONSE", myResponse1);
                                       runOnUiThread(new Runnable() {
                                           public void run() {
                                               Toast.makeText(getApplicationContext(), "SOS Request Sent Successfully", Toast.LENGTH_LONG).show();
                                           }
                                       });
                                   }
                               });//
                           }
                       }
                   });
                   client.getLastLocation().addOnFailureListener(MainActivity.this, new OnFailureListener()
                   {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Log.e("response", "location not working");
                           runOnUiThread(new Runnable() {
                               public void run() {
                                   Toast.makeText(getApplicationContext(), "GPS NOT Working", Toast.LENGTH_LONG).show();
                               }
                           });
                       }
                   });

               }
           });

        source.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateCoordinates();

                if (!source.getText().toString().equals("") && !destination.getText().toString().equals("")) {

                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setTitle("Fetching Safest Route");
                    progressDialog.setMessage("Please Wait ...");
                    // progressDialog.setCancelable(false);
                    progressDialog.show();


                    OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
                    okhttpBuilder.connectTimeout(10, TimeUnit.MINUTES) // connect timeout
                            .writeTimeout(10, TimeUnit.MINUTES) // write timeout
                            .readTimeout(10, TimeUnit.MINUTES); // read timeout

                    OkHttpClient client = okhttpBuilder.build();

                    // Initialize Builder (not RequestBody)
                    FormBody.Builder builder = new FormBody.Builder();

                    builder.add("start_lat", sourceLat);
                    builder.add("start_long", sourceLong);
                    builder.add("end_lat", destinationLat);
                    builder.add("end_long", destinationLong);

                    // Create RequestBody
                    RequestBody requestBody = builder.build();

                    Request request = new Request.Builder()
                            .url(IP + Safest)
                            .post(requestBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("ERROR", e.getMessage());
                            call.cancel();
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            myResponse = response.body().string();
                            //System.out.println(myResponse);
                            Log.e("RESPONSE", myResponse);
//                            Log.e("datatype",myResponse.getClass().getName());
                            try {
                                JSONArray jsonArray = new JSONArray(myResponse);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String[] strs = jsonArray.get(i).toString().split(",");
                                    Loc loc = new Loc("Latitude : " + strs[0].substring(1), "Longitude : " + strs[1].substring(0, strs[1].length() - 1));
                                    arrayList.add(loc);
                                    helper.waypoints.add(new LatLng(Double.valueOf(strs[0].substring(1)),Double.valueOf(strs[1].substring(0, strs[1].length() - 1))));
                                    Log.e("V1", strs[0].substring(1));
                                    Log.e("V2", strs[1].substring(0, strs[1].length() - 1));
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        locationAdapter = new LocationAdapter(arrayList);
                                        // inflate dialog box
                                        final View answerView = getLayoutInflater().inflate(R.layout.dialog_layout, null, false);
                                        RecyclerView answerRecyclerView = answerView.findViewById(R.id.rv);
                                        answerRecyclerView.setAdapter(locationAdapter);
                                        answerRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                                        // Alert Dialog Box
                                        final AlertDialog.Builder alertAnswer = new AlertDialog.Builder(MainActivity.this);
                                        // this is set the view from XML inside AlertDialog
                                        alertAnswer.setView(answerView);
                                        alertAnswer.setTitle("WayPoints");
                                        // disallow cancel of AlertDialog on click of back button and outside touch
                                        alertAnswer.setCancelable(false);
                                        alertAnswer.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Cancel implies user doesn't want to add questions
                                                arrayList.clear();
                                                //helper.waypoints.clear();
                                            }
                                        });
                                        final AlertDialog dialogAnswer = alertAnswer.create();
                                        dialogAnswer.show();
                                    }
                                });
                            } catch (JSONException e) {
//                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Please Enter Correct Locations",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Enter Location(s)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Fetching Travel Route");
                progressDialog.setMessage("Please Wait ...");
                // progressDialog.setCancelable(false);
                progressDialog.show();
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                progressDialog.dismiss();

            }
        });

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCoordinates();
                Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateCoordinates() {
        GeocodingLocation locationAddress = new GeocodingLocation();
        if (!source.getText().toString().equals("")) {
            locationAddress.getAddressFromLocation("src", source.getText().toString(), getApplicationContext(), new GeocoderHandler());
        }
        if (!destination.getText().toString().equals("")) {
            locationAddress.getAddressFromLocation("dest", destination.getText().toString(), getApplicationContext(), new GeocoderHandler());
        }

        Log.e("START_LAT", sourceLat);
        Log.e("START_LONG", sourceLong);
        Log.e("END_LAT", destinationLat);
        Log.e("END_LONG", destinationLong);
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress = null;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    if (bundle.getString("sd") != null && bundle.getString("sd").equals("src")) {
                        String lines[] = locationAddress.split("\\r?\\n");
                        sourceLat = lines[0];
                        sourceLong = lines[1];
                    } else if (bundle.getString("sd") != null && bundle.getString("sd").equals("dest")) {
                        String lines[] = locationAddress.split("\\r?\\n");
                        destinationLat = lines[0];
                        destinationLong = lines[1];
                    }
                    break;
                default:
                    locationAddress = null;
            }
        }
    }
}
