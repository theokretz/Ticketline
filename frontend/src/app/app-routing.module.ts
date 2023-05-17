import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {MessageComponent} from './components/message/message.component';
import { PerformanceComponent } from './components/performance/performance.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import {CartComponent} from './components/cart/cart.component';
import {ReservationComponent} from './components/reservation/reservation.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  { path: 'message', canActivate: [AuthGuard], component: MessageComponent },
  {
    path: 'performance', children: [
      { path: '', component: PageNotFoundComponent},
      { path: ':id', component: PerformanceComponent },
    ]
  },
  {path: 'reservation', component: ReservationComponent},
  {path: 'cart', component: CartComponent},
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
