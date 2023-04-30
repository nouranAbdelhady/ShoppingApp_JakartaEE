import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { HomepageComponent } from '../../../Views/Homepage/homepage.compoent';

const baseUrl = 'http://localhost:16957/UserService-1.0-SNAPSHOT/api/users'
const productbaseUrl ='http://localhost:9314/ProductService-1.0-SNAPSHOT/api/products';
const orderbaseUrl = 'http://localhost:9314/OrderService-1.0-SNAPSHOT/api/orders';

@Component({
  selector: 'app-customer-component',
  templateUrl: './customer-component.component.html',
  styleUrls: ['./customer-component.component.css']
})
export class CustomerComponent {
  customers: any[] = [];
  orders: any[] = [];
  cart: any[] = [];
  products: any[] = [];
  customerUsername: string | undefined = '';
  companyUsername: string | undefined = '';

  ngOnInit(): void {
    this.customerUsername = this.homepage.loggedInUser.username
    console.log("User init")
    console.log("Customer username: " + this.customerUsername)
  }

  constructor(private http: HttpClient, private homepage: HomepageComponent) {
  }


  buttonClicked = false;
  public showProducts: boolean = false;
  public viewCurrent: boolean = false;
  public viewPurchased: boolean = false;
  public showCart: boolean = false;

  viewProducts() {
    this.showProducts = true;
    this.viewCurrent = false;
    this.viewPurchased = false;
    this.showCart = false;
    this.buttonClicked = !this.buttonClicked;
    this.http.get<any[]>(`${productbaseUrl}`).subscribe({
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

  viewCurrentOrders() {
    this.showProducts = false;
    this.viewCurrent = true;
    this.viewPurchased = false;
    this.showCart = false;
    this.buttonClicked = !this.buttonClicked;
    console.log(this.orders); // check if orders is initialized correctly
    this.http.get<any[]>(`${baseUrl}/getCurrentAndPurchasedOrders/${this.customerUsername}/created`).subscribe({
      next: (data: any[]) => {
        this.orders = data;
        console.log(data);
      },
      error: (error: any) => {
        console.error(error);
      },
      complete: () => {
        console.log('Request completed successfully')
      }
    });
  }


  viewPurchasedOrders() {
    this.showProducts = false;
    this.viewCurrent = false;
    this.viewPurchased = true;
    this.showCart = false;
    this.buttonClicked = !this.buttonClicked;
    console.log(this.orders); // check if orders is initialized correctly
    this.http.get<any[]>(`${baseUrl}/getPurchasedOrders/${this.customerUsername}`).subscribe({
      next: (data: any[]) => {
        this.orders = data;
        console.log(data);
      },
      error: (error: any) => {
        console.error(error);
      },
      complete: () => {
        console.log('Request completed successfully')
      }
    });
  }


  addToCart(product: any) {
    this.cart.push(product); // add product to cart
    selected: false
    console.log(`Added ${product.name} to cart.`);
    alert(`Added ${product.name} to cart.`);
  }

  viewCart() {
    this.showProducts = false;
    this.viewCurrent = false;
    this.viewPurchased = false;
    this.showCart = true;
    this.buttonClicked = !this.buttonClicked;
    if (this.buttonClicked) {
      this.cart.forEach((product: any) => {
        product.selected = false;
      });
    }
  }

  createOrder(product: any) {
    const order = {
      username: this.customerUsername,
      productId: product.id,
      amount: product.price,
      state: "created"
    };

    fetch('http://localhost:9314/OrderService-1.0-SNAPSHOT/api/purchase', {
      method: 'POST',
      body: JSON.stringify(order),
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .then(response => response.json())
      .then(data => {
        console.log('Order created:', data);
        // remove product from cart
        this.cart = this.cart.filter((p: any) => p.id !== product.id);
        alert('Order created!');
      })
      .catch(error => {
        console.error('Error creating order:', error);
        alert('Error creating order!');
      });
  }
}
