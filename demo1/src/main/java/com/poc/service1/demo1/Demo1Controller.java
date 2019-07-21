package com.poc.service1.demo1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin(
        allowCredentials = "true",
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT}
)
@RestController
@RequestMapping(value = "/Demo1")
public class Demo1Controller {


    private static List<Demo1Class> demo1ClassList;

    static {

        demo1ClassList = Stream.of(new Demo1Class(1, "hello-world1"), new Demo1Class(2, "hello-world2"))
                               .collect(Collectors.toList());
    }

    @GetMapping
    public ResponseEntity<List<Demo1Class>> getAllValue() {

        return ResponseEntity.ok(demo1ClassList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Demo1Class> getValue(int id) {

        final Optional<Demo1Class> any = demo1ClassList.parallelStream()
                                                       .filter(demo1Class -> demo1Class.getId() == id)
                                                       .findAny();

        if (any.isPresent()) {
            return ResponseEntity.ok(any.get());
        } else {
            return ResponseEntity.notFound()
                                 .build();
        }
    }
}
