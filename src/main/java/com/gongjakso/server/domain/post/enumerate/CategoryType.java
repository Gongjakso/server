package com.gongjakso.server.domain.post.enumerate;

public enum CategoryType {
    PLAN,DESIGN,FE,BE,ETC,LATER;
    @Override
    public String toString() {
        return name();
    }

    public static boolean isValid(String category) {
        for (CategoryType type : CategoryType.values()) {
            if (type.name().equals(category)) {
                return true;
            }
        }
        return false;
    }

}
