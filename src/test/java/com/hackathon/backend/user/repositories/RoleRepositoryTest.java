package com.hackathon.backend.user.repositories;

import com.hackathon.backend.entities.user.RoleEntity;
import com.hackathon.backend.repositories.user.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    void findByRole() {
        //given
        RoleEntity role = new RoleEntity();
        role.setRole("USER");
        roleRepository.save(role);

        //when
        Optional<RoleEntity> response = roleRepository.findByRole("USER");

        //then
        assertTrue(response.isPresent());
        assertEquals(response.get().getRole(), "USER");
    }
}