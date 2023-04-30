import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {HomepageComponent} from "../../Views/Homepage/homepage.compoent";

const baseUrl = 'http://localhost:9314/ProductService-1.0-SNAPSHOT/api/selling_company';
const orderUrl = 'http://localhost:9314/OrderService-1.0-SNAPSHOT/api/orders';
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
    this.prevButtonClicked = false;
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

  prevButtonClicked = false;
  viewPreviouslySold() {
    this.buttonClicked = !this.buttonClicked;
    this.prevButtonClicked = true;
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

        // send product id to order service to get order details (customer and shipping company)
        for(let i=0; i<this.products.length; i++){
          const productId: number= this.products[i].id;
          console.log("Product id: "+productId);

          this.http.get<any>(`${orderUrl}/productId/${productId}`).subscribe({
            next: (data: any) => {
              console.log("Orders for product "+productId);
              //console.log(data);
              this.products[i].order = data;
            },
            error: (error: any) => {
              console.error(error);
            }
          });
        }
      }
    });
  }

  createNewProductForm=false
  newProductName = '';
  newProductDescription = '';
  newImageUrl = '';
  newProductPrice = 0;
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
      imageUrl: this.newImageUrl
    };
    console.log("New product: " + data.name + " " + data.description + " " + data.price );
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
    this.newImageUrl = '';
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
