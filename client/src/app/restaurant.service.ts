import { HttpClient } from "@angular/common/http";
import { firstValueFrom } from "rxjs";

export class RestaurantService {

  constructor(private http: HttpClient) {	}

  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems(): Promise<any> {
    return firstValueFrom(this.http.get("/api/menu"));
  }

  // TODO: Task 3.2
}
