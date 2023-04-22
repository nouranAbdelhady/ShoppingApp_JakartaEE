import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerComponent } from './User/Customer/customer-component.component';
import { RegisterComponent } from "./Views/Register/register.component";
import { AdminComponent } from "./User/Admin/admin-component.component";
import { LoginComponent } from "./Views/Login/login.component";
import { ConfirmComponent } from "./Views/Register/confirmation/confirm.component";

const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'confirm', component: ConfirmComponent },
  { path: 'customer', component: CustomerComponent },
  { path: 'admin', component: AdminComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
