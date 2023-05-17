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
    this.service.getOrders(1).subscribe({
      next: data => {
        this.orders = data;
      },
      error: error => {
        console.error('Could not fetch orders');
      }
    });
  }

  dateToString(order: Order): string {
    const date = new Date(order.orderTs);
    return 'Ordered on ' + date.toDateString();
  }

  totalPrice(order: Order): number {
    let totalPrice = 0;
    for (const ticket of order.tickets) {
      totalPrice += ticket.price;
    }
    return totalPrice;
  }
}
