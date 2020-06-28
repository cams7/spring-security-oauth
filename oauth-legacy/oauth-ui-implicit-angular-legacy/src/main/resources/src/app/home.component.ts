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
    public isLoggedIn = false;

    constructor(
        private service:AppService
    ){}
    
    ngOnInit() {
        this.isLoggedIn = this.service.isLoggedIn();
    }

    login() {
        this.service.obtainAccessToken();
    }

    logout() {
        this.service.logout();
    }
}