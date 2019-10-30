package com.crxdemo.ticketoffice;

import com.crxdemo.ticketoffice.entities.Customer;
import com.crxdemo.ticketoffice.entities.Ticket;
import org.junit.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TicketofficeApplication.class)
@TestPropertySource(value={"classpath:application.properties"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TicketofficeApplicationTests {
    private static JdbcTemplate jdbcTemplate;
    private static String Customer_URL = null;
    private static String Customer_ticket_URL = null;
    private static String ticket_URL = null;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Value("${local.server.port}")
    protected int port;

    private String cid = "111";
    private String tid = "112";

    @Before
    public void setupEnv() {
        Customer_URL = "http://localhost:"+port+"/v1/customers";
        Customer_ticket_URL = "http://localhost:"+port+"/v1/customer/"+cid+"/tickets";
        ticket_URL = "http://localhost:"+port+"/v1/customer/"+cid+"/tickets/"+tid;
    }
    @Test
    @Sql(scripts = "/testdata/users.sql")
    public void getCustomersTest() throws URISyntaxException {
        int expectedSize = 2;
        RequestEntity requestEntity = RequestEntity.get(new URI(Customer_URL)).headers(getHeaders()).build();
        ResponseEntity<List<Customer>> responseEntity = this.restTemplate.exchange(requestEntity,  new ParameterizedTypeReference<List<Customer>>(){});
        List<Customer>  testEntity = responseEntity.getBody();

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(expectedSize,testEntity.size());
    }

    @Test
    public void saveCustomersTest() throws URISyntaxException {
        Customer added = new Customer("JinJin","SHANGHAI");
        HttpEntity<Customer> formEntity = new HttpEntity<Customer>(added, getHeaders());

        RequestEntity requestEntity = RequestEntity.post(new URI(Customer_URL)).headers(getHeaders()).body(formEntity);
        ResponseEntity responseEntity = this.restTemplate.exchange(requestEntity,ResponseEntity.class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);

        int expectedSize = 1;
        RequestEntity requestEntityGet = RequestEntity.get(new URI(Customer_URL)).headers(getHeaders()).build();
        ResponseEntity<List<Customer>> responseGet = this.restTemplate.exchange(requestEntityGet,  new ParameterizedTypeReference<List<Customer>>(){});
        List<Customer>  getEntity = responseGet.getBody();
        Assert.assertEquals(responseGet.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(expectedSize,getEntity.size());
    }

    @Test
    @Sql(scripts = "/testdata/users.sql")
    public void updateCustomersTest() throws URISyntaxException {
        Customer updated = new Customer("xubin","SHANGHAI");
        String cid = "111";
        String url = Customer_URL+"/"+cid;

        RequestEntity requestEntity = RequestEntity.put(new URI(url)).headers(getHeaders()).body(updated);
        ResponseEntity responseEntity = this.restTemplate.exchange(requestEntity,ResponseEntity.class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);

        RequestEntity requestEntityGet = RequestEntity.get(new URI(Customer_URL)).headers(getHeaders()).build();
        ResponseEntity<List<Customer>> responseGet = this.restTemplate.exchange(requestEntityGet,  new ParameterizedTypeReference<List<Customer>>(){});
        List<Customer>  getEntity = responseGet.getBody();
        Assert.assertEquals(responseGet.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals("SHANGHAI",getEntity.stream().filter(x->x.getCid()==Long.parseLong(cid)).findFirst().get().getAddress());
    }

    @Test
    @Sql(scripts = "/testdata/users.sql")
    public void getCustomersTicketsTest() throws URISyntaxException {
        int expectedSize = 2;
        RequestEntity requestEntity = RequestEntity.get(new URI(Customer_ticket_URL)).headers(getHeaders()).build();
        ResponseEntity<Customer> responseEntity = this.restTemplate.exchange(requestEntity,  Customer.class);
        Customer testEntity = responseEntity.getBody();

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(expectedSize,testEntity.getTickets().size());
    }

    @Test
    @Sql(scripts = "/testdata/users.sql")
    public void assignCustomersNewTicketTest() throws URISyntaxException {
        Ticket bookedTickets = new Ticket(11.25f,"20191101","20191001","20191101","Ordered");

        RequestEntity requestEntity = RequestEntity.post(new URI(Customer_ticket_URL)).headers(getHeaders()).body(bookedTickets);
        ResponseEntity responseEntity = this.restTemplate.exchange(requestEntity,ResponseEntity.class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);

        int expectedSize = 3;
        RequestEntity requestEntityGet = RequestEntity.get(new URI(Customer_ticket_URL)).headers(getHeaders()).build();
        ResponseEntity<Customer> responseEntityGet = this.restTemplate.exchange(requestEntityGet,  Customer.class);
        Customer testEntity = responseEntityGet.getBody();
        Assert.assertEquals(responseEntityGet.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(expectedSize,testEntity.getTickets().size());
    }

    @Test
    @Sql(scripts = "/testdata/users.sql")
    public void assignCustomersExistingTicketTest() throws URISyntaxException {
        Ticket bookedTickets = new Ticket(11.0f,"20191101","20191020","20191101","Generated");
        bookedTickets.setTid(114L);
        HttpEntity<Ticket> formEntity = new HttpEntity<Ticket>(bookedTickets, getHeaders());

        RequestEntity requestEntity = RequestEntity.post(new URI(Customer_ticket_URL)).headers(getHeaders()).body(formEntity);
        ResponseEntity responseEntity = this.restTemplate.exchange(requestEntity,ResponseEntity.class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);

        int expectedSize = 3;
        RequestEntity requestEntityGet = RequestEntity.get(new URI(Customer_ticket_URL)).headers(getHeaders()).build();
        ResponseEntity<Customer> responseEntityGet = this.restTemplate.exchange(requestEntityGet,  Customer.class);
        Customer testEntity = responseEntityGet.getBody();
        Assert.assertEquals(responseEntityGet.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(expectedSize,testEntity.getTickets().size());
    }

    @Test
    @Sql(scripts = "/testdata/users.sql")
    public void updateStateOfTicketTest() throws URISyntaxException {
        RequestEntity requestEntity = RequestEntity.patch(new URI(ticket_URL)).headers(getHeaders()).build();
        ResponseEntity responseEntity = this.restTemplate.exchange(requestEntity,ResponseEntity.class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);

        RequestEntity requestEntityGet = RequestEntity.get(new URI(Customer_ticket_URL)).headers(getHeaders()).build();
        ResponseEntity<Customer> responseEntityGet = this.restTemplate.exchange(requestEntityGet,  Customer.class);
        Customer testEntity = responseEntityGet.getBody();
        Assert.assertEquals(responseEntityGet.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals("Confirmed",testEntity.getTickets().stream().filter(x->x.getTid()==Long.parseLong(tid)).findFirst().get().getState());
    }

    @Test
    @Sql(scripts = "/testdata/users.sql")
    public void cancelTicketTest() throws URISyntaxException {
        RequestEntity requestEntity = RequestEntity.delete(new URI(ticket_URL)).headers(getHeaders()).build();
        ResponseEntity responseEntity = this.restTemplate.exchange(requestEntity,ResponseEntity.class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);

        int expectedSize = 1;
        RequestEntity requestEntityGet = RequestEntity.get(new URI(Customer_ticket_URL)).headers(getHeaders()).build();
        ResponseEntity<Customer> responseEntityGet = this.restTemplate.exchange(requestEntityGet,  Customer.class);
        Customer testEntity = responseEntityGet.getBody();
        Assert.assertEquals(responseEntityGet.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(expectedSize,testEntity.getTickets().size());
    }

    @After
    public void cleanDB() throws SQLException {
        Resource resource = new ClassPathResource("/testdata/CleanDB.sql");
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, resource);
        connection.close();
    }

    private HttpHeaders getHeaders() {
        String plainCredentials = "admin:admin";
        String base64Credentials = Base64.getEncoder().encodeToString(plainCredentials.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        return headers;
    }
}
