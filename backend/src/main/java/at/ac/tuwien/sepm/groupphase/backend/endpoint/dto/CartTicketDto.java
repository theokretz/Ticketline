package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


public class CartTicketDto {

    SeatDto seat;
    PerformanceDto performance;

    public void setPerformance(PerformanceDto performance) {
        this.performance = performance;
    }

    public PerformanceDto getPerformance() {
        return performance;
    }

    public void setSeat(SeatDto seat) {
        this.seat = seat;
    }

    public SeatDto getSeat() {
        return seat;
    }
}
