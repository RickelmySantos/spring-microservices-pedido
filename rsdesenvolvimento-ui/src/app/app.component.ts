import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth.service';

@Component({
    selector: 'app-root',
    template: `
        <router-outlet></router-outlet>
    `,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    standalone: true,
    imports: [RouterOutlet],
})
export class AppComponent implements OnInit {
    private readonly authService: AuthService = inject(AuthService);

    title = 'rsdesenvolvimento-ui';

    ngOnInit(): void {
        const userProfile = this.authService.getIdentityClaims();
        console.log('User Profile:', userProfile);
    }
}
