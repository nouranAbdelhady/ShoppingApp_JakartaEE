import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs";

const baseUrl = 'http://localhost:16957/AccountService-1.0-SNAPSHOT/api/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loggedInUser: any;
  token: string | null = null;
  constructor(private http: HttpClient) {}

  login(data: any): Observable<any> {
    return this.http.put(`${(baseUrl)}/login`, data, { withCredentials: true });
  }

  logout() {
    return this.http.put(`${(baseUrl)}/logout`, {},  { withCredentials: true });
  }

  getLoggedInUser() {
    return this.http.get(`${(baseUrl)}/user`, { withCredentials: true });
  }
}
