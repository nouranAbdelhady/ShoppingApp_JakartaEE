import { Component, OnInit } from '@angular/core';
import {UserService} from "../user.service";

@Component({
  selector: 'app-customer-component',
  templateUrl: './customer-component.component.html',
  styleUrls: ['./customer-component.component.css']
})
export class CustomerComponent {
  customers: any[] = [];

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.getAllCustomers();
  }

  getAllCustomers() {
    this.userService.getAllCustomers().subscribe({
      next: (data: any[]) => {
        this.customers = data;
        console.log(data);
      },
      error: (error: any) => {
        console.error(error);
      },
      complete: () => {
        console.log('Request completed successfully');
      }
    }
    );
  }
}
