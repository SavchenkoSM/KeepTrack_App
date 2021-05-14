package com.example.keeptrack;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity  {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private AllUsersFragment allUsersFragment;
    private InboxFragment inboxFragment;
    private OutboxFragment outboxFragment;

    private AppBarLayout appBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_activity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        allUsersFragment = new AllUsersFragment();
        inboxFragment = new InboxFragment();
        outboxFragment  = new OutboxFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(allUsersFragment, "All Users");
        viewPagerAdapter.addFragment(inboxFragment, "Inbox");
        viewPagerAdapter.addFragment(outboxFragment, "Outbox");
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_users);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_inbox);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_outbox);

        //BadgeDrawable badgeDrawable = tabLayout.getTabAt(1).getOrCreateBadge();
        //badgeDrawable.setVisible(true);
        //badgeDrawable.setNumber(5);

        //BadgeDrawable badgeDrawable1 = tabLayout.getTabAt(2).getOrCreateBadge();
        //badgeDrawable1.setVisible(true);
        //badgeDrawable1.setNumber(5);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();


        private ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
                return fragments.get(position);
            }

        @Override
        public int getCount() {
             return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
                return fragmentTitle.get(position);
            }
    }
}
