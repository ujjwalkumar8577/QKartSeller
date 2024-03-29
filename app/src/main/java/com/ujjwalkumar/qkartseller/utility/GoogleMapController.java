package com.ujjwalkumar.qkartseller.utility;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

// this class contains necessary functions for Google Maps operations
public class GoogleMapController {

    private final MapView mapView;
    private final HashMap<String, Marker> mapMarker;
    private GoogleMap googleMap;
    private GoogleMap.OnMarkerClickListener onMarkerClickListener;

    public GoogleMapController(MapView mapView, OnMapReadyCallback onMapReadyCallback) {
        this.mapView = mapView;
        mapMarker = new HashMap<>();

        this.mapView.getMapAsync(onMapReadyCallback);
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (onMarkerClickListener != null) {
            this.googleMap.setOnMarkerClickListener(onMarkerClickListener);
        }
    }

    public void setMapType(int _mapType) {
        if (googleMap == null) return;

        googleMap.setMapType(_mapType);
    }

    public void setOnMarkerClickListener(GoogleMap.OnMarkerClickListener onMarkerClickListener) {
        this.onMarkerClickListener = onMarkerClickListener;

        if (googleMap != null) {
            this.googleMap.setOnMarkerClickListener(onMarkerClickListener);
        }
    }

    public void addMarker(String id, double lat, double lng) {
        if (googleMap == null) return;

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lng));
        Marker marker = googleMap.addMarker(markerOptions);
        marker.setTag(id);
        mapMarker.put(id, marker);
    }

    public Marker getMarker(String id) {
        return mapMarker.get(id);
    }

    public void setMarkerInfo(String id, String title, String snippet) {
        Marker marker = mapMarker.get(id);
        if (marker == null) return;

        marker.setTitle(title);
        marker.setSnippet(snippet);
    }

    public void setMarkerPosition(String id, double lat, double lng) {
        Marker marker = mapMarker.get(id);
        if (marker == null) return;

        marker.setPosition(new LatLng(lat, lng));
    }

    public void setMarkerColor(String id, float color, double alpha) {
        Marker marker = mapMarker.get(id);
        if (marker == null) return;

        marker.setAlpha((float) alpha);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(color));
    }

    public void setMarkerIcon(String id, int resIcon) {
        Marker marker = mapMarker.get(id);
        if (marker == null) return;

        marker.setIcon(BitmapDescriptorFactory.fromResource(resIcon));
    }

    public void setMarkerVisible(String id, boolean visible) {
        Marker marker = mapMarker.get(id);
        if (marker == null) return;

        marker.setVisible(visible);
    }


    public void moveCamera(double lat, double lng) {
        if (googleMap == null) return;

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
    }

    public void zoomTo(double zoom) {
        if (googleMap == null) return;

        googleMap.moveCamera(CameraUpdateFactory.zoomTo((float) zoom));
    }

    public void zoomIn() {
        if (googleMap == null) return;

        googleMap.moveCamera(CameraUpdateFactory.zoomIn());
    }

    public void zoomOut() {
        if (googleMap == null) return;

        googleMap.moveCamera(CameraUpdateFactory.zoomOut());
    }
}