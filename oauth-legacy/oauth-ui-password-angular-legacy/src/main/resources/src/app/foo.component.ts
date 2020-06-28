import { Component } from '@angular/core';
import {AppService, Foo} from './app.service'

@Component({
    selector: 'foo-details',
    providers: [AppService],  
    template: `
      <div class="container" *ngIf="!!foo">
          <h1 class="col-sm-12">Foo Details</h1>
          <div class="col-sm-12">
              <label class="col-sm-3">ID</label> <span>{{foo.id}}</span>
          </div>
          <div class="col-sm-12">
              <label class="col-sm-3">Name</label> <span>{{foo.name}}</span>
          </div>    
      </div>
      <div class="col-sm-12">
          <button class="btn btn-primary" (click)="getFoo()" type="submit">New Foo</button>        
      </div>`
  })
  
  export class FooComponent {
      private _foo: Foo;
      readonly foosUrl = 'http://localhost:8082/spring-security-oauth-resource/foos/';  
  
      constructor(
          private _service: AppService
      ) {}
  
      getFoo(){
          this._service.getResource(`${this.foosUrl}1`).subscribe(
              data => {
                  console.log('getFoo: ', data);
                  this._foo = data;
              },
              error => {
                  console.error('getFoo: ', error);
              },
              () => {
                  console.log('getFoo: completed');
              }
          );
      }
  
      get foo() {
          return this._foo;
      }
  }
