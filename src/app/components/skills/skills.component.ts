import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Skill } from '../../models/skill.model';
import { PublicService } from '../../serivces/public.service';
import { CommonModule } from '@angular/common';
import { SwiperModule } from 'swiper/angular';

@Component({
  selector: 'app-skills',
  standalone: true,
  imports: [CommonModule, SwiperModule],
  templateUrl: './skills.component.html',
  styleUrls: ['./skills.component.scss'],

})
export class SkillsComponent implements OnInit {
  skills: Skill[] = [];
  groupedSkills: { [category: string]: Skill[] } = {};
  skillCategories: string[] = [];

  constructor(private publicService: PublicService) {}

  ngOnInit(): void {
    this.publicService.getSkills().subscribe({
      next: (res: any) => {
        this.skills = res.data || [];
        this.groupByCategory();
      },
      error: (err) => {
        console.error('Failed to load skills:', err);
      }
    });
  }

  groupByCategory(): void {
    this.groupedSkills = {};
    this.skillCategories = [];

    this.skills.forEach((skill) => {
      if (!this.groupedSkills[skill.category]) {
        this.groupedSkills[skill.category] = [];
        this.skillCategories.push(skill.category);
      }
      this.groupedSkills[skill.category].push(skill);
    });
  }

  getBarColor(proficiency: number): string {
    if (proficiency >= 85) {
      return 'linear-gradient(to right, #00c6ff, #0072ff)';
    } else if (proficiency >= 60) {
      return 'linear-gradient(to right, #42e695, #3bb2b8)';
    } else {
      return 'linear-gradient(to right, #f093fb, #f5576c)';
    }
  }

}
