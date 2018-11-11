package com.dh.pro1822.pwassistance;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class PwListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private PasswordAdapter adapter;
    private String queryText;

    private static final int PERMISSION_REQUEST_CODE = 1822;
    private static final String QUERY_TEXT = "queryText";

    private DataBaseFileManager backUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_list);

        backUp = new DataBaseFileManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkVerify();
        }

        if (savedInstanceState == null) {
            queryText = "";
        } else {
            queryText = savedInstanceState.getString(QUERY_TEXT);
        }

        makeToolbar();
        makeListView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        findViewById(R.id.list_header).setVisibility(View.INVISIBLE);
        findViewById(R.id.search_list).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.list_header).setVisibility(View.VISIBLE);
        findViewById(R.id.search_list).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.changeCursor(getPwListWhereCursor(queryText));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(QUERY_TEXT, queryText);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            showExitDialog();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        String message;

        switch (item.getItemId()) {
            case R.id.nav_manage:
                intent = new Intent(this, PwEditActivity.class);
                break;
            case R.id.nav_change_login:
                intent = new Intent(this, ChangePasswordActivity.class);
                break;
            case R.id.nav_db_export:
                intent = null;
                message = backUp.exportDB();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_db_import:
                intent = null;
                message = backUp.importDB();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                onRestart();
                break;
            default:
                intent = null;
        }

        if (intent != null) {
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String message = backUp.exportDB();
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_searchview, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) item.getActionView();
//        searchView.onActionViewExpanded();

        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.search_hint));

            /*
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {

                    adapter.changeCursor(null);

                    if (query.length() > 0) {
//                        Toast.makeText(getApplicationContext(), "filter query: " + query, Toast.LENGTH_SHORT).show();
                        adapter.changeCursor(getPwListWhereCursor(query));
                    } else {
                        Toast.makeText(getApplicationContext(), "filter query: " + query, Toast.LENGTH_SHORT).show();
                        adapter.changeCursor(getPwListCursor());
                    }

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            };

            searchView.setOnQueryTextListener(queryTextListener);
            */

            EditText searchQuery = searchView.findViewById(R.id.search_src_text);
            searchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                        queryText = v.getText().toString();
                        adapter.changeCursor(getPwListWhereCursor(queryText));

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }

                        return true;
                    }
                    return false;
                }
            });
        }

        return true;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, PwEditActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */

    private void makeToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_list);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void makeListView() {
        ListView listView = findViewById(R.id.search_list);
//        adapter = new PasswordAdapter(this, getPwListCursor(), CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        adapter = new PasswordAdapter(this, getPwListWhereCursor(queryText));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PwListActivity.this, PwEditActivity.class);
                intent.putExtra(ContractDB.PwListEntry.COLUMN_ID, (int) id);
                startActivity(intent);
            }
        });
    }

    private void showExitDialog() {
        ExitDialogFragment fragment = new ExitDialogFragment();
        fragment.show(getSupportFragmentManager(), "exit");
    }

    private Cursor getPwListWhereCursor(String parm) {

        PwDatabaseHelper pwDatabaseHelper = PwDatabaseHelper.getsInstance(this);
        SQLiteDatabase db = pwDatabaseHelper.getReadableDatabase();

        Cursor cursor = PwDatabaseHelper.getPwListWhereCursor(db, parm);

        if (cursor == null) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }

        return cursor;
    }

    private void checkVerify() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    public void onClickFab(View view) {
        Intent intent = new Intent(this, PwEditActivity.class);
        startActivity(intent);
    }

}
