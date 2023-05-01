import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this.authService.token;
    const cloned = request.clone({
      headers: request.headers.set('Authorization', `Bearer ${token}`),
      withCredentials: true
    });

    return next.handle(cloned).pipe(
      catchError((err: HttpErrorResponse) => {
        // ADD ERROR HANDLING HERE
        return throwError(() => new Error('ERROR'));
      })
    );
  }
}
