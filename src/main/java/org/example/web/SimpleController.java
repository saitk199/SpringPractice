package org.example.web;

import lombok.extern.slf4j.Slf4j;
import org.example.service.SimpleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

/**
 * TODO
 *
 * @author <a href="mailto:saitek1998chita@gmail.ru">Ostrovskiy L.I.</a>
 */
@Slf4j
@RestController
@RequestMapping(value = "/simple-practice/api/v1")
public class SimpleController {

    /**
     * todo
     */
    private final SimpleService service;

    @Autowired
    public SimpleController(final SimpleService service) {
        this.service = service;
    }

    @GetMapping(value = "/application-name")
    public ResponseEntity<String> getApplicationName() { //TODO HATEOAS
        log.info("GET /application-name");
        return ok(service.getApplicationNameWithVersion());
    }
}
