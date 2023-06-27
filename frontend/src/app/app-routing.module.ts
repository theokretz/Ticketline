import {Injectable, NgModule} from '@angular/core';
import {CanActivate, Router, RouterModule, Routes} from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from './guards/auth.guard';
import { NewsComponent } from './components/news/news.component';
import { PerformanceComponent } from './components/performance/performance.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { OrderHistoryComponent } from './components/order-history/order-history.component';
import { ReservationComponent } from './components/reservation/reservation.component';
import { CartComponent } from './components/cart/cart.component';
import { MerchandiseComponent } from './components/merchandise/merchandise.component';
import { RegisterComponent } from './components/register/register.component';
import { NewsDetailedComponent } from './components/news/news-detailed/news-detailed.component';
import { SearchEventByArtistComponent } from './components/search/search-event-by-artist/search-event-by-artist.component';
import { EventDetailsComponent } from './components/event-details/event-details.component';
import { ForgotPasswordComponent } from './components/login/forgot-password/forgot-password.component';
import { ResetComponent } from './components/reset/reset.component';
import { OrderDetailedComponent } from './components/order-detailed/order-detailed.component';
import {ProfileComponent} from './components/profile/profile.component';
import {EditProfileComponent} from './components/profile/edit-profile/edit-profile.component';
import {
  SearchPerformancesByLocationComponent
} from './components/search/search-performances-by-location/search-performances-by-location.component';
import {
  PerformanceOnLocationComponent
} from './components/performance/performance-on-location/performance-on-location.component';
import {TopTenComponent} from './components/top-ten/top-ten.component';
import {SearchEventsComponent} from './components/search/search-events/search-events.component';
import {AdminViewComponent} from './components/admin-view/admin-view.component';
import {AuthService} from './services/auth.service';
import {EditPasswordComponent} from './components/profile/edit-password/edit-password.component';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(private authService: AuthService,
              private router: Router) {}

  canActivate(): boolean {
    if (this.authService.getUserRole() === 'ADMIN') {
      return true;
    } else {
      this.router.navigate(['/']);
      return false;
    }
  }
}

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  {path: 'edit-profile', component: EditProfileComponent},
  {path: 'profile', component: ProfileComponent},
  { path: 'edit-password', component: EditPasswordComponent },
  { path: 'news', canActivate: [AuthGuard], component: NewsComponent },
  { path: 'news/:id', component: NewsDetailedComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'reset/:token', component: ResetComponent },
  { path: 'performances/:id', component: PerformanceComponent },
  { path: 'performances/location/:id', component: PerformanceOnLocationComponent},
  { path: 'reservations', component: ReservationComponent },
  { path: 'order-history', component: OrderHistoryComponent },
  { path: 'merchandise', component: MerchandiseComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'cart', component: CartComponent },
  { path: 'top-ten', component: TopTenComponent},
  {
    path: 'search', children: [
      { path: 'artists', component: SearchEventByArtistComponent },
      { path: 'locations', component: SearchPerformancesByLocationComponent },
      { path: 'events', component: SearchEventsComponent },
    ],
  },
  { path: ':id/event', component: EventDetailsComponent },
  { path: 'orders/:id', component: OrderDetailedComponent },
  { path: 'admin', component: AdminViewComponent, canActivate: [RoleGuard] },
  { path: 'admin/users/register', component: RegisterComponent, data: { isAdmin: true }, canActivate: [RoleGuard] },
  { path: '**', component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
