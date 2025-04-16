export interface Certification {
  id: string;
  userId: string;
  name: string;
  issuer: string;
  credentialId: string;
  credentialUrl: string;
  issueDate: string;
  expiryDate: string | null;
  noExpiry: boolean;
  description: string;
  badgeUrl: string;
}
