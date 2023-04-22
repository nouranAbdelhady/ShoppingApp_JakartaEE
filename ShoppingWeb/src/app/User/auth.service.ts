import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs";
import {User} from "./user.model";

const baseUrl = 'http://localhost:8080/UserService-1.0-SNAPSHOT/api/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loggedInUser: any;
  constructor(private http: HttpClient) {}

  login(data: any): Observable<any> {
    return this.http.put(`${(baseUrl)}/login`, data);
  }

  logout() {
    return this.http.put(`${(baseUrl)}/logout`, {});
  }

  getLoggedInUser() {
    return this.http.get(`${(baseUrl)}/user`);
  }
}
