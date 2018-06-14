package ru.infocom.university.modules;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ru.infocom.university.R;
import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.modules.academicPlan.AcademicPlanModule;
import ru.infocom.university.modules.grades.GradesModule;
import ru.infocom.university.modules.schedule.ScheduleModule;

public class ModulesConfig {
    private static Module[] AVAILABLE_MODULES = new Module[]{
        new AcademicPlanModule(),
        new ScheduleModule(),
        new ru.infocom.university.modules.scheduleV1.ScheduleModule(),
        new GradesModule()
    };

    public static List<Requirement> moduleConfigList = new ArrayList<Requirement>() {
        {
            add(new Requirement("GetCurriculumLoad", 1));
            add(new Requirement("GetSchedule", 1));
            add(new Requirement("EducationalPerformance", 1));
        }
    };

    public static AuthorizationObject.Role sRole;
    public static Module[] sModules;

    public static void prepareModules(NavigationView navigationView, AuthorizationObject.Role role) {
        List<Requirement> configList = moduleConfigList;

        if (ModulesConfig.sModules != null && ModulesConfig.sRole != null && ModulesConfig.sRole.equals(role)) {
            ModulesConfig.configNavView(navigationView, ModulesConfig.sModules);
            return;
        }

        List<Module> resultModulesList = new ArrayList<>();
        for(Module module : AVAILABLE_MODULES) {
            boolean checkRequirement = false;
            for (Requirement requirement : module.getModuleRequirements()) {
                checkRequirement = configList.contains(requirement);
            }

            if (checkRequirement && (role.equals(module.getRole()) || module.getRole().equals(AuthorizationObject.Role.BOTH))) {
                resultModulesList.add(module);
            }
        }

        ModulesConfig.sModules = resultModulesList.toArray(new Module[0]);
        ModulesConfig.sRole = role;
        ModulesConfig.configNavView(navigationView, ModulesConfig.sModules);
    }

    /**
     * Получение фрагмента из массива модулей
     * @param selectedModuleId - id модуля фрагмент которого требуется получить
     * @return фрагмент, полученный вызовом getFragmentItem у найденного модуля
     */
    public static Fragment getFragment(int selectedModuleId) {
        for (Module module: ModulesConfig.sModules) {
            if (module.getModuleId() == selectedModuleId) {
                return module.getFragmentItem();
            }
        }
        return null;
    }

    /**
     * Заполнение NavigationView массивом модулей Module
     * @param navigationView - NavigationView в который будет добавлены элементы меню
     * @param modules - массив элементов Module, который будет добавлен в navigationView
     */
    private static void configNavView(NavigationView navigationView, Module[] modules) {
        navigationView.inflateMenu(R.menu.drawer_view);

        Menu menu = navigationView.getMenu();
        for (Module module : modules) {
            MenuItem item = menu.add(R.id.group_1, module.getModuleId(), Menu.NONE, module.getModuleTitle());
            item.setIcon(module.getModuleIcon());
        }
        menu.setGroupCheckable(R.id.group_1, true, true);
        navigationView.invalidate();
    }
}
