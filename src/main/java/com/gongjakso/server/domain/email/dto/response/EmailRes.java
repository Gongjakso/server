package com.gongjakso.server.domain.email.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.email.entity.Email;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmailRes(
        Long emailId,
        String address,
        Boolean isReceive
) {

    public static EmailRes of(Email email) {
        return EmailRes.builder()
                .emailId(email.getId())
                .address(email.getAddress())
                .isReceive(email.getIsReceive())
                .build();
    }
}
