package ru.infocom.university.modules.grades;

import android.support.v4.app.Fragment;

import ru.infocom.university.R;
import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.modules.Module;
import ru.infocom.university.modules.Requirement;

public class GradesModule implements Module {
    @Override
    public int getModuleId() {
        return R.id.nav_info;
    }

    @Override
    public int getModuleTitle() {
        return R.string.nav_marks;
    }

    @Override
    public int getModuleIcon() {
        return R.drawable.proxy_ic_nav_marks_18dp_black;
    }

    @Override
    public AuthorizationObject.Role getRole() {
        return AuthorizationObject.Role.STUDENT;
    }

    @Override
    public Requirement[] getModuleRequirements() {
        return new Requirement[]{
                new Requirement("EducationalPerformance", 1),
        };
    }

    @Override
    public Fragment getFragmentItem() {
        return GradesViewPagerFragment.newInstance();
    }
}
