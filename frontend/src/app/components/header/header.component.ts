import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {CartService} from '../../services/cart.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  userId: number;
  userPoints: number;
  bannerError: string | null = null;
  gotPoints = false;

  constructor(public authService: AuthService,
              private service: CartService,
              private notification: ToastrService) {}
  ngOnInit() {}
  getPoints(): number {
    if (this.authService.isLoggedIn() && !this.gotPoints) {
      this.userId = this.authService.getUserId();

      this.service.getCartPoints(this.userId).subscribe({
        next: (data) => {
          this.userPoints = data;
        },
        error: (error) => {
            this.bannerError = error;
            this.notification.error(error.error.details, 'Error while fetching points');
        }
      });
    }
    this.gotPoints = true;
    return this.userPoints;
  }
}

