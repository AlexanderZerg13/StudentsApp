package com.example.pilipenko.studentsapp.interfaces;

import android.support.v7.widget.Toolbar;

public interface IToolbar {
    void useToolbar(Toolbar toolbar, int strResource);
    void useToolbarWithBackStack(Toolbar toolbar, int strResource);
    void setToolbarTitle(int strResource);
}
