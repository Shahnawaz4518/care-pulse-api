package com.devshahnawaz.hospitalManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devshahnawaz.hospitalManagement.entity.User;
import com.devshahnawaz.hospitalManagement.entity.type.AuthProviderType;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User>findByUsername(String username);

    Optional<User> findByProviderIdAndProviderType(String providerId, AuthProviderType providerType);
}
