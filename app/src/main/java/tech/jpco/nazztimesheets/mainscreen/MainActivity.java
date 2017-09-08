package tech.jpco.nazztimesheets.mainscreen;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import tech.jpco.nazztimesheets.R;
import tech.jpco.nazztimesheets.model.WorkSession;

public class MainActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks, MainContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void setMinionActive() {

    }

    @Override
    public void setPersonalActive() {

    }

    @Override
    public void setInactive() {

    }

    @Override
    public void setRvList(List<WorkSession> log) {

    }

    @Override
    public void notifyRvUpdated() {

    }

    @Override
    public void setMinionAhead(float hours) {

    }

    @Override
    public void setPersonalAhead(float hours) {

    }

    @Override
    public void setHoursEven() {

    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {

    }
}
