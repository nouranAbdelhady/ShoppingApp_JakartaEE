import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  url = 'http://localhost:8080/UserService-1.0-SNAPSHOT/api/users';

  constructor(private http: HttpClient) { }

  getAllAdmins(): Observable<any> {
    return this.http.get(this.url+'/admins');
  }
}
