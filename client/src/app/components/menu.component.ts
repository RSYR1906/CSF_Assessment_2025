import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RestaurantService } from '../restaurant.service';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit {
  // TODO: Task 2
  menu: string[]=[];

  constructor(private restaurantSvc: RestaurantService, private router: Router) { }

  ngOnInit(): void {
    this.restaurantSvc.getMenuItems().then(result=> {
      console.log(result)
      this.menu = result;
    }).catch(err => {
      console.error(err)
    });
  }

}
