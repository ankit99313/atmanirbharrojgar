package com.atmanirbharrogar.company.work;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;

public class GetLocation extends AppCompatActivity {

    MaterialButton currLocation;
    MaterialButton otherLocation;
    int PLACE_PICKER_REQUEST = 1;

    FusedLocationProviderClient fusedLocationProviderClient;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Location lastlocation;
    String SAddress;
    String Scity;

    public void goPlacePicker(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(GetLocation.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(GetLocation.this, data);
                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    //String city = addresses.get(0).getAddressLine();
                    String city1 = addresses.get(0).getSubAdminArea();
                    //String country = addresses.get(0).getAddressLine(2)
                    String knownName1 = addresses.get(0).getFeatureName();
                    String Locality = addresses.get(0).getLocality();
                    if (city1 != null) {
                        Scity = city1;
                    } else if (knownName1 != null) {
                        Scity = knownName1;
                    } else if (Locality != null) {
                        Scity = Locality;
                    }
                    editor.putString("City", Locality);
                    editor.putString("Address", address);
                    editor.commit();
                    //Toast.makeText(GetLocation.this , "AddressPlacePicker" + address , Toast.LENGTH_LONG).show();
                    //Toast.makeText(GetLocation.this , "CityPlacePicker" + Scity , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), AvailableWorkers.class);
                    startActivity(intent);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //tv.setText(place.getAddress());
                //Toast.makeText(GetLocation.this, "Address" + place.getAddress() + "\n", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
    }
}