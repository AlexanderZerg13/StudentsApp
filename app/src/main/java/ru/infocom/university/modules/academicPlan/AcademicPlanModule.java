package ru.infocom.university.modules.academicPlan;

import android.support.v4.app.Fragment;

import ru.infocom.university.R;
import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.modules.Module;
import ru.infocom.university.modules.Requirement;

public class AcademicPlanModule implements Module {
    @Override
    public int getModuleId() {
        return R.id.nav_marks;
    }

    @Override
    public int getModuleTitle() {
        return R.string.nav_info;
    }

    @Override
    public int getModuleIcon() {
        return R.drawable.proxy_ic_nav_info_20dp_black;
    }

    @Override
    public AuthorizationObject.Role getRole() {
        return AuthorizationObject.Role.STUDENT;
    }

    @Override
    public Requirement[] getModuleRequirements() {
        return new Requirement[]{
                new Requirement("GetCurriculumLoad", 1),
        };
    }

    @Override
    public Fragment getFragmentItem() {
        return AcademicPlanViewPagerFragment.newInstance();
    }
}
