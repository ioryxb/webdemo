package com.crxdemo.ticketoffice.controller;

import com.crxdemo.ticketoffice.dao.CustomerRepository;
import com.crxdemo.ticketoffice.entities.Customer;
import com.crxdemo.ticketoffice.entities.Ticket;
import com.crxdemo.ticketoffice.exception.ErrorMessage;
import com.crxdemo.ticketoffice.exception.LogicException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class TicketOfficeController {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CustomerRepository repository;

    @RequestMapping(value = "/v1/customers", method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        logger.debug("we are trying to findAll customers");
        List<Customer> result = repository.findAll();
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/v1/customers", method = RequestMethod.POST)
    public ResponseEntity<Object> save(@RequestBody Customer customerpo) {
        repository.save(customerpo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/v1/customers/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateCustomerInfo(@PathVariable("id") String cid, @RequestBody Customer input) {
        Customer result = repository.findById(Long.parseLong(cid));
        result.setAddress(input.getAddress());
        result.setName(input.getName());
        repository.save(result);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/v1/customer/{id}/tickets", method = RequestMethod.GET)
    public ResponseEntity<Object> findTicketsByCustomerId(@PathVariable("id") String id) {
        logger.debug("we are trying to find Tickets By CustomerId ");
        Customer result = repository.findById(Long.parseLong(id));
        return ResponseEntity.ok().body(result);
    }

    @RequestMapping(value = "/v1/customer/{id}/tickets", method = RequestMethod.POST)
    public ResponseEntity<Object> assignTicket(@PathVariable("id") String cid, @RequestBody Ticket bookedTickets) {
        Customer result = repository.findById(Long.parseLong(cid));
        bookedTickets.setCustomer(result);
        Set<Ticket> ctickets = Optional.ofNullable(result.getTickets()).orElse(new HashSet<Ticket>());
        ctickets.stream().filter(x->x.getTid()==bookedTickets.getTid()).findFirst().ifPresent(x->bookedTickets.setVersion(x.getVersion()));
        ctickets.add(bookedTickets);
        result.setTickets(ctickets);
        repository.save(result);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/v1/customer/{id}/tickets/{tid}", method = RequestMethod.PATCH)
    public ResponseEntity<Object> updateTicketStatus(@PathVariable("id") String cid, @PathVariable("tid") String tid) {
        String confirmed = "Confirmed";
        Customer result = repository.findById(Long.parseLong(cid));
        Set<Ticket> ctickets = result.getTickets().stream().map(x-> {
            if(x.getTid()==Long.parseLong(tid)) {
                x.setState(confirmed);
            }
            return x;
        }).collect(Collectors.toSet());
        result.setTickets(ctickets);
        repository.save(result);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/v1/customer/{id}/tickets/{tid}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> cancelTicket(@PathVariable("id") String cid, @PathVariable("tid") String tid) {
        Customer result = repository.findById(Long.parseLong(cid));
        Set<Ticket> ctickets = result.getTickets();
        Ticket canceledTicket = ctickets.stream().filter(x->x.getTid()==Long.parseLong(tid)).findFirst().get();

        if(cancellationInvalid(canceledTicket.getDate())){
            logger.warn("We do not support cancellations after less than 2 days to the departure day cid:" + cid + "  " +canceledTicket.getTid());
            throw LogicException.le(ErrorMessage.CANCELLATION_ERROR);
        }

        canceledTicket.setCustomer(null);
        result.setTickets(ctickets.stream().filter(x->x.getTid()!=canceledTicket.getTid()).collect(Collectors.toSet()));
        repository.save(result);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private boolean cancellationInvalid(String date){
        LocalDate departureDate = LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate nowDay = LocalDate.now();
        return nowDay.plusDays(2).isAfter(departureDate);
    }

}
