package com.alexb8466gmail.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by User on 06.02.2017.
 */

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private static final int REQUEST_CODE=0;
    private static final int REQUEST_CONTACT=1;
    private static final int REQUEST_PHOTO=2;

    private static final String ARG_CRIME_ID="crime_id";
    private static final String DIALOG_DATE="DialogDate";
    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args=new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment=new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeID=(UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime=CrimeLab.getCrimeLab(getActivity()).getCrime(crimeID);
        mPhotoFile=CrimeLab.getCrimeLab(getActivity()).getPhotoFile(mCrime);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_crime,container,false);
//mTitleField.setText(mCrime.getTitle());
        final Intent pickContact=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton=(Button)v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        if (mCrime.getSuspect()!=null){
            mSuspectButton.setText(mCrime.getSuspect());
        }
        PackageManager packageManager=getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY)==null){
            mSuspectButton.setEnabled(false);
        }
        TextView mTitleOfCrime=(TextView)v.findViewById(R.id.crime_title_label);
        mTitleOfCrime.setText(mCrime.getTitle());
        mReportButton=(Button)v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                i=Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });
        mTitleField=(EditText)v.findViewById(R.id.crime_title);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
mDateButton=(Button)v.findViewById(R.id.crime_date);
       /// Date currentDate = new Date();
      //  SimpleDateFormat dateFormat = null;

      //  mDateButton.setText(dateFormat.format( currentDate ) );
        updateDate();

        mDateButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        FragmentManager manager=getFragmentManager();
        DatePickerFragment dialog=DatePickerFragment.newInstance(mCrime.getDate());
        dialog.setTargetFragment(CrimeFragment.this, REQUEST_CODE);
        dialog.show(manager, DIALOG_DATE);
    }
});

        mSolvedCheckBox=(CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheked) {
                mCrime.setSolved(isCheked);
            }
        });


mPhotoButton=(ImageButton)v.findViewById(R.id.crime_camera);
        final Intent captureImage=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto=mPhotoFile!=null&&captureImage.resolveActivity(packageManager)!=null;
        mPhotoButton.setEnabled(canTakePhoto);
        if (canTakePhoto){
            Uri uri=Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });
        mPhotoView=(ImageView)v.findViewById(R.id.crime_photo);
updatePhotoView();
        return v;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK)
        {
            return;
        }
        if(requestCode==REQUEST_CODE){
Date date=(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();

        }else if(requestCode==REQUEST_CONTACT&&data!=null) {
            Uri contactUri=data.getData();
            String[] queryFields=new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor c=getActivity().getContentResolver().query(contactUri,queryFields,null,null,null);

        try {
            if (c.getCount()==0){
            return;
            }

        c.moveToFirst();
            String suspect=c.getString(0);
            mCrime.setSuspect(suspect);
            mSuspectButton.setText(suspect);
        }finally {
           c.close();
        }
        }else if(requestCode==REQUEST_PHOTO){
            updatePhotoView();
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getCrimeLab(getActivity()).updateCrime(mCrime);
    }
    private String getCrimeReport(){


        String solvedString=null;
        if(mCrime.isSolved()){
            solvedString=getString(R.string.crime_report_solved);
        }else {
                solvedString=getString(R.string.crime_report_unsolved);
        }
        String dateFormat="EEE, MMM dd";
        String dateString=DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect=mCrime.getSuspect();
        if(suspect==null){
            suspect=getString(R.string.crime_report_no_suspect);
        }else {
            suspect=getString(R.string.crime_report_suspect, suspect);
        }
        String report=getString(R.string.crime_report,mCrime.getTitle(),dateString, solvedString, suspect);
        return report;

    }
    private void updatePhotoView(){
        if (mPhotoView==null||!mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else {
            Bitmap bitmap=PicturesUtil.getScaledBitMap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }

    }
}
