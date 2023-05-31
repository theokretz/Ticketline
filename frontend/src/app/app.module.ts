import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { MessageComponent } from './components/message/message.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { httpInterceptorProviders } from './interceptors';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PerformanceComponent } from './components/performance/performance.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { CartComponent } from './components/cart/cart.component';
import { ReservationComponent } from './components/reservation/reservation.component';
import { OrderHistoryComponent } from './components/order-history/order-history.component';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { PaymentDetailComponent } from './components/cart/payment-detail/payment-detail.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
import { DeliveryAddressComponent } from './components/cart/delivery-address/delivery-address.component';
import { BuyComponent } from './components/cart/buy/buy.component';
import { MerchandiseComponent } from './components/merchandise/merchandise.component';
import { CookieService } from 'ngx-cookie-service';
import { MerchandiseEventComponent } from './components/merchandise/merchandise-event/merchandise-event.component';
import { RegisterComponent } from './components/register/register.component';
import { SearchEventByArtistComponent } from './components/search/search-event-by-artist/search-event-by-artist.component';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import { EventDetailsComponent } from './components/event-details/event-details.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    MessageComponent,
    PerformanceComponent,
    PageNotFoundComponent,
    CartComponent,
    PaymentDetailComponent,
    ReservationComponent,
    OrderHistoryComponent,
    DeliveryAddressComponent,
    BuyComponent,
    MerchandiseComponent,
    MerchandiseComponent,
    MerchandiseEventComponent,
    RegisterComponent,
    SearchEventByArtistComponent,
    EventDetailsComponent,
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        ReactiveFormsModule,
        HttpClientModule,
        //Needed for Toastr
        ToastrModule.forRoot({
            timeOut: 3000,
            extendedTimeOut: 1000,
            closeButton: true,
            progressBar: true,
            tapToDismiss: false,
        }),
        BrowserAnimationsModule,
        ReactiveFormsModule,
        //-------
        NgbModule,
        FormsModule,
        //Needed for MatDialog
        MatDialogModule,
        MatTableModule,
        MatCardModule,
        MatButtonModule,
        MatInputModule,
        MatIconModule,
        MatToolbarModule,
        MatAutocompleteModule,
        MatButtonToggleModule,
    ],
  providers: [httpInterceptorProviders, CookieService],
  bootstrap: [AppComponent],
})
export class AppModule {}
