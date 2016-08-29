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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.data.Basic;
import com.example.pilipenko.studentsapp.data.GroupLab;
import com.example.pilipenko.studentsapp.data.UniversityLab;

import java.util.List;

public class ChooseEducationFragment extends Fragment {

    private ImageButton mCloseButton;
    private EditText mInputEditText;

    private RecyclerView mFoundItemsRecyclerView;
    private BasicItemAdapter mAdapter;

    private String mLastRequest;
    private int mRequestType;

    public static final String KEY_RETURN_BASIC = "key_return_basic";

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
        mInputEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        InputMethodManager imm = (InputMethodManager) ChooseEducationFragment.this
                                .getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mInputEditText.getWindowToken(), 0);
                        return true;
                    default:
                        return false;
                }
            }
        });

        mFoundItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    private void updateUI() {
        List<? extends Basic> list;
        if (mRequestType == MainChooseActivity.KEY_REQUEST_UNIVERSITY) {
            UniversityLab universityLab = UniversityLab.get(getActivity());
            list = TextUtils.isEmpty(mLastRequest) ?
                    universityLab.getAllUniversities() : universityLab.findUniversities(mLastRequest);
        } else {
            GroupLab groupLab = GroupLab.get(getActivity());
            list = TextUtils.isEmpty(mLastRequest) ?
                    groupLab.getAllGroups() : groupLab.findGroups(mLastRequest);
        }

        mAdapter = new BasicItemAdapter(list);
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

    private class BasicItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mFirstTextView;
        private TextView mSecondTextView;
        private Basic mBasic;

        public BasicItemHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mFirstTextView = (TextView) itemView.findViewById(R.id.item_found_tv_name);
            mSecondTextView = (TextView) itemView.findViewById(R.id.item_found_tv_city);
        }

        public void bindFoundItem(Basic basic) {
            mBasic = basic;
            if (mRequestType == MainChooseActivity.KEY_REQUEST_UNIVERSITY) {
                mSecondTextView.setTextColor(getResources().getColor(R.color.colorBlack_54a));
            } else {
                mSecondTextView.setTextColor(getResources().getColor(R.color.colorDeepOrange));
            }

            if (TextUtils.isEmpty(mLastRequest)) {
                mFirstTextView.setText(mBasic.firstData());
            } else {
                mFirstTextView.setText(Utils.getSpannableStringMatches(mBasic.firstData(), mLastRequest));
            }
            mSecondTextView.setText(mBasic.secondData());
        }

        @Override
        public void onClick(View view) {
            Intent data = new Intent();
            data.putExtra(KEY_RETURN_BASIC, mBasic);
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();
        }
    }

    private class BasicItemAdapter extends RecyclerView.Adapter<BasicItemHolder> {

        private List<? extends Basic> mList;

        public BasicItemAdapter(List<? extends Basic> list) {
            mList = list;
        }

        @Override
        public BasicItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_found, parent, false);
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
