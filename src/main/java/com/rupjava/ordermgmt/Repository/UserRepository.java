package com.rupjava.ordermgmt.Repository;

import com.rupjava.ordermgmt.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);


    @Query(value = "SELECT * FROM users u WHERE u.name=?1 AND u.email=?2" , nativeQuery=true)
    List<User> findByNameAndEmail(String name, String email);

}
