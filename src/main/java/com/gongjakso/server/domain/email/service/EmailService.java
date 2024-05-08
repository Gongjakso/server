package com.gongjakso.server.domain.email.service;

import com.gongjakso.server.domain.email.dto.request.EmailReq;
import com.gongjakso.server.domain.email.dto.response.EmailRes;
import com.gongjakso.server.domain.email.entity.Email;
import com.gongjakso.server.domain.email.repository.EmailRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;

    @Transactional
    public EmailRes registerEmail(EmailReq emailReq) {
        // Validation
        if(emailRepository.existsEmailByAddressAndDeletedAtIsNull(emailReq.address())) {
            throw new ApplicationException(ErrorCode.ALREADY_EXIST_EXCEPTION);
        }

        // Business Logic
        Email email = emailReq.from();
        Email saveEmail = emailRepository.save(email);

        // Response
        return EmailRes.of(saveEmail);
    }
}
