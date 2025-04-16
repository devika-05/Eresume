export interface Experience {
  id: string;
  userId: string;
  company: string;
  position: string;
  location: string;
  startDate: string;
  endDate: string | null;
  current: boolean;
  description: string;
  companyLogoUrl: string;
  responsibilities: string[];
  achievements: string[];
}
