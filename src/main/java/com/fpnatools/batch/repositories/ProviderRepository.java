package com.fpnatools.batch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpnatools.batch.domain.ProviderEntity;

public interface ProviderRepository extends JpaRepository<ProviderEntity, Long>{

}
