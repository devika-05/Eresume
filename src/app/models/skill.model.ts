export interface Skill {
  id: string;
  userId: string;
  name: string;
  category: string;
  proficiency: number; // 1-10 scale
  iconUrl: string;
}
