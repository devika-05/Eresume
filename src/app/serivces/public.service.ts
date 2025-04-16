import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PublicService {
  private baseUrl = 'http://localhost:8080/api/public'; // Update this if your backend runs elsewhere

  constructor(private http: HttpClient) {}

  getAbout(): Observable<any> {
    return this.http.get(`${this.baseUrl}/about`);
  }

  getSkills(): Observable<any> {
    return this.http.get(`${this.baseUrl}/skills`);
  }

  getProjects(): Observable<any> {
    return this.http.get(`${this.baseUrl}/projects`);
  }

  getCertifications(): Observable<any> {
    return this.http.get(`${this.baseUrl}/certification`);
  }

  getExperience(): Observable<any> {
    return this.http.get(`${this.baseUrl}/experience`);
  }

  getHobbies(): Observable<any> {
    return this.http.get(`${this.baseUrl}/hobbies`);
  }

  getContact(): Observable<any> {
    return this.http.get(`${this.baseUrl}/contact`);
  }
}
