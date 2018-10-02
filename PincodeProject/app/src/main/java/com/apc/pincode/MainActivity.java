package com.apc.pincode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.apc.pincode.sync.APIClient;
import com.apc.pincode.sync.APIInterface;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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
    private LinkedHashSet recentSearchTerms;
    private SharedPreferences mPreferences;
    private RecentlySearchedItemsListAdapter recentSearchesAdapter;
    private LinearLayout llRecentSearches;
//    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mPreferences = mContext.getSharedPreferences(AppConstants.PREF_NAME, MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar);

        DebugDB.getAddressLog();

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
//        lineSeperatorView.setVisibility(View.GONE);
        if (mPreferences.getString(AppConstants.RECENT_SEARCHES, "").isEmpty()) {
            recentSearchTerms = new LinkedHashSet();
        } else {
            recentSearchTerms = new LinkedHashSet();

            String recent = mPreferences.getString(AppConstants.RECENT_SEARCHES, "");
            JsonArray recentlySearched = Utils.gson.fromJson(recent, JsonArray.class);

//            if (recentlySearched == null) {
//                llRecentSearches.setVisibility(View.GONE);
//                return;
//            }

            for (int i = 0; i < recentlySearched.size(); i++) {
                JsonElement b = recentlySearched.get(i);
                recentSearchTerms.add(b.getAsString());
            }

        }


        llRecentSearches = findViewById(R.id.llRecentSearches);
        loadRecentSearchedItems();
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
                hideSoftKeyboard();

                menuItem.setChecked(true);
                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();


                switch (menuItem.getItemId()) {
                    case R.id.drawer_home:
                        Utils.trackGoogleAnalyticsEvent(AnalyticConstants.DRAWER_CLICK, AnalyticConstants.HOME_BUTTON_CLICKED, "", 1);

                        break;
                    case R.id.current_pin:
                        Utils.trackGoogleAnalyticsEvent(AnalyticConstants.DRAWER_CLICK, AnalyticConstants.FIND_NEAREST_POST_OFFICE, "", 1);

                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=postoffice near me");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);

                        break;

                    case R.id.about_us:

                        Utils.trackGoogleAnalyticsEvent(AnalyticConstants.DRAWER_CLICK, AnalyticConstants.ABOUT_US_CLICKED, "", 1);


                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("We are a company of 1 person");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        alertDialog.show();
                        break;


                    case R.id.privacy_policy:
                        Utils.trackGoogleAnalyticsEvent(AnalyticConstants.DRAWER_CLICK, AnalyticConstants.PRIVACY_POLICY_CLICKED, "", 1);

                        final Dialog dialog = new Dialog(mContext);
                        dialog.setContentView(R.layout.privacy_policy_custom_dialog);
                        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        dialog.setTitle("Privacy policy");
                        Button dialogButton = (Button) dialog.findViewById(R.id.btnDialogOK);
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

    private void loadRecentSearchedItems() {

        String recent = mPreferences.getString(AppConstants.RECENT_SEARCHES, "");
        JsonArray recentlySearched = Utils.gson.fromJson(recent, JsonArray.class);

        if (recentlySearched == null) {

            llRecentSearches.setVisibility(View.GONE);
            return;
        }

        final ArrayList<String> recentItemsList = new ArrayList<>();
        for (int i = recentlySearched.size() - 1; i >= 0; i--) {
            JsonElement b = recentlySearched.get(i);
            recentItemsList.add(b.getAsString());
        }

        lvRecentSearches.setVisibility(View.VISIBLE);
        Log.d(TAG, "loadRecentSearchedItems: " + recentItemsList);
        recentSearchesAdapter = new RecentlySearchedItemsListAdapter(mContext, 0, recentItemsList);
        lvRecentSearches.setAdapter(recentSearchesAdapter);


        lvRecentSearches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showProgressDialog();
                String searchString = recentItemsList.get(i);
                Utils.trackGoogleAnalyticsEvent(AnalyticConstants.RECENT_SEARCH_LIST, AnalyticConstants.RECENT_SEARCH_ITEM_CLICK, searchString, i);
                if (Utils.isInteger(searchString)) {
                    llRecentSearches.setVisibility(View.GONE);
                    getPOByPin(searchString);
                } else {
                    llRecentSearches.setVisibility(View.GONE);
                    getPOByName(searchString);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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

//        lineSeperatorView.setVisibility(View.VISIBLE);
        if (response.body().getMessage().equals(getResources().getString(R.string.no_results_found))) {
            if (adapter != null) {
                adapter.clear();
            }
        }

        tvNumberOfResultsFound.setText(response.body().getMessage());

        if (response.body().postOffice == null) {
            dismissProgressDialog();
            showErrorDialog(getResources().getString(R.string.text_error));
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
                llRecentSearches.setVisibility(View.GONE);
                String searchText = "";
                showProgressDialog();
                if (!etSearchText.getText().toString().isEmpty()) {
                    hideSoftKeyboard();
                    searchText = etSearchText.getText().toString();
                    Utils.trackGoogleAnalyticsEvent(AnalyticConstants.SEARCH, AnalyticConstants.TEXT_SEARCHED, searchText, 1);

                    if (!searchText.matches("[a-zA-Z1234567890 ]*")) {
//                        Toast.makeText(mContext, R.string.text_error, Toast.LENGTH_SHORT).show();
                        showErrorDialog(getResources().getString(R.string.text_error));
                        dismissProgressDialog();
                        return;

                    }
                    addSearchedTextToSet(searchText);
                    if (Utils.isInteger(searchText)) {
                        Utils.trackGoogleAnalyticsEvent(AnalyticConstants.SEARCH, AnalyticConstants.TEXT_SEARCHED, "Search by pin", 1);
                        getPOByPin(searchText);
                    } else {
                        Utils.trackGoogleAnalyticsEvent(AnalyticConstants.SEARCH, AnalyticConstants.TEXT_SEARCHED, "Search by name", 1);
                        getPOByName(searchText);
                    }

                } else {
                    dismissProgressDialog();
                    Utils.trackGoogleAnalyticsEvent(AnalyticConstants.SEARCH, AnalyticConstants.TEXT_SEARCHED, "Invalid text", 1);
//                    Toast.makeText(mContext, R.string.enter_text, Toast.LENGTH_SHORT).show();
                    showErrorDialog(getResources().getString(R.string.enter_text));
                }
        }
    }

    private void showErrorDialog(String text) {
        adapter.clear();
        tvNumberOfResultsFound.setText("");

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();

    }

    private void addSearchedTextToSet(String searchText) {

        recentSearchTerms.add(searchText);
        Log.d(TAG, "addSearchedTextToSet: " + recentSearchTerms);
        if (recentSearchTerms.size() > 5) {
            recentSearchTerms.remove(recentSearchTerms.iterator().next());
        }
        Log.d(TAG, "addSearchedTextToSet: " + recentSearchTerms);
        String recentSearchJsonStrings = Utils.gson.toJson(recentSearchTerms);

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(AppConstants.RECENT_SEARCHES, recentSearchJsonStrings);
        editor.apply();

    }
}
