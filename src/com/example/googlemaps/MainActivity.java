package com.example.googlemaps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends Activity {
	private LocationManager mLocationManager;
	private MyLocationListener mLocationListener;
	private ProgressDialog pd;
	private EditText latitud_text, longitud_text;
	private Button button_myLocation, button_findLocation;
	private GoogleMap map;
	private Geocoder geocoder;
	private CameraUpdate zoom;
	private CameraUpdate center;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geocoder = new Geocoder(this, Locale.getDefault());
        zoom=CameraUpdateFactory.zoomTo(15);
        latitud_text = (EditText) this.findViewById(R.id.text_latitud);
        longitud_text = (EditText) this.findViewById(R.id.text_longitud);
        map = ( (MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        button_myLocation = (Button) this.findViewById(R.id.button_myLocation);
        button_findLocation = (Button) this.findViewById(R.id.button_findLocation);
        
        
        button_myLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isGPSAvailable()){
					pd = ProgressDialog.show(v.getContext(), "Ubicación", "Esperando ubicación de gps");
					configGPS();	
				}
		        else 
		        {
		        	Toast.makeText(getApplicationContext(), "Gps no activo. Por favor, activelo.", Toast.LENGTH_LONG).show();
		        }
				
			}
		});
        
        button_findLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				double latitud = Double.parseDouble(latitud_text.getText().toString());
				double longitud = Double.parseDouble(longitud_text.getText().toString());
				map.clear();
				LatLng UBICACION = new LatLng(latitud, longitud); 
				List<android.location.Address> addreses;
				try {
					addreses = geocoder.getFromLocation(latitud, longitud, 1);
					String NombrePais = addreses.get(0).getCountryName();
					String Localidad = addreses.get(0).getLocality();
					String calle = addreses.get(0).getThoroughfare();
					String calle2 = addreses.get(0).getFeatureName();
					if(Localidad == null){Localidad = "";}
					if(calle == null){calle = "";}
					if(calle2 == null){calle2 = "";}
					center = CameraUpdateFactory.newLatLng(UBICACION);
					map.addMarker(new MarkerOptions().position(UBICACION).title(NombrePais+" "+Localidad)
			    			.snippet(calle+" "+calle2).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
					map.moveCamera(center);
				    map.animateCamera(zoom);
				} catch (IOException e1) {
			
					e1.printStackTrace();
				}
		    	
			}
		});
        
        map.setOnMarkerClickListener(new OnMarkerClickListener(){
      
			@Override
			public boolean onMarkerClick(Marker arg0) {
				Toast.makeText(getApplicationContext(), arg0.getTitle(), Toast.LENGTH_LONG).show();
				return false;
			}
        	
        });
    }
    private boolean isGPSAvailable(){
    	mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    		return false;
    	else
    		return true;
    }
    private void configGPS(){
   
    	mLocationListener = new MyLocationListener();
    	mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
    	
    }
    private void updateScreen(Location location){
    	double latitud = location.getLatitude();
    	double longitud = location.getLongitude();
    	latitud_text.setText(String.valueOf(latitud));
    	longitud_text.setText(String.valueOf(longitud));
    	pd.dismiss();
    	map.clear();
    	LatLng UBICACION = new LatLng(latitud, longitud); 
    	List<android.location.Address> addreses;
		try {
			addreses = geocoder.getFromLocation(latitud, longitud, 1);
			String NombrePais = addreses.get(0).getCountryName();
			String Localidad = addreses.get(0).getLocality();
			String calle = addreses.get(0).getThoroughfare();
			String calle2 = addreses.get(0).getFeatureName();
			if(Localidad == null){Localidad = "";}
			if(calle == null){calle = "";}
			if(calle2 == null){calle2 = "";}
			center = CameraUpdateFactory.newLatLng(UBICACION);
			map.addMarker(new MarkerOptions().position(UBICACION).title(NombrePais+" "+Localidad)
	    			.snippet(calle+" "+calle2).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
			map.moveCamera(center);
		    map.animateCamera(zoom);
			
		} catch (IOException e1) {
	
			e1.printStackTrace();
		}
 
    	onDestroy();
    	
    }
    public  void onDestroy()
    {
        super.onDestroy();
        mLocationManager.removeUpdates(mLocationListener);
    }
    private class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			updateScreen(location);
			
			
			
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
    	
    }
   
    
}
