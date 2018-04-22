import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { SERVER_OAUTH2_URL } from '../../app.constants';

@Injectable()
export class AuthServerProvider {

    constructor(
        private http: HttpClient
    ) {}

    logout(): Observable<any> {
        var result;
        // logout from the server
        //this.http.get('http://localhost:9080/auth/realms/jhipster/protocol/openid-connect/logout').subscribe(() => {}, () => {  });
        return this.http.post(SERVER_API_URL + 'api/logout', {}, { observe: 'response' }).map((response: HttpResponse<any>) => {
            // to get a new csrf token call the api
            //this.http.get(SERVER_API_URL + 'api/account').subscribe(() => {}, () => {});       
            location.href = SERVER_OAUTH2_URL + '/protocol/openid-connect/logout?redirect_uri=http%3A%2F%2Flocalhost%3A8080';
            return response;
        });  
        
    }
}
