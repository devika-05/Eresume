export interface Project {
  id: string;
  userId: string;
  name: string;
  description: string;
  role: string;
  startDate: string; // LocalDate as ISO string
  completionDate: string;
  ongoing: boolean;
  githubUrl: string;
  demoUrl: string;
  imageUrl: string;
  technologies: string[];
  tags: string[];
  keyFeatures: string[];
}
