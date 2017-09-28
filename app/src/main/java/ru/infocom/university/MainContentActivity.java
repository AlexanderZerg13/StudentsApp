package ru.infocom.university;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SingleSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.data.LessonLab;
import ru.infocom.university.data.LessonPlanLab;
import ru.infocom.university.data.LessonProgressLab;
import ru.infocom.university.data.StudentGroup;
import ru.infocom.university.data.StudentGroupLab;
import ru.infocom.university.interfaces.IToolbar;
import ru.infocom.university.interfaces.ITransitionActions;
import ru.infocom.university.model.RecordBook;
import ru.infocom.university.service.FetchDataIntentService;

import java.util.List;


public class MainContentActivity extends AppCompatActivity implements IToolbar, ITransitionActions {

    private static final String TAG = "MainContentActivity";

    private static final String KEY_AUTH_OBJECT = "AUTH_OBJECT";

    private View mHeaderView;
    private TextView mNameTextView;
    private TextView mExtraTextView;
    private ImageView mSwitchMenuButton;

    private DrawerLayout mDrawer;
    private NavigationView mNavView;
    private NavigationView mNavViewExit;
    private ActionBarDrawerToggle mDrawerToggle;

    private AuthorizationObject user;
    private FetchDataReceiver mFetchDataReceiver;

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

        IntentFilter intentFilter = new IntentFilter(FetchDataIntentService.BROADCAST_ACTION);
        mFetchDataReceiver = new FetchDataReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mFetchDataReceiver,
                intentFilter);

        Window window = getWindow();

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getAttributes().flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        user = (AuthorizationObject) getIntent().getSerializableExtra(KEY_AUTH_OBJECT);

        mMultiSelector.setSelectable(true);
        mMultiSelector.setSelected(0, 0, true);

        mDrawer = findViewById(R.id.main_content_drwLayout);
        mDrawer.setStatusBarBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mNavView = findViewById(R.id.main_content_navView);
        mNavViewExit = findViewById(R.id.main_content_navView_bottom);

        if (user.getRole().equals(AuthorizationObject.Role.TEACHER)) {
            mNavView.getMenu().findItem(R.id.nav_marks).setVisible(false);
            mNavView.getMenu().findItem(R.id.nav_info).setVisible(false);
        }

        setupDrawerContent(mNavView);
        setupDrawerContent(mNavViewExit);

        mHeaderView = mNavView.getHeaderView(0);
        mNameTextView = mHeaderView.findViewById(R.id.header_layout_tv_name);
        mExtraTextView = mHeaderView.findViewById(R.id.header_layout_tv_extra);
        mSwitchMenuButton = mHeaderView.findViewById(R.id.header_layout_btn_expand);

        String[] fio = user.getName().split(" ");
        mNameTextView.setText(fio[1] + " " + fio[0]);

        mSwitchMenuButton.setVisibility(user.getRole() == AuthorizationObject.Role.TEACHER? View.GONE: View.VISIBLE);
        mSwitchMenuButton.setOnClickListener(view -> {
            Menu menu = mNavView.getMenu();
            MenuItem item = menu.findItem(R.id.nav_marks);
            menu.clear();
            if (item == null) {
                if (mNavView.getHeaderCount() != 1) {
                    View v = mNavView.getHeaderView(1);
                    mNavView.removeHeaderView(v);
                }
                mNavView.inflateMenu(R.menu.drawer_view);
                if (user.getRole().equals(AuthorizationObject.Role.TEACHER)) {
                    mNavView.getMenu().findItem(R.id.nav_marks).setVisible(false);
                    mNavView.getMenu().findItem(R.id.nav_info).setVisible(false);
                }
            } else {
                if (user.getRole().equals(AuthorizationObject.Role.STUDENT)) {
                    View subHead = LayoutInflater.from(MainContentActivity.this).inflate(R.layout.items_recycler_view, mNavView, false);
                    RecyclerView recyclerView = subHead.findViewById(R.id.items_recycler_view);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainContentActivity.this));

                    List<RecordBook> recordBooks = user.getRecordBooks();
                    BasicItemAdapter bIA = new BasicItemAdapter(recordBooks);
                    recyclerView.setAdapter(bIA);

                    mNavView.addHeaderView(subHead);
                }
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(() -> {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_content_fragmentContainer);
            if (fragment instanceof ScheduleDayViewPagerFragment) {
                mNavView.setCheckedItem(R.id.nav_classes_schedule);
            }
        });
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_content_fragmentContainer);

        if (fragment == null) {
            fragment = ScheduleDayViewPagerFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.main_content_fragmentContainer, fragment)
                    .commit();
        }

        if (user.getRole().equals(AuthorizationObject.Role.STUDENT)) {
            mExtraTextView.setText(R.string.nav_student);
        } else {
            mExtraTextView.setText(R.string.nav_teacher);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mFetchDataReceiver);
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
                                if (!(fragment instanceof GradesViewPagerFragment)) {
                                    newFragment = GradesViewPagerFragment.newInstance();
                                }
                                break;
                            case R.id.nav_classes_schedule:
                                if (!(fragment instanceof ScheduleDayViewPagerFragment)) {
                                    newFragment = ScheduleDayViewPagerFragment.newInstance();
                                }
                                break;
