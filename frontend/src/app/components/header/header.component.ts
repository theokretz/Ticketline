import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  userId: number;
  constructor(public authService: AuthService) { }

  ngOnInit() {
    this.userId = this.authService.getUserId();
  }

}
