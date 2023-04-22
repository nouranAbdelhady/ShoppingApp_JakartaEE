import {Component, OnInit} from "@angular/core";
import {User} from 'src/app/User/user.model';
import {Router} from '@angular/router';
import {HomepageComponent} from "../Homepage/homepage.compoent";
import {AuthService} from "../../User/auth.service";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  user: User = {
    username: '',
    password: ''
  };
  loggedIn = false;

  constructor(private authService: AuthService, private router: Router, private homepage: HomepageComponent) {
  }

  loginUser(): void {
    const data = {
      username: this.user.username,
      password: this.user.password
    };

    console.log(data)
    this.authService.login(data).subscribe({
      next: (res) => {
        console.log(res)
        if (res != null) {
          this.loggedIn = true;
          console.log("logged in: true");
          this.router.navigate(['/homepage']).then(r => console.log("Navigated to homepage"));
        }
      }
    })
  }
}
