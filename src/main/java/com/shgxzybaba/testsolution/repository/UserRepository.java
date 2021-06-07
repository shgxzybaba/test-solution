package com.shgxzybaba.testsolution.repository;

import com.shgxzybaba.testsolution.enums.Status;
import com.shgxzybaba.testsolution.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    Page<User> findByStatus(Status status, Pageable pageable);
}
