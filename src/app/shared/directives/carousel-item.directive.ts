import { Directive, TemplateRef } from '@angular/core';

/**
 * Directive to mark a template as a carousel item.
 * This allows for more flexible content projection in the carousel component.
 */
@Directive({
  selector: '[appCarouselItem]'
})
export class CarouselItemDirective {
  constructor(public templateRef: TemplateRef<any>) {}
}
