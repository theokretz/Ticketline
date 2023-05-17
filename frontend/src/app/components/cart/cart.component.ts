import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';
import { BookingTicket, CartTicket } from '../../dtos/ticket';
import { ToastrService } from 'ngx-toastr';
import { PaymentDetail } from '../../dtos/payment-detail';
import { Booking } from 'src/app/dtos/booking';
import { MatDialog } from '@angular/material/dialog';
import { PaymentDetailComponent } from './payment-detail/payment-detail.component';
import { DeliveryAddressComponent } from './delivery-address/delivery-address.component';
import { BuyComponent } from './buy/buy.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss'],
})
export class CartComponent implements OnInit {
  cartTickets: CartTicket[] = [];
  bannerError: string | null = null;
  paymentDetail1: PaymentDetail = new PaymentDetail(
    1,
    'First Last',
    123456789,
    111,
    new Date('2030-01-01')
  );
  paymentDetails: PaymentDetail[] = [this.paymentDetail1, this.paymentDetail1];

  constructor(
    private service: CartService,
    private authService: AuthService,
    private notification: ToastrService,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.reloadCart();
  }

  reloadCart() {
    this.service.getCartTickets(this.authService.getUserId()).subscribe({
      next: (data) => {
        this.cartTickets = data;
        this.cartTickets.forEach((cartTicket) => {
          cartTicket.reservation = false;
        });
      },
      error: (error) => {
        console.error('Error fetching cart', error);
        this.bannerError = 'Could not fetch cart: ' + error.message;
        const errorMessage =
          error.status === 0 ? 'Is the user logged in?' : error.message.message;
        this.notification.error(errorMessage, 'Could Not Fetch Cart');
      },
    });
  }

  removeTicket(ticketId: number) {
    this.service
      .removeTicketFromCart(this.authService.getUserId(), ticketId)
      .subscribe({
        next: () => {
          this.reloadCart();
        },
        error: (error) => {
          console.error(error.message);
        },
      });
  }

  dateToLocaleDate(cartTicket: CartTicket): string {
    return new Date(cartTicket.date).toLocaleDateString();
  }

  seatToString(cartTicket: CartTicket): string {
    return 'Row: ' + cartTicket.seatRow + ' Seat: ' + cartTicket.seatNumber;
  }

  locationToString(cartTicket: CartTicket) {
    return (
      cartTicket.locationCity +
      ', ' +
      cartTicket.locationStreet +
      '' +
      '\nHall: ' +
      cartTicket.hallName
    );
  }

  checkout() {
    this.service.getCheckoutDetails(this.authService.getUserId()).subscribe({
      next: async (checkoutDetails) => {
        const booking = new Booking();
        // convert cartTickets to bookingTickets
        booking.tickets = this.cartTickets.map((cartTicket) => {
          const bookingTicket = new BookingTicket();
          bookingTicket.ticketId = cartTicket.id;
          bookingTicket.reservation = cartTicket.reservation;
          return bookingTicket;
        });
        try {
          const paymentDetailId = await this.openPaymentDialog(
            checkoutDetails.paymentDetails
          );
          booking.paymentDetailId = paymentDetailId;

          const locationId = await this.openLocationDialog(
            checkoutDetails.locations
          );
          booking.locationId = locationId;

          const buy = await this.openBuyDialog();
          if (buy) {
            this.service.buy(booking).subscribe({
              next: () => {
                this.notification.success('Successfully booked tickets');
                this.router.navigate(['/']);
              },
              error: (err) => {
                console.error(err);
                this.notification.error(
                  err.error.detail,
                  'Could Not Book Tickets'
                );
                this.reloadCart();
              },
            });
          }
        } catch (err) {
          console.error(err);
          this.notification.error(
            err.error.detail,
            'Could Not Fetch Checkout Details'
          );
        }
      },
      error: (err) => {
        console.error(err);
        this.notification.error(
          err.error.detail,
          'Could Not Fetch Checkout Details'
        );
      },
    });
  }

  openPaymentDialog(paymentDetails): Promise<number> {
    return new Promise((resolve, reject) => {
      const paymentRef = this.dialog.open(PaymentDetailComponent, {
        width: '25%',
        data: { paymentDetails },
      });

      paymentRef.componentInstance.paymentSelector.subscribe(
        (paymentDetailId: number) => {
          paymentRef.close();
          resolve(paymentDetailId);
        }
      );
    });
  }

  openLocationDialog(locations): Promise<number> {
    return new Promise((resolve, reject) => {
      const locationRef = this.dialog.open(DeliveryAddressComponent, {
        width: '25%',
        data: { locations },
      });

      locationRef.componentInstance.locationSelector.subscribe(
        (locationId: number) => {
          locationRef.close();
          resolve(locationId);
        }
      );
    });
  }

  openBuyDialog(): Promise<boolean> {
    return new Promise((resolve, reject) => {
      const buyRef = this.dialog.open(BuyComponent, {
        width: '25%',
      });

      buyRef.componentInstance.buyEmitter.subscribe((buy: boolean) => {
        buyRef.close();
        resolve(buy);
      });
    });
  }
}
