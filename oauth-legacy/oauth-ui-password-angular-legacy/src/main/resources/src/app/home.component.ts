import {Component} from '@angular/core';
import {AppService} from './app.service'
 
@Component({
    selector: 'home-header',
    providers: [AppService],
  template: `<div class="container" >
    <div class="content">
        <span>Welcome !!</span>
        <a class="btn btn-default pull-right"(click)="logout()" href="#">Logout</a>
    </div>
    <foo-details></foo-details>
</div>`
})
 
export class HomeComponent {
 
    constructor(
        private service: AppService
    ){}
 
    ngOnInit(){
        this.service.checkCredentials();
    }
 
    logout() {
        this.service.logout();
    }
}