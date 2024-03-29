package com.gongjakso.server.domain.post.enumerate;

public enum StackNameType {
    REACT,
    TYPESCRIPT,
    JAVASCRIPT,
    NEXTJS,
    NODEJS,
    JAVA,
    SPRING,
    KOTLIN,
    SWIFT,
    FLUTTER,
    FIGMA,
    ETC;

    @Override
    public String toString() {
        return name();
    }

    public static boolean isValid(String stackName) {
        for (StackNameType type : StackNameType.values()) {
            if (type.name().equals(stackName)) {
                return true;
            }
        }
        return false;
    }

}
