export const APP_CONFIG = {
    apiUrl: 'http://localhost:8080/api',
    defaultPageSize: 10,
    cache: {
        ttl: 5 * 60 * 1000, // 5 minutes
    },
    cloudinary: {
        baseUrl: 'https://res.cloudinary.com/rsdesenvolvimento-estoque-api/image/upload/',
    },
} as const;
