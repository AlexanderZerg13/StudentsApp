package ru.infocom.university.network;

import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import ru.arturvasilov.rxloader.RxUtils;
import ru.infocom.university.Utils;
import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.data.Lesson;
import ru.infocom.university.data.LessonPlan;
import ru.infocom.university.data.LessonProgress;
import ru.infocom.university.data.LessonProgressLab;
import ru.infocom.university.model.CurriculumLoad;
import ru.infocom.university.model.Day;
import ru.infocom.university.model.MarkRecord;
import ru.infocom.university.model.RecordBook;
import ru.infocom.university.model.Return;
import ru.infocom.university.model.Roles;
import ru.infocom.university.model.ScheduleCell;
import ru.infocom.university.model.User;
import ru.infocom.university.model.request.AuthorizationRequestEnvelop;
import ru.infocom.university.model.request.CurriculumLoadRequestEnvelop;
import ru.infocom.university.model.request.EducationPerformanceRequestEnvelop;
import ru.infocom.university.model.request.RecordBooksRequestEnvelop;
import ru.infocom.university.model.request.ScheduleRequestEnvelop;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Alexander Pilipenko on 28.09.2017.
 */

/*TODO More code int rxJava operators. Need to reduce*/
public class DataRepository {
    private static final String DEFAULT_TYPE = "Full";
    private static DataRepository sDataRepository;
    private int universityId;

    private AuthorizationObject authorizationObject;

    /*TODO not the best way*/
    public static DataRepository get(int universityId) {
        if (sDataRepository == null) {
            sDataRepository = new DataRepository(universityId);
        } else if (sDataRepository.universityId != universityId) {
            sDataRepository.setUniversityId(universityId);
        }

        return sDataRepository;
    }

    public DataRepository(int universityId) {
        this.universityId = universityId;
    }

    public void setUniversityId(int universityId) {
        this.universityId = universityId;
    }

    @NonNull
    public Observable<AuthorizationObject> authorization(@NonNull String login, @NonNull String password) {
        String passwordSha1 = new String(Hex.encodeHex(DigestUtils.sha1(password)));

        return ApiFactory.getStudyService()
                .authorization(universityId, AuthorizationRequestEnvelop.generate("", login, passwordSha1))
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
                        ApiFactory.getStudyService().getRecordBooks(universityId, RecordBooksRequestEnvelop.generate(user.getUserId()))
                )
                .flatMap(recordBooksResponseEnvelop -> {
                    Return returnObject = recordBooksResponseEnvelop.getReturnContainer().getReturn();

                    List<RecordBook> list = returnObject.getRecordBooksList();
                    if (list == null) {
                        if (authorizationObject.getRole() == AuthorizationObject.Role.STUDENT) {
                            return Observable.error(new AuthorizationException("Invalid authorization"));
                        } else if (authorizationObject.getRole() == AuthorizationObject.Role.BOTH) {
                            authorizationObject.setRole(AuthorizationObject.Role.TEACHER);
                        }
                    } else {
                        authorizationObject.setRecordBooks(list);
                    }
                    return Observable.just(authorizationObject);
                })
                .compose(RxUtils.async());
    }

    /*TODO need add restriction to scheduleObjectType. it may be Teacher or AcademicGroup*/
    @NonNull
    public synchronized Observable<List<Lesson>> getSchedule(@NonNull String scheduleObjectType, @NonNull String ScheduleObjectId, @NonNull Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        Log.i("getSchedule okHttp", "getSchedule: " + date);

        return ApiFactory.getStudyService()
                .getSchedule(universityId, ScheduleRequestEnvelop.generate(scheduleObjectType, ScheduleObjectId, DEFAULT_TYPE, date, date))
                .flatMap(scheduleResponseEnvelop -> {
                    Return returnObject = scheduleResponseEnvelop.getReturnContainer().getReturn();
                    List<Day> dayList = returnObject.getDayList();
                    return Observable.just(dayList);
                })
                .flatMap(dayList -> {
                    List<Lesson> lessons = new ArrayList<>();
                    if (dayList == null) {
                        return Observable.just(lessons);
                    }
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
                                lesson.setAudience(soapLesson.getAudience());
                            }

                            lessons.add(lesson);
                        }
                    }
                    return Observable.just(lessons);
                })
                .compose(RxUtils.async());
    }

    @NonNull
    public Observable<Map<Integer, List<LessonProgress>>> getEducationalPerformance(@NonNull String userId, @NonNull String recordBookId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy", Locale.US);

        return ApiFactory.getStudyService()
                .getEducationPerformance(universityId, EducationPerformanceRequestEnvelop.generate(userId, recordBookId))
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
                        if (markRecord.getMark() != null) {
                            lessonProgress.setMark(LessonProgress.Mark.fromString(markRecord.getMark()));
                        }
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

    public Observable<Map<Integer, List<LessonPlan>>> getAcademicPlan(@NonNull String curriculumId) {
        return ApiFactory.getStudyService()
                .getCurriculumLoad(universityId, CurriculumLoadRequestEnvelop.generate(curriculumId, ""))
                .flatMap(curriculumLoadResponseEnvelop -> {
                    Return returnObject = curriculumLoadResponseEnvelop.getReturnContainer().getReturn();
                    List<CurriculumLoad> curriculumLoadList = returnObject.getCurriculumLoadList();
                    if (curriculumLoadList != null && curriculumLoadList.size() != 0) {
                        return Observable.just(curriculumLoadList);
                    } else {
                        return Observable.error(new ScheduleException("There are not Days in response"));
                    }
                })
                .flatMap(curriculumLoads -> {
                    Map<Integer, Map<String, LessonPlan>> lessonsPlanMap = new TreeMap<>();
                    Map<Integer, List<LessonPlan>> map = new TreeMap<>();

                    for (CurriculumLoad curriculumLoad : curriculumLoads) {
                        int semesterNumber = Utils.getSemesterFromString(curriculumLoad.getTerm());

                        Map<String, LessonPlan> semesterMap = lessonsPlanMap.get(semesterNumber);
                        if (semesterMap == null) {
                            semesterMap = new TreeMap<>();
                            lessonsPlanMap.put(semesterNumber, semesterMap);
                        }

                        LessonPlan lessonPlan = semesterMap.get(curriculumLoad.getSubject());
                        if (lessonPlan == null) {
                            lessonPlan = new LessonPlan();
                            semesterMap.put(curriculumLoad.getSubject(), lessonPlan);
                            lessonPlan.setName(curriculumLoad.getSubject());
                            lessonPlan.setSemester(semesterNumber);
                        }

                        switch (curriculumLoad.getLoadType().toLowerCase()) {
                            case "экзамен":
                                lessonPlan.setExam(true);
                                break;
                            case "зачет":
                                lessonPlan.setSet(true);
                                break;
                            case "курсовая работа":
                                lessonPlan.setCourse(true);
                                break;
                            default:
                                lessonPlan.setLoadToMap(curriculumLoad.getLoadType(), curriculumLoad.getAmount());
                                break;
                        }
                    }

                    for(Map.Entry<Integer, Map<String, LessonPlan>> entry: lessonsPlanMap.entrySet()) {
                        List<LessonPlan> lessonPlanList = new ArrayList<>(entry.getValue().values());
                        map.put(entry.getKey(), lessonPlanList);
                    }

                    return Observable.just(map);
                })
                .compose(RxUtils.async());

    }
}
