import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { CommonModule } from '@angular/common';
import { provideHttpClient } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { MenuComponent } from './components/menu.component';
import { PlaceOrderComponent } from './components/place-order.component';

import { RouterModule, Routes, provideRouter } from '@angular/router';
import { ConfirmationComponent } from './components/confirmation.component';
import { RestaurantService } from './restaurant.service';

const appRoutes: Routes = [
  { path: '', component: MenuComponent },
  { path: 'place-order', component: PlaceOrderComponent },
  { path: 'confirmation', component: ConfirmationComponent },
  { path: '**', redirectTo: '', pathMatch: 'full' }
];

@NgModule({
  declarations: [
    AppComponent, 
    MenuComponent, 
    PlaceOrderComponent, 
    ConfirmationComponent
  ],
  imports: [
    BrowserModule,
    CommonModule, 
    ReactiveFormsModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    provideHttpClient(
      /* Add withJsonpSupport() if needed */
    ),
    provideRouter(appRoutes),
    RestaurantService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }