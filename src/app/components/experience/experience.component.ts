import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Experience } from '../../models/experience.model';
import { PublicService } from '../../serivces/public.service';

@Component({
  selector: 'app-experience',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './experience.component.html',
  styleUrls: ['./experience.component.scss']
})
export class ExperienceComponent implements OnInit {
  experiences: Experience[] = [];

  constructor(private publicService: PublicService) {}

  ngOnInit(): void {
    this.publicService.getExperience().subscribe({
      next: (res: any) => {
        this.experiences = res.data || [];
      },
      error: (err) => {
        console.error('Failed to load experience:', err);
      }
    });
  }

  formatDate(date: string | null): string {
    return date ? new Date(date).toLocaleDateString(undefined, { year: 'numeric', month: 'short' }) : 'Present';
  }
}
