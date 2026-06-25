const track = document.querySelector('.carousel-track');
const nextButton = document.querySelector('.next-btn');
const prevButton = document.querySelector('.prev-btn');

let currentIndex = 0;

// Función para mover el carrusel
function moveCarousel(index) {
    const cardWidth = document.querySelector('.doctor-card').offsetWidth;
    const gap = 20; // El gap definido en el CSS
    
    // Calculamos el desplazamiento
    track.style.transform = `translateX(-${index * (cardWidth + gap)}px)`;
    currentIndex = index;
}

// Botón Siguiente
nextButton.addEventListener('click', () => {
    const cards = document.querySelectorAll('.doctor-card');
    // En móviles el máximo índice es cards.length - 1, en PC depende de cuántas se vean.
    // Limitamos de forma sencilla para este ejemplo básico:
    if (currentIndex < cards.length - 1) {
        moveCarousel(currentIndex + 1);
    } else {
        moveCarousel(0); // Regresa al inicio
    }
});

// Botón Anterior
prevButton.addEventListener('click', () => {
    if (currentIndex > 0) {
        moveCarousel(currentIndex - 1);
    } else {
        const cards = document.querySelectorAll('.doctor-card');
        moveCarousel(cards.length - 1); // Va al final
    }
});