package com.gongjakso.server.domain.banner.repository;

import com.gongjakso.server.domain.banner.entity.Banner;
import com.gongjakso.server.domain.banner.enumerate.DomainType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    List<Banner> findAllByDomainTypeAndDeletedAtIsNullOrderByPriorityAsc(DomainType domainType);
}
