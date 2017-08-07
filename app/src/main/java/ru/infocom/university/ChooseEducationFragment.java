package ru.infocom.university;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.infocom.university.data.Basic;
import ru.infocom.university.data.GroupLab;
import ru.infocom.university.data.University;
import ru.infocom.university.data.UniversityLab;
import ru.infocom.university.service.FetchDataIntentService;

import java.util.List;

public class ChooseEducationFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<University>>, MainContentActivity.IFragmentReceiver {

    private ImageButton mCloseButton;
    private EditText mInputEditText;

    private ProgressBar mProgressBar;
    private RecyclerView mFoundItemsRecyclerView;
    private FrameLayout mFrameLayoutError;
    private BasicItemAdapter mAdapter;

    private String mLastRequest;
    private int mRequestType;

    public static final String KEY_RETURN_BASIC = "key_return_basic";

    private static final String TAG = "ChooseEducationFragment";

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
        getLoaderManager().initLoader(0, null, this);
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
        mProgressBar = (ProgressBar) v.findViewById(R.id.fragment_choose_education_progress_bar);
        mFrameLayoutError = (FrameLayout) v.findViewById(R.id.fragment_choose_education_layout_error);

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

        mProgressBar.setVisibility(View.VISIBLE);
        mFoundItemsRecyclerView.setVisibility(View.GONE);
        mFrameLayoutError.setVisibility(View.GONE);
        getLoaderManager().getLoader(0).forceLoad();
        return v;
    }

    @Override
    public Loader<List<University>> onCreateLoader(int id, Bundle args) {
        return new UniversityAsyncTaskLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<University>> loader, List<University> list) {
        if (list == null || list.size() == 0) {

            if(!FetchUtils.isNetworkAvailableAndConnected(getContext())) {
                showErrorNetwork();
                return;
            }



            Intent intent = FetchDataIntentService.newIntentFetchUniversityList(getActivity());
            this.getContext().startService(intent);

            return;
        }


        updateUI();
    }

    @Override
    public void onLoaderReset(Loader<List<University>> loader) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        if (!intent.getStringExtra(FetchDataIntentService.KEY_EXTRA_ACTION).equals(FetchDataIntentService.ACTION_UNIVERSITIES)) {
            return;
        }

        boolean result = intent.getBooleanExtra(FetchDataIntentService.KEY_EXTRA_STATUS, false);

        if (result) {
            getLoaderManager().getLoader(0).forceLoad();
        } else {
            showErrorNetwork();
        }
    }

    private void updateUI() {
        mProgressBar.setVisibility(View.GONE);
        mFoundItemsRecyclerView.setVisibility(View.VISIBLE);
        mFrameLayoutError.setVisibility(View.GONE);
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

    private void showErrorNetwork() {
        mProgressBar.setVisibility(View.GONE);
        mFoundItemsRecyclerView.setVisibility(View.GONE);
        mFrameLayoutError.setVisibility(View.VISIBLE);

        TextView textViewTitle = (TextView) mFrameLayoutError.findViewById(R.id.layout_error_text_view_title);
        TextView textViewSubTitle = (TextView) mFrameLayoutError.findViewById(R.id.layout_error_text_view_sub_title);
        Button buttonGoTo = (Button) mFrameLayoutError.findViewById(R.id.layout_error_button_go_to);

        textViewTitle.setText(R.string.error);
        textViewSubTitle.setText(R.string.errorLoadUniversities);
        buttonGoTo.setText(R.string.errorLessonsRefresh);
        buttonGoTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                mFoundItemsRecyclerView.setVisibility(View.GONE);
                mFrameLayoutError.setVisibility(View.GONE);
                getLoaderManager().getLoader(0).forceLoad();
            }
        });
    }

    private static class UniversityAsyncTaskLoader extends AsyncTaskLoader<List<University>> {

        public UniversityAsyncTaskLoader(Context context) {
            super(context);
        }

        @Override
        public List<University> loadInBackground() {
            Log.i(TAG, "loadInBackground: ");
            List<University> list;
            UniversityLab universityLab = UniversityLab.get(getContext());
            list = universityLab.getAllUniversities();

            return list;
        }
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
