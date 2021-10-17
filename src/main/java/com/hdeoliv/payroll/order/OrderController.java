package com.hdeoliv.payroll.order;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class OrderController {
    private final OrderRepository repository;
    private final OrderModelAssembler assembler;

    OrderController(OrderRepository repository, OrderModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/orders")
    CollectionModel<EntityModel<Order>> all() {
        List<EntityModel<Order>> orders = repository.findAll().stream().map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    EntityModel<Order> one(@PathVariable long id) {
        Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        return assembler.toModel(order);
    }

    @PostMapping("/orders")
    ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order order) {
        order.setStatus(OrderStatus.IN_PROGRESS);
        Order newOrder = repository.save(order);
        return ResponseEntity.created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri())
                .body(assembler.toModel(newOrder));
    }

    @DeleteMapping("/orders/{id}")
    ResponseEntity<?> cancel(@PathVariable long id) {
        Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == OrderStatus.IN_PROGRESS) {
            order.setStatus(OrderStatus.CANCELLED);
            return ResponseEntity.ok(assembler.toModel(repository.save(order)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create().withTitle("Method Not allowed")
                        .withDetail("You can't cancel a order that is in the " + order.getStatus() + " status"));
    }

    @PutMapping("/orders/{id}")
    ResponseEntity<?> complete(@PathVariable long id) {
        Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == OrderStatus.IN_PROGRESS) {
            order.setStatus(OrderStatus.COMPLETED);
            return ResponseEntity.ok(assembler.toModel(repository.save(order)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create().withTitle("Method Not allowed")
                        .withDetail("You can't complete a order that is in the " + order.getStatus() + " status"));
    }

}
