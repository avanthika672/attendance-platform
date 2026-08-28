package com.zepic.attendance_platform.security;
public class TenantContext {
    private static final ThreadLocal<Long> COLLEGE_ID =
            new ThreadLocal<>();
    public static void set(Long collegeId) {
        COLLEGE_ID.set(collegeId);
    }
    public static Long get() {
        return COLLEGE_ID.get();
    }
    public static void clear() {
        COLLEGE_ID.remove();
    }
}