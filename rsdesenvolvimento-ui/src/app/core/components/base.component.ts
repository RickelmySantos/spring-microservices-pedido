import { Directive } from '@angular/core';
import { RefreshableComponent } from 'src/app/core/components/refreshable.component';

@Directive({ standalone: true })
export abstract class BaseComponent extends RefreshableComponent {
    override ngOnInit(): void {
        super.ngOnInit();
    }

    override ngAfterViewInit(): void {
        super.ngAfterViewInit();
    }

    onLoadingStateChange(loading: boolean): void {
        this.updateChildrenLoadingState(loading);
    }

    updateChildrenLoadingState(loading: boolean): void {
        this.refresh();
    }

    override ngOnDestroy(): void {
        super.ngOnDestroy();
    }
}
