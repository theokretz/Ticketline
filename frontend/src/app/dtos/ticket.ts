import {SimpleSeat} from './seat';

export class PerformanceTicket {
  ticketId: number;
  reserved: boolean;
  row: number;
  number: number;
  sectorId: number;
}

export class SimpleTicket {
  id: number;
}

export class ReservedTicket {
  id: number;
  seat: SimpleSeat;
}
