import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import { Cookie } from 'ng2-cookies';
import { Http, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { map } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
 
const ACCESS_TOKEN = 'access_token';
@Injectable()
export class AppService {

  readonly clientId = 'fooClientIdPassword';
  readonly clientSecret = 'secret';

  constructor(
    private router: Router, 
    private http: Http
  ){}
 
  obtainAccessToken(loginData: any) {
    let params = new URLSearchParams();
    params.append('username', loginData.username);
    params.append('password', loginData.password);    
    params.append('grant_type', 'password');
    params.append('client_id', this.clientId);

    const headers = new Headers({
      'Content-type': 'application/x-www-form-urlencoded; charset=utf-8', 
      'Authorization': `Basic ${btoa(`fooClientIdPassword:${this.clientSecret}`)}`
    });
    const options = new RequestOptions({ headers: headers });
    this.http.post(
      'http://localhost:8081/spring-security-oauth-server/oauth/token', 
      params.toString(), 
      options
    ).pipe(
      map(res => res.json())
    ).subscribe(
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
    var expireDate = new Date().getTime() + (1000 * token.expires_in);
    Cookie.set(ACCESS_TOKEN, token.access_token, expireDate);
    this.router.navigate(['/']);
  }

  getResource(resourceUrl: string) : Observable<any>{
    const headers = new Headers({
      'Content-type': 'application/x-www-form-urlencoded; charset=utf-8', 
      'Authorization': `Bearer ${Cookie.get(ACCESS_TOKEN)}`
    });
    var options = new RequestOptions({ headers: headers });
    return this.http.get(
      resourceUrl, 
      options
    ).pipe(
      map(res => res.json()),
      catchError(error => {
        const errorMessage = error.json().error || 'Server error';
        console.error('getResource: ', errorMessage);
        return throwError(errorMessage);
      })
    );
  }

  checkCredentials(){
    if (!Cookie.check(ACCESS_TOKEN)){
        this.router.navigate(['/login']);
    }
  } 

  logout() {
    Cookie.delete(ACCESS_TOKEN);
    this.router.navigate(['/login']);
  }
}

export class Foo {
  constructor(
    public id: number,
    public name: string) { }
} 