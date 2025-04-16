import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Project } from '../../models/project.model';
import { PublicService } from '../../serivces/public.service';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.scss']
})
export class ProjectsComponent implements OnInit {
  projects: Project[] = [];

  constructor(private publicService: PublicService) {}

  ngOnInit(): void {
    this.publicService.getProjects().subscribe({
      next: (res: any) => {
        this.projects = res.data || [];
      },
      error: (err) => console.error('Failed to load projects', err)
    });
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short'
    });
  }
}