//                            case R.id.nav_session_schedule:
//                                if (!(fragment instanceof ScheduleSessionFragment)) {
//                                    newFragment = ScheduleSessionFragment.newInstance();
//                                }
//                                break;
                            case R.id.nav_info:
                                if (!(fragment instanceof AcademicPlanViewPagerFragment)) {
                                    newFragment = AcademicPlanViewPagerFragment.newInstance();
                                }
                                break;
                            /*case R.id.nav_settings:
                                if (!(fragment instanceof SettingsFragment)) {
                                    newFragment = SettingsFragment.newInstance();
                                }
                                break;*/
                            case R.id.nav_exit:
                                DataPreferenceManager.provideUserPreferences().clearUser(MainContentActivity.this);
                                StudentGroupLab.get(MainContentActivity.this).clearStudentGroups();
                                LessonLab.get(MainContentActivity.this).clearLesson();
                                LessonProgressLab.get(MainContentActivity.this).clearLessonProgress();
                                LessonPlanLab.get(MainContentActivity.this).clearLessonsPlan();
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
    public void goToDescribeAcademicPlan(int idSemester, int idDiscipline) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = AcademicPlanDescribeFragment.newInstance(idSemester, idDiscipline);
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
//        mNavView.setCheckedItem(R.id.nav_session_schedule);
    }

    @Override
    public void setToolbarTitle(int strResource) {
        if (strResource == 0) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else {
            getSupportActionBar().setTitle(strResource);
        }
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

        setToolbarTitle(strResource);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
    }

    @Override
    public void useToolbarWithBackStack(Toolbar toolbar, int strResource) {
        setSupportActionBar(toolbar);

        setToolbarTitle(strResource);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private class BasicItemHolder extends SwappingHolder implements View.OnClickListener {

        private TextView mFirstTextView;
        private TextView mSecondTextView;
        private ImageView mImageView;
        private RecordBook mRecordBook;

        public BasicItemHolder(View itemView) {
            super(itemView, mMultiSelector);

            itemView.setOnClickListener(this);
            itemView.setClickable(true);
            mFirstTextView = itemView.findViewById(R.id.item_found_tv_name);
            mSecondTextView = itemView.findViewById(R.id.item_found_tv_city);
            mImageView = itemView.findViewById(R.id.item_speciality_iv);
            this.setSelectionModeBackgroundDrawable(null);
        }

        public void bindFoundItem(RecordBook recordBook) {
            mRecordBook = recordBook;

            mFirstTextView.setText(mRecordBook.getSpecialityName());
            mSecondTextView.setText(mRecordBook.getAcademicGroupName());
        }

        @Override
        public void onClick(View view) {
            mMultiSelector.setSelected(this, true);
        }
    }

    private class BasicItemAdapter extends RecyclerView.Adapter<BasicItemHolder> {

        private List<RecordBook> mList;

        public BasicItemAdapter(List<RecordBook> list) {
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
            RecordBook recordBook = mList.get(position);
            holder.bindFoundItem(recordBook);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    public interface IFragmentReceiver {
        void onReceive(Context context, Intent intent);
    }

    private class FetchDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: ");

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_content_fragmentContainer);
            if (fragment != null && fragment instanceof IFragmentReceiver) {
                ((IFragmentReceiver) fragment).onReceive(context, intent);
            }
        }
    }
}
