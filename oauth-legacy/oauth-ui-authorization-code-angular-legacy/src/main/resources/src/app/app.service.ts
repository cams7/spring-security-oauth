import {Injectable} from '@angular/core';
import { Cookie } from 'ng2-cookies';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

const ACCESS_TOKEN = 'access_token';
@Injectable()
export class AppService {
   readonly clientId = 'fooClientIdPassword';
   readonly clientSecret = 'secret';
   readonly redirectUri = 'http://localhost:8089/';
   
  constructor(
    private http: HttpClient
  ){}

  retrieveToken(code: string){
    let params = new URLSearchParams();   
    params.append('grant_type','authorization_code');
    params.append('client_id', this.clientId);
    params.append('redirect_uri', this.redirectUri);
    params.append('code', code);

    const headers = new HttpHeaders({
      'Content-type': 'application/x-www-form-urlencoded; charset=utf-8', 
      'Authorization': `Basic ${btoa(`${this.clientId}:${this.clientSecret}`)}` 
    });
    this.http.post(
      'http://localhost:8081/spring-security-oauth-server/oauth/token', 
      params.toString(), 
      { 
        headers: headers 
      }
    )
    .subscribe(
      token => {
        console.log(`Token: ${token}`);
        this.saveToken(token);
      },
      err => {
        console.error('retrieveToken: ', err);
        alert('Invalid Credentials');
      },
      () => {
        console.log('retrieveToken: completed');
      }
    ); 
  }

  private saveToken(token: any){
    const expireDate = new Date().getTime() + (1000 * token.expires_in);
    Cookie.set(ACCESS_TOKEN, token.access_token, expireDate);
    window.location.href = this.redirectUri;
  }

  getResource(resourceUrl: string) : Observable<any>{
    const headers = new HttpHeaders({
      'Content-type': 'application/x-www-form-urlencoded; charset=utf-8', 
      'Authorization': `Bearer ${Cookie.get(ACCESS_TOKEN)}`
    });
    return this.http.get(
      resourceUrl, 
      { 
        headers: headers 
      }
    ).pipe(
      catchError(error => {
        const errorMessage = error.json().error || 'Server error';
        console.error('getResource: ', errorMessage);
        return throwError(errorMessage);
      })
    );
  }

  isLoggedIn(){
    const accessToken = Cookie.check(ACCESS_TOKEN);
    return accessToken;
  }

  logout() {
    Cookie.delete(ACCESS_TOKEN);
    window.location.reload();
    //window.location.href = `http://localhost:8081/spring-security-oauth-server/logout`;
  }
}


export class Foo {
  constructor(
    public id: number,
    public name: string
  ) {     
  }
} 