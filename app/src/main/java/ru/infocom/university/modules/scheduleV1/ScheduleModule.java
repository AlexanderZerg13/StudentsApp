package ru.infocom.university.modules.scheduleV1;

import android.support.v4.app.Fragment;

import ru.infocom.university.R;
import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.modules.Module;
import ru.infocom.university.modules.Requirement;

public class ScheduleModule implements Module {

    @Override
    public int getModuleId() {
        return R.id.nav_classes_schedule1;
    }

    @Override
    public int getModuleTitle() {
        return R.string.nav_classes_schedule1;
    }

    @Override
    public int getModuleIcon() {
        return R.drawable.proxy_ic_nav_classes_schedule_18dp_black;
    }

    @Override
    public AuthorizationObject.Role getRole() {
        return AuthorizationObject.Role.BOTH;
    }

    @Override
    public Requirement[] getModuleRequirements() {
        return new Requirement[]{
                new Requirement("GetSchedule", 2),
                new Requirement("MockMethod", 1),
        };
    }

    @Override
    public Fragment getFragmentItem() {
        return ScheduleDayViewPagerFragment.newInstance();
    }
}
