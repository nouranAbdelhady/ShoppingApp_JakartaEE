import {Component, Injectable, OnInit} from "@angular/core";
import {User} from "../../Accounts/User/user.model";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../Accounts/User/auth.service";
import {Router} from "@angular/router";


const notificationUrl = 'http://localhost:8080/ShippingService-1.0-SNAPSHOT/api/notifactions'
const sendUrl = 'http://localhost:8080/ShippingService-1.0-SNAPSHOT/api/send'


@Component({
  selector: 'app-homepage-component',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
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
  showingNotifications = false;
  notificationCount = 0;
  notificationList: any = {
    id: '',
    date: '',
    message: '',
    sender_username: '',
    targeted_username: '',
    request: ''
  };
  loggedInUser: User = {
    id: '',
    username: '',
    password: '',
    email: '',
    fullname: '',
    type: '',
    is_logged_in: false,
    geographicalRegion: {
      name: '',
      id: 0
    },
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
          this.getNotifications();
        }
      }
    })
  }

  logoutUser(): void {
    if (this.loggedInUser.type == "Customer"){
      // notify that cart will be emptied (Ok/Cancel)
      if (confirm("Are you sure you want to logout? Your cart will be emptied.")) {
        // Logout

      } else {
        // Do not logout (skip the rest of the function)
        return;
      }
    }

    console.log("Logging out user: " + this.loggedInUser.username);
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
            geographicalRegion: {
              name: '',
              id: 0
            }
          }
          this.router.navigate(['/']).then(r => console.log("Navigated to login"));
        }
      }
    })
  }

  goToProfilePage() {
    // Navigate to the user's profile page
    this.router.navigate(['/profile']);
  }

  showNotifications() {
    this.getNotifications();
    this.showingNotifications = !this.showingNotifications;
  }

  getNotifications() {
    this.http.get<any[]>(`${notificationUrl}/receiver/${this.loggedInUser.username}`).subscribe({
      next: (res) => {
        console.log("Notifications: ");
        console.log(res);
        this.notificationList = res;
        this.notificationCount = res.length;
      }
    })
  }

  acceptNotification(notificationId: number) {
    console.log("Accepting notification: " + notificationId);
    if (this.loggedInUser.type="Shipping_company"){
      console.log("Shipping company");
      this.http.get<void>(`${sendUrl}/review_request/${notificationId}/YES`).subscribe({
        next: () => {
          console.log("Accepted Notification!");
          this.getNotifications();    //update notifications
        }
      })
    }
  }

  rejectNotification(notificationId: any) {
    console.log("Rejecting notification: " + notificationId);
    if (this.loggedInUser.type="Shipping_company"){
      console.log("Shipping company");
      this.http.get<void>(`${sendUrl}/review_request/${notificationId}/NO`).subscribe({
        next: () => {
          console.log("Rejected Notification!");
          this.getNotifications();    //update notifications
        }
      })
    }
  }

}
