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
  ShippingCompany ={
    newCompanyName: '',
    newCompanyPassword: ''
  }

  // only 1 form can be shown at a time
  shown = false;

  createProductSellingCompanies(): void {
    this.getUnassinedSellingCompanyNames();
    this.createSellingFormShown = !this.createSellingFormShown;
    this.createShippingFormShown = false;
    this.submitForm = false;
    this.shown = false;
  }

  createShippingCompanies(): void {
    this.createShippingFormShown = !this.createShippingFormShown;
    this.createSellingFormShown = false;
    this.submitForm = false;
    this.shown = false;
  }

  // Function to handle form submit
  createdCompanyCredentials: any = {
    username: '',
    password: ''
  };
  submitNewSelling() {
    console.log("New selling company: " + this.selectedCompanyName);

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
      name: this.ShippingCompany.newCompanyName,
      password: this.ShippingCompany.newCompanyPassword,
    };
    console.log("New shipping company: " + data.name + " " + data.password);

    const sendData = data.name + "," + data.password;
    const companyName = data.name;

    this.http.post(`${adminUrl}/add_shipping_company`, sendData).subscribe({
      next: (data: any) => {
        this.createShippingFormShown = false;
      },
      error: (error: any) => {
        console.error(error);
      },
      complete: () => {
        console.log('Request completed successfully');
        this.submitForm=true;
        this.loadCredentials(companyName);
      }
    });

    this.ShippingCompany.newCompanyName = '';
    this.ShippingCompany.newCompanyPassword = '';
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
    this.shown = !this.shown;
    this.createShippingFormShown = false;
    this.createSellingFormShown = false;
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
    this.shown = !this.shown;
    this.createShippingFormShown = false;
    this.createSellingFormShown = false;
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
    this.shown = !this.shown;
    this.createShippingFormShown = false;
    this.createSellingFormShown = false;
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
    this.http.get<String[]>(`${adminUrl}/get_credentials/${companyName}`).subscribe({
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
