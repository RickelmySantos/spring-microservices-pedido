import { Injectable } from '@angular/core';
import { CarrinhoLogic } from '../shared/state/carrinhoLogic.service';
import { CarrinhoStorageService } from './storage/carrinho-storage.service';

@Injectable({ providedIn: 'root' })
export class CarrinhoService extends CarrinhoLogic {
    constructor(carrinhoStorage: CarrinhoStorageService) {
        super(carrinhoStorage, 'carrinho');
    }
}
