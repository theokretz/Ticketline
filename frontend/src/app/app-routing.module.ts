import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {NewsComponent} from './components/news/news.component';
import {PerformanceComponent} from './components/performance/performance.component';
import {PageNotFoundComponent} from './components/page-not-found/page-not-found.component';
import {OrderHistoryComponent} from './components/order-history/order-history.component';
import {ReservationComponent} from './components/reservation/reservation.component';
import {CartComponent} from './components/cart/cart.component';
import {MerchandiseComponent} from './components/merchandise/merchandise.component';
import {RegisterComponent} from './components/register/register.component';
import {NewsDetailedComponent} from './components/news/news-detailed/news-detailed.component';
import {
  SearchEventByArtistComponent
} from './components/search/search-event-by-artist/search-event-by-artist.component';
import {EventDetailsComponent} from './components/event-details/event-details.component';
import {ForgotPasswordComponent} from './components/login/forgot-password/forgot-password.component';
import {ResetComponent} from './components/reset/reset.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'news', canActivate: [AuthGuard], component: NewsComponent},
  {path: 'news/:id', component: NewsDetailedComponent},
  {path: 'forgot-password', component: ForgotPasswordComponent},
  {path: 'reset/:token', component: ResetComponent},
  {path: 'performances/:id', component: PerformanceComponent},
  {path: 'reservations', component: ReservationComponent},
  {path: 'order-history', component: OrderHistoryComponent},
  {path: 'merchandise', component: MerchandiseComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'cart', component: CartComponent},
  {
    path: 'search', children: [
      {path: 'artists', component: SearchEventByArtistComponent},
    ]
  },
  {path: ':id/event', component: EventDetailsComponent},
  {path: '**', component: PageNotFoundComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
