package com.kpi.lab2.models.daos;

import com.kpi.lab2.models.entities.RailwayRoute;
import com.kpi.lab2.models.entities.Ticket;
import com.kpi.lab2.models.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

class TicketDaoTest {

    private final TicketDao ticketDao = new TicketDao(EntityDaoTest.getDataSource());

    @Test
    public void readTickets() {
        List<Ticket> tickets = ticketDao.findAll().stream().filter(ticket -> ticket.getId() <= 5).toList();
        Assertions.assertEquals(5, tickets.size());
        for (Ticket ticket : tickets) {
            Ticket ticketById = ticketDao.findById(ticket.getId());
            compareTickets(ticket, ticketById);
        }
    }

    @Test
    public void createTicket() {
        Long seatNumber = 12L;
        RailwayRoute railwayRoute = RailwayRoute.builder().id(1L).build();
        Ticket ticket = ticketDao.save(getTicket(seatNumber, railwayRoute));
        Assertions.assertNotNull(ticket.getId());
        Assertions.assertEquals(seatNumber, ticket.getSeatNumber());
        Assertions.assertEquals(railwayRoute.getId(), ticket.getRoute().getId());
        Assertions.assertEquals(1L, ticket.getOwner().getId());
    }

    @Test
    public void updateTicket() {
        Ticket ticket = getTicket(15L, RailwayRoute.builder().id(1L).build());
        ticket = ticketDao.save(ticket);
        Assertions.assertNotNull(ticket.getId());
        ticket.setSeatNumber(20L);
        ticket.setRoute(RailwayRoute.builder().id(2L).build());
        Ticket updatedTicket = ticketDao.save(ticket);
        compareTickets(ticket, updatedTicket);
        Assertions.assertEquals(ticket.getId(), updatedTicket.getId());
        Assertions.assertEquals(ticket.getSeatNumber(), updatedTicket.getSeatNumber());
        Assertions.assertEquals(ticket.getRoute().getId(), updatedTicket.getRoute().getId());
        Assertions.assertEquals(ticket.getOwner().getId(), updatedTicket.getOwner().getId());
    }

    @Test
    public void deleteTicket() {
        Ticket ticket = ticketDao.save(getTicket(5L, RailwayRoute.builder().id(1L).build()));
        Assertions.assertNotNull(ticket.getId());
        ticketDao.delete(ticket);
        Assertions.assertThrows(NoSuchElementException.class, () -> ticketDao.findById(ticket.getId()));
    }

    private Ticket getTicket(Long seatNumber, RailwayRoute railwayRoute) {
        return Ticket.builder()
                .seatNumber(seatNumber)
                .route(railwayRoute)
                .owner(User.builder().id(1L).build())
                .build();
    }

    private void compareTickets(Ticket expectedTicket, Ticket actualTicket) {
        Assertions.assertEquals(expectedTicket.getId(), actualTicket.getId());
        Assertions.assertEquals(expectedTicket.getSeatNumber(), actualTicket.getSeatNumber());
        Assertions.assertEquals(expectedTicket.getRoute().getId(), actualTicket.getRoute().getId());
        Assertions.assertEquals(expectedTicket.getOwner().getId(), actualTicket.getOwner().getId());
    }
}