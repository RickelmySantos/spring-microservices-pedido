import { Injectable } from '@angular/core';
import { StorageSerivce } from 'src/app/core/services/storage.service';
import { ItemCarrinho } from 'src/app/models/itemCarrinho.model';

@Injectable({ providedIn: 'root' })
export class CarrinhoStorageService extends StorageSerivce<ItemCarrinho[]> {
    protected readonly STORAGE_KEY = 'carrinho';
}
