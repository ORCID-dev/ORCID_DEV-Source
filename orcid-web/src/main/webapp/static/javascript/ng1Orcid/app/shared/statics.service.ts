import { Injectable } 
    from '@angular/core';

import { Headers, Http, RequestOptions, Response } 
    from '@angular/http';

import { Observable } 
    from 'rxjs/Observable';

import { Subject }
    from 'rxjs/Subject';

import 'rxjs/Rx';

@Injectable()
export class StaticsService {
    private headers: Headers;
    private url: string;
    private notify = new Subject<any>();
    
    notifyObservable$ = this.notify.asObservable();

    constructor( private http: Http ){
        this.headers = new Headers(
            { 
                'Content-Type': 'application/json' 
            }
        );
    }

    getLiveIds(): Observable<any> {
        return this.http.get(
            getBaseUri()+'/statistics/liveids.json'
        )
        .map((res:Response) => res.json()).share();
    }
}
