package com.kpi.lab2.models.services;

import com.kpi.lab2.models.daos.TicketDao;
import com.kpi.lab2.models.entities.RailwayRoute;
import com.kpi.lab2.models.entities.Ticket;
import com.kpi.lab2.models.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.kpi.lab2.models.services.RailwayRouteServiceTest.railwayRoutes;
import static com.kpi.lab2.models.services.UserServiceTest.users;
import static org.mockito.ArgumentMatchers.any;

class TicketServiceTest {
    public static List<Ticket> tickets = List.of(
            Ticket.builder()
                    .id(1L)
                    .route(railwayRoutes.get(0))
                    .seatNumber(3L)
                    .owner(users.get(1))
                    .build(),
            Ticket.builder()
                    .id(2L)
                    .route(railwayRoutes.get(1))
                    .seatNumber(5L)
                    .owner(users.get(1))
                    .build(),
            Ticket.builder()
                    .id(3L)
                    .route(railwayRoutes.get(2))
                    .seatNumber(7L)
                    .owner(users.get(1))
                    .build()
    );

    private TicketService ticketService;

    @BeforeEach
    public void init() {
        TicketDao ticketDao = Mockito.mock(TicketDao.class);
        RailwayRouteService railwayRouteService = Mockito.mock(RailwayRouteService.class);
        UserService userService = Mockito.mock(UserService.class);

        ticketService = new TicketService(ticketDao, userService, railwayRouteService);

        Mockito.doReturn(tickets).when(ticketDao).findAll();

        Mockito.doAnswer(invocation -> {
            Long id = invocation.getArgument(0, Long.class);
            return tickets.stream().filter(ticket -> ticket.getId().equals(id)).findFirst().orElseThrow(NoSuchElementException::new);
        }).when(ticketDao).findById(any(Long.class));

        Mockito.doAnswer(invocation -> {
            Long id = invocation.getArgument(0, Long.class);
            return railwayRoutes.stream().filter(railwayRoute -> railwayRoute.getId().equals(id)).findFirst().orElseThrow(NoSuchElementException::new);
        }).when(railwayRouteService).findById(any(Long.class));

        Mockito.doAnswer(invocation -> {
            Long id = invocation.getArgument(0, Long.class);
            return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElseThrow(NoSuchElementException::new);
        }).when(userService).findById(any(Long.class));

        Mockito.doAnswer(invocation -> {
            RailwayRoute route = invocation.getArgument(0, RailwayRoute.class);
            return tickets.stream().filter(ticket -> ticket.getRoute().equals(route)).collect(Collectors.toList());
        }).when(ticketDao).findByRoute(any(RailwayRoute.class));

        Mockito.doAnswer(invocation -> {
            User owner = invocation.getArgument(0, User.class);
            return tickets.stream().filter(ticket -> ticket.getOwner().equals(owner)).collect(Collectors.toList());
        }).when(ticketDao).findByOwner(any(User.class));

        Mockito.doAnswer(invocation -> {
            RailwayRoute route = invocation.getArgument(0, RailwayRoute.class);
            return (long) tickets.stream().filter(ticket -> ticket.getRoute().equals(route)).toList().size();
        }).when(ticketDao).countByRoute(any(RailwayRoute.class));
    }

    @Test
    public void findAll() {
        List<Ticket> actualTickets = ticketService.findAll();
        Assertions.assertEquals(tickets.size(), actualTickets.size());
        Assertions.assertEquals(tickets, actualTickets);
    }

    @Test
    public void findById() {
        Ticket actualTickets = ticketService.findById(1L);
        Assertions.assertEquals(tickets.get(0), actualTickets);
        Assertions.assertThrows(NoSuchElementException.class, () -> ticketService.findById(10L));
    }

    @Test
    public void findByRoute() {
        List<Ticket> actualTickets = ticketService.findByRoute(railwayRoutes.get(0));
        Assertions.assertEquals(1, actualTickets.size());
        Assertions.assertEquals(tickets.get(0), actualTickets.get(0));
    }

    @Test
    public void findByOwner() {
        List<Ticket> actualTickets = ticketService.findByOwner(users.get(1));
        Assertions.assertEquals(3, actualTickets.size());
        for (int i = 0; i < actualTickets.size(); i++) {
            Assertions.assertEquals(tickets.get(i), actualTickets.get(i));
        }
        Assertions.assertEquals(0, ticketService.findByOwner(users.get(0)).size());
    }

    @Test
    public void countByRoute() {
        for (int i = 0; i < 3; i++) {
            Assertions.assertEquals(1L, ticketService.countByRoute(railwayRoutes.get(i)));
        }
    }
}