import { Injectable } from '@angular/core';
import { CarrinhoStorageService } from './carrinho-storage.service';
import { CarrinhoLogic } from './carrinhoLogic.service';

@Injectable({ providedIn: 'root' })
export class CarrinhoService extends CarrinhoLogic {
    constructor(carrinhoStorage: CarrinhoStorageService) {
        super(carrinhoStorage, 'carrinho');
    }
}
