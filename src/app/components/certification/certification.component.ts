import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Certification } from '../../models/certification.model';
import { PublicService } from '../../serivces/public.service';

@Component({
  selector: 'app-certification',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './certification.component.html',
  styleUrls: ['./certification.component.scss']
})
export class CertificationComponent implements OnInit {
  certifications: Certification[] = [];

  constructor(private publicService: PublicService) {}

  ngOnInit(): void {
    this.publicService.getCertifications().subscribe({
      next: (res: any) => {
        this.certifications = res.data || [];
      },
      error: (err) => {
        console.error('Failed to load certifications:', err);
      }
    });
  }

  formatDate(date: string | null): string {
    return date ? new Date(date).toLocaleDateString(undefined, { year: 'numeric', month: 'short' }) : 'No Expiry';
  }
}
