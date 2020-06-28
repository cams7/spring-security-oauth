import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { OAuthService } from 'angular-oauth2-oidc';
import { Observable } from 'rxjs/Observable';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';


@Injectable()
export class AppService {
 
  constructor(
    private http: HttpClient, 
    private oauthService: OAuthService
  ) {
    this.oauthService.configure({
        loginUrl: 'http://localhost:8081/spring-security-oauth-server/oauth/authorize',
        redirectUri: 'http://localhost:8086/',
        clientId: 'sampleClientId',
        scope: 'read write foo bar',
        oidc: false
    })
    this.oauthService.setStorage(sessionStorage);
    this.oauthService.tryLogin({});
  }
 
  obtainAccessToken() {
    this.oauthService.initImplicitFlow();
  }

  getResource(resourceUrl: string) : Observable<any> {
    const headers = new HttpHeaders({
      'Content-type': 'application/x-www-form-urlencoded; charset=utf-8', 
      'Authorization': `Bearer  ${this.oauthService.getAccessToken()}`
    });
    return this.http.get(
      resourceUrl, 
      { headers: headers }
    ).pipe(
      catchError(error => {
        const errorMessage = error.json().error || 'Server error';
        console.error('getResource: ', errorMessage);
        return throwError(errorMessage);
      })
    );
  }

  isLoggedIn() {
    const accessToken = this.oauthService.getAccessToken();
    console.log(`accessToken: ${accessToken}`);  
    return !!accessToken;
  } 

  logout() {
      this.oauthService.logOut();
      location.reload();
  }
}

export class Foo {
  constructor(
    public id: number,
    public name: string
  ) {     
  }
} 