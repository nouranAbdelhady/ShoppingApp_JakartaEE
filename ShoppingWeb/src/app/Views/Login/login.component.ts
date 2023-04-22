import { Component, OnInit } from "@angular/core";
import { User } from 'src/app/User/user.model';
import { UserService } from 'src/app/User/user.service'
import {AppComponent} from "../../app.component";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ['./login.component.css']
})
export class LoginComponent  {
  user: User = {
    username: '',
    password: ''
  };
  loggedIn = false;

  loggedInUser: User = {
    id: '',
    username: '',
    password: '',
    email: '',
    fullname: '',
    type: '',
    is_logged_in: false
  };

  setLoggedInUser(User: User){
    this.loggedInUser = User;
  }

  constructor(private  userService: UserService, private appComponent: AppComponent) {}

  loginUser(): void {
    const data = {
      username: this.user.username,
      password: this.user.password
    };

    this.userService.login(data)
      .subscribe({
        next: (res) => {
          console.log(res)
          console.log(data);
          if(res!=null){
            this.loggedIn = true;
            console.log("Logged in user:"+res);
            this.setLoggedInUser(res);
          }
        },
        error: (e) => console.error(e)
      });
  }

}
