import { Component, OnInit } from '@angular/core';
import { PublicService } from '../../serivces/public.service';
import { Contact } from '../../models/contact.model';
import { CommonModule } from '@angular/common';
import { LucideAngularModule, Mail, Phone, Linkedin, Github, Twitter, Globe } from 'lucide-angular';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent implements OnInit {
  contact: Contact | null = null;
  activeContact: string | null = null;
  contactItems: any[] = [];

  constructor(private publicService: PublicService) {}

  ngOnInit(): void {
    this.publicService.getContact().subscribe({
      next: (res: any) => {
        const data = res.data;
        this.contact = Array.isArray(data) ? data[0] : data;

        if (this.contact) {
          this.contactItems = [
            { key: 'email', icon: '📧', value: this.contact.email, isLink: false },
            { key: 'phone', icon: '📞', value: this.contact.phone, isLink: false },
            { key: 'linkedIn', icon: '🔗', value: this.contact.linkedIn, isLink: true },
            { key: 'github', icon: '🐙', value: this.contact.github, isLink: true },
            { key: 'twitter', icon: '🐦', value: this.contact.twitter, isLink: true },
            { key: 'website', icon: '🌐', value: this.contact.website, isLink: true }
          ];
        }
      },
      error: (err) => {
        console.error('Failed to load contact info:', err);
      },
    });
  }

  toggleContact(key: string) {
    this.activeContact = this.activeContact === key ? null : key;
  }
}
