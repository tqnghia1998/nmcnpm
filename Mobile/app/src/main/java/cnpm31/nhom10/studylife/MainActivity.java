package cnpm31.nhom10.studylife;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                RegisterFragment.OnRegisterFragmentListener {

    /* Định nghĩa các URL */
    static final String urlMajor = "http://10.0.2.2:51197/api/subject";

    /* Danh sách các khoa */
    ArrayList<String> listMajor = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Button email
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Layout chính
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        // Xử lý action bar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Xử lý thanh điều hướng
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*-----------------------------------------------------------------------*/

        /* Kết nối server để lấy danh sách tên khoa */
        Thread threadGetMajor = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray arrayJson = JsonHandler.getJsonFromUrl(urlMajor);
                for (int i=0; i<arrayJson.length(); i++){
                    try {
                        listMajor.add(arrayJson.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadGetMajor.start();
    }

    // Xử lý nút back
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Thêm button vào action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Xử lý sự kiện action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Xử lý sự kiện thanh điều hướng
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /* Giao diện đăng ký môn học */
        if (id == R.id.nav_register)
        {
            /* Tạo bundle chứa danh sách khoa */
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("listMajor", listMajor);

            /* Tạo fragment registerFragment */
            RegisterFragment registerFragment = new RegisterFragment();
            registerFragment.setArguments(bundle);

            /* Hiển thị fagment */
            FragmentTransaction fragmentTransaction =getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.registerFrame, registerFragment);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_gallery)
        {

        }
        else if (id == R.id.nav_slideshow)
        {

        }
        else if (id == R.id.nav_manage)
        {

        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_send)
        {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
