import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { inject } from '@angular/core';
import { About } from '../../models/about.model';
import { PublicService } from '../../serivces/public.service';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [CommonModule],
  providers: [PublicService],
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.scss']
})
export class AboutComponent implements OnInit {
  about!: About;
  animatedTitle: string = "";
  private publicService = inject(PublicService);

  ngOnInit(): void {
    this.publicService.getAbout().subscribe({
      next: (res: any) => {
        this.about = res.data;
        this.animateTitle(this.about.title); // Start animation
      },
      error: (err) => {
        console.error('Failed to load about info:', err);
      }
    });
  }

  animateTitle(title: string): void {
    let i = 0;
    this.animatedTitle = "";

    const interval = setInterval(() => {
      if (i < title.length) {
        this.animatedTitle += title[i];
        i++;
      } else {
        clearInterval(interval);
      }
    }, 100); // Adjust speed here
  }
}
