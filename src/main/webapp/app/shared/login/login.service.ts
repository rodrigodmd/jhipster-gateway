import { Injectable } from '@angular/core';

import { Principal } from '../auth/principal.service';
import { AuthServerProvider } from '../auth/auth-session.service';
import { HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from '../../app.constants';

@Injectable()
export class LoginService {

    constructor(
        private principal: Principal,
        private authServerProvider: AuthServerProvider,
        private http: HttpClient
    ) {}

    login() {
        let port = (location.port ? ':' + location.port : '');
        if (port === ':9000') {
            port = ':8080';
        }
        location.href = '//' + location.hostname + port + '/login';
    }

    logout() {
        this.principal.authenticate(null);
        this.authServerProvider.logout().subscribe();   
    }

    manageAccount() {
        location.href = SERVER_API_URL + 'api/manage-account';
    }
}
