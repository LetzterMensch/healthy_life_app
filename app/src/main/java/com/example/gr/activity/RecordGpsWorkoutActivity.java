package com.example.gr.activity;

import android.Manifest;
import android.animation.Animator;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;

import com.example.gr.R;
//import com.example.gr.gps.GpsComponent;
//import com.example.gr.gps.LocationChangeEvent;
//import com.example.gr.gps.MapControls;
//import com.example.gr.gps.MapManager;
//import com.example.gr.gps.NavigationModeHandler;
//import com.example.gr.gps.SatelliteCountEvent;
//import com.example.gr.gps.ThemeUtils;
//import com.example.gr.gps.WorkoutGPSStateChanged;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.download.TileDownloadLayer;
import org.mapsforge.map.layer.overlay.FixedPixelCircle;
import org.mapsforge.map.layer.overlay.Polyline;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecordGpsWorkoutActivity extends BaseActivity {
}