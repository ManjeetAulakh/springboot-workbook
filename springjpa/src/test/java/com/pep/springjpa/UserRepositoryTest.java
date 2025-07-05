package com.pep.springjpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.pep.springjpa.models.User;
import com.pep.springjpa.repo.UserRepo;

@DataJpaTest // Loads only JPA components and sets up an in-memory DB by default
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// // Prevents Spring from replacing your real DB with H2, fow now i work on h2
// so i comment it
@ActiveProfiles("test") // Activates the "test" profile, so application-test.properties will be used
public class UserRepositoryTest {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        userRepo.deleteAllInBatch();

        user1 = new User("Alice", "alice@gmail.com");
        user2 = new User("Bob", "bob@gmail.com");
        user3 = new User("amn","amn@gmail.com");

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testFindByEmailFound() {
        Optional<User> foundUser = userRepo.findByEmail("alice@gmail.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Alice");
        assertThat(foundUser.get().getEmail()).isEqualTo("alice@gmail.com");
    }

    @Test
    void testFindByEmailNotFound() {
        Optional<User> foundUser = userRepo.findByEmail("notfound@example.com");
        assertThat(foundUser).isNotPresent();
    }

    @Test
    void testSaveUser() {
        User newUser = new User("Alice", "alice@gmail.com");
        User savedUser = userRepo.save(newUser);

        // 🔎 Checks if the saved user is not null (basic sanity check)
        assertThat(savedUser).isNotNull();

        assertThat(savedUser.getId()).isNotNull(); // as id not set by us and As
        // tester we dont know in backend login so we want to only check that id id
        // notnull
        assertThat(savedUser.getName()).isEqualTo("Alice");
        assertThat(savedUser.getEmail()).isEqualTo("alice@gmail.com");

        /*
         * assertThat(savedUser.getName().equals(newUser.getName())).isTrue();
         * ❌ It hides the actual values in case of failure.
         * ❌ It only returns true/false, so your test failure message won't say what was
         * expected vs what was found.
         * ❌ It makes debugging harder if something fails
         */

    }

    @Test
    void testUpdateUser(){
        String newName = "amanpal";
        String newEmail = "amanpal@gmail.com";

        user3.setName(newName);
        user3.setEmail(newEmail);
        User updatedUser = userRepo.save(user3);

        assertThat(updatedUser.getName()).isEqualTo("amanpal");
        assertThat(updatedUser.getEmail()).isEqualTo("amanpal@gmail.com");
    }
}
