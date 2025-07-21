import { Injectable } from '@angular/core';
import { APP_CONFIG } from '../constants/app.constants';

@Injectable({
    providedIn: 'root',
})
export class ConfigService {
    get apiUrl(): string {
        return APP_CONFIG.apiUrl;
    }

    get cloudinaryBaseUrl(): string {
        return APP_CONFIG.cloudinary.baseUrl;
    }

    get defaultPageSize(): number {
        return APP_CONFIG.defaultPageSize;
    }

    get cacheTtl(): number {
        return APP_CONFIG.cache.ttl;
    }

    buildApiUrl(endpoint: string): string {
        return `${this.apiUrl}/${endpoint.replace(/^\//, '')}`;
    }

    processImageUrl(imageUrl: string, addBase = false): string {
        if (addBase) {
            return imageUrl.startsWith('http') ? imageUrl : `${this.cloudinaryBaseUrl}${imageUrl}`;
        }
        return imageUrl.replace(this.cloudinaryBaseUrl, '');
    }
}
