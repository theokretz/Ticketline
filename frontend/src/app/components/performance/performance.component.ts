import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Performance } from '../../dtos/performance';
import { PerformanceTicket, SimpleTicket } from '../../dtos/ticket';
import { PerformanceService } from '../../services/performance.service';
import { UserService } from '../../services/user.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-performance',
  templateUrl: './performance.component.html',
  styleUrls: ['./performance.component.scss'],
})
export class PerformanceComponent implements OnInit {
  performance: Performance = null;
  selectedTickets: PerformanceTicket[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private performanceService: PerformanceService,
    private userService: UserService,
    private notification: ToastrService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.performance = null;
      this.fetchPerformance(params['id']);
    });
  }

  fetchPerformance(id: number): void {
    this.performanceService.getPerformanceById(id).subscribe({
      next: (res) => (this.performance = res),
      error: (err) => {
        if (err.status === 404) {
          this.router.navigate(['/404']);
        } else {
          this.notification.error(
            err.error.detail,
            'Failed loading performance'
          );
          console.log(err);
        }
      },
    });
  }

  generateGridStyles(): { [key: string]: string } {
    if (!this.performance) {
      return {};
    }
    const cols = this.performance.tickets[0].length;
    const templateColumns = `repeat(${cols}, 1.5rem)`;

    return {
      // eslint-disable-next-line @typescript-eslint/naming-convention
      'grid-template-columns': templateColumns,
    };
  }

  toggleTicket(ticket: PerformanceTicket): void {
    if (ticket === null || ticket.reserved) {
      return;
    }

    if (this.isTicketSelected(ticket)) {
      this.selectedTickets = this.selectedTickets.filter((t) => t !== ticket);
    } else if (this.selectedTickets.length < 6) {
      this.selectedTickets.push(ticket);
    }
  }

  isTicketSelected(ticket: PerformanceTicket): boolean {
    return this.selectedTickets.includes(ticket);
  }

  totalTicketPrice(): number {
    let total = 0;
    for (const ticket of this.selectedTickets) {
      total += this.performance.performanceSector[ticket.sectorId].price;
    }
    return total;
  }

  getSectorName(ticket: PerformanceTicket): string {
    return this.performance.performanceSector[ticket.sectorId].name;
  }

  getPrice(ticket: PerformanceTicket): number {
    return this.performance.performanceSector[ticket.sectorId].price;
  }

  clearSelectedTickets(): void {
    this.selectedTickets = [];
  }

  addTicketsToCart(): void {
    const ticketIds: SimpleTicket[] = this.selectedTickets.map((t) => ({
      id: t.ticketId,
    }));
    this.userService
      .addTicketsToCart(
        1, // TODO: replace with actual user id
        ticketIds
      )
      .subscribe({
        next: () => {
          this.notification.success('Items added to cart');
          this.router.navigate(['/cart']);
        },
        error: (err) => {
          this.notification.error(
            err.error.detail,
            'Failed to add items to cart'
          );
          console.log(err);
        },
      });
  }
}
