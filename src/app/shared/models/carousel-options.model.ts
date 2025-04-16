/**
 * Interface defining the options for configuring the carousel component.
 */
export interface CarouselOptions {
  /**
   * Number of items to show at once
   */
  items: number;

  /**
   * Enable autoplay
   */
  autoplay: boolean;

  /**
   * Autoplay timeout in milliseconds
   */
  autoplayTimeout: number;

  /**
   * Pause autoplay on mouse hover
   */
  autoplayHoverPause: boolean;

  /**
   * Show dots navigation
   */
  dots: boolean;

  /**
   * Show navigation arrows
   */
  nav: boolean;

  /**
   * Enable loop
   */
  loop: boolean;

  /**
   * Enable center mode
   */
  center: boolean;

  /**
   * Enable responsive breakpoints
   */
  responsive: {
    [key: number]: {
      items: number;
    };
  };

  /**
   * Margin between items in pixels
   */
  margin: number;
}

/**
 * Default carousel configuration
 */
export const DEFAULT_CAROUSEL_OPTIONS: CarouselOptions = {
  items: 3,
  autoplay: true,
  autoplayTimeout: 5000,
  autoplayHoverPause: true,
  dots: true,
  nav: true,
  loop: true,
  center: false,
  responsive: {
    0: { items: 1 },
    576: { items: 2 },
    992: { items: 3 }
  },
  margin: 20
};
