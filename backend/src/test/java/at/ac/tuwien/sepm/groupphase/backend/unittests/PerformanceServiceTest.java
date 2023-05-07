package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartSeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PaymentDetailRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class PerformanceServiceTest {


    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private PerformanceService performanceService;

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

    private Performance performance;
    private ApplicationUser user;
    private Location location;
    private Set<Location> locationSet;
    private PaymentDetail paymentDetail;
    private Set<PaymentDetail> paymentDetailSet;
    private UserDto userDto;

    @BeforeAll
    public void beforeAll() {
        Event event = new Event();
        event.setName("The Eras Tour");
        event.setLength(Duration.ZERO);
        eventRepository.save(event);

        this.location = new Location();
        location.setCity("Vienna");
        location.setCountry("Austria");
        location.setPostalCode(1120);
        location.setStreet("Straße 1");
        locationSet = new HashSet<>();
        locationSet.add(location);
        locationRepository.save(location);

        Hall hall = new Hall();
        hall.setName("Halle 1");
        hall.setLocation(location);
        hallRepository.save(hall);

        this.performance = new Performance();
        this.performance.setDatetime(LocalDateTime.now());
        this.performance.setEvent(event);
        this.performance.setHall(hall);
        performanceRepository.save(performance);

        this.user = new ApplicationUser();
        this.user.setEmail("hallo@123");
        user.setAdmin(false);
        user.setFirstName("Theo");
        user.setLastName("Kretz");
        user.setPassword("Password");
        user.setLocked(false);
        user.setSalt("asdjaslkdjaös");
        user.setPoints(10000);

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
    }

    @Test
    public void buyValidTicketsReturnCorrectOrder() {

        CartSeatDto cartSeatDto = new CartSeatDto();
        cartSeatDto.setRow(1);
        cartSeatDto.setNumber(1);
        CartDto cartDto = new CartDto();
        List<CartSeatDto> seatList = new ArrayList<>();
        seatList.add(cartSeatDto);
        cartDto.setSeats(seatList);
        cartDto.setStanding(1);


        OrderDto order = performanceService.buyTickets(cartDto, performance.getId(), userDto);
        assertThat(order)
            .isNotNull()
            .extracting("id", "tickets", "transactions", "paymentDetail", "deliveryAdress", "cancelled")
            .contains(order.getId(), order.getTickets(), order.getTransactions(), paymentDetail, location, false);
    }
}
