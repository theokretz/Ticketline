import {Component, OnInit} from '@angular/core';
import {Order} from '../../dtos/order';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrls: ['./order-history.component.scss']
})
export class OrderHistoryComponent implements OnInit {
  orders: Order[];

  constructor(
    private service: UserService,
  ) {
  }

  ngOnInit(): void {
    this.reloadOrders();
  }
  reloadOrders(): void {
    // TODO: get user id
    this.service.getOrders(20).subscribe({
      next: data => {
        this.orders = data;
      },
      error: error => {
        console.error('Could not fetch orders');
      }
    });
  }

  dateToString(input: Date): string {
    const date = new Date(input);
    return '' + date.toLocaleDateString(
      'en-GB',
      {weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric'
      });
  }

  totalPrice(order: Order): number {
    return order.totalPrice;
  }

}
