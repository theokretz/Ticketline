package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MerchandiseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingMerchandiseDto;

import java.util.List;

public interface MerchandiseService {


    List<MerchandiseDto> getMerchandise(boolean withPoints);

    //List<MerchandiseDto> filterMerchandise(MerchandiseFilterDto filterParams);

    List<MerchandiseDto> getInfoFromBooking(List<BookingMerchandiseDto> bookingMerchandises);
}
