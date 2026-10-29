package com.blumbit.eblumbit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blumbit.eblumbit.entities.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser_Id(Integer userId);
}
