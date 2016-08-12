package com.example.pilipenko.studentsapp.com.example.pilipenko.data;

import java.util.ArrayList;
import java.util.List;

public abstract class StaticData {
    public static List<University> sUniversities = new ArrayList<University>() {
        {
            this.add(new University("Санкт-Петербургский государственный университет","Санкт-Петербургск"));
            this.add(new University("Московский государственный университет", "Москва"));
            this.add(new University("Московский физико-технический институт", "Москва"));
            this.add(new University("Национальный исследовательский ядерный университет «МИФИ»", "Москва"));
            this.add(new University("Национальный исследовательский Томский политехнический университет", "Томск"));
            this.add(new University("Тюменский государственный нефтегазовый университет", "Тюменская область"));
            this.add(new University("Воронежская государственная медицинская академия им. Н. Н. Бурденко", "Воронежская область"));
            this.add(new University("Северо-Кавказский федеральный университет", "Ставрополь"));
        }
    };
}
