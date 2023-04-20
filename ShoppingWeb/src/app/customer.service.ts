import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(private http: HttpClient) { }

  getAllCustomers(): Observable<any> {
    return this.http.get('http://localhost:8080/CustomerService-1.0-SNAPSHOT/api/customers');
  }
}
