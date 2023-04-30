import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User }  from '../User/user.model';

const baseUrl = 'http://localhost:16957/UserService-1.0-SNAPSHOT/api/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<User[]> {
    return this.http.get<User[]>(baseUrl);
  }

  getAllAdmins(): Observable<User[]> {
    return this.http.get<User[]>(`${baseUrl}/admins`);
  }

  getAllCustomers(): Observable<User[]> {
    return this.http.get<User[]>(`${baseUrl}/customers`);
  }

  get(id: any): Observable<User> {
    return this.http.get(`${baseUrl}/${id}`);
  }

  register(data: any): Observable<any> {
    return this.http.post(`${baseUrl}`, data);
  }

  login(data: any): Observable<any> {
    return this.http.put(`${baseUrl}/login`, data);
  }

}
