package com.manjot.snapnote.repository;

import com.manjot.snapnote.model.Role;
import com.manjot.snapnote.model.enums.ERole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Role entities in MongoDB.
 */
@Repository
public interface RoleRepository extends MongoRepository<Role,String> {
    Optional<Role> findByName(ERole name);
}
