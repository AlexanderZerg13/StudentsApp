package ru.infocom.university;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.data.Lesson;
import ru.infocom.university.data.LessonLab;
import ru.infocom.university.data.StaticData;
import ru.infocom.university.interfaces.IToolbar;
import ru.infocom.university.interfaces.ITransitionActions;

import java.util.List;
import java.util.Random;

public class LessonDescribeFragment extends Fragment {

    private static final String TAG = "LessonDescribeFragment";

    private static final String KEY_BUNDLE_LESSON = "BUNDLE_LESSON";

    private IToolbar mToolbarActivity;

    private Lesson mLesson;

    private Button mMoreTeacherButton;
    private Button mAboutDisciplineButton;
    private TextView mLessonNameTextView;
    private TextView mStartTimeTextView;
    private TextView mEndTimeTextView;
    private LinearLayout mAudienceLinearLayout;
    private TextView mAudienceTextView;
    private TextView mTypeTextView;
    private LinearLayout mTeachersOrGroupsLinearLayout;
    private LinearLayout mTeachersOrGroupsLinearLayoutMain;

    /*TODO Lesson must be parcelable */
    public static LessonDescribeFragment newInstance(Lesson lesson) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_BUNDLE_LESSON, lesson);

        LessonDescribeFragment fragment = new LessonDescribeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLesson = (Lesson) getArguments().getSerializable(KEY_BUNDLE_LESSON);
        Log.i(TAG, "onCreate: " + mLesson);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_describe, container, false);

        Toolbar toolbar = view.findViewById(R.id.fragment_lesson_describe_toolbar);
        mToolbarActivity.useToolbarWithBackStack(toolbar, R.string.lesson_describe);

        AuthorizationObject user = DataPreferenceManager.provideUserPreferences().getUser(getContext());

        mMoreTeacherButton = view.findViewById(R.id.fragment_academic_plan_describe_btn_more);
        mAboutDisciplineButton = view.findViewById(R.id.fragment_lesson_describe_btn_about);
        mLessonNameTextView = view.findViewById(R.id.fragment_academic_plan_describe_tv_name);
        mStartTimeTextView = view.findViewById(R.id.fragment_lesson_describe_tv_start_time);
        mEndTimeTextView = view.findViewById(R.id.fragment_lesson_describe_tv_end_time);
        mAudienceLinearLayout = view.findViewById(R.id.fragment_lesson_describe_audience_linear_layout);
        mAudienceTextView = view.findViewById(R.id.fragment_lesson_describe_audience);
        mTypeTextView = view.findViewById(R.id.fragment_lesson_describe_tv_type);
        mTeachersOrGroupsLinearLayout = view.findViewById(R.id.fragment_academic_plan_describe_ll_teachers);
        mTeachersOrGroupsLinearLayoutMain = view.findViewById(R.id.fragment_lesson_describe_ll_teachers_main);

        if (user.getRole().equals(AuthorizationObject.Role.STUDENT)) {
            setupTeachers();
        } else if (user.getRole().equals(AuthorizationObject.Role.TEACHER)) {
            setupGroups();
        }

        mAudienceLinearLayout.setVisibility(mLesson.getAudience() == null ? View.GONE : View.VISIBLE);
        mAudienceTextView.setText(mLesson.getAudience());

        mStartTimeTextView.setText("Начало: " + mLesson.getTimeStart());
        mEndTimeTextView.setText("Конец: " + mLesson.getTimeEnd());
        mLessonNameTextView.setText(mLesson.getName());
        String typeString = (mLesson.getType().length() >= 3 ? mLesson.getType().substring(0, 3) : mLesson.getType()).toUpperCase();
        switch (typeString) {
            case "ЛЕК":
                mTypeTextView.setText(Utils.coloredSomePartOfText(mLesson.getType(),
                        ContextCompat.getColor(getActivity(), R.color.colorGreen2),
                        null));
                break;
            default:
                mTypeTextView.setText(Utils.coloredSomePartOfText(mLesson.getType(),
                        ContextCompat.getColor(getActivity(), R.color.colorBlue2),
                        null));
                break;
        }
        mAboutDisciplineButton.setOnClickListener(view1 -> {
            //mITransitionActions.goToDescribeAcademicPlan(0, 0);
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mToolbarActivity = (IToolbar) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mToolbarActivity = null;
    }

    private void setupTeachers() {
        final List<String> list = mLesson.getTeachers();
        if (TextUtils.isEmpty(mLesson.getTeachersString())) {
            Log.i(TAG, "setupTeachers: null" + list.size());
            mTeachersOrGroupsLinearLayoutMain.setVisibility(View.GONE);
            return;
        }

        mTeachersOrGroupsLinearLayout.addView(getTeacherView(list.get(0), null));

        if (list.size() == 1) {
            mMoreTeacherButton.setVisibility(View.INVISIBLE);
        } else {
            mMoreTeacherButton.setText(getString(R.string.another, list.size() - 1));
            mMoreTeacherButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMoreTeacherButton.setVisibility(View.INVISIBLE);
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    Resources r = getResources();
                    for (int i = 1; i < list.size(); i++) {
                        LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        int topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, r.getDisplayMetrics());
                        margin.setMargins(0, topMargin, 0, 0);
                        Log.i("TAG", "onClick: " + topMargin);
                        mTeachersOrGroupsLinearLayout.addView(getTeacherView(list.get(i), layoutInflater), margin);
                    }
                }
            });
        }
    }

    private void setupGroups() {
        String group = mLesson.getGroup();
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View groupView = inflater.inflate(R.layout.item_teacher, mTeachersOrGroupsLinearLayout, false);
        TextView groupName = groupView.findViewById(R.id.item_teacher_tv_teacher_name);
        TextView groupDescribe = groupView.findViewById(R.id.item_teacher_tv_teacher_describe);
        groupDescribe.setVisibility(View.GONE);
        groupName.setText(group);

        mTeachersOrGroupsLinearLayout.addView(groupView);
        mMoreTeacherButton.setVisibility(View.INVISIBLE);
    }

    private View getTeacherView(String teacher, LayoutInflater inflater) {
        if (inflater == null) {
            inflater = LayoutInflater.from(getActivity());
        }
        //Random random = new Random();
        View teacherView = inflater.inflate(R.layout.item_teacher, mTeachersOrGroupsLinearLayout, false);
        TextView teacherName = teacherView.findViewById(R.id.item_teacher_tv_teacher_name);
        TextView teacherDescribe = teacherView.findViewById(R.id.item_teacher_tv_teacher_describe);
        teacherName.setText(teacher);
        teacherDescribe.setVisibility(View.GONE);
        //teacherDescribe.setText(StaticData.sTeachers.get(random.nextInt(StaticData.sTeachers.size())).getPost());

        return teacherView;
    }
}
