package ru.infocom.university.modules;

import android.support.v4.app.Fragment;

import ru.infocom.university.data.AuthorizationObject;

/**
 * Интерфейс для модуля
 */
public interface Module {

    /**
     * Уникальный идетификатор модуля
     * @return идентификатор модуля
     */
    int getModuleId();

    /**
     * Заголовок модуля, отображается в навигацонном меню
     * @return заголовок модуля
     */
    int getModuleTitle();

    /**
     * Сылка на drawable иконку модуля, отображаеся в навигацинном меню
     * @return drawable иконка
     */
    int getModuleIcon();

    /**
     * Роль для которой доступен модуль
     * @return роль
     */
    AuthorizationObject.Role getRole();

    /**
     * Массив требований модуля
     * @return массив требований модуля
     */
    Requirement[] getModuleRequirements();

    /**
     * Фрагмент(представление) модуля, которое будет отображаться при выборе пункта в меню
     * @return фрагмент модуля
     */
    Fragment getFragmentItem();
}
