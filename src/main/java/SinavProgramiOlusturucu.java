import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

// Ana domain sınıfları
class Classroom {
    private String code;
    private int capacity;
    private List<ExamSchedule> scheduledExams;

    public Classroom(String code, int capacity) {
        this.code = code;
        this.capacity = capacity;
        this.scheduledExams = new ArrayList<>();
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public List<ExamSchedule> getScheduledExams() { return scheduledExams; }
    public void setScheduledExams(List<ExamSchedule> scheduledExams) { this.scheduledExams = scheduledExams; }
}

class Course {
    private String code;
    private String name;
    private String gradeLevel;
    private int studentCount;
    private int customDuration;
    private boolean excluded;

    public Course(String code, String name, String gradeLevel, int studentCount, int customDuration) {
        this.code = code;
        this.name = name;
        this.gradeLevel = gradeLevel;
        this.studentCount = studentCount;
        this.customDuration = customDuration;
        this.excluded = false;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; }
    public int getStudentCount() { return studentCount; }
    public void setStudentCount(int studentCount) { this.studentCount = studentCount; }
    public int getCustomDuration() { return customDuration; }
    public void setCustomDuration(int customDuration) { this.customDuration = customDuration; }
    public boolean isExcluded() { return excluded; }
    public void setExcluded(boolean excluded) { this.excluded = excluded; }
}

class Student {
    private String id;
    private String name;
    private List<String> enrolledCourses;

    public Student(String id, String name, List<String> enrolledCourses) {
        this.id = id;
        this.name = name;
        this.enrolledCourses = enrolledCourses != null ? enrolledCourses : new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<String> getEnrolledCourses() { return enrolledCourses; }
    public void setEnrolledCourses(List<String> enrolledCourses) { this.enrolledCourses = enrolledCourses; }

    public void addCourse(String courseCode) {
        if (!this.enrolledCourses.contains(courseCode)) {
            this.enrolledCourses.add(courseCode);
        }
    }
}

class ExamSchedule {
    private String courseCode;
    private String courseName;
    private LocalDateTime examDateTime;
    private int duration;
    private List<String> classroomCodes;
    private List<String> studentIds;

    public ExamSchedule(String courseCode, String courseName, LocalDateTime examDateTime, int duration, List<String> classroomCodes, List<String> studentIds) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.examDateTime = examDateTime;
        this.duration = duration;
        this.classroomCodes = classroomCodes != null ? classroomCodes : new ArrayList<>();
        this.studentIds = studentIds != null ? studentIds : new ArrayList<>();
    }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public LocalDateTime getExamDateTime() { return examDateTime; }
    public void setExamDateTime(LocalDateTime examDateTime) { this.examDateTime = examDateTime; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public List<String> getClassroomCodes() { return classroomCodes; }
    public void setClassroomCodes(List<String> classroomCodes) { this.classroomCodes = classroomCodes; }
    public List<String> getStudentIds() { return studentIds; }
    public void setStudentIds(List<String> studentIds) { this.studentIds = studentIds; }

    public String getClassroomCodesAsString() {
        return String.join(", ", classroomCodes);
    }
}

class ExamScheduler {
    private List<Classroom> classrooms;
    private List<Course> courses;
    private List<Student> students;
    private List<LocalDate> availableDates;
    private Map<String, Integer> examDurations;
    private int breakDuration;
    private boolean noOverlap;
    private LocalTime startTime = LocalTime.of(9, 0);
    private LocalTime endTime = LocalTime.of(17, 0);

    public ExamScheduler() {
        this.classrooms = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.students = new ArrayList<>();
        this.availableDates = new ArrayList<>();
        this.examDurations = new HashMap<>();
        this.breakDuration = 15;
        this.noOverlap = true;
    }

