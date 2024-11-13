package com.gongjakso.server.domain.team.enumerate;

import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum TeamStatus {

    RECRUITING("모집 중"),
    EXTENSION("모집 연장"),
    CANCELED("모집 취소"),
    CLOSED("모집 마감"),
    ACTIVE("활동 중"),
    FINISHED("활동 종료");

    private final String description;

    TeamStatus(String description) {
        this.description = description;
    }

    /**
     * 활동 중 또는 활동 종료를 업데이트할 때 유효성 검사하는 메소드
     * @param status 입력받은 활동 문자열 (한국어)
     * @return 해당 문자열에 해당하는 ENUM 값
     */
    public static TeamStatus checkActiveORFinished(String status) {
        if(status.equals(ACTIVE.getDescription())) {
            return ACTIVE;
        } else if(status.equals(FINISHED.getDescription())) {
            return FINISHED;
        }
        throw new ApplicationException(ErrorCode.INVALID_TEAM_STATUS_EXCEPTION);
    }
}
