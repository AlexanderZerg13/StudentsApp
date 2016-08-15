package com.example.pilipenko.studentsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.com.example.pilipenko.data.University;
import com.example.pilipenko.studentsapp.com.example.pilipenko.data.UniversityLab;

import java.util.List;

public class ChooseEducationFragment extends Fragment {

    private ImageButton mCloseButton;
    private EditText mInputEditText;

    private RecyclerView mFoundItemsRecyclerView;
    private FoundItemAdapter mAdapter;

    private String mLastRequest;
    private int mRequestType;

    public static final String KEY_RETURN_UNIVERSITY = "key_return_university";
    public static final String KEY_RETURN_SPECIALITY = "key_return_speciality";

    private static final String KEY_REQUEST_CODE = "request";

    public static ChooseEducationFragment newInstance(int requestCode) {

        Bundle args = new Bundle();
        args.putInt(KEY_REQUEST_CODE, requestCode);

        ChooseEducationFragment fragment = new ChooseEducationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRequestType = getArguments().getInt(KEY_REQUEST_CODE);
        if (mRequestType != MainChooseActivity.KEY_REQUEST_SPECIALITY && mRequestType != MainChooseActivity.KEY_REQUEST_UNIVERSITY) {
            throw new IllegalStateException("request type must be KEY_REQUEST_SPECIALITY or KEY_REQUEST_UNIVERSITY");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_education, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.fragment_choose_education_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        mCloseButton = (ImageButton) v.findViewById(R.id.fragment_choose_education_btn_close);
        mInputEditText = (EditText) v.findViewById(R.id.fragment_choose_education_et_input);
        mFoundItemsRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_choose_education_rv);

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        mInputEditText.setHint(
                mRequestType == MainChooseActivity.KEY_REQUEST_UNIVERSITY ?
                        R.string.fragment_choose_education_et_input_vuz :
                        R.string.fragment_choose_education_et_input_speciality);

        mInputEditText.addTextChangedListener(new InputEditTextTextWatcher());
        mInputEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) ChooseEducationFragment.this
                            .getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mInputEditText.getWindowToken(), 0);

                    return true;
                }

                return false;
            }
        });

        mFoundItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    private void updateUI() {
        UniversityLab universityLab = UniversityLab.get(getActivity());
        List<University> list = TextUtils.isEmpty(mLastRequest) ?
                universityLab.getAllUniversities() : universityLab.findUniversities(mLastRequest);
        mAdapter = new FoundItemAdapter(list);
        mFoundItemsRecyclerView.setAdapter(mAdapter);
    }

    private class InputEditTextTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            mLastRequest = editable.toString();
            updateUI();
        }
    }

    private class FoundItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameTextView;
        private TextView mCityTextView;
        private University mUniversity;

        public FoundItemHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mNameTextView = (TextView) itemView.findViewById(R.id.item_found_tv_name);
            mCityTextView = (TextView) itemView.findViewById(R.id.item_found_tv_city);
        }

        public void bindFoundItem(University university) {
            String name = university.getName();
            mUniversity = university;
            if (TextUtils.isEmpty(mLastRequest)) {
                mNameTextView.setText(name);
            } else {
                mNameTextView.setText(UniversityLab.getSpannableStringMatches(university, mLastRequest));
            }
            mCityTextView.setText(university.getCity());
        }

        @Override
        public void onClick(View view) {
            Intent data = new Intent();
            data.putExtra(KEY_RETURN_UNIVERSITY, mUniversity);
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();
        }
    }

    private class FoundItemAdapter extends RecyclerView.Adapter<FoundItemHolder> {

        private List<University> mList;

        public FoundItemAdapter(List<University> list) {
            mList = list;
        }

        @Override
        public FoundItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_found, parent, false);
            return new FoundItemHolder(view);
        }

        @Override
        public void onBindViewHolder(FoundItemHolder holder, int position) {
            University university = mList.get(position);
            holder.bindFoundItem(university);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
