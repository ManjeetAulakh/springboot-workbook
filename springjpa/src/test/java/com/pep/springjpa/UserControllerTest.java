package com.pep.springjpa;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pep.springjpa.models.User;
import com.pep.springjpa.repo.UserRepo;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // for converti object to json and vice-versa

    @Autowired
    private UserRepo userRepo;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userRepo.deleteAll();
        objectMapper.registerModule(new JavaTimeModule());

        User initialUser1 = new User("Jon", "jon1@gmail.com");
        User initialUser2 = new User("Jane", "jane@gmail.com");

        List<User> savedInitialUsers = userRepo.saveAll(Arrays.asList(initialUser1, initialUser2));
        this.user1 = savedInitialUsers.get(0);
        this.user2 = savedInitialUsers.get(1);

        /*
         * initialUser1 is created in memory, but has no id.
         * 
         * Only after saveAll(...) does it get saved to DB and assigned an
         * auto-generated id.
         * So we assign the saved user with ID → to this.user1 and user2.
         * ✅ You now have user1 and user2 which can be used in test methods (e.g. for
         * testing GET or DELETE).
         * 
         */

        List<User> manyUsers = IntStream.rangeClosed(1, 25)
                .mapToObj(i -> new User("User " + i, "user" + i + "@gmail.com"))
                .collect(Collectors.toList());
        userRepo.saveAll(manyUsers);

        // these 25 users can be used for sorting, pagination, searching
    }

    @Test
    void testGetUserByIdFound() throws Exception {
        mockMvc.perform(get("/users/" + user1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user1.getId())))
                .andExpect(jsonPath("$.name", is(user1.getName())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())));

        // Test for user2 however create differnt func for each testcase assertion
        mockMvc.perform(get("/users/" + user2.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user2.getId())))
                .andExpect(jsonPath("$.name", is(user2.getName())))
                .andExpect(jsonPath("$.email", is(user2.getEmail())));

    }

    @Test
    void testCreateUser() throws Exception {
        User newUser = new User("Alice", "alice@gmail.com");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Alice")))
                .andExpect(jsonPath("$.email", is("alice@gmail.com")));
                
        // jsonPath() is used in Spring's MockMvc tests to validate the JSON response
        // body returned by a REST API.

    }

    @Test
    void testDeleteUserFound() throws Exception {
        mockMvc.perform(delete("/users/" + user1.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/" + user1.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUserNotFound() throws Exception {
        mockMvc.perform(delete("/users/999999"))
                .andExpect(status().isNotFound());
    }
}
