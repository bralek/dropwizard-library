import { Injectable } from '@angular/core';
import { RequestService } from './request.service';
import { HttpParams } from '@angular/common/http';

@Injectable({providedIn: 'root'})
export class AppService {
    constructor(private requestService: RequestService) {}

    searchBooks(params: HttpParams) {
        return this.requestService.doGET(
            `/book`,
            params
        );
    }

    addBooks(body: any) {
        return this.requestService.doPOST(
            `/book`,
            body
        );
    }
    
}