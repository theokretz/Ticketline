import {OrderTicket} from './ticket';

export class Order {
  id: number;
  orderTs: Date;
  cancelled: boolean;
  tickets: OrderTicket[];
}
