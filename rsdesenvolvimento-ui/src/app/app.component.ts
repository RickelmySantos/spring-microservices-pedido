import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';

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
    title = 'rsdesenvolvimento-ui';

    ngOnInit(): void {}
}
