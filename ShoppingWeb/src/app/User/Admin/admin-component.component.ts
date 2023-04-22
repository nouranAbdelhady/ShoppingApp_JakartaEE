import { Component, OnInit } from '@angular/core';
import {UserService} from "../user.service";

@Component({
  selector: 'app-admin-component',
  templateUrl: './admin-component.component.html',
  styleUrls: ['./admin-component.component.css']
})
export class AdminComponent {
  admins: any[] = [];

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.getAllAdmins();
  }

  getAllAdmins() {
    this.userService.getAllAdmins().subscribe({
      next: (data: any[]) => {
        this.admins = data;
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
