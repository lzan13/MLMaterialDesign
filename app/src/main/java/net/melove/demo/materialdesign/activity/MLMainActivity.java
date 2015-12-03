package net.melove.demo.materialdesign.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.melove.demo.materialdesign.R;

/**
 * MLMainActivity 主界面，继承自自定义的 Activity基类 {@link MLBaseActivity}，实现了侧滑菜单
 */
public class MLMainActivity extends MLBaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initToolbar();
        initDrawer();
        initNavigation();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.layout_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.view_navigation);
        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.btn_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "悬浮按钮", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 初始化 Toolbar
     */
    private void initToolbar() {
        mToolbar.setTitle(R.string.title_home);
        setSupportActionBar(mToolbar);
    }

    /**
     * 初始化侧滑不举的一些操作
     */
    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.nav_drawer_open, R.string.nav_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * 初始化侧滑导航
     */
    private void initNavigation() {
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent();
                intent.setClass(this, MLSearchActivity.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(this,
                        android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent, optionsCompat.toBundle());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 侧滑导航的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:

                break;
            case R.id.nav_group:

                break;
            case R.id.nav_call:

                break;
            case R.id.nav_chat:

                break;
            case R.id.nav_video_call:

                break;

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
