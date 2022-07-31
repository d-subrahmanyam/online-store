package io.subbu.estore.rest.controllers;

import io.subbu.estore.contracts.ControllerI;
import io.subbu.estore.models.User;
import io.subbu.estore.services.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "api/users")
public class UsersRestController implements ControllerI<User> {

    @Autowired
    private UserService userService;

    /**
     * This method retrieves all the
     * entities from the repo for the
     * given type T
     *
     * @return List<User>
     */
    @Override
    @GetMapping(produces = "application/json")
    public List<User> all() {
        return userService.all();
    }

    /**
     * This method retrieves a single
     * entity of type T for the given
     * uuid from the repo
     *
     * @param userUuid
     * @return User
     */
    @Override
    @GetMapping(value = "/{userUuid}", produces = "application/json")
    @SneakyThrows
    public User get(@PathVariable String userUuid) {
        return userService.get(userUuid);
    }

    /**
     * This method saves the given entity
     * of type T to the repo
     *
     * @param user
     */
    @Override
    @PostMapping
    public void save(User user) {
        userService.save(user);
    }

    /**
     * This method deletes the entity
     * of type T from the repo for the
     * given uuid
     *
     * @param userUuid
     */
    @Override
    @DeleteMapping(value = "/{userUuid}")
    @SneakyThrows
    public void delete(@PathVariable String userUuid) {
        userService.delete(userUuid);
    }
}
