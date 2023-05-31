import {OrderTicket} from './ticket';
import {OrderMerchandise} from './merchandise';

export class Order {
  id: number;
  orderTs: Date;
  cancelled: boolean;
  tickets: OrderTicket[];
  merchandises: OrderMerchandise[];
  totalPrice: number;
  totalPoints: number;
}
