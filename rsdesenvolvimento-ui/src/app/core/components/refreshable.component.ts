import { AfterViewInit, ChangeDetectorRef, Directive, ElementRef, inject, Input, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Directive({ standalone: true })
export class RefreshableComponent implements OnInit, AfterViewInit, OnDestroy {
    protected readonly cd: ChangeDetectorRef = inject(ChangeDetectorRef);

    protected readonly translate: TranslateService = inject(TranslateService);

    public readonly el: ElementRef = inject(ElementRef);
    protected readonly renderer: Renderer2 = inject(Renderer2);

    @Input()
    id: string;

    constructor() {
        this.renderer.setAttribute(this.el.nativeElement, 'id', this.id);
    }

    ngAfterViewInit(): void {}

    ngOnInit(): void {}

    // ngOnDestroy(): void {
    //     this.observableUtils.destroyAll();
    // }

    ngOnDestroy(): void {
        // Cleanup logic if needed
    }

    refresh(): void {
        this.markForRefresh();
        this.cd.detectChanges();
    }

    markForRefresh(): void {
        this.cd.markForCheck();
    }
}
