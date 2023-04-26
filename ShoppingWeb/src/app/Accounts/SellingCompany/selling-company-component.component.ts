import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {HomepageComponent} from "../../Views/Homepage/homepage.compoent";

const baseUrl = 'http://localhost:8080/ProductService-1.0-SNAPSHOT/api/selling_company';
@Component({
  selector: 'app-selling-company-component',
  templateUrl: './selling-component.component.html',
  styleUrls: ['./selling-company-component.component.css']
})
export class SellingCompanyComponent implements OnInit{
  products: any[] = [];
  companyUsername: string | undefined = '';

  ngOnInit() {
    this.companyUsername = this.homepage.loggedInUser.username;
    console.log("Selling Company init")
    console.log("Company username: "+this.companyUsername);
  }

  constructor(private http: HttpClient, private homepage : HomepageComponent) { }

  viewProductsOnSale() {
    this.http.get<any[]>(`${baseUrl}/${this.companyUsername}/products/state/available`).subscribe({
      next: (data: any[]) => {
        this.products = data;
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

  viewPreviouslySold() {
    this.http.get<any[]>(`${baseUrl}/${this.companyUsername}/products/state/sold`).subscribe({
      next: (data: any[]) => {
        this.products = data;
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

  addNewProduct() {

  }


}
