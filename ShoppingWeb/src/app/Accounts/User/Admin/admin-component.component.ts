import {Component, OnInit} from '@angular/core';
import {User} from "../user.model";
import {HttpClient} from "@angular/common/http";

const baseUrl = 'http://localhost:8080/AccountService-1.0-SNAPSHOT/api/accounts';
const adminUrl = 'http://localhost:8080/UserService-1.0-SNAPSHOT/api/admin';

@Component({
  selector: 'app-admin-component',
  templateUrl: './admin-component.component.html',
  styleUrls: ['./admin-component.component.css']
})
export class AdminComponent {
  accounts: User[] = [];
  unassignedCompanyNames: String[] = [];

  constructor(private http: HttpClient) {
  }

  // To show/hide forms
  createSellingFormShown = false;
  createShippingFormShown = false;

  // To view credentials after creating a company
  submitForm: any = null

  // Selling company form
  selectedCompanyName = '';

  // Shipping company form
  newCompanyName = '';
  newCompanyPassword = '';

  createProductSellingCompanies(): void {
    this.getUnassinedSellingCompanyNames();
    this.createSellingFormShown = true;
    this.submitForm = false;
  }

  createShippingCompanies(): void {
    this.createShippingFormShown = true;
    this.submitForm = false;
  }

  // Function to handle form submit
  createdCompanyCredentials: any = {
    username: '',
    password: ''
  };
  submitNewSelling() {
    console.log("New selling company: " + this.selectedCompanyName);
    newCompanyName: this.selectedCompanyName;

    this.http.post(`${adminUrl}/add_selling_company`, this.selectedCompanyName).subscribe({
      next: (data: any) => {
        this.createSellingFormShown = false;
      },
      error: (error: any) => {
        console.error(error);
      },
      complete: () => {
        console.log('Request completed successfully');
        this.submitForm=true;
        this.loadCredentials(this.selectedCompanyName);
      }
    });
    this.createSellingFormShown = false;
  }


  submitNewShipping() {
    // Use the value of this.newCompanyName to create a new company
    // Reset form and hide it after submitting
    const data = {
      username: this.newCompanyName,
      password: this.newCompanyPassword,
    };
    console.log("New shipping company: " + this.newCompanyName);

    this.newCompanyName = '';
    this.newCompanyPassword = '';
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
    this.submitForm = false;
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
    this.submitForm = false;
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
    this.submitForm = false;
  }

  getUnassinedSellingCompanyNames() {
    this.http.get<String[]>(`${adminUrl}/selling_company/unassigned_names`).subscribe({
      next: (data: String[]) => {
        this.unassignedCompanyNames = data;
        console.log(data);
      },
      error: (error: any) => {
        console.error(error);
      },
      complete: () => {
        console.log('Request (unassigned names) completed successfully');
      }
    });
  }

  loadCredentials(companyName: String) {
    console.log("Load credentials for: " + companyName);
    this.http.get<String[]>(`${adminUrl}/get_selling_company_credentials/${companyName}`).subscribe({
      next: (data: String[]) => {
        console.log("Credentials: "+data);
        console.log("Username: "+data[0]);
        console.log("Password: "+data[1]);
        this.createdCompanyCredentials.username = data[0];
        this.createdCompanyCredentials.password = data[1];
        console.log("Credentials 2: "+this.createdCompanyCredentials);
        console.log("Username2: "+this.createdCompanyCredentials.username);
        console.log("Password2: "+this.createdCompanyCredentials.password);
      },
      error: (error: any) => {
        console.error(error);
      },
      complete: () => {
        console.log('Request (unassigned names) completed successfully');
      }
    });
  }
}
