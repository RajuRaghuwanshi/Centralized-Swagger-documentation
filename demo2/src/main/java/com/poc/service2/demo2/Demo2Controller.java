package com.poc.service2.demo2;

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
public class Demo2Controller {


    private static List<Demo2Class> demo2ClassList;

    static {

        demo2ClassList = Stream.of(new Demo2Class(1, "hello-world1"), new Demo2Class(2, "hello-world2"),new Demo2Class(3, "hello-world3"), new Demo2Class(4, "hello-world4"))
                               .collect(Collectors.toList());
    }

    @GetMapping
    public ResponseEntity<List<Demo2Class>> getAllValue() {

        return ResponseEntity.ok(demo2ClassList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Demo2Class> getValue(int id) {

        final Optional<Demo2Class> any = demo2ClassList.parallelStream()
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
