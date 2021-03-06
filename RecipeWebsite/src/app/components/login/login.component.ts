import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { user } from '../../models/user.model';
import { loginUser } from '../../models/loginUser.model';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  User: user = <user>{};
  username: string;
  password: string;
  test: string;
  userInfo: string;
  recipeHistory: string;
  userRecipes: string;

  constructor(private router: Router,
              private loginService: LoginService,
              private cookieService: CookieService
              ) { 
  }

  ngOnInit() {
  }

/**
 * This function is called when the user enters their
 * login credentials and hits the "login" button.
 * Upon a successful login, their username is stored
 * in cookies, and their recipes, user info, and recipe
 * history is stored in local storage to be used throughout
 * the program.
 */

  loginUser() {
    this.User.username = this.username;
    this.User.password = this.password;
    var tmp = JSON.stringify(this.User);
    var userInfo = JSON.parse(tmp);
    this.loginService.loginUser(userInfo)
      .subscribe((data: loginUser) => {
        this.cookieService.set('username', data.UserInfo.userName);
        this.recipeHistory = JSON.stringify(data.Recipes);
        this.userInfo = JSON.stringify(data.UserInfo)
        this.userRecipes = JSON.stringify(data.Recipes);
        localStorage.setItem("recipeHistory", this.recipeHistory);
        localStorage.setItem("userInfo", this.userInfo);
        localStorage.setItem("userRecipes", this.userRecipes);
      });
    this.router.navigate(['/home']);
  }
}
