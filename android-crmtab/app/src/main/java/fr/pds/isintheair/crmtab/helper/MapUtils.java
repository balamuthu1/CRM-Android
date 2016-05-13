package fr.pds.isintheair.crmtab.helper;

import android.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;

import fr.pds.isintheair.crmtab.model.entity.Customer;
import fr.pds.isintheair.crmtab.model.entity.MapInfo;
import fr.pds.isintheair.crmtab.model.entity.MapInfoDAO;

/**
 * Created by tlacouque on 09/03/2016.
 * Class used  to give tool to set/verify map for fragments.
 */
public class MapUtils {


    /**
     * Initialise the map in a view. There is a scrollView if the method is called from DetailHCFragment
     * @param map
     * @param fragment
     * @param scrollView
     * @param locationOverlay
     * @param customer
     */
    public static void initMap(final MapView map,Fragment fragment, final ScrollView scrollView,
                         MyLocationNewOverlay locationOverlay, Customer customer) {

        setParamMap(map,fragment,locationOverlay,customer);

        map.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return map.onTouchEvent(event);
            }
        });
        map.invalidate();
    }

    /**
     * Initialise the map in a view. Method called from a detailHCFragment.
     * @param map
     * @param fragment
     * @param locationOverlay
     * @param customer
     */
    public static void initMap(final MapView map,Fragment fragment,
                         MyLocationNewOverlay locationOverlay, Customer customer) {
        setParamMap(map, fragment, locationOverlay, customer);
    }

    /**
     * Set principal parameters for maps. They are identical for health center and independant maps
     * even if they are online or offline.
     * @param map
     * @param fragment
     * @param locationOverlay
     * @param customer
     */
    public static void setParamMap(final MapView map,Fragment fragment,
                            MyLocationNewOverlay locationOverlay, Customer customer) {
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setUseDataConnection(true);
        if(CheckInternetConnexion.isNetworkAvailable(fragment.getContext())) {
            initOnlineMap(map,fragment,locationOverlay,customer);
        } else {
            initClientLocation(true,map,customer,locationOverlay,fragment);
        }
        map.invalidate();
    }


    /**
     * Init client when there is no internet connexion depend on the actual connexion
     * offline parameter
     * @param offline
     * @param map
     * @param customer
     * @param locationOverlay
     * @param fragment
     */
    public static void initClientLocation(boolean offline, MapView map, Customer customer,
                                   MyLocationNewOverlay locationOverlay, Fragment fragment) {
        IMapController mapController = map.getController();
        mapController.setZoom(15);
        GeoPoint startPoint = new GeoPoint(customer.getLattitude(), customer.getLongitude());
        if(offline) {
            mapController.setCenter(startPoint);
            if(locationOverlay != null) {
                map.getOverlays().remove(locationOverlay);
            }
        }
        Marker marker = new Marker(map);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(fragment.getResources().getDrawable(android.R.drawable.star_on, null));
        marker.setTitle(customer.getName());
        map.getOverlays().add(marker);
        map.invalidate();
    }

    /**
     * Initialise the map when there is an internet connexion
     * @param map
     * @param fragment
     * @param locationOverlay
     * @param customer
     */
    public static void initOnlineMap(final MapView map,Fragment fragment,
                              MyLocationNewOverlay locationOverlay,Customer customer) {
        locationOverlay = new MyLocationNewOverlay(fragment.getContext(),map);
        GpsMyLocationProvider gpsMyLocationProvider = new GpsMyLocationProvider(fragment.getContext());
        gpsMyLocationProvider.startLocationProvider(locationOverlay);
        gpsMyLocationProvider.setLocationUpdateMinTime(10);
        gpsMyLocationProvider.setLocationUpdateMinDistance(5);
        locationOverlay.enableMyLocation(gpsMyLocationProvider);
        locationOverlay.enableFollowLocation();
        map.getOverlays().add(locationOverlay);
        initClientLocation(false, map, customer, locationOverlay, fragment);
    }

    /**
     * Method used to know if there is a tile file of a mapinfo saved on a device.
     * @param mapInfo
     * @return boolean
     */
    public static boolean isTileFileSavedOnDevice(MapInfo mapInfo) {
      return  new File(FormatValidator.formatPathTile(mapInfo)).exists();
    }

    /**
     * Method used to know if there is a tile saved on a device for a siret number.
     * It will check if there is a mapinfo for a customer and a tile.
     * @param siretNumber
     * @return boolean
     */
    public static boolean isTileSavedOnDevice(long siretNumber) {
        if(!MapInfoDAO.isMapInfoExist(siretNumber)) return false;
        if(isTileFileSavedOnDevice(MapInfoDAO.getMapInfo(siretNumber))) return true;
        return false;
    }


}
