package com.crxdemo.ticketoffice.entities;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Customer_DO")
public class Customer {
    @Id
    @GeneratedValue
    private Long cid;

    String name;

    String address;

    @Version
    private long version;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Ticket> tickets;

    public Customer() {

    }

    public Customer(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Customer(String name, String address, Set<Ticket> tickets) {
        this.name = name;
        this.address = address;
        this.tickets = tickets;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return cid.equals(customer.cid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cid);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "cid=" + cid +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", version=" + version +
                ", tickets=" + tickets +
                '}';
    }

}
