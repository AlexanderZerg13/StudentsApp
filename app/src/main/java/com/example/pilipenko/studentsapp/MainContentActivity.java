package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.com.example.pilipenko.data.Basic;
import com.example.pilipenko.studentsapp.com.example.pilipenko.data.Group;
import com.example.pilipenko.studentsapp.com.example.pilipenko.data.StaticData;

import java.util.List;


public class MainContentActivity extends AppCompatActivity implements IToolbar, DisciplineFragment.IDisciplineActions {

    private View mHeaderView;
    private TextView mNameTextView;
    private TextView mExtraTextView;
    private ImageButton mSwitchMenuButton;

    private DrawerLayout mDrawer;
    private NavigationView mNavView;
    private ActionBarDrawerToggle mDrawerToggle;


    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MainContentActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);

        mDrawer = (DrawerLayout) findViewById(R.id.main_content_drwLayout);
        mDrawer.setStatusBarBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mNavView = (NavigationView) findViewById(R.id.main_content_navView);
        setupDrawerContent(mNavView);

        mHeaderView = mNavView.getHeaderView(0);
        mNameTextView = (TextView) mHeaderView.findViewById(R.id.header_layout_tv_name);
        mExtraTextView = (TextView) mHeaderView.findViewById(R.id.header_layout_tv_extra);
        mSwitchMenuButton = (ImageButton) mHeaderView.findViewById(R.id.header_layout_btn_expand);

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
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_content_fragmentContainer);

        if (fragment == null) {
            fragment = GradesFragment.newInstance();
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
                            case R.id.nav_info:
                                if (!(fragment instanceof DisciplineFragment)) {
                                    newFragment = DisciplineFragment.newInstance();
                                }

                                break;
                            case R.id.nav_marks:
                                if (!(fragment instanceof GradesFragment)) {
                                    newFragment = GradesFragment.newInstance();
                                }
                                break;
                            default:
                                if (!(fragment instanceof BasicFragment)) {
                                    newFragment = BasicFragment.newInstance();
                                }
                                break;
                        }
                        if (newFragment != null) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_content_fragmentContainer, newFragment)
                                    .commit();
                        }
                        mDrawer.closeDrawers();
                        return true;
                    }
                }
        );
    }

    @Override
    public void goToDescribeDiscipline(int idSemester, int idDiscipline) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = DisciplineDescribeFragment.newInstance(idSemester, idDiscipline);
        fragmentManager.beginTransaction()
                .replace(R.id.main_content_fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void useToolbar(Toolbar toolbar) {
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

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
    }

    private class BasicItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mFirstTextView;
        private TextView mSecondTextView;
        private Basic mBasic;

        public BasicItemHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setClickable(true);
            mFirstTextView = (TextView) itemView.findViewById(R.id.item_found_tv_name);
            mSecondTextView = (TextView) itemView.findViewById(R.id.item_found_tv_city);
        }

        public void bindFoundItem(Basic basic) {
            mBasic = basic;

            mFirstTextView.setText(mBasic.firstData());
            mSecondTextView.setText(mBasic.secondData());
        }

        @Override
        public void onClick(View view) {
            itemView.setActivated(true);
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
