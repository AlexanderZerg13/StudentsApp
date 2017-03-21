package com.example.pilipenko.studentsapp.data.api;

import com.example.pilipenko.studentsapp.data.AuthorizationObject;
import com.example.pilipenko.studentsapp.data.Lesson;
import com.example.pilipenko.studentsapp.data.LessonPlan;
import com.example.pilipenko.studentsapp.data.LessonProgress;
import com.example.pilipenko.studentsapp.data.StudentGroup;
import com.example.pilipenko.studentsapp.data.University;
import com.example.pilipenko.studentsapp.data.respones.UniversitiesRespones;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by pilipenko on 20.03.2017.
 */

public interface UniversityApiService {

    @FormUrlEncoded
    @POST("/Authorization/Passwords")
    Call<AuthorizationObject> getUser(@Field("login") String name, @Field("hash") String passwordHash);

    @FormUrlEncoded
    @POST("/Students/TimeTableGroups")
    Call<List<StudentGroup>> getUserGroups(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("/StudentsPlan/Specialties/Specialties")
    Call<String> getIDSpeciality(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("/StudentsPlan/Plans/Plans")
    Call<String> getIDPlan(@Field("specialty_id") String specialityId);

    /**
     * Fetch Schedule data for student.
     * FieldMap:
     * "objectType", "group"
     * "objectId", group
     * "scheduleType", "day"
     * "scheduleStartDate", date
     * "scheduleEndDate", date
     */
    @FormUrlEncoded
    @POST("/Students/TimeTable")
    Call<List<Lesson>> getSchedulePerDayForStudents(@FieldMap Map<String, String> map);

    /**
     * Fetch Schedule data for student.
     * FieldMap:
     * "objectType", "teacher"
     * "objectId", teacherId
     * "scheduleType", "day"
     * "scheduleStartDate", date
     * "scheduleEndDate", date
     */
    @FormUrlEncoded
    @POST("/Students/TimeTable/TimeTable")
    Call<List<Lesson>> getSchedulePerDayForTeacher(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/Students/EducationalPerformance")
    Call<List<LessonProgress>> getEducationalPerformance(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("/StudentsPlan/PlanLoad/PlanLoad")
    Call<List<LessonPlan>> getPlanLoad(@Field("academic_plan_id") String academicPlanId);

    @GET("/university.xml")
    Call<UniversitiesRespones> getUniversities();
}
