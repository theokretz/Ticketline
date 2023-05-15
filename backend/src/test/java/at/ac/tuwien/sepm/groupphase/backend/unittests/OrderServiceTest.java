package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PaymentDetailRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceSectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private NotUserRepository NotUserRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private PerformanceSectorRepository performanceSectorRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private SeatMapper seatMapper;

    private Performance performance;
    private ApplicationUser user;
    private Location location;
    private Set<Location> locationSet;
    private PaymentDetail paymentDetail;
    private Set<PaymentDetail> paymentDetailSet;
    private UserDto userDto;
    private Event event;
    private Hall hall;
    private Sector standingSector;
    private Sector seatedSector;
    private PerformanceSector standingPerformanceSector;
    private PerformanceSector seatedPerformanceSector;
    private Seat standingSeat;
    private Seat seatedSeat;
    private CartTicketDto cartTicketDtoSeated;
    private CartTicketDto cartTicketDtoStanding;
    private CartDto cartDto;
    private Ticket standingTicket;
    private Ticket seatedTicket;
    private CartDto cartDto2;
    private CartDto cartDto3;
    private ApplicationUser user2;

    @BeforeAll
    public void beforeAll() {
        event = new Event();
        event.setName("The Eras Tour");
        event.setLength(Duration.ZERO);
        eventRepository.save(event);

        location = new Location();
        location.setCity("Vienna");
        location.setCountry("Austria");
        location.setPostalCode(1120);
        location.setStreet("Straße 1");
        locationSet = new HashSet<>();
        locationSet.add(location);
        locationRepository.save(location);

        hall = new Hall();
        hall.setName("Halle 1");
        hall.setLocation(location);
        hallRepository.save(hall);

        this.performance = new Performance();
        this.performance.setDatetime(LocalDateTime.now());
        this.performance.setEvent(event);
        this.performance.setHall(hall);
        this.performanceRepository.save(performance);

        this.user = new ApplicationUser();
        this.user.setId(1);
        this.user.setEmail("hallo@123");
        this.user.setAdmin(false);
        this.user.setFirstName("Theo");
        this.user.setLastName("Kretz");
        this.user.setPassword("Password");
        this.user.setLocked(false);
        this.user.setSalt("asdjaslkdjaös");
        this.user.setPoints(10000);

        this.user2 = new ApplicationUser();
        this.user2.setId(9);
        this.user2.setEmail("hallo@12332323");
        this.user2.setAdmin(false);
        this.user2.setFirstName("Hallo");
        this.user2.setLastName("Kretz");
        this.user2.setPassword("Password2");
        this.user2.setLocked(false);
        this.user2.setSalt("asdjds");
        this.user2.setPoints(10000);
        NotUserRepository.save(user2);

        paymentDetail = new PaymentDetail();
        paymentDetail.setCvv(222);
        paymentDetail.setCardHolder("hallo");
        paymentDetail.setCardNumber(23123131);
        paymentDetail.setExpirationDate(LocalDate.now());
        paymentDetail.setUser(user);

        paymentDetailSet = new HashSet<>();
        paymentDetailSet.add(paymentDetail);
        user.setPaymentDetails(paymentDetailSet);
        user.setLocations(locationSet);

        NotUserRepository.save(user);

        paymentDetailRepository.save(paymentDetail);

        userDto = new UserDto();
        userDto = userMapper.applicationUserToDto(user);

        standingSector = new Sector();
        standingSector.setHall(hall);
        standingSector.setName("Standing Sector 1");
        standingSector.setStanding(true);
        sectorRepository.save(standingSector);

        seatedSector = new Sector();
        seatedSector.setStanding(false);
        seatedSector.setName("Seated Sector 1");
        seatedSector.setHall(hall);
        sectorRepository.save(seatedSector);

        standingPerformanceSector = new PerformanceSector();
        standingPerformanceSector.setPerformance(performance);
        standingPerformanceSector.setSector(standingSector);
        standingPerformanceSector.setPrice(BigDecimal.valueOf(100.0));
        standingPerformanceSector.setPointsReward(100);
        performanceSectorRepository.save(standingPerformanceSector);

        seatedPerformanceSector = new PerformanceSector();
        seatedPerformanceSector.setPerformance(performance);
        seatedPerformanceSector.setSector(seatedSector);
        seatedPerformanceSector.setPrice(BigDecimal.valueOf(50.0));
        seatedPerformanceSector.setPointsReward(50);
        performanceSectorRepository.save(seatedPerformanceSector);

        Set<PerformanceSector> performanceSectorSet = new HashSet<>();
        performanceSectorSet.add(standingPerformanceSector);
        performanceSectorSet.add(seatedPerformanceSector);

        seatedSector.setPerformanceSectors(performanceSectorSet);
        standingSector.setPerformanceSectors(performanceSectorSet);


        standingSeat = new Seat();
        standingSeat.setSector(standingSector);
        standingSeat.setRow(5);
        standingSeat.setNumber(5);
        seatRepository.save(standingSeat);

        seatedSeat = new Seat();
        seatedSeat.setNumber(1);
        seatedSeat.setRow(1);
        seatedSeat.setSector(seatedSector);
        seatRepository.save(seatedSeat);


        seatedTicket = new Ticket();
        seatedTicket.setSeat(seatedSeat);
        seatedTicket.setPerformance(performance);
        ticketRepository.save(seatedTicket);

        standingTicket = new Ticket();
        standingTicket.setSeat(standingSeat);
        standingTicket.setPerformance(performance);
        ticketRepository.save(standingTicket);

        cartTicketDtoSeated = new CartTicketDto();
        cartTicketDtoStanding = new CartTicketDto();
        cartTicketDtoSeated.setId(seatedSeat.getId());
        cartTicketDtoSeated.setSeatNumber(seatedSeat.getNumber());
        cartTicketDtoSeated.setSeatRow(seatedSeat.getRow());
        cartTicketDtoStanding.setId(standingSeat.getId());
        cartTicketDtoStanding.setSeatNumber(standingSeat.getNumber());
        cartTicketDtoStanding.setSeatRow(standingSeat.getRow());
        cartDto = new CartDto();
        List<CartTicketDto> ticketList = new ArrayList<>();
        ticketList.add(cartTicketDtoSeated);
        ticketList.add(cartTicketDtoStanding);
        cartDto.setUserId(1);
        cartDto.setTickets(ticketList);

        cartDto2 = new CartDto();
        cartDto2.setUserId(999);

        cartDto3 = new CartDto();
        cartDto3.setUserId(9);
    }

    @Test
    public void buyValidTicketsReturnCorrectOrder() {

        OrderDto order = orderService.buyTickets(1);
        Order orderEntity = orderRepository.getOrderById(order.getId());
        System.out.println(orderEntity.getTickets());
        assertThat(order)
            .isNotNull()
            .extracting("id", "tickets", "transactions", "paymentDetail", "deliveryAddress.id", "cancelled")
            .contains(orderEntity.getId(), order.getTickets(), order.getTransactions(), paymentDetail.getId(), location.getId(), false);
    }


    @Test
    public void buyTicketsWithInvalidUserShouldThrow() {
        assertThrows(NotFoundException.class, () -> orderService.buyTickets(99999));
    }


}
