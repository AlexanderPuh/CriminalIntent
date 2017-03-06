package com.alexb8466gmail.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.List;
import java.util.UUID;

/**
 * Created by User on 13.02.2017.
 */

public class CrimePagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private static final String EXTRA_CRIME_ID="com.alexb8466.criminalintent.crime_id";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
   setContentView(R.layout.acrivity_crime_pager);
        final UUID crimeId=(UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager=(ViewPager)findViewById(R.id.activity_crime_pager_view_pager);
        mCrimes=CrimeLab.getCrimeLab(this).getCrimes();
        FragmentManager fragmentManager=getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime=mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {

                return mCrimes.size();
            }
        });
for (int i=0;i<mCrimes.size();i++){

    if (mCrimes.get(i).getId().equals(crimeId)){
mViewPager.setCurrentItem(i);
        break;


    }


}
    }
    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent=new Intent(packageContext,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;

    }


}