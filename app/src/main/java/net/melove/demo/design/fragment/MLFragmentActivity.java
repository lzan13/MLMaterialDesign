package net.melove.demo.design.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import net.melove.demo.design.R;
import net.melove.demo.design.application.MLBaseActivity;

/**
 * Created by lzan13 on 2016/11/30.
 */

public class MLFragmentActivity extends MLBaseActivity {

    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.view_pager) ViewPager mViewPager;

    private MLFragmentPagerAdapter mAdapter;

    private String[] mTabTitles;
    private Fragment[] mFragments;
    private MLFirstFragment firstFragment;
    private MLSecondFragment secondFragment;
    private MLThirdFragment thirdFragment;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mTabTitles = new String[] {
                "FirstFragmetn", "SecondFragment", "ThirdFragment"
        };

        firstFragment = new MLFirstFragment();
        secondFragment = new MLSecondFragment();
        thirdFragment = new MLThirdFragment();
        mFragments = new Fragment[] { firstFragment, secondFragment, thirdFragment };

        mAdapter = new MLFragmentPagerAdapter(getSupportFragmentManager(), mTabTitles, mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        // 设置 ViewPager 预加载数量
        mViewPager.setOffscreenPageLimit(3);
        // 添加 ViewPager 页面改变监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {

            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
    }
}
