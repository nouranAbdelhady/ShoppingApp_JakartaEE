import { Component, OnInit } from "@angular/core";
import { User } from 'src/app/User/user.model';
import { UserService } from 'src/app/User/user.service'

@Component({
  selector: "app-register",
  templateUrl: "./register.component.html",
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  user: User = {
    id: '',
    username: '',
    password: '',
    email: '',
    fullname: '',
    type: '',
    is_logged_in: false
  };
  submitted = false;
  userType = 'Customer'; // default value for the user type

  constructor(private  userService: UserService) {}

  registerUser(): void {
    const userType = this.userType === 'admin' ? "Admin" : "Customer";
    const data = {
      username: this.user.username,
      password: this.user.password,
      type: this.userType,
      email: this.user.email,
      fullname: this.user.fullname
    };
    this.userService.register(data)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.submitted = true;
        },
        error: (e) => console.error(e)
      });
  }

  newUser(): void {
    this.submitted = false;
    this.user = {
      id: '',
      username: '',
      password: '',
      email: '',
      fullname: '',
      type: '',
      is_logged_in: false
    };
  }
}
