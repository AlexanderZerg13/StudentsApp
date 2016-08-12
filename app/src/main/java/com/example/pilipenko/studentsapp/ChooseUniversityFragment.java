package com.example.pilipenko.studentsapp;

import android.content.Context;
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

import org.w3c.dom.Text;

import java.util.List;

public class ChooseUniversityFragment extends Fragment{

    private ImageButton mCloseButton;
    private EditText mInputEditText;

    private RecyclerView mFoundItemsRecyclerView;
    private FoundItemAdapter mAdapter;

    private String lastRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_something, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.fragment_choose_something_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        mCloseButton = (ImageButton) v.findViewById(R.id.fragment_choose_something_btn_close);
        mInputEditText = (EditText) v.findViewById(R.id.fragment_choose_something_et_input);
        mFoundItemsRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_choose_something_recycler_view);

        mInputEditText.setHint(R.string.fragment_choose_something_et_input_vuz);
        mInputEditText.addTextChangedListener(new InputEditTextTextWatcher());
        mInputEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)ChooseUniversityFragment.this
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

    private void updateUI(){
        UniversityLab universityLab = UniversityLab.get(getActivity());
        List<University> list = TextUtils.isEmpty(lastRequest)?
                universityLab.getAllUniversities(): universityLab.findUniversities(lastRequest);
        mAdapter = new FoundItemAdapter(list);
        mFoundItemsRecyclerView.setAdapter(mAdapter);
    }

    private class InputEditTextTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            lastRequest = editable.toString();
            updateUI();
        }
    }

    private class FoundItemHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView;
        private TextView mCityTextView;

        public FoundItemHolder(View itemView) {
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.item_found_tv_name);
            mCityTextView = (TextView) itemView.findViewById(R.id.item_found_tv_city);
        }

        public void bindFoundItem(University university) {
            mNameTextView.setText(university.getName());
            mCityTextView.setText(university.getCity());
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
