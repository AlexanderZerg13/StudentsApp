package ru.infocom.university.modules;

import android.support.v4.app.Fragment;

import ru.infocom.university.data.AuthorizationObject;

/**
 * Интерфейс для модуля
 */
public interface Module {

    int getModuleId();

    int getModuleTitle();

    int getModuleIcon();

    AuthorizationObject.Role getRole();

    Requirement[] getModuleRequirements();

    Fragment getFragmentItem();
}
