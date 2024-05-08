package com.gongjakso.server.domain.email.repository;

import com.gongjakso.server.domain.email.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {

    Boolean existsEmailByAddressAndDeletedAtIsNull(String address);
}
