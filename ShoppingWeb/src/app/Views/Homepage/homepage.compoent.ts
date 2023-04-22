import {Component, Injectable, OnInit} from "@angular/core";
import {User} from "../../User/user.model";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../User/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-homepage-component',
  templateUrl: './homepage.component.html',
})

@Injectable({
  providedIn: 'root'
})
export class HomepageComponent implements OnInit{
  constructor(private http: HttpClient, private authService: AuthService, private router : Router) { }

  ngOnInit(): void {
    console.log("Homepage init");
    this.getLoggedInUser();
  }

  title = 'Shopping Homepage';
  loggedInUser: User = {
    id: '',
    username: '',
    password: '',
    email: '',
    fullname: '',
    type: '',
    is_logged_in: false,
    geographicalRegion: '',
  };
  loggedIn = false;

  getLoggedInUser(){
    this.authService.getLoggedInUser().subscribe({
      next: (res) => {
        console.log("Homepage: "+res)
        if(res != null){
          console.log("User logged in")
          this.loggedIn = true;
          this.loggedInUser = res;
        }
      }
    })
  }

  logoutUser(): void {
    this.authService.logout().subscribe({
      next: (res) => {
        console.log(res)
        if (res != null) {
          this.loggedIn = false;
          console.log("logged in: false");
          this.loggedInUser = {
            id: '',
            username: '',
            password: '',
            email: '',
            fullname: '',
            type: '',
            is_logged_in: false,
            geographicalRegion: '',
          }
          this.router.navigate(['/']).then(r => console.log("Navigated to login"));
        }
      }
    })
  }

}
