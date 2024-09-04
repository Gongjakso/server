package com.gongjakso.server.global.common;

import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Position {

    PLAN("기획"),
    DESIGN("디자인"),
    DEVELOP("개발"),
    ETC("기타");

    private final String koreanName;

    Position(String koreanName) {
        this.koreanName = koreanName;
    }

    public static Position convert(String koreanName) {
        Position convertPosition = null;
        for (Position position : Position.values()) {
            if (position.getKoreanName().equals(koreanName)) {
                convertPosition = position;
            }
        }

        if(convertPosition == null) {
            throw new ApplicationException(ErrorCode.INVALID_POSITION_EXCEPTION);
        }

        return convertPosition;
    }

}
