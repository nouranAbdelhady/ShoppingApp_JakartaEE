import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {HomepageComponent} from "../../Views/Homepage/homepage.compoent";

const baseUrl = 'http://localhost:8080/ShippingService-1.0-SNAPSHOT/api/shipping_company';
@Component({
  selector: 'app-shipping-company-component',
  templateUrl: './shipping-component.component.html',
  styleUrls: ['./shipping-company-component.component.css']
})
export class ShippingCompanyComponent implements OnInit{
  companyUsername: string | undefined = '';
  regions: String[] = [];
  showForm = false;

  ngOnInit() {
    this.companyUsername = this.homepage.loggedInUser.username;
    console.log("Shipping Company init")
    console.log("Company username: "+this.companyUsername);
  }

  constructor(private http: HttpClient, private homepage : HomepageComponent) { }


  // To show/hide forms
  createRegionFormShown = false;
  newRegionName: String = '';
  addRegion() {
    this.createRegionFormShown = !this.createRegionFormShown;
    this.showForm = false;
  }
  submitRegionForm() {
    console.log("New region name: " + this.newRegionName);
    this.http.post(`${baseUrl}/${this.companyUsername}/regions`, this.newRegionName).subscribe({
      next: (data: any) => {
        this.createRegionFormShown = false;
      },
      error: (error: any) => {
        console.error(error);
      },
      complete: () => {
        console.log('Request completed successfully');
      }
    });
  }
  viewRegions() {
    this.http.get<any[]>(`${baseUrl}/${this.companyUsername}/regions`).subscribe({
      next: (data: String[]) => {
        this.regions = data;
        console.log(data);
      },
      error: (error: any) => {
        console.error(error);
      },
      complete: () => {
        console.log('Request completed successfully');
      }
    });
    console.log("Regions: "+this.regions);
    this.showForm = !this.showForm;
    this.createRegionFormShown = false;
  }

  viewShippingRequests() {

  }

  viewAcceptedRequests(){

  }


}
