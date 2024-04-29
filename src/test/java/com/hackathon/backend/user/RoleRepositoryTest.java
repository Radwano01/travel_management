package com.hackathon.backend.user;

import com.hackathon.backend.entities.user.RoleEntity;
import com.hackathon.backend.repositories.user.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByRole() {
        //given
        RoleEntity role = new RoleEntity(
                "USER"
        );
        roleRepository.save(role);
        //when
        RoleEntity response = roleRepository.findByRole("USER")
                .orElseThrow(()-> new EntityNotFoundException("error"));
        //then
        assertEquals(response.getRole(), "USER");
    }
}