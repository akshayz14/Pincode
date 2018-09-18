package com.apc.pincode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apc.pincode.sync.APIClient;
import com.apc.pincode.sync.APIInterface;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etSearchText;
    private Button btnSearch;
    private ListView lvPostOffices, lvRecentSearches;
    private Context mContext;
    private PostOfficeListAdapter adapter;
    private TextView tvNumberOfResultsFound;
    private InputMethodManager imm;
    private ProgressDialog progressDialog;
    private final static String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private AdView mAdView;
    private View lineSeperatorView;
//    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        Toolbar toolbar = findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_toggle);

        etSearchText = findViewById(R.id.etSearchText);
        btnSearch = findViewById(R.id.btnSearch);
        lvPostOffices = findViewById(R.id.lvPostOffices);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tvNumberOfResultsFound = findViewById(R.id.tvNumberOfResultsFound);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        lvRecentSearches = findViewById(R.id.lvRecentSearches);
        lineSeperatorView = findViewById(R.id.lineSeperatorView);
        lineSeperatorView.setVisibility(View.GONE);
//        setupAds();

//        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });



//        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open,
//                R.string.navigation_drawer_close);


        NavigationView navigationView = findViewById(R.id.nav_view);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // set item as selected to persist highlight
                menuItem.setChecked(true);
                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();


                switch (menuItem.getItemId()) {
                    case R.id.drawer_home:
                        break;
                    case R.id.current_pin:

//                        Intent intent = new Intent(MainActivity.this, MapLocationActivity.class);
//                        startActivity(intent);

                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=postoffice near me");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);

                        break;

                    case R.id.about_us:


                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("We are a company og one person");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();


                        break;


                    case R.id.privacy_policy:

                        final Dialog dialog = new Dialog(mContext);
                        dialog.setContentView(R.layout.privacy_policy_custom_dialog);
                        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        dialog.setTitle("Privacy policy");
                        Button dialogButton = (Button) dialog.findViewById(R.id.btnDialogOK);
                        // if button is clicked, close the custom dialog

                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();


                        break;
                }
                return true;
            }
        });


        hideSoftKeyboard();
        btnSearch.setOnClickListener(this);

    }



    private void setupAds() {

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Searching Post Offices...");
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    private void getPOByPin(String searchText) {
        APIInterface apiInterface = APIClient.getClient(mContext).create(APIInterface.class);
        Call<PostOfficeNames> call = apiInterface.getPOByPincode(searchText);
        call.enqueue(new Callback<PostOfficeNames>() {
            @Override
            public void onResponse(Call<PostOfficeNames> call, Response<PostOfficeNames> response) {
                Log.d(TAG, "onResponse: " + response.body());
                setupAdapter(response);
            }

            @Override
            public void onFailure(Call<PostOfficeNames> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }


    private void getPOByName(String searchText) {
        APIInterface apiInterface = APIClient.getClient(mContext).create(APIInterface.class);
        Call<PostOfficeNames> call = apiInterface.getPOByName(searchText);
        call.enqueue(new Callback<PostOfficeNames>() {
            @Override
            public void onResponse(Call<PostOfficeNames> call, Response<PostOfficeNames> response) {
                Log.d(TAG, "onResponse: " + response);
                setupAdapter(response);
            }

            @Override
            public void onFailure(Call<PostOfficeNames> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });

    }

    private void hideSoftKeyboard() {
        imm.hideSoftInputFromWindow(etSearchText.getWindowToken(), 0);
    }

    private void setupAdapter(Response<PostOfficeNames> response) {

        lineSeperatorView.setVisibility(View.VISIBLE);
        tvNumberOfResultsFound.setText(response.body().getMessage());
        if (response.body().postOffice == null) {
            dismissProgressDialog();
            Toast.makeText(mContext, "Please enter a valid pincode or place.", Toast.LENGTH_SHORT).show();
            return;
        }
        List<PostOffice> postOfficeList = response.body().getPostOffice();
        String pincode = response.body().getPostOffice().get(0).getPincode();
        if (response.body().getPostOffice().get(0).getPincode() == null) {
            adapter = new PostOfficeListAdapter(mContext, 0, postOfficeList, etSearchText.getText().toString());

        } else {
            adapter = new PostOfficeListAdapter(mContext, 0, postOfficeList);
        }

        lvPostOffices.setAdapter(adapter);
        dismissProgressDialog();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSearch:
                String searchText = "";
                showProgressDialog();
                if (!etSearchText.getText().toString().isEmpty()) {
                    hideSoftKeyboard();
                    searchText = etSearchText.getText().toString();
                    if (!searchText.matches("[a-zA-Z ]*")) {
                        Toast.makeText(mContext, R.string.text_error, Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                        return;

                    }
                    if (Utils.isInteger(searchText)) {
                        getPOByPin(searchText);
                    } else {
                        getPOByName(searchText);
                    }

                } else {
                    dismissProgressDialog();
                    Toast.makeText(mContext, R.string.enter_text, Toast.LENGTH_SHORT).show();
                }
        }
    }
}
