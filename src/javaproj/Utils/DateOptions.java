package javaproj.Utils;

import java.time.*;
import java.util.*;

public final class DateOptions {
    
    private DateOptions() {}

    // Returns the current year and the next two years.
    public static List<String> yearList(LocalDate today) {
        List<String> years = new ArrayList<>();
        int currentYear = today.getYear();
        for (int i = 0; i < 3; i++) years.add(String.valueOf(currentYear + i));
        return years;
    }
    
    // Returns valid months and skips past months in the current year.
    public static List<String> monthList(LocalDate today, int year) {
        List<String> months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            if (year == today.getYear() && (i + 1) < today.getMonthValue()) continue;
            months.add(String.valueOf(i + 1));
        }
        return months;
    }  
    
    // Returns valid days and skips past dates in the current month.
    public static List<String> dayList(LocalDate today, int year, int month) {
        List<String> days = new ArrayList<>();
        YearMonth ym = YearMonth.of(year, month);
        for (int d = 1; d <= ym.lengthOfMonth(); d++) {
            if (year == today.getYear() && month == today.getMonthValue() && d < today.getDayOfMonth()) continue;
            days.add(String.valueOf(d));
        }
        return days;
    }
    
    // Returns valid hour slots for the selected date.
    public static List<String> hourList(LocalDateTime now, int year, int month, int day) {
        List<String> hours = new ArrayList<>();
        LocalDate selected = LocalDate.of(year, month, day);
        for (int h = 0; h < 24; h++) {
            if (selected.isEqual(now.toLocalDate())) {
                if (h < now.getHour()) continue;
                if (h == now.getHour() && now.getMinute() >= 45) continue;
            }
            hours.add(String.format("%02d", h));
        }
        return hours;
    }
    
    // Returns 15-minute slots and skips times that have already passed.
    public static List<String> minuteList(LocalDateTime now, int year, int month, int day, int hour) {
        List<String> minutes = new ArrayList<>();
        LocalDate selectedDate = LocalDate.of(year, month, day);
        for (int i = 0; i < 60; i += 15) {
            if (selectedDate.isEqual(now.toLocalDate())
                    && hour == now.getHour()
                    && i < now.getMinute()) {
                continue;
            }
            minutes.add(String.format("%02d", i));
        }
        return minutes;
    }
    
    // Rounds a time up to the next slot boundary.
    public static LocalDateTime roundUpTime(LocalDateTime time, int gap){
        long minute = time.getMinute();
        long remainder = minute % gap;
        
        if (remainder != 0){
            long minuteToAdd = gap - remainder;
            time = time.plusMinutes(minuteToAdd);
        }
        time = time.withSecond(0).withNano(0);
        return time;
    }
}
