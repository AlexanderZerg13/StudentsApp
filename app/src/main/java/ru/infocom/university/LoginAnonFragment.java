package ru.infocom.university;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ru.infocom.university.data.Group;
import ru.infocom.university.data.University;

public class LoginAnonFragment extends Fragment {

    private Button mEnterButton;
    private Button mEnterAuthorizationButton;
    private TextInputLayout mVuzSelectorEditTextTIL;
    private TextInputLayout mSpecialitySelectorEditTextTIL;
    private EditText mVuzSelectorEditText;
    private EditText mSpecialitySelectorEditText;

    private ILoginAuth mLoginAuth;

    public static LoginAnonFragment newInstance() {
        
        Bundle args = new Bundle();
        
        LoginAnonFragment fragment = new LoginAnonFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_anon, container, false);

        mEnterButton = (Button) v.findViewById(R.id.fragment_login_anon_btn_enter);
        mEnterAuthorizationButton = (Button) v.findViewById(R.id.fragment_login_anon_btn_enter_auth);

        mVuzSelectorEditText = (EditText) v.findViewById(R.id.fragment_login_anon_et_select_vuz);
        mSpecialitySelectorEditText = (EditText) v.findViewById(R.id.fragment_login_anon_et_select_speciality);

        mVuzSelectorEditTextTIL = (TextInputLayout) v.findViewById(R.id.fragment_login_anon_et_select_vuz_til);
        mSpecialitySelectorEditTextTIL = (TextInputLayout) v.findViewById(R.id.fragment_login_anon_et_select_speciality_til);
        mVuzSelectorEditTextTIL.setHintAnimationEnabled(false);
        mSpecialitySelectorEditTextTIL.setHintAnimationEnabled(false);

        LoginAnonOnClickListener onClickListener = new LoginAnonOnClickListener();

        mEnterButton.setOnClickListener(onClickListener);
        mEnterAuthorizationButton.setOnClickListener(onClickListener);
        mVuzSelectorEditText.setOnClickListener(onClickListener);
        mSpecialitySelectorEditText.setOnClickListener(onClickListener);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mVuzSelectorEditText.getText()) && !TextUtils.isEmpty(mSpecialitySelectorEditText.getText())) {
            mEnterButton.setEnabled(true);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLoginAuth = (ILoginAuth) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLoginAuth = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == MainChooseActivity.KEY_REQUEST_UNIVERSITY) {
            University university = (University) data.getSerializableExtra(ChooseEducationFragment.KEY_RETURN_BASIC);
            mVuzSelectorEditText.setText(university.getName());
        } else if (requestCode == MainChooseActivity.KEY_REQUEST_SPECIALITY) {
            Group group = (Group) data.getSerializableExtra(ChooseEducationFragment.KEY_RETURN_BASIC);
            String text = group.getSpeciality() + "\n" + group.getGroup();
            int index = text.indexOf(group.getGroup());
            SpannableString s = new SpannableString(text);
            s.setSpan(new TextAppearanceSpan(getActivity(), R.style.StyleUnderGroup),
                    index, index + group.getGroup().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mSpecialitySelectorEditText.setText(s);
        }
    }

    private class LoginAnonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.fragment_login_anon_btn_enter:
                    break;
                case R.id.fragment_login_anon_btn_enter_auth:
                    mLoginAuth.goToLoginAuth();
                    break;
                case R.id.fragment_login_anon_et_select_vuz:
                    intent = MainChooseActivity.newIntent(getActivity(), MainChooseActivity.KEY_REQUEST_UNIVERSITY);
                    startActivityForResult(intent, MainChooseActivity.KEY_REQUEST_UNIVERSITY);
                    break;
                case R.id.fragment_login_anon_et_select_speciality:
                    if (!TextUtils.isEmpty(mVuzSelectorEditText.getText())) {
                        intent = MainChooseActivity.newIntent(getActivity(), MainChooseActivity.KEY_REQUEST_SPECIALITY);
                        startActivityForResult(intent, MainChooseActivity.KEY_REQUEST_SPECIALITY);
                    }
                    break;
                default:
                    mLoginAuth.goToLoginAuth();
            }
        }
    }

    public interface ILoginAuth {
        void goToLoginAuth();
    }

}
