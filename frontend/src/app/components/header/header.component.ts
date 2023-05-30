import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {CartService} from '../../services/cart.service';
import {ToastrService} from 'ngx-toastr';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  userId: number;
  userPoints: Observable<number>;
  bannerError: string | null = null;
  constructor(public authService: AuthService, private service: CartService,
              private notification: ToastrService, private cartService: CartService) {
  }

  ngOnInit() {
    this.userId = this.authService.getUserId();
    this.userPoints  = this.cartService.getCartPoints(this.userId);
  }
}
