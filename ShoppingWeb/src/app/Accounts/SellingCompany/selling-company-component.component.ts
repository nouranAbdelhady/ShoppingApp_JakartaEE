import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {HomepageComponent} from "../../Views/Homepage/homepage.compoent";

const baseUrl = 'http://localhost:9314/ProductService-1.0-SNAPSHOT/api/selling_company';
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

  buttonClicked = false;
  viewProductsOnSale() {
    this.buttonClicked = !this.buttonClicked;
    this.createNewProductForm = false;
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
    this.buttonClicked = !this.buttonClicked;
    this.createNewProductForm = false;
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

  createNewProductForm=false
  newProductName = '';
  newProductDescription = '';
  newProductPrice = 0;
  newProductQuantity = 0;
  addNewProduct() {
    this.createNewProductForm = !this.createNewProductForm;
    this.buttonClicked = false;
  }

  submitNewProduct() {
    // Use the value of this.newCompanyName to create a new company
    // Reset form and hide it after submitting
    const data = {
      name: this.newProductName,
      description: this.newProductDescription,
      price: this.newProductPrice,
      quantity: this.newProductQuantity,
    };
    console.log("New product: " + data.name + " " + data.description + " " + data.price + " " + data.quantity);
    console.log("Company username: "+this.companyUsername);

    this.http.post(`${baseUrl}/${this.companyUsername}/products`, data).subscribe({
      next: (data: any) => {
        this.createNewProductForm = false;
      },
      error: (error: any) => {
        console.error(error);
      },
      complete: () => {
        console.log('Request completed successfully');
      }
    });
    this.newProductName = '';
    this.newProductDescription = '';
    this.newProductPrice = 0;
    this.newProductQuantity = 0;
  }

  deleteProduct(productId: number) {
    console.log("Deleting product: " + productId);
    this.http.delete(`${baseUrl}/${this.companyUsername}/products/${productId}`).subscribe({
      next: (data: any) => {
        console.log(data);
        this.products = this.products.filter(product => product.id !== productId);
      },
      error: (error: any) => {
        console.error(error);
      },
      complete: () => {
        console.log('Delete completed successfully');
      }
    });
  }



}
