import { CommonModule } from '@angular/common';
import { OwlModule } from 'ngx-owl-carousel-o';
import { CarouselComponent } from './components/carousel/carousel.component';
import { CarouselItemDirective } from './directives/carousel-item.directive';
import { NgModule } from '@angular/core';
@NgModule({
  declarations: [
    CarouselComponent,
    CarouselItemDirective
  ],
  imports: [
    CommonModule,
    OwlModule
  ],
  exports: [
    CarouselComponent,
    CarouselItemDirective
  ]
})
export class CarouselModule { }
