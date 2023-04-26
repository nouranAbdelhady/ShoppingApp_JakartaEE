import {Component, Injectable, OnInit} from "@angular/core";
import {User} from "../../Accounts/User/user.model";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../Accounts/User/auth.service";
import {Router} from "@angular/router";

const baseUrl = 'http://localhost:8080/AccountService-1.0-SNAPSHOT/api/auth';
const userUrl = 'http://localhost:8080/UserService-1.0-SNAPSHOT/api/users';
const regionUrl = 'http://localhost:8080/UserService-1.0-SNAPSHOT/api/regions';
const shippingUrl = 'http://localhost:8080/ShippingService-1.0-SNAPSHOT/api/shipping_company';
const sellingUrl = 'http://localhost:8080/ProductService-1.0-SNAPSHOT/api/selling_company';

@Injectable({
  providedIn: 'root'
})
@Component({
  selector: 'app-profile-component',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  constructor(private http: HttpClient, private authService: AuthService, private router: Router) {
  }

  availableRegions: any = [];
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
    }
  };
  loggedIn = false;
  selectedRegion = this.loggedInUser.geographicalRegion?.name || null;


  ngOnInit(): void {
    console.log("profile init");
    this.getLoggedInUser();
    this.loadRegions();
  }


  getLoggedInUser() {
    this.authService.getLoggedInUser().subscribe({
      next: (res) => {
        console.log("Profile: " + res)
        if (res != null) {
          console.log("User logged in")
          this.loggedIn = true;
          this.loggedInUser = res;
          if (this.loggedInUser.type == "Admin" || this.loggedInUser.type == "Customer") {
            console.log("Get user:", this.loggedInUser.username);
            this.http.get(`${(userUrl)}/username/${this.loggedInUser.username}`).subscribe({
              next: (res) => {
                if (res != null) {
                  this.loggedInUser = res;
                  this.selectedRegion = this.loggedInUser.geographicalRegion?.name || null;
                  console.log("User data loaded:", this.loggedInUser);
                }
              }
            });
          }
        }
      }
    })
  }

  saveUser() {
    console.log("User data saved:", this.loggedInUser);
    if (this.loggedInUser.type == "Admin" || this.loggedInUser.type == "Customer") {
      // Update region
      this.updateGeographicalRegion();
      // Update user
      this.http.put(`${(userUrl)}/${this.loggedInUser.username}`, this.loggedInUser).subscribe({
        next: (res) => {
          console.log("User updated:", this.loggedInUser);
        }
      });
    }

    const account = {
      name: this.loggedInUser.username,
      password: this.loggedInUser.password,
    }

    if (this.loggedInUser.type == "Selling_Company") {
      // update selling company
      this.http.put(`${(sellingUrl)}/${this.loggedInUser.username}`, account).subscribe({
        next: (res) => {
          console.log("Company updated:", this.loggedInUser);
        }
      });
    }

    if (this.loggedInUser.type == "Shipping_Company") {
      // update shipping company
      this.http.put(`${(shippingUrl)}/${this.loggedInUser.username}`, account).subscribe({
        next: (res) => {
          console.log("Company updated:", this.loggedInUser);
        }
      });
    }
  }

  loadRegions() {
    this.http.get(`${(regionUrl)}`).subscribe({
      next: (res) => {
        if (res != null) {
          this.availableRegions = res;
          console.log("Regions loaded:", this.availableRegions);
        }
      }
    });
  }

  updateGeographicalRegion() {
    if (this.selectedRegion) {
      this.http.get(`${(regionUrl)}/name/${this.selectedRegion}`).subscribe({
        next: (res) => {
          this.loggedInUser.geographicalRegion = res;
          console.log("Region updated:", this.loggedInUser.geographicalRegion);
        }
      });
    } else {
      this.loggedInUser.geographicalRegion = {
        name: '',
        id: 0
      };
    }
  }
}
