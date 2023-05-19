import {Component, OnInit} from '@angular/core';
import {DetailedReservation} from '../../dtos/reservation';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {ToastrService} from 'ngx-toastr';
import {ReservationService} from '../../services/reservation.service';
import {SimpleTicket} from '../../dtos/ticket';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.scss']
})
export class ReservationComponent implements OnInit{
  reservations: DetailedReservation[] = [];
  tickets: SimpleTicket[] = [];
  userId = 1;
  public clickedReservations = new Map<number, boolean>();


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private reservationService: ReservationService,
    private notification: ToastrService
  ) { }

  getReservations(): DetailedReservation[] {
    return this.reservations;
  }

  ngOnInit(): void {
    this.route.params.subscribe(() => {
      this.fetchReservations();
    });
  }

  fetchReservations(): void {
    this.reservations = [];
    this.reservationService
      .getReservationsById(this.userId).subscribe({
      next: (res) => {
        this.reservations = res;
        console.log(res);
      },
      error: (err) => {
        if (err.status === 404) {
          this.router.navigate(['/404']);
        } else {
          this.notification.error(
            err.error.detail,
            'Failed loading reservations'
          );
        }
      },
    });
  }

  public addTicket(reservation: DetailedReservation): void {
    this.clickedReservations.set(reservation.id, true);
    // Rest of your code to handle adding the ticket
    const ticket: SimpleTicket = {id: reservation.ticket.id };
    this.tickets.push(ticket);
  }

  removeTicket(reservation: DetailedReservation): void {
    this.clickedReservations.set(reservation.id, false);
    const index = this.tickets.findIndex(ticket => ticket.id === reservation.ticket.id);
    if (index !== -1) {
      this.tickets.splice(index, 1);
    }
  }

  addTicketsToCart(): void {
    console.log('TEEESSSST:::');
    console.log(this.userId);
    console.log(this.tickets);
    this.userService
      .addTicketsToCart(
        this.userId,
        this.tickets
      )
      .subscribe({
        next: () => {
          this.notification.success('Reservations added to cart');
          this.router.navigate(['/cart']);
        },
        error: (err) => {
          this.notification.error(
            err.error.detail,
            'Failed to add reservations to cart'
          );
          this.fetchReservations();
        },
      });
  }


  test(): void {
    console.log(this.reservations);
  }
}
