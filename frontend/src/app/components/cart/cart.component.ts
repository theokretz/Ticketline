import {Component, OnInit} from '@angular/core';
import {CartService} from '../../services/cart.service';
import {AuthService} from '../../services/auth.service';
import {CartTicket} from '../../dtos/cart-ticket';
import {ToastrService} from 'ngx-toastr';
import {Cart} from '../../dtos/cart';
import {PaymentDetail} from '../../dtos/payment-detail';
import {Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {PaymentDetailComponent} from '../payment-detail/payment-detail.component';


@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  cartTickets: CartTicket[] = [];
  cart: Cart = new Cart();
  bannerError: string | null = null;
  paymentDetail1: PaymentDetail = new PaymentDetail(
    1, 'First Last', 123456789, 111, new Date('2030-01-01'));
  paymentDetails: PaymentDetail[] = [
    this.paymentDetail1
  ];

  constructor(
    private service: CartService,
    private authService: AuthService,
    private notification: ToastrService,
    private router: Router,
    private dialog: MatDialog,
  ) {
  }

  ngOnInit(): void {
    this.reloadCart();
  }

  reloadCart() {
    this.service.getCartTickets(1)
      .subscribe({
        next: data => {
          this.cartTickets = data;
          this.cart.setUserId(1);
          this.cart.setCartTickets(data);
        },
        error: error => {
          console.error('Error fetching cart', error);
          this.bannerError = 'Could not fetch cart: ' + error.message;
          const errorMessage = error.status === 0
            ? 'Is the user logged in?'
            : error.message.message;
          this.notification.error(errorMessage, 'Could Not Fetch Cart');
        }
      });
  }

  removeTicket(id: number) {
    this.service.removeTicketFromCart(this.authService.getUserId(), id).subscribe({
      next: data => {
        this.reloadCart();
      },
      error: error => {
        console.error(error.message);
        error.error.message('Failed to remove ticket: ' + error.error.message);
      }
    });
  }

  dateToLocaleDate(cartTicket: CartTicket): string {
    return new Date(cartTicket.date).toLocaleDateString();
  }

  seatToString(cartTicket: CartTicket): string {
    return 'Row: ' + cartTicket.seatRow + ' Seat: ' + cartTicket.seatNumber;
  }

  locationToString(cartTicket: CartTicket) {
    return cartTicket.locationCity + ', ' + cartTicket.locationStreet + '' + '\nHall: ' + cartTicket.hallName;
  }

  checkout() {
    const dialogRef = this.dialog.open(PaymentDetailComponent, {
      width: '50%',
      data: {paymentDetails: this.paymentDetails},
    });
    dialogRef.afterClosed().subscribe(() => {
      this.service.buy(1).subscribe({
        next: () => {
          this.router.navigate(['']);
        },
        error: error => {
          console.error(error.message);
          error.error.message('Failed to buy tickets: ' + error.error.message);
        },
        complete: () => {

        }
      });
    });
  }
}
