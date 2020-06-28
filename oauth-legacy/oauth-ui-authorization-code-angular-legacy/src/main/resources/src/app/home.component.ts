import {Component} from '@angular/core';
import {AppService} from './app.service'
 
@Component({
    selector: 'home-header',
    providers: [AppService],
    template: `<div class="container" >
    <button *ngIf="!isLoggedIn" class="btn btn-primary" (click)="login()" type="submit">Login</button>
    <div *ngIf="isLoggedIn" class="content">
        <span>Welcome !!</span>
        <a class="btn btn-default pull-right"(click)="logout()" href="#">Logout</a>
        <br/>
        <foo-details></foo-details>
    </div>
</div>`
})
 
export class HomeComponent {
    private _isLoggedIn = false;

    constructor(
        private service: AppService
    ){}
 
    ngOnInit(){
        this._isLoggedIn = this.service.isLoggedIn();    
        const i = window.location.href.indexOf('code');
        if(!this.isLoggedIn && i != -1) {
            const code = window.location.href.substring(i + 5);
            console.log(`Access code: ${code}`);
            this.service.retrieveToken(code);
        }
    }

    login() {
        window.location.href = `http://localhost:8081/spring-security-oauth-server/oauth/authorize?response_type=code&client_id=${this.service.clientId}&redirect_uri=${this.service.redirectUri}`;
    }
 
    logout() {
        this.service.logout();
    }

    get isLoggedIn() {
        return this._isLoggedIn;
    }
}