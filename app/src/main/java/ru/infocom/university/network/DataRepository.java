package ru.infocom.university.network;

import android.support.annotation.NonNull;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import ru.arturvasilov.rxloader.RxUtils;
import ru.infocom.university.Utils;
import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.data.Lesson;
import ru.infocom.university.data.LessonProgress;
import ru.infocom.university.data.LessonProgressLab;
import ru.infocom.university.model.Day;
import ru.infocom.university.model.MarkRecord;
import ru.infocom.university.model.RecordBook;
import ru.infocom.university.model.Return;
import ru.infocom.university.model.Roles;
import ru.infocom.university.model.ScheduleCell;
import ru.infocom.university.model.User;
import ru.infocom.university.model.request.AuthorizationRequestEnvelop;
import ru.infocom.university.model.request.EducationPerformanceRequestEnvelop;
import ru.infocom.university.model.request.RecordBooksRequestEnvelop;
import ru.infocom.university.model.request.ScheduleRequestEnvelop;
import rx.Observable;

/**
 * Created by Alexander Pilipenko on 28.09.2017.
 */

/*TODO More code int rxJava operators. Need to reduce*/
public class DataRepository {
    private static final String DEFAULT_TYPE = "Full";

    private AuthorizationObject authorizationObject;

    @NonNull
    public Observable<AuthorizationObject> authorization(@NonNull String login, @NonNull String password) {
        String passwordSha1 = new String(Hex.encodeHex(DigestUtils.sha1(password)));

        return ApiFactory.getStudyService()
                .authorization(0, AuthorizationRequestEnvelop.generate("", login, passwordSha1))
                .flatMap(authorizationResponse -> {
                    Return returnObject = authorizationResponse.getReturnContainer().getReturn();
                    if (returnObject.getUser() != null) {
                        User user = returnObject.getUser();
                        authorizationObject = new AuthorizationObject();
                        authorizationObject.setId(user.getUserId());
                        authorizationObject.setName(user.getLogin());
                        authorizationObject.setPassword(user.getPasswordHash());
                        List<Roles> listRoles = user.getRolesList();
                        if (listRoles.size() == 2) {
                            authorizationObject.setRole(AuthorizationObject.Role.BOTH);
                        } else {
                            authorizationObject.setRole(
                                    AuthorizationObject.Role.valueOf(listRoles.get(0).getRole().toUpperCase()));
                        }
                        return Observable.just(user);
                    } else {
                        return Observable.error(new AuthorizationException("Invalid authorization"));
                    }
                })
                .flatMap(user ->
                        ApiFactory.getStudyService().getRecordBooks(0, RecordBooksRequestEnvelop.generate(user.getUserId()))
                )
                .flatMap(recordBooksResponseEnvelop -> {
                    Return returnObject = recordBooksResponseEnvelop.getReturnContainer().getReturn();

                    List<RecordBook> list = returnObject.getRecordBooksList();
                    if (list != null) {
                        authorizationObject.setRecordBooks(list);
                        return Observable.just(authorizationObject);
                    } else {
                        return Observable.error(new AuthorizationException("Invalid authorization"));
                    }
                })
                .compose(RxUtils.async());
    }

    /*TODO need add restriction to scheduleObjectType. it may be Teacher or AcademicGroup*/
    @NonNull
    public Observable<List<Lesson>> getSchedule(@NonNull String scheduleObjectType, @NonNull String ScheduleObjectId, @NonNull Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);

        return ApiFactory.getStudyService()
                .getSchedule(0, ScheduleRequestEnvelop.generate(scheduleObjectType, ScheduleObjectId, DEFAULT_TYPE, date, date))
                .flatMap(scheduleResponseEnvelop -> {
                    Return returnObject = scheduleResponseEnvelop.getReturnContainer().getReturn();
                    List<Day> dayList = returnObject.getDayList();
                    if (dayList != null && dayList.size() != 0) {
                        return Observable.just(dayList);
                    } else {
                        return Observable.error(new ScheduleException("There are not Days in response"));
                    }
                })
                .flatMap(dayList -> {
                    List<Lesson> lessons = new ArrayList<>();
                    for (Day day : dayList) {
                        for (ScheduleCell cell : day.getScheduleCells()) {
                            Lesson lesson = new Lesson(true);
                            lesson.setDate(DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).format(day.getDate()));
                            lesson.setTimeStart(simpleDateFormat.format(cell.getDateBegin()));
                            lesson.setTimeEnd(simpleDateFormat.format(cell.getDateEnd()));

                            ru.infocom.university.model.Lesson soapLesson = cell.getLesson();
                            if (soapLesson != null) {
                                lesson.setIsEmpty(false);
                                lesson.setName(soapLesson.getSubject());
                                lesson.setGroup(soapLesson.getAcademicGroupName());
                                lesson.setTeachers(soapLesson.getTeacherName());
                                lesson.setType(soapLesson.getLessonType());
                            }

                            lessons.add(lesson);
                        }
                    }
                    return Observable.just(lessons);
                })
                .compose(RxUtils.async());
    }

    public Observable<Map<Integer, List<LessonProgress>>> getEducationalPerformance(@NonNull String userId, @NonNull String recordBookId) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy", Locale.US);

        return ApiFactory.getStudyService()
                .getEducationPerformance(0, EducationPerformanceRequestEnvelop.generate(userId, recordBookId))
                .flatMap(educationPerformanceResponseEnvelop -> {
                    Return returnObject = educationPerformanceResponseEnvelop.getReturnContainer().getReturn();
                    List<MarkRecord> markRecordList = returnObject.getMarkRecordList();
                    if (markRecordList != null && markRecordList.size() != 0) {
                        return Observable.just(markRecordList);
                    } else {
                        return Observable.error(new ScheduleException("There are not Days in response"));
                    }
                })
                .flatMap(markRecords -> {
                    List<LessonProgress> lessonProgresses = new ArrayList<>();
                    Map<Integer, List<LessonProgress>> map = new TreeMap<>();
                    for (MarkRecord markRecord : markRecords) {
                        LessonProgress lessonProgress = new LessonProgress();
                        lessonProgress.setLessonName(markRecord.getSubject());
                        lessonProgress.setMark(LessonProgress.Mark.fromString(markRecord.getMark()));
                        lessonProgress.setSemester(markRecord.getTerm());
                        lessonProgress.setDate(simpleDateFormat.format(markRecord.getDate()));

                        lessonProgresses.add(lessonProgress);

                        int semesterNumber = Utils.getSemesterFromString(lessonProgress.getSemester());
                        List<LessonProgress> semesterLessonProgressList;
                        if (!map.containsKey(semesterNumber)) {
                            semesterLessonProgressList = new ArrayList<>();
                            map.put(semesterNumber, semesterLessonProgressList);
                        } else {
                            semesterLessonProgressList = map.get(semesterNumber);
                        }
                        semesterLessonProgressList.add(lessonProgress);
                    }

                    return Observable.just(map);
                })
                .compose(RxUtils.async());
    }
}
