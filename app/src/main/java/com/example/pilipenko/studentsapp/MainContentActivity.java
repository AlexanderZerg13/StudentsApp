package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SingleSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.example.pilipenko.studentsapp.data.AuthorizationObject;
import com.example.pilipenko.studentsapp.data.Basic;
import com.example.pilipenko.studentsapp.data.Group;
import com.example.pilipenko.studentsapp.data.StaticData;
import com.example.pilipenko.studentsapp.data.StudentGroup;
import com.example.pilipenko.studentsapp.data.StudentGroupLab;
import com.example.pilipenko.studentsapp.interfaces.IToolbar;
import com.example.pilipenko.studentsapp.interfaces.ITransitionActions;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainContentActivity extends AppCompatActivity implements IToolbar, ITransitionActions {

    private static final String TAG = "MainContentActivity";

    private static final String KEY_AUTH_OBJECT = "AUTH_OBJECT";

    private static final String ADDRESS_GROUP = "http://web-03:8080/InfoBase-Stud/hs/Students/TimeTableGroups1";

    private static final String LOGIN = "ws";
    private static final String PASS = "ws";

    private View mHeaderView;
    private TextView mNameTextView;
    private TextView mExtraTextView;
    private ImageView mSwitchMenuButton;

    private DrawerLayout mDrawer;
    private NavigationView mNavView;
    private ActionBarDrawerToggle mDrawerToggle;

    private AuthorizationObject user;

    private MultiSelector mMultiSelector = new SingleSelector();

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static Intent newIntent(Context packageContext, AuthorizationObject object) {
        Intent intent = new Intent(packageContext, MainContentActivity.class);
        intent.putExtra(KEY_AUTH_OBJECT, object);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);

        Window window = getWindow();

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getAttributes().flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        user = (AuthorizationObject) getIntent().getSerializableExtra(KEY_AUTH_OBJECT);
//        Log.i(TAG, "onCreate: " + user);
        new FetchStudentGroups().execute(user.getId());

        mMultiSelector.setSelectable(true);
        mMultiSelector.setSelected(0, 0, true);

        mDrawer = (DrawerLayout) findViewById(R.id.main_content_drwLayout);
        mDrawer.setStatusBarBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mNavView = (NavigationView) findViewById(R.id.main_content_navView);
        setupDrawerContent(mNavView);

        mHeaderView = mNavView.getHeaderView(0);
        mNameTextView = (TextView) mHeaderView.findViewById(R.id.header_layout_tv_name);
        mExtraTextView = (TextView) mHeaderView.findViewById(R.id.header_layout_tv_extra);
        mSwitchMenuButton = (ImageView) mHeaderView.findViewById(R.id.header_layout_btn_expand);

        String[] fio = user.getName().split(" ");
        mNameTextView.setText(fio[1] + " " + fio[0]);

        mSwitchMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu menu = mNavView.getMenu();
                MenuItem item = menu.findItem(R.id.nav_marks);
                menu.clear();
                if (item == null) {
                    if (mNavView.getHeaderCount() != 1) {
                        View v = mNavView.getHeaderView(1);
                        mNavView.removeHeaderView(v);
                    }
                    mNavView.inflateMenu(R.menu.drawer_view);
                } else {
                    View subHead = LayoutInflater.from(MainContentActivity.this).inflate(R.layout.items_recycler_view, mNavView, false);
                    RecyclerView recyclerView = (RecyclerView) subHead.findViewById(R.id.items_recycler_view);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainContentActivity.this));

                    List<Group> list = StaticData.sGroupsSelect;
                    BasicItemAdapter bIA = new BasicItemAdapter(list);
                    recyclerView.setAdapter(bIA);

                    mNavView.addHeaderView(subHead);
                    mNavView.inflateMenu(R.menu.drawer_view_extra);
                }
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_content_fragmentContainer);
                if (fragment instanceof ScheduleDayFragment) {
                    mNavView.setCheckedItem(R.id.nav_classes_schedule);
                }
            }
        });
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_content_fragmentContainer);

        if (fragment == null) {
            fragment = ScheduleDayFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.main_content_fragmentContainer, fragment)
                    .commit();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                } else {
                    mDrawer.openDrawer(GravityCompat.START);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        item.setChecked(true);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment fragment = fragmentManager.findFragmentById(R.id.main_content_fragmentContainer);
                        Fragment newFragment = null;

                        switch (item.getItemId()) {

                            case R.id.nav_marks:
                                if (!(fragment instanceof GradesFragment)) {
                                    newFragment = GradesFragment.newInstance();
                                }
                                break;
                            case R.id.nav_classes_schedule:
                                if (!(fragment instanceof ScheduleDayFragment)) {
                                    newFragment = ScheduleDayFragment.newInstance();
                                }
                                break;
                            case R.id.nav_session_schedule:
                                if (!(fragment instanceof ScheduleSessionFragment)) {
                                    newFragment = ScheduleSessionFragment.newInstance();
                                }
                                break;
                            case R.id.nav_info:
                                if (!(fragment instanceof DisciplineFragment)) {
                                    newFragment = DisciplineFragment.newInstance();
                                }
                                break;
                            case R.id.nav_exit:
                                UserPreferences.clearUser(MainContentActivity.this);
                                StudentGroupLab.get(MainContentActivity.this).clearStudentGroups();
                                startActivity(MainLoginActivity.newIntent(MainContentActivity.this));
                                break;
                            default:
                                if (!(fragment instanceof BasicFragment)) {
                                    newFragment = BasicFragment.newInstance();
                                }
                                break;
                        }
                        if (newFragment != null) {

                            Log.i("TAG", "onNavigationItemSelected: " + fragmentManager.getBackStackEntryCount());
                            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                                fragmentManager.popBackStack();
                            }
                            Log.i("TAG", "onNavigationItemSelectedAfter: " + fragmentManager.getBackStackEntryCount());
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_content_fragmentContainer, newFragment)
                                    .commit();
                        }
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                }
        );
    }

    @Override
    public void goToDescribeDiscipline(int idSemester, int idDiscipline) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = DisciplineDescribeFragment.newInstance(idSemester, idDiscipline);
        fragmentManager.beginTransaction()
                .replace(R.id.main_content_fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToDescribeLessons(int idLessons) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = LessonDescribeFragment.newInstance(idLessons);
        fragmentManager.beginTransaction()
                .replace(R.id.main_content_fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToSession() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = ScheduleSessionFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.main_content_fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
        mNavView.setCheckedItem(R.id.nav_session_schedule);
    }

    @Override
    public void useToolbar(Toolbar toolbar, int strResource) {
        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            boolean trigger = true;

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (trigger) {
                    IBinder iBinder = MainContentActivity.this.getCurrentFocus().getWindowToken();
                    if (iBinder != null) {
                        InputMethodManager iim = (InputMethodManager) MainContentActivity.this
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        iim.hideSoftInputFromWindow(iBinder, 0);
                    }
                    trigger = false;
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                trigger = true;
                super.onDrawerStateChanged(newState);
            }
        };
        mDrawer.setDrawerListener(mDrawerToggle);

        if (strResource == 0) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else {
            getSupportActionBar().setTitle(strResource);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
    }

    @Override
    public void useToolbarWithBackStack(Toolbar toolbar, int strResource) {
        setSupportActionBar(toolbar);

        if (strResource == 0) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else {
            getSupportActionBar().setTitle(strResource);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private class FetchStudentGroups extends AsyncTask<String, Void, Boolean> {

        private final int STATE_INTERNET_NOT_AVAILABLE = 0;
        private final int STATE_INTERNET_AVAILABLE = 1;
        private final int STATE_INTERNET_AVAILABLE_ERROR = 2;

        private int currentState;

        @Override
        protected void onPreExecute() {
            currentState = STATE_INTERNET_NOT_AVAILABLE;
            if (FetchUtils.isNetworkAvailableAndConnected(MainContentActivity.this)) {
                currentState = STATE_INTERNET_AVAILABLE;
            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String userId = strings[0];

            if (currentState == STATE_INTERNET_NOT_AVAILABLE) {
                return false;
            }

            try {
                List<Pair<String, String>> params = new ArrayList<>();
                params.add(new Pair<>("userId", userId));

                byte[] bytes = FetchUtils.doPostRequest(LOGIN, PASS, ADDRESS_GROUP, params);

                List<StudentGroup> list = Utils.parseStudentsGroups(new ByteArrayInputStream(bytes));
                for (StudentGroup studentGroup : list) {
                    Log.i(TAG, "doInBackground: " + studentGroup);
                }

                long count = StudentGroupLab.get(MainContentActivity.this).addStudentGroup(list);

                if (count == 0) {
                    return false;
                }
                return true;

            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
                currentState = STATE_INTERNET_AVAILABLE_ERROR;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            switch (currentState) {
                case STATE_INTERNET_AVAILABLE:
                    Toast.makeText(MainContentActivity.this, "Internet available", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_INTERNET_NOT_AVAILABLE:
                    Toast.makeText(MainContentActivity.this, "Internet no available", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_INTERNET_AVAILABLE_ERROR:
                    Toast.makeText(MainContentActivity.this, "Internet error", Toast.LENGTH_SHORT).show();
                    break;
            }
            if (result) {
                Toast.makeText(MainContentActivity.this, "Take new data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainContentActivity.this, "Take old data", Toast.LENGTH_SHORT).show();
            }

            List<StudentGroup> list = StudentGroupLab.get(MainContentActivity.this).getStudentGroups();
            if (list != null && list.size() > 0) {
                mExtraTextView.setText(list.get(0).getSpecialityName());
            }

        }
    }

    private class BasicItemHolder extends SwappingHolder implements View.OnClickListener {

        private TextView mFirstTextView;
        private TextView mSecondTextView;
        private ImageView mImageView;
        private Basic mBasic;

        public BasicItemHolder(View itemView) {
            super(itemView, mMultiSelector);

            itemView.setOnClickListener(this);
            itemView.setClickable(true);
            mFirstTextView = (TextView) itemView.findViewById(R.id.item_found_tv_name);
            mSecondTextView = (TextView) itemView.findViewById(R.id.item_found_tv_city);
            mImageView = (ImageView) itemView.findViewById(R.id.item_speciality_iv);
            this.setSelectionModeBackgroundDrawable(null);
        }

        public void bindFoundItem(Basic basic) {
            mBasic = basic;

            mFirstTextView.setText(mBasic.firstData());
            mSecondTextView.setText(mBasic.secondData());
        }

        @Override
        public void onClick(View view) {
            mMultiSelector.setSelected(this, true);
        }
    }

    private class BasicItemAdapter extends RecyclerView.Adapter<BasicItemHolder> {

        private List<? extends Basic> mList;

        public BasicItemAdapter(List<? extends Basic> list) {
            mList = list;
        }

        @Override
        public BasicItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainContentActivity.this);
            View view = layoutInflater.inflate(R.layout.item_speciality, parent, false);
            return new BasicItemHolder(view);
        }

        @Override
        public void onBindViewHolder(BasicItemHolder holder, int position) {
            Basic basic = mList.get(position);
            holder.bindFoundItem(basic);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
