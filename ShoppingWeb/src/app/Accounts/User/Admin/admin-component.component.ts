import { Component, OnInit } from '@angular/core';
import {User} from "../user.model";
import {HttpClient} from "@angular/common/http";

const baseUrl = 'http://localhost:8080/AccountService-1.0-SNAPSHOT/api/accounts';

@Component({
  selector: 'app-admin-component',
  templateUrl: './admin-component.component.html',
  styleUrls: ['./admin-component.component.css']
})
export class AdminComponent {
  accounts: User[] = [];

  constructor(private http: HttpClient) { }

  createSellingFormShown = false;
  createShippingFormShown = false;
  newCompanyName = '';
  newCompanyPassword= '';
  newCompanyType = '';

  createProductSellingCompanies(): void {
    // Code to redirect to a page that creates product selling companies representative accounts
    this.createSellingFormShown = true;

  }

  createShippingCompanies(): void {
    // Code to redirect to a page that creates shipping companies
    this.createShippingFormShown = true;
  }

  // Function to handle form submit
  submitNewSelling() {
    // Use the value of this.newCompanyName to create a new company
    // Reset form and hide it after submitting
    const data = {
        username: this.newCompanyName
    };
    this.newCompanyType = 'Selling_Company';
    console.log("New selling company: " + this.newCompanyName);

    this.newCompanyName = '';
    this.newCompanyType = '';
    this.createSellingFormShown = false;
  }

  submitNewShipping() {
    // Use the value of this.newCompanyName to create a new company
    // Reset form and hide it after submitting
    const data = {
      username: this.newCompanyName,
      password: this.newCompanyPassword,
    };
    this.newCompanyType = 'Shipping_Company';
    console.log("New shipping company: " + this.newCompanyName);

    this.newCompanyName = '';
    this.newCompanyPassword = '';
    this.newCompanyType = '';
    this.createShippingFormShown = false;
  }

  listCustomerAccounts(): void {
    this.http.get<User[]>(`${baseUrl}/type/Customer`).subscribe({
      next: (data: User[]) => {
        this.accounts = data;
        console.log(data);
      },
      error: (error: any) => {
        console.error(error);
      },
      complete: () => {
        console.log('Request completed successfully');
      }
    });
  }

  listShippingCompanies(): void {
    this.http.get<any[]>(`${baseUrl}/type/Shipping_Company`).subscribe({
      next: (data: any[]) => {
        this.accounts = data;
        console.log(data);
      },
      error: (error: any) => {
        console.error(error);
      },
      complete: () => {
        console.log('Request completed successfully');
      }
    });
  }

  listSellingCompaniesRepresentativeAccounts(): void {
    this.http.get<any[]>(`${baseUrl}/type/Selling_Company`).subscribe({
      next: (data: any[]) => {
        this.accounts = data;
        console.log(data);
      },
      error: (error: any) => {
        console.error(error);
      },
      complete: () => {
        console.log('Request completed successfully');
      }
    });
  }
}
