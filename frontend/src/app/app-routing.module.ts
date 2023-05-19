import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from './guards/auth.guard';
import { MessageComponent } from './components/message/message.component';
import { PerformanceComponent } from './components/performance/performance.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { OrderHistoryComponent } from './components/order-history/order-history.component';
import { ReservationComponent } from './components/reservation/reservation.component';
import { CartComponent } from './components/cart/cart.component';
import { MerchandiseComponent } from './components/merchandise/merchandise.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'message', canActivate: [AuthGuard], component: MessageComponent },
  { path: 'performances/:id', component: PerformanceComponent },
  { path: 'reservations', component: ReservationComponent },
  { path: 'order-history', component: OrderHistoryComponent },
  { path: 'merchandise', component: MerchandiseComponent },
  { path: 'cart', component: CartComponent },
  { path: '**', component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
