import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
    selector: 'app-not-found',
    template: `
        <div class="not-found-container">
            <div class="content">
                <h1>404</h1>
                <h2>Página não encontrada</h2>
                <p>A página que você está procurando não existe ou foi movida.</p>
                <a routerLink="/" class="btn btn-primary">Voltar ao início</a>
            </div>
        </div>
    `,
    styles: [
        `
            .not-found-container {
                display: flex;
                justify-content: center;
                align-items: center;
                min-height: 100vh;
                text-align: center;
            }

            .content h1 {
                font-size: 6rem;
                color: var(--primary-color);
                margin: 0;
            }

            .content h2 {
                font-size: 2rem;
                margin: 1rem 0;
            }

            .btn {
                display: inline-block;
                padding: 1rem 2rem;
                margin-top: 2rem;
                text-decoration: none;
                border-radius: 4px;
                transition: background-color 0.3s;
            }

            .btn-primary {
                background-color: var(--primary-color);
                color: white;
            }
        `,
    ],
    standalone: true,
    imports: [RouterLink],
})
export class NotFoundComponent {}