    public List<Classroom> getClassrooms() { return classrooms; }
    public void setClassrooms(List<Classroom> classrooms) { this.classrooms = classrooms; }
    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> courses) { this.courses = courses; }
    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }
    public List<LocalDate> getAvailableDates() { return availableDates; }
    public void setAvailableDates(List<LocalDate> availableDates) { this.availableDates = availableDates; }
    public Map<String, Integer> getExamDurations() { return examDurations; }
    public void setExamDurations(Map<String, Integer> examDurations) { this.examDurations = examDurations; }
    public int getBreakDuration() { return breakDuration; }
    public void setBreakDuration(int breakDuration) { this.breakDuration = breakDuration; }
    public boolean isNoOverlap() { return noOverlap; }
    public void setNoOverlap(boolean noOverlap) { this.noOverlap = noOverlap; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}

class ExamPlanner {
    private ExamScheduler scheduler;
    private List<ExamSchedule> examSchedule;
    private Map<LocalDate, List<ExamSchedule>> dailyExams;

    public ExamPlanner(ExamScheduler scheduler) {
        this.scheduler = scheduler;
        this.examSchedule = new ArrayList<>();
        this.dailyExams = new HashMap<>();
    }

    public List<ExamSchedule> generateExamSchedule() {
        examSchedule.clear();
        dailyExams.clear();

        Map<String, List<Course>> coursesByGrade = groupCoursesByGrade();
        List<LocalDate> filteredDates = filterDates();

        if (filteredDates.isEmpty()) {
            System.out.println("Uygun tarih bulunamadı!");
            return examSchedule;
        }

        // Tarihleri sırala
        List<LocalDate> sortedDates = filteredDates.stream()
                .sorted()
                .collect(Collectors.toList());

        // Önce küçük sınıfları planla (1. sınıf, 2. sınıf...)
        List<String> sortedGrades = coursesByGrade.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        for (String grade : sortedGrades) {
            System.out.println(grade + " sınıfı için planlama yapılıyor...");
            planExamsForGrade(grade, coursesByGrade.get(grade), sortedDates);
        }

        // Sınavları tarih ve saat sırasına göre sırala
        examSchedule.sort((e1, e2) -> {
            int dateCompare = e1.getExamDateTime().toLocalDate().compareTo(e2.getExamDateTime().toLocalDate());
            if (dateCompare != 0) return dateCompare;
            return e1.getExamDateTime().toLocalTime().compareTo(e2.getExamDateTime().toLocalTime());
        });

        System.out.println("Toplam " + examSchedule.size() + " sınav planlandı.");
        return examSchedule;
    }

    private Map<String, List<Course>> groupCoursesByGrade() {
        return scheduler.getCourses().stream()
                .filter(course -> !course.isExcluded())
                .collect(Collectors.groupingBy(Course::getGradeLevel));
    }

    private List<LocalDate> filterDates() {
        return scheduler.getAvailableDates().stream()
                .filter(date -> date.getDayOfWeek().getValue() <= 5)
                .collect(Collectors.toList());
    }

    private void planExamsForGrade(String grade, List<Course> courses, List<LocalDate> dates) {
        if (courses.isEmpty()) return;

        // Dersleri öğrenci sayısına göre büyükten küçüğe sırala
        List<Course> sortedCourses = courses.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getStudentCount(), c1.getStudentCount()))
                .collect(Collectors.toList());

        int daysNeeded = calculateDaysNeeded(sortedCourses.size());
        List<LocalDate> selectedDates = dates.stream()
                .limit(Math.min(daysNeeded, dates.size()))
                .collect(Collectors.toList());

        // Dersleri günlere dağıt
        Map<LocalDate, List<Course>> distribution = distributeCourses(sortedCourses, selectedDates);

        // Her gün için sınavları planla
        for (LocalDate date : distribution.keySet()) {
            planDailyExams(date, distribution.get(date));
        }

