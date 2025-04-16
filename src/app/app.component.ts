import { Component } from '@angular/core';
import { AboutComponent } from './components/about/about.component';
import { SkillsComponent } from './components/skills/skills.component';
import { ProjectsComponent } from './components/projects/project.component';
import { ExperienceComponent } from './components/experience/experience.component';
import { CertificationComponent } from './components/certification/certification.component';
import { HobbyComponent } from './components/hobbies/hobbies.component';
import { ContactComponent } from './components/contact/contact.component';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    AboutComponent,
    SkillsComponent,
    ProjectsComponent,
    ExperienceComponent,
    CertificationComponent,
    HobbyComponent,
    ContactComponent,
],
  template: `
    <app-about></app-about>
    <app-contact></app-contact>
    <app-skills></app-skills>
    <app-experience></app-experience>
    <app-projects></app-projects>
    <app-certification></app-certification>
    <app-hobbies></app-hobbies>
    <!-- <app-carousel></app-carousel> -->

  `
})
export class AppComponent {
  title(title: any) {
    throw new Error('Method not implemented.');
  }
}
