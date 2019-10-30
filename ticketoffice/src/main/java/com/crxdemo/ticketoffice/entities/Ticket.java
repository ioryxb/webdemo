package com.crxdemo.ticketoffice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "Ticket_DO")
public class Ticket {
    @Id
    @GeneratedValue
    private Long tid;

    @ManyToOne(optional = true)
    Customer customer;

    float price;

    String state;

    String date;

    String validFrom;

    String validTo;
    @Version
    private long version;

    public Ticket() {

    }

    public Ticket(long tid, String state) {
         this.tid = tid;
         this.state = state;
    }

    public Ticket(float price, String date, String validFrom, String validTo, String state) {
        this.date = date;
        this.price = price;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.state = state;
    }

    public Ticket(float price, String date, String validFrom, String validTo, String state, Customer customer) {
        this.date = date;
        this.price = price;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.state = state;
        this.customer = customer;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Customer getCustomer() {
        return customer;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    @JsonBackReference
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "tid=" + tid +
                ", customer=" + customer +
                ", price=" + price +
                ", state='" + state + '\'' +
                ", date='" + date + '\'' +
                ", validFrom='" + validFrom + '\'' +
                ", validTo='" + validTo + '\'' +
                ", version=" + version +
                '}';
    }
}