        // Planlanamayan dersler için ikinci deneme
        List<Course> unplannedCourses = findUnplannedCourses(courses);
        if (!unplannedCourses.isEmpty()) {
            System.out.println(grade + " sınıfı için " + unplannedCourses.size() + " ders planlanamadı, ikinci deneme yapılıyor...");
            retryUnplannedCourses(unplannedCourses, dates);
        }
    }

    private int calculateDaysNeeded(int courseCount) {
        if (courseCount <= 2) return 1;
        if (courseCount <= 4) return 2;
        if (courseCount <= 6) return 3;
        if (courseCount <= 8) return 4;
        return (int) Math.ceil(courseCount / 2.0);
    }

    private Map<LocalDate, List<Course>> distributeCourses(List<Course> courses, List<LocalDate> selectedDates) {
        Map<LocalDate, List<Course>> distribution = new LinkedHashMap<>();

        for (LocalDate date : selectedDates) {
            distribution.put(date, new ArrayList<>());
        }

        // Dersleri günlere dengeli dağıt
        int dateIndex = 0;
        for (Course course : courses) {
            LocalDate date = selectedDates.get(dateIndex);
            distribution.get(date).add(course);
            dateIndex = (dateIndex + 1) % selectedDates.size();
        }

        return distribution;
    }

    private void planDailyExams(LocalDate date, List<Course> courses) {
        if (courses.isEmpty()) return;

        // Gün içinde dersleri öğrenci sayısına göre sırala
        List<Course> sortedCourses = courses.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getStudentCount(), c1.getStudentCount()))
                .collect(Collectors.toList());

        for (Course course : sortedCourses) {
            if (!isCoursePlanned(course.getCode())) {
                planCourseExam(date, course);
            }
        }
    }

    private void planCourseExam(LocalDate date, Course course) {
        int remainingStudents = course.getStudentCount();
        int duration = getExamDuration(course);

        ExamTimeSlot bestTimeSlot = findBestTimeSlotForCourse(date, course, remainingStudents, duration);

        if (bestTimeSlot == null) {
            // Alternatif zaman aralığı deneme
            bestTimeSlot = findAlternativeTimeSlot(date, course, remainingStudents, duration);
        }

        if (bestTimeSlot == null) {
            System.out.println("Uygun zaman/derslik bulunamadı: " + course.getCode() + " - " + course.getName());
            return;
        }

        List<String> allClassroomCodes = bestTimeSlot.classrooms.stream()
                .map(Classroom::getCode)
                .collect(Collectors.toList());

        List<String> allStudentIds = getStudentIdsForCourse(course.getCode());

        ExamSchedule exam = new ExamSchedule(course.getCode(), course.getName(),
                LocalDateTime.of(date, bestTimeSlot.startTime), duration,
                allClassroomCodes, allStudentIds);

        examSchedule.add(exam);

        for (Classroom classroom : bestTimeSlot.classrooms) {
            classroom.getScheduledExams().add(exam);
        }

        System.out.println("Planlandı: " + course.getCode() +
                " - " + exam.getExamDateTime() +
                " - Derslikler: " + allClassroomCodes +
                " - Toplam Öğrenci: " + allStudentIds.size() +
                " - Süre: " + exam.getDuration() + "dk");
    }

    private ExamTimeSlot findBestTimeSlotForCourse(LocalDate date, Course course, int remainingStudents, int duration) {
        LocalTime currentTime = scheduler.getStartTime();

        while (currentTime.isBefore(scheduler.getEndTime().minusMinutes(duration))) {
            List<Classroom> availableClassrooms = findAvailableClassroomsForTimeSlot(date, currentTime, duration, course);

            if (!availableClassrooms.isEmpty()) {
                List<Classroom> selectedClassrooms = selectOptimalClassrooms(availableClassrooms, remainingStudents);
                if (!selectedClassrooms.isEmpty()) {
                    return new ExamTimeSlot(currentTime, selectedClassrooms);
                }
            }

            currentTime = currentTime.plusMinutes(15); // 15 dakikalık artışlarla kontrol et
        }

        return null;
    }

    private ExamTimeSlot findAlternativeTimeSlot(LocalDate date, Course course, int remainingStudents, int duration) {
        // Daha esnek zaman arama - molaları azalt
        LocalTime currentTime = scheduler.getStartTime();

        while (currentTime.isBefore(scheduler.getEndTime().minusMinutes(duration))) {
            List<Classroom> availableClassrooms = findAvailableClassroomsForTimeSlotFlexible(date, currentTime, duration, course);

            if (!availableClassrooms.isEmpty()) {
                List<Classroom> selectedClassrooms = selectOptimalClassrooms(availableClassrooms, remainingStudents);
                if (!selectedClassrooms.isEmpty()) {
                    return new ExamTimeSlot(currentTime, selectedClassrooms);
                }
            }

            currentTime = currentTime.plusMinutes(10); // Daha sık kontrol
        }

        return null;
    }

    private List<Classroom> findAvailableClassroomsForTimeSlot(LocalDate date, LocalTime startTime,
                                                               int duration, Course course) {
        LocalTime endTime = startTime.plusMinutes(duration);

        if (endTime.isAfter(scheduler.getEndTime())) {
            return new ArrayList<>();
        }

        if (hasStudentConflict(date, startTime, duration, course)) {
            return new ArrayList<>();
        }

        return scheduler.getClassrooms().stream()
                .filter(classroom -> classroom.getCapacity() > 0)
                .filter(classroom -> isClassroomAvailableWithBreak(classroom, date, startTime, duration))
                .sorted((c1, c2) -> Integer.compare(c2.getCapacity(), c1.getCapacity())) // Büyük kapasiteli önce
                .collect(Collectors.toList());
    }

    private List<Classroom> findAvailableClassroomsForTimeSlotFlexible(LocalDate date, LocalTime startTime,
                                                                       int duration, Course course) {
        LocalTime endTime = startTime.plusMinutes(duration);

        if (endTime.isAfter(scheduler.getEndTime())) {
            return new ArrayList<>();
        }

        // Daha esnek çakışma kontrolü - sadece aynı öğrenciler için kontrol et
        if (hasCriticalStudentConflict(date, startTime, duration, course)) {
            return new ArrayList<>();
        }

        return scheduler.getClassrooms().stream()
                .filter(classroom -> classroom.getCapacity() > 0)
                .filter(classroom -> isClassroomAvailableFlexible(classroom, date, startTime, duration))
                .sorted((c1, c2) -> Integer.compare(c2.getCapacity(), c1.getCapacity()))
                .collect(Collectors.toList());
    }

    private boolean isClassroomAvailableWithBreak(Classroom classroom, LocalDate date, LocalTime startTime, int duration) {
        LocalDateTime examStart = LocalDateTime.of(date, startTime);
        LocalDateTime examEnd = examStart.plusMinutes(duration);
        LocalDateTime examStartWithBreak = examStart.minusMinutes(scheduler.getBreakDuration());
        LocalDateTime examEndWithBreak = examEnd.plusMinutes(scheduler.getBreakDuration());

        return classroom.getScheduledExams().stream()
                .noneMatch(exam -> {
                    LocalDateTime scheduledStart = exam.getExamDateTime();
                    LocalDateTime scheduledEnd = scheduledStart.plusMinutes(exam.getDuration());
                    return (examStartWithBreak.isBefore(scheduledEnd) && examEndWithBreak.isAfter(scheduledStart));
                });
    }

    private boolean isClassroomAvailableFlexible(Classroom classroom, LocalDate date, LocalTime startTime, int duration) {
        LocalDateTime examStart = LocalDateTime.of(date, startTime);
        LocalDateTime examEnd = examStart.plusMinutes(duration);

        return classroom.getScheduledExams().stream()
                .noneMatch(exam -> {
                    LocalDateTime scheduledStart = exam.getExamDateTime();
                    LocalDateTime scheduledEnd = scheduledStart.plusMinutes(exam.getDuration());
                    return (examStart.isBefore(scheduledEnd) && examEnd.isAfter(scheduledStart));
                });
    }

    private List<Classroom> selectOptimalClassrooms(List<Classroom> availableClassrooms, int requiredCapacity) {
        List<Classroom> selected = new ArrayList<>();
        int currentCapacity = 0;

        // En az sayıda derslik kullanacak şekilde seçim yap
        for (Classroom classroom : availableClassrooms) {
            if (currentCapacity >= requiredCapacity) {
                break;
            }
            selected.add(classroom);
            currentCapacity += classroom.getCapacity();
        }

        return currentCapacity >= requiredCapacity ? selected : new ArrayList<>();
    }

    private List<String> getStudentIdsForCourse(String courseCode) {
        return scheduler.getStudents().stream()
                .filter(student -> student.getEnrolledCourses().contains(courseCode))
                .map(Student::getId)
                .collect(Collectors.toList());
    }

    private boolean hasStudentConflict(LocalDate date, LocalTime time, int duration, Course course) {
        if (!scheduler.isNoOverlap()) {
            return false;
        }

        LocalDateTime examStart = LocalDateTime.of(date, time);
        LocalDateTime examEnd = examStart.plusMinutes(duration);
        LocalDateTime examStartWithBreak = examStart.minusMinutes(scheduler.getBreakDuration());
        LocalDateTime examEndWithBreak = examEnd.plusMinutes(scheduler.getBreakDuration());

        for (ExamSchedule scheduledExam : examSchedule) {
            if (scheduledExam.getExamDateTime().toLocalDate().equals(date)) {
                LocalDateTime scheduledStart = scheduledExam.getExamDateTime();
                LocalDateTime scheduledEnd = scheduledStart.plusMinutes(scheduledExam.getDuration());

                if (hasCommonStudents(course.getCode(), scheduledExam.getCourseCode())) {
                    if (examStartWithBreak.isBefore(scheduledEnd) && examEndWithBreak.isAfter(scheduledStart)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean hasCriticalStudentConflict(LocalDate date, LocalTime time, int duration, Course course) {
        LocalDateTime examStart = LocalDateTime.of(date, time);
        LocalDateTime examEnd = examStart.plusMinutes(duration);

        for (ExamSchedule scheduledExam : examSchedule) {
            if (scheduledExam.getExamDateTime().toLocalDate().equals(date)) {
                LocalDateTime scheduledStart = scheduledExam.getExamDateTime();
                LocalDateTime scheduledEnd = scheduledStart.plusMinutes(scheduledExam.getDuration());

                // Sadece aynı anda olan sınavlarda çakışma kontrolü
                if (hasCommonStudents(course.getCode(), scheduledExam.getCourseCode())) {
                    if (examStart.isBefore(scheduledEnd) && examEnd.isAfter(scheduledStart)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean hasCommonStudents(String course1, String course2) {
        return scheduler.getStudents().stream()
                .anyMatch(student -> student.getEnrolledCourses().contains(course1) &&
                        student.getEnrolledCourses().contains(course2));
    }

    private int getExamDuration(Course course) {
        if (course.getCustomDuration() > 0) {
            return course.getCustomDuration();
        }
        return scheduler.getExamDurations().getOrDefault(course.getCode(), 75);
    }

    private boolean isCoursePlanned(String courseCode) {
        return examSchedule.stream()
                .anyMatch(exam -> exam.getCourseCode().equals(courseCode));
    }

    private List<Course> findUnplannedCourses(List<Course> courses) {
        return courses.stream()
                .filter(course -> !isCoursePlanned(course.getCode()))
                .collect(Collectors.toList());
    }

    private void retryUnplannedCourses(List<Course> unplannedCourses, List<LocalDate> dates) {
        for (Course course : unplannedCourses) {
            for (LocalDate date : dates) {
                if (planCourseExamRetry(date, course)) {
                    break;
                }
            }
        }
    }

    private boolean planCourseExamRetry(LocalDate date, Course course) {
        int remainingStudents = course.getStudentCount();
        int duration = getExamDuration(course);

        // Çok esnek zaman arama - molasız ve daha sık
        LocalTime currentTime = scheduler.getStartTime();

        while (currentTime.isBefore(scheduler.getEndTime().minusMinutes(duration))) {
            LocalTime finalCurrentTime = currentTime;
            List<Classroom> availableClassrooms = scheduler.getClassrooms().stream()
                    .filter(classroom -> classroom.getCapacity() > 0)
                    .filter(classroom -> isClassroomAvailableFlexible(classroom, date, finalCurrentTime, duration))
                    .sorted((c1, c2) -> Integer.compare(c2.getCapacity(), c1.getCapacity()))
                    .collect(Collectors.toList());

            if (!availableClassrooms.isEmpty()) {
                List<Classroom> selectedClassrooms = selectOptimalClassrooms(availableClassrooms, remainingStudents);
                if (!selectedClassrooms.isEmpty()) {
                    // Kritik öğrenci çakışması kontrolü
                    if (!hasCriticalStudentConflict(date, currentTime, duration, course)) {
                        List<String> allClassroomCodes = selectedClassrooms.stream()
                                .map(Classroom::getCode)
                                .collect(Collectors.toList());

                        List<String> allStudentIds = getStudentIdsForCourse(course.getCode());

                        ExamSchedule exam = new ExamSchedule(course.getCode(), course.getName(),
                                LocalDateTime.of(date, currentTime), duration,
                                allClassroomCodes, allStudentIds);

                        examSchedule.add(exam);

                        for (Classroom classroom : selectedClassrooms) {
                            classroom.getScheduledExams().add(exam);
                        }

                        System.out.println("Yeniden planlandı: " + course.getCode() +
                                " - " + exam.getExamDateTime() +
                                " - Derslikler: " + allClassroomCodes +
                                " - Toplam Öğrenci: " + allStudentIds.size() +
                                " - Süre: " + exam.getDuration() + "dk");
                        return true;
                    }
                }
            }

            currentTime = currentTime.plusMinutes(10);
        }

        return false;
    }

    private class ExamTimeSlot {
        LocalTime startTime;
        List<Classroom> classrooms;

        ExamTimeSlot(LocalTime startTime, List<Classroom> classrooms) {
            this.startTime = startTime;
            this.classrooms = classrooms;
        }
    }
}

class ExcelExporter {
    public void exportToExcel(List<ExamSchedule> examSchedule, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sınav Programı");

            // Modern stil oluşturma
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle timeStyle = createTimeStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);
            CellStyle durationStyle = createDurationStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle classroomStyle = createClassroomStyle(workbook);
            CellStyle darkGreenStyle = createDarkGreenStyle(workbook);
            CellStyle lightGreenStyle = createLightGreenStyle(workbook);
            CellStyle emptyRowStyle = createEmptyRowStyle(workbook); // Boş satır stili

            // Başlık satırı - KOYU YEŞİL
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Tarih", "Başlangıç Saati", "Ders Kodu", "Ders Adı", "Derslik", "Sınav Süresi (dk)"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Veri satırları - Tarihe göre gruplayarak
            int rowNum = 1;
            LocalDate currentDate = null;
            int dateStartRow = 1;

            for (ExamSchedule exam : examSchedule) {
                LocalDate examDate = exam.getExamDateTime().toLocalDate();

                // Yeni tarih başladığında önceki tarihin satırlarını renklendir ve boşluk ekle
                if (currentDate != null && !examDate.equals(currentDate)) {
                    applyDateGroupColoring(sheet, dateStartRow, rowNum - 1, darkGreenStyle, lightGreenStyle);

                    // Tarih değişiminde boş satır ekle - KOYU YEŞİL
                    Row emptyRow = sheet.createRow(rowNum++);
                    for (int i = 0; i < headers.length; i++) {
                        Cell emptyCell = emptyRow.createCell(i);
                        emptyCell.setCellStyle(emptyRowStyle);
                    }
                    dateStartRow = rowNum;
                }
                currentDate = examDate;

                Row row = sheet.createRow(rowNum++);

                // Tarih
                Cell dateCell = row.createCell(0);
                dateCell.setCellValue(examDate.toString());
                dateCell.setCellStyle(dateStyle);

                // Başlangıç Saati
                Cell timeCell = row.createCell(1);
                timeCell.setCellValue(exam.getExamDateTime().toLocalTime().toString());
                timeCell.setCellStyle(timeStyle);

                // Ders Kodu
                Cell codeCell = row.createCell(2);
                codeCell.setCellValue(exam.getCourseCode());
                codeCell.setCellStyle(normalStyle);

                // Ders Adı
                Cell nameCell = row.createCell(3);
                nameCell.setCellValue(exam.getCourseName());
                nameCell.setCellStyle(normalStyle);

                // Derslik
                Cell classroomCell = row.createCell(4);
                classroomCell.setCellValue(exam.getClassroomCodesAsString());
                classroomCell.setCellStyle(classroomStyle);

                // Sınav Süresi
                Cell durationCell = row.createCell(5);
                durationCell.setCellValue(exam.getDuration());
                durationCell.setCellStyle(durationStyle);
            }

            // Son tarih grubunu da renklendir
            if (dateStartRow < rowNum) {
                applyDateGroupColoring(sheet, dateStartRow, rowNum - 1, darkGreenStyle, lightGreenStyle);
            }

            // Sütun genişliklerini ayarla
            sheet.setColumnWidth(0, 3500);
            sheet.setColumnWidth(1, 4000);
            sheet.setColumnWidth(2, 3500);
            sheet.setColumnWidth(3, 9000);
            sheet.setColumnWidth(4, 5000);
            sheet.setColumnWidth(5, 3500);

            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
                System.out.println("Modern Excel dosyası oluşturuldu: " + filePath);
            }

        } catch (Exception e) {
            System.err.println("Excel oluşturma hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void applyDateGroupColoring(Sheet sheet, int startRow, int endRow, CellStyle oddStyle, CellStyle evenStyle) {
        for (int i = startRow; i <= endRow; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                boolean isOddRow = (i - startRow) % 2 == 0; // İlk satır koyu yeşil
                CellStyle styleToApply = isOddRow ? oddStyle : evenStyle;

                for (int j = 0; j < 6; j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        cell.setCellStyle(styleToApply);
                    }
                }
            }
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontHeightInPoints((short)12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex()); // KOYU YEŞİL başlık
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        return style;
    }

    private CellStyle createEmptyRowStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE1.getIndex()); // KOYU YEŞİL boş satır
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createTimeStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setDataFormat(workbook.createDataFormat().getFormat("HH:mm"));
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createClassroomStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createNormalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDurationStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.DARK_RED.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDarkGreenStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREEN.getIndex()); // YEŞİL
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        Font font = workbook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        return style;
    }

    private CellStyle createLightGreenStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex()); // AÇIK YEŞİL
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}

public class SinavProgramiOlusturucu {

    private static final String URL = "jdbc:postgresql://localhost:5432/Sinav_Yonetim_Sistemi";
    private static final String USER = "postgres";
    private static final String PASSWORD = "06green08";

    public void Olustur(ObservableList<SinavaDahilDerslerModel> dersler, List<LocalDate> dates, int breakDuration, boolean noOverlap, String baslik) {
        ExamScheduler scheduler = createData(dersler, dates, breakDuration, noOverlap);

        ExamPlanner planner = new ExamPlanner(scheduler);
        List<ExamSchedule> examSchedule = planner.generateExamSchedule();

        ExcelExporter exporter = new ExcelExporter();
        exporter.exportToExcel(examSchedule, baslik + "_sinav_programi.xlsx");

        printSchedule(examSchedule);
        VeritabaninaKaydet(examSchedule);
    }

    private ExamScheduler createData(ObservableList<SinavaDahilDerslerModel> dersler, List<LocalDate> dates, int breakDuration, boolean noOverlap) {
        ExamScheduler scheduler = new ExamScheduler();

        // Derslikleri Al
        List<Classroom> classrooms = new ArrayList<>();
        DerslikleriAl(classrooms, scheduler);

        // Dersleri Al
        List<Course> courses = new ArrayList<>();
        DersBilgileriniAl(dersler, courses, scheduler);

        // Öğrencileri Al
        List<Student> students = new ArrayList<>();
        OgrencileriAl(students, scheduler);

        // Tarihleri ayarla
        scheduler.setAvailableDates(dates);

        Map<String, Integer> examDurations = new HashMap<>();
        scheduler.setExamDurations(examDurations);

        scheduler.setBreakDuration(breakDuration);
        scheduler.setNoOverlap(noOverlap);

        return scheduler;
    }

    private void printSchedule(List<ExamSchedule> examSchedule) {
        System.out.println("\n=== Sınav Programı ===");
        System.out.println("Tarih      | Başlangıç | Ders Kodu | Ders Adı              | Derslikler          | Süre");
        System.out.println("-----------|-----------|-----------|----------------------|-------------------|------");

        LocalDate currentDate = null;
        for (ExamSchedule exam : examSchedule) {
            LocalDate examDate = exam.getExamDateTime().toLocalDate();

            if (currentDate != null && !examDate.equals(currentDate)) {
                System.out.println();
            }
            currentDate = examDate;

            System.out.printf("%-11s | %-9s | %-9s | %-20s | %-17s | %-4d\n",
                    examDate,
                    exam.getExamDateTime().toLocalTime(),
                    exam.getCourseCode(),
                    exam.getCourseName().length() > 20 ? exam.getCourseName().substring(0, 17) + "..." : exam.getCourseName(),
                    exam.getClassroomCodesAsString(),
                    exam.getDuration());
        }
    }

    private void VeritabaninaKaydet(List<ExamSchedule> examSchedule)
    {
        String sql = "INSERT INTO sinavlar (bolum, derslik_adi, ders_adi, ders_kodu, tarih, saat, sure)\n" +
                "VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDate currentDate = null;
            for (ExamSchedule exam : examSchedule) {
                LocalDate examDate = exam.getExamDateTime().toLocalDate();

                if (currentDate != null && !examDate.equals(currentDate)) {
                    System.out.println();
                }
                currentDate = examDate;
                stmt.setString(1, LoginController.bolum);
                stmt.setString(2, exam.getClassroomCodesAsString());
                stmt.setString(3, exam.getCourseName());
                stmt.setString(4, exam.getCourseCode());
                stmt.setString(5, examDate.toString());
                stmt.setString(6, String.valueOf(exam.getExamDateTime().toLocalTime()));
                stmt.setString(7, String.valueOf(exam.getDuration()));
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void DerslikleriAl(List<Classroom> classrooms, ExamScheduler scheduler) {
        String sql = "SELECT derslik_adi, derslik_kapasitesi FROM derslikler WHERE bolum = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, LoginController.bolum);
            ResultSet rs = stmt.executeQuery();
            classrooms.clear();
            while(rs.next()) {
                classrooms.add(new Classroom(rs.getString("derslik_adi"), rs.getInt("derslik_kapasitesi")));
            }
            scheduler.setClassrooms(classrooms);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void DersBilgileriniAl(ObservableList<SinavaDahilDerslerModel> dersler, List<Course> courses, ExamScheduler scheduler) {
        String sql = """
            SELECT 
                d.ders_kodu, d.ders_adi, d.sinif,
                (SELECT COUNT(DISTINCT o.ogrenci_numarasi) 
                 FROM ogrenciler o 
                 WHERE o.ders_kodu = d.ders_kodu) AS ogrenci_sayisi
            FROM dersler d
            WHERE d.ders_kodu = ?;
            """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for(SinavaDahilDerslerModel ders : dersler) {
                stmt.setString(1, ders.getDersKodu());
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                    int customDuration = Integer.parseInt(ders.dersSuresi.get());
                    courses.add(new Course(rs.getString("ders_kodu"), rs.getString("ders_adi"),
                            rs.getString("sinif"), rs.getInt("ogrenci_sayisi"), customDuration));
                }
            }
            scheduler.setCourses(courses);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void OgrencileriAl(List<Student> students, ExamScheduler scheduler) {
        String sql = "SELECT DISTINCT ogrenci_numarasi, ogrenci_adi FROM ogrenciler WHERE bolum = ?";
        String sql2 = "SELECT ders_kodu FROM ogrenciler WHERE ogrenci_numarasi = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             PreparedStatement stmt2 = conn.prepareStatement(sql2)) {

            stmt.setString(1, LoginController.bolum);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                String ogrenciNo = rs.getString("ogrenci_numarasi");
                String ogrenciAdi = rs.getString("ogrenci_adi");

                stmt2.setString(1, ogrenciNo);
                ResultSet rs2 = stmt2.executeQuery();

                List<String> courses = new ArrayList<>();
                while(rs2.next()) {
                    courses.add(rs2.getString("ders_kodu"));
                }

                students.add(new Student(ogrenciNo, ogrenciAdi, courses));
            }
            scheduler.setStudents(students);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}