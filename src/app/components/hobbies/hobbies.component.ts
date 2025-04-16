import { Component, OnInit } from '@angular/core';
import { PublicService } from '../../serivces/public.service';
import { Hobby } from '../../models/hobby.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-hobbies',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './hobbies.component.html',
  styleUrls: ['./hobbies.component.scss']
})
export class HobbyComponent implements OnInit {
  hobbies: Hobby[] = [];

  constructor(private publicService: PublicService) {}

  ngOnInit(): void {
    this.publicService.getHobbies().subscribe({
      next: (res: any) => {
        this.hobbies = res.data || [];
      },
      error: (err) => {
        console.error('Failed to load hobbies:', err);
      }
    });
  }
}
