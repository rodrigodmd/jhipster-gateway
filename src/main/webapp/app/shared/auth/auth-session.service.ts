import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

@Injectable()
export class AuthServerProvider {

    constructor(
        private http: HttpClient
    ) {}

    logout(): Observable<any> {
        var result;
        // logout from the server
        return this.http.post(SERVER_API_URL + 'api/logout', {}, { observe: 'response' }).map((response: HttpResponse<any>) => {
            location.href = SERVER_API_URL + 'api/logout-redirect';
            return response;
        });  
        
    }
}
