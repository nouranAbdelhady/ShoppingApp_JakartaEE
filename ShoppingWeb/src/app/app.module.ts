import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {CustomerComponent} from './Accounts/User/Customer/customer-component.component';
import {AdminComponent} from "./Accounts/User/Admin/admin-component.component";
import {RegisterComponent} from "./Views/Register/register.component";
import {LoginComponent} from "./Views/Login/login.component";
import {FormsModule} from "@angular/forms";
import {ConfirmComponent} from "./Views/Register/confirmation/confirm.component";
import {HomepageComponent} from "./Views/Homepage/homepage.compoent";
import {NoneComponent} from "./Views/None/none-component";
import {SellingCompanyComponent} from "./Accounts/SellingCompany/selling-company-component.component";
import {ShippingCompanyComponent} from "./Accounts/ShippingCompany/shipping-company-component.component";
import {ProfileComponent} from "./Views/Profile/profile.compoent";

@NgModule({
  declarations: [
    AppComponent,
    CustomerComponent,
    AdminComponent,
    RegisterComponent,
    ConfirmComponent,
    LoginComponent,
    HomepageComponent,
    ProfileComponent,
    NoneComponent,
    SellingCompanyComponent,
    ShippingCompanyComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
