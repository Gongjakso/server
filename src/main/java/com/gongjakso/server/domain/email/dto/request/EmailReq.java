package com.gongjakso.server.domain.email.dto.request;

import com.gongjakso.server.domain.email.entity.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record EmailReq(
        @NotNull(message = "이메일 정보가 담겨있지 않습니다.")
        @jakarta.validation.constraints.Email(message = "유효한 이메일 형식이 아닙니다.")
        String address
) {

    public Email from() {
        return Email.builder()
                .address(this.address)
                .build();
    }
}
