package com.example.diplomayin;

import androidx.room.TypeConverter;

public class Converters {

    @TypeConverter
    public static String fromStatus(UserStatus status) {
        return status == null ? null : status.name();
    }

    @TypeConverter
    public static UserStatus toStatus(String status) {
        return status == null ? null : UserStatus.valueOf(status);
    }
}